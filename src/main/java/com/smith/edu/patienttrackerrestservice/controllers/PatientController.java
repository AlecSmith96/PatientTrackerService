package com.smith.edu.patienttrackerrestservice.controllers;

import com.smith.edu.patienttrackerrestservice.data.Patient;
import com.smith.edu.patienttrackerrestservice.database.MongoConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling of REST calls from external front-end applications.
 */
@RestController
@RequestMapping("/patients")
public class PatientController
{
    @Autowired
    private MongoConnector mongoConnector;

    @GetMapping("/all")
    public List<Patient> getPatients()
    {
        return mongoConnector.getPatients();
    }

    @GetMapping("/{name}")
    public Patient displayPatientDetails(@PathVariable String name)
    {
        return mongoConnector.getPatientDetails(name);
    }

    @PostMapping("/add")
    public void addPatient(@RequestBody Patient patient)
    {
        mongoConnector.addNewPatient(patient);
    }

    @PostMapping("/discharge/{name}")
    public void dischargePatient(@PathVariable String name)
    {
        mongoConnector.removePatientRecord(name);
    }
}
