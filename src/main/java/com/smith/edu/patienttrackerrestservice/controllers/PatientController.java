package com.smith.edu.patienttrackerrestservice.controllers;

import com.smith.edu.patienttrackerrestservice.data.Patient;
import com.smith.edu.patienttrackerrestservice.database.MongoConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{id}")
    public Patient displayPatientDetails(@PathVariable String id)
    {
        return mongoConnector.getPatientDetails(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/add")
    public String addPatient(@RequestBody Patient patient)
    {
        mongoConnector.addNewPatient(patient);
        return "Patient Added";
    }

    @PostMapping("/discharge/{id}")
    public void dischargePatient(@PathVariable String id)
    {
        mongoConnector.removePatientRecord(id);
    }

    @PostMapping("update/{id}")
    public void updatePatientDetails(@PathVariable String id)
    {
        mongoConnector.updatePatientRecord(id);
    }
}
