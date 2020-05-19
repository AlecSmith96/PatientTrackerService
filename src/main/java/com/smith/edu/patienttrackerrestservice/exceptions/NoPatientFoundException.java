package com.smith.edu.patienttrackerrestservice.exceptions;

public class NoPatientFoundException extends Exception
{
    public NoPatientFoundException(String errorMessage)
    {
        super(errorMessage);
    }
}
