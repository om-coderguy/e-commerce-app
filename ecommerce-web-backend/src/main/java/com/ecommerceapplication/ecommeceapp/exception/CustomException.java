package com.ecommerceapplication.ecommeceapp.exception;

public class CustomException extends RuntimeException{
    private  String message;

    public CustomException(String message) {
        this.message = message;
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
