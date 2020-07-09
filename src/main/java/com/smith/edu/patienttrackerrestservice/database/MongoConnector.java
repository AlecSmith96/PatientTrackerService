package com.smith.edu.patienttrackerrestservice.database;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.smith.edu.patienttrackerrestservice.data.Allergy;
import com.smith.edu.patienttrackerrestservice.data.Patient;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.all;
import static com.mongodb.client.model.Filters.eq;

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
            allergyDoc.put("_id", "A-"+ (++numAllergyRecords));
            allergyDoc.put("description", allergy);
            allergyDoc.put("patient_id", "P-" + numPatientRecords);
            allergies.add(allergyDoc);
        });
        return allergies;
    }

    private Document createPatientDoc(Patient newPatient)
    {
        Document doc = new Document();
        doc.put("_id", "P-" + (++numPatientRecords));
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
        Document patientToGet = new Document("_id", id);
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
        collection.deleteOne(eq("_id", id))/*.wasAcknowledged()*/;       //returns true if record deleted, can be used for error handling later
        collection.deleteMany(eq("patient_id", id));
    }

    @Override
    public Patient updatePatientRecord(String id)
    {

        return null;
    }
}
