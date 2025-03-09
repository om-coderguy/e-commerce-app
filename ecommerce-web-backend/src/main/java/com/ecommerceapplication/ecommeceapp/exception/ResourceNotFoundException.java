package com.ecommerceapplication.ecommeceapp.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceNotFoundException extends RuntimeException{

    private String message;

    static final Logger LOGGER= LoggerFactory.getLogger(ResourceNotFoundException.class);


    public ResourceNotFoundException(String message) {
        this.message = message;
        LOGGER.error("Data not found");
    }

    public ResourceNotFoundException(String message,Throwable cause) {
        super(message, cause);
        this.message = message;
        LOGGER.error("Data not found");
    }
}
