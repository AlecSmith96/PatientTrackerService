package com.smith.edu.patienttrackerrestservice.controllers;

import com.smith.edu.patienttrackerrestservice.data.Patient;
import com.smith.edu.patienttrackerrestservice.database.MongoConnector;
import com.smith.edu.patienttrackerrestservice.database.PatientRepository;
import com.smith.edu.patienttrackerrestservice.exceptions.NoPatientFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/patients")
public class PatientController
{
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MongoConnector mongoConnector;

    /**
     * Method executed as soon as Bean is created.
     */
    @PostConstruct
    private void getDbConnection()
    {
        mongoConnector.getConnection();
    }

    @GetMapping("/all")
    public List<Patient> getPatients()
    {
        return patientRepository.getPatients();
    }

    @GetMapping("/{id}")
    public Patient displayPatientDetails(@PathVariable int id)
    {
        Patient patient;
        try
        {
            patient = patientRepository.getPatient(id);
        } catch (NoPatientFoundException e)
        {
            return new Patient("","");
        }
        return patient;
    }

    @PostMapping("/add")
    public void addPatient(@PathVariable String name, @PathVariable String email, @PathVariable String phoneNumber)
    {
        patientRepository.addPatient(new Patient(name, email, phoneNumber));
    }
}
