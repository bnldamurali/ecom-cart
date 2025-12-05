package com.ecom.cart.exception;

import com.ecom.cart.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String BAD_REQUEST_ERROR_MSG="BAD Request Error: {}";
    private static final String ERROR_MSG="Unexpected error occurred: {}";

    private static final String UNEXPECTED_ERROR_MSG="An unexpected error occurred";

    private static final String PRODUCT_NOT_FOUND_ERROR_MSG="Product Not Found Error: {}";
    private static final String ERROR_MESSAGE="error";

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(HandlerMethodValidationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getAllErrors().forEach(error -> {
            if (error instanceof FieldError fieldError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            } else {
                errors.put(ERROR_MESSAGE, error.getDefaultMessage());
            }
        });
        logger.error(BAD_REQUEST_ERROR_MSG, errors);
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), errors),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        logger.error(BAD_REQUEST_ERROR_MSG, errors);
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), errors),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProbeUserNotFoundException(ProductNotFoundException ex) {
        logger.error(PRODUCT_NOT_FOUND_ERROR_MSG, ex.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.NOT_FOUND.toString(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), null),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error(ERROR_MSG, ex.getMessage(), ex);
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message(UNEXPECTED_ERROR_MSG)
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .timestamp(LocalDateTime.now()).build()
                ,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
