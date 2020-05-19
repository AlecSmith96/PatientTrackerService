package com.smith.edu.patienttrackerrestservice.database;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.smith.edu.patienttrackerrestservice.data.Patient;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

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
    private static final String PATIENTS = "patients";

    public void getConnection()
    {
        clientURI = new MongoClientURI(mongoURI);
        mongoClient = new MongoClient(clientURI);
        database = mongoClient.getDatabase(dbName);
    }

    /**
     * Public Method for returning all patient records in the database.
     * @return List<Patient> - A list of Patient objects.
     */
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
}
