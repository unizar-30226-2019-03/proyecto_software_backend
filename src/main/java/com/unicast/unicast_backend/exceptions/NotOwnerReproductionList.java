package com.unicast.unicast_backend.exceptions;

@SuppressWarnings("serial")
public class NotOwnerReproductionList extends Exception { 
    public NotOwnerReproductionList(String errorMessage) {
        super(errorMessage);
    }
}