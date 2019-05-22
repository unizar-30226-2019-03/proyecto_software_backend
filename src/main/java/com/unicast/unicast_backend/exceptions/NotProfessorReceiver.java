package com.unicast.unicast_backend.exceptions;

@SuppressWarnings("serial")
public class NotProfessorReceiver extends Exception { 
    public NotProfessorReceiver(String errorMessage) {
        super(errorMessage);
    }
}