package com.smith.edu.patienttrackerrestservice.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.smith.edu.patienttrackerrestservice.data.Patient;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class MongoConnector
{
    @Value("${mongo.dbName}")
    private String dbName;

    @Value("${mongo.uri}")
    private String mongoURI;

    private MongoClientURI clientURI;
    private MongoClient mongoClient;
    private MongoDatabase database;
    public static final String PATIENTS = "patients";

    public void getConnection()
    {
        clientURI = new MongoClientURI(mongoURI);
        mongoClient = new MongoClient(clientURI);
        database = mongoClient.getDatabase(dbName);
    }

    public MongoCollection<Document> getPatients()
    {
        return database.getCollection(PATIENTS);
    }
}
