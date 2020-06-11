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

    /**
     * Controller method for returning a List of all Patient records in the database.
     * @return List<Patient> - A list of Patient objects relating to records in the database.
     */
    @GetMapping("/all")
    public List<Patient> getPatients()
    {
        return mongoConnector.getPatients();
    }

    /**
     * Controller method for returning a patients details.
     * @param id - The id of the patient in the database.
     * @return Patient - A Patient object conatining all details for the patient.
     */
    @GetMapping("/{id}")
    public Patient displayPatientDetails(@PathVariable String id)
    {
        return mongoConnector.getPatientDetails(id);
    }

    /**
     * Controller method for adding a new patient record to the database.
     * @param patient - Patient object containing all details for the new patient record.
     * @return HttpStatus - The status of the request.
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/add")
    public String addPatient(@RequestBody Patient patient)
    {
        mongoConnector.addNewPatient(patient);
        return "Patient Added";
    }

    /**
     * Controller method for removing a patient record from the database
     * @param id - the id of the patient to be removed.
     */
    @PostMapping("/discharge/{id}")
    public void dischargePatient(@PathVariable String id)
    {
        mongoConnector.removePatientRecord(id);
    }

    /**
     * Controller method for updating the details of a patient record.
     * @param id - the id of the patient to be updated.
     * @param patient - Patient object containing details to be updated to. Null values, empty strings and "underfined"
     *                  values are to be ignored.
     */
    @PostMapping("update/{id}")
    public void updatePatientDetails(@PathVariable String id, @RequestBody Patient patient)
    {
        mongoConnector.updatePatientRecord(id, patient);
    }
}
