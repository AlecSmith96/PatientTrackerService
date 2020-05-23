package com.smith.edu.patienttrackerrestservice.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data class representing an Allergy object to pass database record into object. Constructors, getters and setters
 * provided by Lombok annotations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Allergy
{
    private Object _id;
    private String description;
    private String patient_id;
}
