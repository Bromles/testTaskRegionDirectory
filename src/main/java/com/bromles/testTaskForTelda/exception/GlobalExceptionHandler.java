package com.bromles.testTaskForTelda.exception;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.bromles.testTaskForTelda.exception.ExceptionResponseEntityGenerator.generate;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return generate(HttpStatus.BAD_REQUEST, new ImmutablePair<>("errors", errors));
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatchException(TypeMismatchException ex) {
        return generate(HttpStatus.BAD_REQUEST, new ImmutablePair<>("message",
                String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                        ex.getPropertyName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName())));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return generate(HttpStatus.BAD_REQUEST, new ImmutablePair<>("message", "Unformed JSON in request body"));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return generate(HttpStatus.INTERNAL_SERVER_ERROR, new ImmutablePair<>("message", "Exception handler not found"));
    }
}
