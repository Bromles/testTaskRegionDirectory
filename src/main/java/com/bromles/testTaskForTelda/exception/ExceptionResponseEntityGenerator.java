package com.bromles.testTaskForTelda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExceptionResponseEntityGenerator {

    public static ResponseEntity<Object> generate(HttpStatus status, String fieldName, Object value) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put(fieldName, value);

        return new ResponseEntity<>(body, status);
    }
}
