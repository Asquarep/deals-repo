package com.example.clusteredDataWarehouse.exception;

import com.example.clusteredDataWarehouse.dto.response.ErrorResponse;
import com.example.clusteredDataWarehouse.enums.ResponseCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {


    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("Error Message: {}", ex.getMessage(), ex);
        return ErrorResponse.with(
                ResponseCodes.FAILURE.getValue(),
                ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage()
        );
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error("Invalid argument in request: {}", ex.getMessage(), ex);
        return composeFieldErrorResponse(ex.getBindingResult().getFieldErrors());
    }


    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBindException(BindException ex) {
        log.error("Bind exception: {}", ex.getMessage(), ex);
        return composeFieldErrorResponse(ex.getBindingResult().getFieldErrors());
    }

    private ErrorResponse composeFieldErrorResponse(List<FieldError> fieldErrors) {
        List<String> errors = fieldErrors
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                .code(ResponseCodes.FAILURE.getValue())
                .messages(errors)
                .build();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleValidationException(ValidationException ex) {
        log.error("VALIDATION ERROR: {}", ex.getMessage(), ex);
        return ErrorResponse.with(
                ResponseCodes.FAILURE.getValue(),
                ex.getMessage()
        );
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateException(DuplicateException ex) {
        log.error("DUPLICATE ERROR: {}", ex.getMessage(), ex);
        return ErrorResponse.with(
                ResponseCodes.FAILURE.getValue(),
                ex.getMessage()
        );
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(Exception ex) {
        log.error("GENERAL EXCEPTION: {}", ex.getMessage(), ex);

        return ErrorResponse.with(
                ResponseCodes.FAILURE.getValue(),
                "An error occurred while processing. Please try again later"
        );
    }
}
