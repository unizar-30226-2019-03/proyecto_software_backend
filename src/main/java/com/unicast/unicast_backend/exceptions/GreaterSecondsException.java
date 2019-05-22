package com.unicast.unicast_backend.exceptions;

@SuppressWarnings("serial")
public class GreaterSecondsException extends Exception { 
    public GreaterSecondsException(String errorMessage) {
        super(errorMessage);
    }
}