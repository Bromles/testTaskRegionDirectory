package com.bromles.test_task_region_directory.exception.handler;

import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений на уровне контроллеров
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Обрабатывает исключение, генерируемое при невозможности конвертировать параметр запроса в требуемый тип
     *
     * @param ex Тип: {@link TypeMismatchException}. Перехваченное исключение
     * @param headers Тип: {@link HttpHeaders}. Заголовки HTTP-запроса, вызвавшего исключение
     * @param status Тип: {@link HttpStatus}. Статус HTTP-запроса, вызвавшего исключение
     * @param request Тип: {@link WebRequest}. HTTP-запрос, вызвавший исключение
     * @return Возвращает сущность ответа сервера, содержащую статус 400 и сообщение об ошибке
     */
    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return generate(HttpStatus.BAD_REQUEST, "message",
                String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                        ex.getPropertyName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName()));
    }

    /**
     * Обрабатывает исключение, генерируемое при получении запроса с некорректным JSON в теле
     *
     * @param ex Тип: {@link HttpMessageNotReadableException}. Перехваченное исключение
     * @param headers Тип: {@link HttpHeaders}. Заголовки HTTP-запроса, вызвавшего исключение
     * @param status Тип: {@link HttpStatus}. Статус HTTP-запроса, вызвавшего исключение
     * @param request Тип: {@link WebRequest}. HTTP-запрос, вызвавший исключение
     * @return Возвращает сущность ответа сервера, содержащую статус 400 и сообщение об ошибке
     */
    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return generate(HttpStatus.BAD_REQUEST, "message", "Unformed JSON in request body");
    }

    /**
     * Обрабатывает исключение, генерируемое при провале валидации входных объектов трансфера данных
     *
     * @param ex Тип: {@link MethodArgumentNotValidException}. Перехваченное исключение
     * @param headers Тип: {@link HttpHeaders}. Заголовки HTTP-запроса, вызвавшего исключение
     * @param status Тип: {@link HttpStatus}. Статус HTTP-запроса, вызвавшего исключение
     * @param request Тип: {@link WebRequest}. HTTP-запрос, вызвавший исключение
     * @return Возвращает сущность ответа сервера, содержащую статус 400 и сообщение об ошибке
     */
    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return generate(HttpStatus.BAD_REQUEST, "errors", errors);
    }

    /**
     * Обрабатывает исключение, генерируемое при получении запроса с неверным типом содержимого
     *
     * @param ex Тип: {@link HttpMediaTypeNotSupportedException}. Перехваченное исключение
     * @param headers Тип: {@link HttpHeaders}. Заголовки HTTP-запроса, вызвавшего исключение
     * @param status Тип: {@link HttpStatus}. Статус HTTP-запроса, вызвавшего исключение
     * @param request Тип: {@link WebRequest}. HTTP-запрос, вызвавший исключение
     * @return Возвращает сущность ответа сервера, содержащую статус 415 и сообщение об ошибке
     */
    @Override
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return generate(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "message", "Server supports only application/json");
    }

    /**
     * Обрабатывает исключение, генерируемое при отсутствии подходящего обработчика для некоторого исключения
     *
     * @param ex Тип: {@link NoHandlerFoundException}. Перехваченное исключение
     * @param headers Тип: {@link HttpHeaders}. Заголовки HTTP-запроса, вызвавшего исключение
     * @param status Тип: {@link HttpStatus}. Статус HTTP-запроса, вызвавшего исключение
     * @param request Тип: {@link WebRequest}. HTTP-запрос, вызвавший исключение
     * @return Возвращает сущность ответа сервера, содержащую статус 500 и сообщение об ошибке
     */
    @Override
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return generate(HttpStatus.INTERNAL_SERVER_ERROR, "message", "Exception handler not found");
    }

    /**
     * Обрабатывает исключение, генерируемое при провале валидации входных параметров, не являющихся объектами
     * трансфера данных
     *
     * @param ex Тип: {@link ConstraintViolationException}. Перехваченное исключение
     * @return Возвращает сущность ответа сервера, содержащую статус 400 и сообщение об ошибке
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(x -> x.getConstraintDescriptor().getMessageTemplate())
                .collect(Collectors.toList());

        return generate(HttpStatus.BAD_REQUEST, "errors", errors);
    }

    /**
     * Генерирует сущность ответа сервера, , содержащую HTTP-статус и сообщение об ошибке
     *
     * @param status Тип: {@link HttpStatus}. Статус генерируемого ответа сервера
     * @param fieldName Тип: {@link String}. Название поля с информацией об ошибке в теле ответа
     * @param value Тип: {@link Object}. Значение поля с информацией об ошибке в теле ответа
     * @return Возвращает сущность ответа сервера, содержащую HTTP-статус и сообщение об ошибке
     */
    private static ResponseEntity<Object> generate(HttpStatus status, String fieldName, Object value) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put(fieldName, value);

        return ResponseEntity.status(status).body(body);
    }
}
