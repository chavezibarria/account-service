package com.jgci.springboot.app.account.handler;

import com.jgci.springboot.app.account.exceptions.AccountNotFoundException;
import com.jgci.springboot.app.account.exceptions.MaximumTransactionReachedException;
import com.jgci.springboot.app.account.exceptions.NotEnoughBalanceException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = AccountNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage AccountNotFoundException(AccountNotFoundException ex) {
        ErrorMessage message = new ErrorMessage(
                ex.getStatus(),
                ex.getMessage()
        );

        return message;
    }

    @ExceptionHandler(value = NotEnoughBalanceException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage NotEnoughBalanceException(NotEnoughBalanceException ex) {
        ErrorMessage message = new ErrorMessage(
                ex.getStatus(),
                ex.getMessage()
        );

        return message;
    }

    @ExceptionHandler(value = MaximumTransactionReachedException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage MaximumTransactionReachedException(MaximumTransactionReachedException ex) {
        ErrorMessage message = new ErrorMessage(
                ex.getStatus(),
                ex.getMessage()
        );

        return message;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}