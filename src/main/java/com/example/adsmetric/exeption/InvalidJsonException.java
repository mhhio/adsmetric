package com.example.adsmetric.exeption;

public class InvalidJsonException extends RuntimeException{
    public InvalidJsonException(String message) {
        super(message);
    }

    public InvalidJsonException(Throwable cause) {
        super(cause);
    }
}
