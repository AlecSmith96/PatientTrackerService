package com.smith.edu.patienttrackerrestservice.database;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.smith.edu.patienttrackerrestservice.data.Allergy;
import com.smith.edu.patienttrackerrestservice.data.Patient;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;
import static org.mongojack.DBUpdate.set;

/**
 * Class for communicating with a MongoDb database hosted in an Atlas cluster.
 */
@Component
public class MongoConnector implements DatabaseConnector
{
    @Value("${mongo.dbName}")
    private String dbName;

    @Value("${mongo.uri}")
    private String mongoURI;

    private MongoClientURI clientURI;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private Long numPatientRecords;
    private Long numAllergyRecords;
    private static final String ALLERGIES = "allergies";
    private static final String PATIENTS = "patients";
    private static final String ID = "_id";
    private static final String UNDEFINED = "undefined";
    static Logger log = Logger.getLogger(MongoConnector.class.getName());

    /**
     * Method executed as soon as Bean is created.
     */
    @PostConstruct
    public void getConnection()
    {
        clientURI = new MongoClientURI(mongoURI);
        mongoClient = new MongoClient(clientURI);
        database = mongoClient.getDatabase(dbName);
        getNumRecordsPerTable();
    }

    private void getNumRecordsPerTable()
    {
        numPatientRecords = database.getCollection(PATIENTS).countDocuments();
        numAllergyRecords = database.getCollection(ALLERGIES).countDocuments();
    }

    public List<Patient> getPatients()
    {
        List<Patient> patients = new ArrayList<>();
        MongoCursor<Document> cursor = database.getCollection(PATIENTS).find().iterator();
        while (cursor.hasNext())
        {
            patients.add(new Gson().fromJson(cursor.next().toJson(), Patient.class));
        }
        return patients;
    }

    public void addNewPatient(Patient newPatient)
    {
        MongoCollection<Document> patientsCollection = database.getCollection(PATIENTS);
        Document patient = createPatientDoc(newPatient);
        if (!newPatient.getAllergies().isEmpty())
        {
            insertAllergyRecords(newPatient);
        }
        patientsCollection.insertOne(patient);
    }

    private void insertAllergyRecords(Patient newPatient)
    {
        MongoCollection<Document> allergiesCollection = database.getCollection(ALLERGIES);
        List<Document> allergies = createAllergyDocs(newPatient);
        allergiesCollection.insertMany(allergies);
    }

    private List<Document> createAllergyDocs(Patient newPatient)
    {
        List<Document> allergies = new ArrayList<>();
        newPatient.getAllergies().stream().forEach(allergy -> {
            Document allergyDoc = new Document();
            allergyDoc.put(ID, "A-"+ (++numAllergyRecords));
            allergyDoc.put("description", allergy);
            allergyDoc.put("patient_id", "P-" + numPatientRecords);
            allergies.add(allergyDoc);
        });
        return allergies;
    }

    private Document createPatientDoc(Patient newPatient)
    {
        Document doc = new Document();
        doc.put(ID, "P-" + (++numPatientRecords));
        doc.put("name", newPatient.getName());
        doc.put("email", newPatient.getEmail());
        doc.put("phoneNumber", newPatient.getPhoneNumber());
        doc.put("dateOfBirth", newPatient.getDateOfBirth());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        doc.put("triageDate", dateFormat.format(new Date()));
        return doc;
    }

    public Patient getPatientDetails(String id)
    {
        MongoCollection<Document> collection = database.getCollection(PATIENTS);
        Document patientToGet = new Document(ID, id);
        Document foundPatient = collection.find(patientToGet).first();
        Patient patient = new Gson().fromJson(foundPatient.toJson(), Patient.class);

        checkForAllergiesOfPatient(id, patient);
        return patient;
    }

    private void checkForAllergiesOfPatient(String id, Patient patient)
    {
        MongoCursor<Document> allergiesCollection = database.getCollection(ALLERGIES).find(new Document("patient_id", id)).iterator();

        allergiesCollection.forEachRemaining(allergy -> {
            Allergy allergyObj = new Gson().fromJson(allergy.toJson(), Allergy.class);
            patient.getAllergies().add(allergyObj.getDescription());
        });
    }

    @Override
    public void removePatientRecord(String id)
    {
        MongoCollection<Document> collection = database.getCollection(PATIENTS);
        collection.deleteOne(eq(ID, id))/*.wasAcknowledged()*/;       //returns true if record deleted, can be used for error handling later
        collection.deleteMany(eq("patient_id", id));
    }

    @Override
    public Patient updatePatientRecord(String id, Patient patient)
    {
        MongoCollection<Document> patients = database.getCollection(PATIENTS);
        Field[] fields = patient.getClass().getDeclaredFields();
        updateFieldsIfChanged(id, patient, patients, fields);

        //check to add new allergy record
        updateAllergiesForPatient(fields);
        return null;
    }

    private void updateFieldsIfChanged(String id, Patient patient, MongoCollection<Document> patients, Field[] fields)
    {
        for(Field field : fields)
        {
            String fieldName = field.getName();
            if (fieldName.equals(ID))
            {
                continue;
            }

            Optional<Object> fieldValueOptional = getFieldValueFromObject(patient, fieldName);
            if (fieldValueOptional.isPresent())
            {
                updateFieldForPatient(id, patients, field, fieldName, fieldValueOptional);
            }
        }
    }

    private void updateFieldForPatient(String id, MongoCollection<Document> patients, Field field, String fieldName, Optional<Object> fieldValueOptional)
    {
        Object fieldValue = fieldValueOptional.get();
        if (!fieldValue.equals("") && !fieldValue.equals(UNDEFINED))
        {
            patients.updateOne(eq(ID, id), set(fieldName, fieldValue));
        }
    }

    private void updateAllergiesForPatient(Field[] fields)
    {

    }

    private Optional<Object> getFieldValueFromObject(Patient patient, String fieldName)
    {
        try
        {
            PropertyDescriptor propDesc = new PropertyDescriptor(fieldName, patient.getClass());
            Method getter = propDesc.getReadMethod();
            Optional<Object> value = Optional.ofNullable(getter.invoke(patient));
            return value;
        } catch (IllegalAccessException | InvocationTargetException | IntrospectionException e)
        {
            log.log(Level.SEVERE, "Unable to get Field Value from Patient object." + e);

        }

        return Optional.empty();
    }
}
