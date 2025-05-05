package app.netbooks.backend.advices;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import app.netbooks.backend.errors.HttpError;
import app.netbooks.backend.errors.RouteNotFound;
import app.netbooks.backend.errors.ValidationsError;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(ValidationsError.class)
    public ResponseEntity<Map<String, Object>> handleHttpErrors(ValidationsError error) {
        return ResponseEntity
            .status(error.getStatus())
            .body(error.getError());
    };

    @ExceptionHandler(HttpError.class)
    public ResponseEntity<Map<String, Object>> handleHttpErrors(HttpError error) {
        return ResponseEntity
            .status(error.getStatus())
            .body(error.getError());
    };

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleHandlerNotFound() {
        RouteNotFound error = new RouteNotFound();
        return ResponseEntity
            .status(error.getStatus())
            .body(error.getError());
    };
};