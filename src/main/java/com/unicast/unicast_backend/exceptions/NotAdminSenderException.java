package com.unicast.unicast_backend.exceptions;

@SuppressWarnings("serial")
public class NotAdminSenderException extends Exception { 
    public NotAdminSenderException(String errorMessage) {
        super(errorMessage);
    }
}