package com.smith.edu.patienttrackerrestservice.database;

import com.smith.edu.patienttrackerrestservice.data.Patient;

import java.util.List;

/**
 * Interface for restricting how database connector classes interact with their databases and the format in which they
 * return data.
 */
public interface DatabaseConnector
{
    /**
     * Method for returning all patient records in the database.
     * @return List<Patient> - A list of Patient objects.
     */
    List<Patient> getPatients();

    /**
     * Method for adding a new Patient record to the database.
     * @param newPatient - A Patient object containing name, email and phoneNumber values.
     */
    void addNewPatient(Patient newPatient);
}
