package org.zeki.aprobados.exception;

public class SupabaseConnectionException extends RuntimeException {
    public SupabaseConnectionException(String message) {
        super(message);
    }
}
