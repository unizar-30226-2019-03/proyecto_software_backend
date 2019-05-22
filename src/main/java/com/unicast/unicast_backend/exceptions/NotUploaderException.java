package com.unicast.unicast_backend.exceptions;

@SuppressWarnings("serial")
public class NotUploaderException extends Exception { 
    public NotUploaderException(String errorMessage) {
        super(errorMessage);
    }
}