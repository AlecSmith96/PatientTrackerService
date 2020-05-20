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
import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

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
        doc.put("name", newPatient.getName());
        doc.put("email", newPatient.getEmail());
        doc.put("phoneNumber", newPatient.getPhoneNumber());
        collection.insertOne(doc);
    }
}
