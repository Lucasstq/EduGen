package dev.lucas.edugen.EduGen.config.exceptions;

import dev.lucas.edugen.EduGen.dtos.apiError.ApiError;
import dev.lucas.edugen.EduGen.eduGenException.businessExeception.*;
import dev.lucas.edugen.EduGen.eduGenException.infrastructureException.AiSpecGenerationException;
import dev.lucas.edugen.EduGen.eduGenException.infrastructureException.PdfGenerationException;
import dev.lucas.edugen.EduGen.eduGenException.resourceNotFoundException.UserNotFoundException;
import dev.lucas.edugen.EduGen.eduGenException.resourceNotFoundException.WorksheetNotFoundException;
import dev.lucas.edugen.EduGen.eduGenException.resourceNotFoundException.WorksheetVersionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.InvalidClassException;
import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> genericException(Exception ex) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .errors(List.of(ex.getMessage()))
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            WorksheetNotFoundException.class,
            WorksheetVersionNotFoundException.class
    })
    public ResponseEntity<ApiError> notFoundException(RuntimeException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND.name())
                .errors(List.of(ex.getMessage()))
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            AiSpecGenerationException.class,
            PdfGenerationException.class
    })
    public ResponseEntity<ApiError> infrastructureException(RuntimeException ex) {
        log.error("Infrastructure error: {}", ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .errors(List.of(ex.getMessage()))
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            EmailAlreadyExistsException.class,
            EmailException.class,
            PasswordMustDifferentException.class,
            InvalidClassException.class,
            PasswordMismatchException.class,
            UsernameAlreadyExistsException.class,
            EmailTokenInvalidException.class
    })
    public ResponseEntity<ApiError> businessException(RuntimeException ex){
        log.warn("Business rule violation: {}", ex.getMessage());
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.CONFLICT.value())
                .status(HttpStatus.CONFLICT.name())
                .errors(List.of(ex.getMessage()))
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        log.warn("Validation errors: {}", errors);

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.name())
                .errors(errors)
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


}
