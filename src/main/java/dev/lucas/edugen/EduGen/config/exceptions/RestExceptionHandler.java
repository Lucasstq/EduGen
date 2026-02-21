package dev.lucas.edugen.EduGen.config.exceptions;

import dev.lucas.edugen.EduGen.dtos.apiError.ApiError;
import dev.lucas.edugen.EduGen.eduGenException.businessExeception.EmailAlreadyExistsException;
import dev.lucas.edugen.EduGen.eduGenException.businessExeception.PasswordMismatchException;
import dev.lucas.edugen.EduGen.eduGenException.businessExeception.UsernameAlreadyExistsException;
import dev.lucas.edugen.EduGen.eduGenException.infrastructureException.AiSpecGenerationException;
import dev.lucas.edugen.EduGen.eduGenException.infrastructureException.PdfGenerationException;
import dev.lucas.edugen.EduGen.eduGenException.resourceNotFoundException.UserNotFoundException;
import dev.lucas.edugen.EduGen.eduGenException.resourceNotFoundException.WorksheetNotFoundException;
import dev.lucas.edugen.EduGen.eduGenException.resourceNotFoundException.WorksheetVersionNotFoundException;
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> genericException(Exception ex) {
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
            InvalidClassException.class,
            PasswordMismatchException.class,
            UsernameAlreadyExistsException.class
    })
    public ResponseEntity<ApiError> businessException(RuntimeException ex){
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

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.name())
                .errors(errors)
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


}
