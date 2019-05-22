package com.unicast.unicast_backend.exceptions;

@SuppressWarnings("serial")
public class NotProfessorException extends Exception { 
    public NotProfessorException(String errorMessage) {
        super(errorMessage);
    }
}