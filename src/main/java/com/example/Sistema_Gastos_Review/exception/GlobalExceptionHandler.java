package com.example.Sistema_Gastos_Review.exception;

import org.hibernate.StaleObjectStateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(StaleObjectStateException.class)
    public ResponseEntity<Map<String, Object>> handleStaleObjectStateException(StaleObjectStateException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", 409);
        body.put("error", "Conflito de versão");
        body.put("message", "A conta foi alterada por outra transação. Tente novamente.");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
}
