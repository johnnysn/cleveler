package com.hive.apps.cleveler.cnc.exception;

public class InvalidGCODEException extends RuntimeException {

    public InvalidGCODEException(String message) {
        super(message);
    }

    public InvalidGCODEException(String message, Throwable cause) {
        super(message, cause);
    }
}
