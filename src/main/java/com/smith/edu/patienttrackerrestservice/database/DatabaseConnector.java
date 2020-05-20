package com.smith.edu.patienttrackerrestservice.database;

import com.smith.edu.patienttrackerrestservice.data.Patient;

import java.util.List;

/**
 * Interface for restricting how database connector classes interact with their databases and the format in which they
 * return data.
 */
public interface DatabaseConnector
{
    List<Patient> getPatients();
}
