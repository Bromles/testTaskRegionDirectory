package com.bromles.testTaskForTelda.exception;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExceptionResponseEntityGenerator {

    public static ResponseEntity<Object> generate(HttpStatus status, Pair<String, Object> pair) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put(pair.getKey(), pair.getValue());

        return new ResponseEntity<>(body, status);
    }
}
