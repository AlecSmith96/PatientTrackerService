package com.smith.edu.patienttrackerrestservice.database;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
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
        numPatientRecords = database.getCollection(ALLERGIES).countDocuments();
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
        MongoCollection<Document> collection = database.getCollection(PATIENTS);
        Document doc = new Document();
        doc.put("_id", "P-" + (++numPatientRecords));
        doc.put("name", newPatient.getName());
        doc.put("email", newPatient.getEmail());
        doc.put("phoneNumber", newPatient.getPhoneNumber());
        doc.put("dateOfBirth", newPatient.getDateOfBirth());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        doc.put("triageDate", dateFormat.format(new Date()));
        collection.insertOne(doc);
    }

    public Patient getPatientDetails(String name)
    {
        MongoCollection<Document> collection = database.getCollection(PATIENTS);
        Document patientToGet = new Document("name", name);
        Optional<Document> foundPatientOp = Optional.of(collection.find(patientToGet).first());

        if (foundPatientOp.isPresent())
        {
            return new Gson().fromJson(foundPatientOp.get().toJson(), Patient.class);
        }
        else
        {
            return new Patient("", "");
        }
    }

    @Override
    public void removePatientRecord(String name)
    {
        MongoCollection<Document> collection = database.getCollection(PATIENTS);
        collection.deleteOne(eq("name", name))/*.wasAcknowledged()*/;       //returns true if record deleted, can be used for error handling later
    }
}
