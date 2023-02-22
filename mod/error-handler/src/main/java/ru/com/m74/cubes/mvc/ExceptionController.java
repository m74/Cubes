package ru.com.m74.cubes.mvc;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Обработчик исключительных ситуаций.
 * Перехватывает все подряд для отображения на UI
 *
 * @author mixam
 * @since 10.03.16 17:17
 */
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler(Exception ex) {
        if (ex instanceof ResponseStatusException) {
            return new ResponseEntity<>(ex, ((ResponseStatusException) ex).getStatus());
        } else {
            return new ResponseEntity<>(new ExceptionDTO(ex), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
