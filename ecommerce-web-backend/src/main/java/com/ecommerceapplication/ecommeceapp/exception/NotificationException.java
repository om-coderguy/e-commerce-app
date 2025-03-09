package com.ecommerceapplication.ecommeceapp.exception;

public class NotificationException extends RuntimeException{

    private final String message;

    public NotificationException(String message) {
        this.message = message;
    }

    public NotificationException(String message, Throwable cause){
        super(message, cause);
        this.message = message;
    }
}
