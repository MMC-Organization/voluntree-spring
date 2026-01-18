package com.voluntree.backend.exception;

public class ActivityNotFoundException extends RuntimeException {
    public ActivityNotFoundException(Long id) {
        super("Atividade não encontrada com ID: " + id);
    }
}
