package br.com.sys.productapi.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionGlobalHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException exception) {
        var details = new ExceptionDetails();
        details.setStatus(HttpStatus.BAD_REQUEST.value());
        details.setMessage(exception.getMessage());
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException exception) {
        var details = new ExceptionDetails();
        details.setStatus(HttpStatus.UNAUTHORIZED.value());
        details.setMessage(exception.getMessage());
        return new ResponseEntity<>(details, HttpStatus.UNAUTHORIZED);
    }
}
