package com.smith.edu.patienttrackerrestservice.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Data class representing a Patient object to pass database record into object. Constructors, getters and setters
 * provided by Lombok annotations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient
{
    private Object _id;
    private String name;
    private String email;
    private String phoneNumber;
    private String triageDate;
    private String dateOfBirth;
    private List<String> allergies = new ArrayList<>();
}
