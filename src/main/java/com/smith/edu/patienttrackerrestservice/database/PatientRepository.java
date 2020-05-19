package com.smith.edu.patienttrackerrestservice.database;

import com.smith.edu.patienttrackerrestservice.data.Patient;
import com.smith.edu.patienttrackerrestservice.exceptions.NoPatientFoundException;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class PatientRepository
{
    @Getter
    private List<Patient> patients = populateArray();

    private ArrayList<Patient> populateArray()
    {
        patients = new ArrayList<>();
        Stream.of("John", "Julie", "Jennifer", "Helen", "Rachel").forEach(name -> {
            Patient patient = new Patient(name, name.toLowerCase() + "@domain.com");
            this.addPatient(patient);
        });

        return (ArrayList<Patient>) patients;
    }

    public void addPatient(Patient patient)
    {
        patients.add(patient);
    }

    public Patient getPatient(int id) throws NoPatientFoundException
    {
        Optional<Patient> op = patients.stream().filter(patient -> patient.getId() == id).findFirst();
        if (op.isPresent())
        {
            return op.get();
        }
        throw new NoPatientFoundException("No patient found matching criteria.");
    }
}
