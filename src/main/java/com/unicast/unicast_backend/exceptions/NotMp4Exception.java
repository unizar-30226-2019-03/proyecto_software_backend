package com.unicast.unicast_backend.exceptions;

@SuppressWarnings("serial")
public class NotMp4Exception extends Exception { 
    public NotMp4Exception(String errorMessage) {
        super(errorMessage);
    }
}