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

    /**
     * Method for retrieving a Patient record from the database with the corresponding name.
     * @param name - String representing the name of a patient.
     * @return Patient - Object containing patients records.
     */
    Patient getPatientDetails(String name);

    /**
     * Method for removing a Patient record and all relating records from the database.
     * @param name - String representing the name of the patient.
     */
    void removePatientRecord(String name);

    /**
     * Method for updating a patient record with new details.
     * @param id - String representing the id of the patient record to be updated
     * @return Patient - The updated patient record
     */
    Patient updatePatientRecord(String id);
}
