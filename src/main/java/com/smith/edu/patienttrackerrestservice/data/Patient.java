package com.smith.edu.patienttrackerrestservice.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient
{
    private static final AtomicInteger ID_COUNT = new AtomicInteger(0);
    private Object _id;
    private String name;
    private String email;
    private String phoneNumber;
    private String triageDate;
    private String dateOfBirth;
    private List<Map<String, String>> medications;
    private List<String> allergies;

    public Patient(String name, String email)
    {
        this.name = name;
        this.email = email;
    }

    public Patient(String name, String email, String phoneNumber, String dateOfBirth)
    {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }
}
