package com.unicast.unicast_backend.exceptions;

@SuppressWarnings("serial")
public class ProfessorNotInSubjectException extends Exception { 
    public ProfessorNotInSubjectException(String errorMessage) {
        super(errorMessage);
    }
}