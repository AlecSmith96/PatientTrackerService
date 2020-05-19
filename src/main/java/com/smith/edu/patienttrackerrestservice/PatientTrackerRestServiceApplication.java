package com.smith.edu.patienttrackerrestservice;

import com.smith.edu.patienttrackerrestservice.data.Patient;
import com.smith.edu.patienttrackerrestservice.database.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class PatientTrackerRestServiceApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(PatientTrackerRestServiceApplication.class, args);
    }
}


