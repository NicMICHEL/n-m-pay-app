package com.paynet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InsufficientAccountBalanceException extends Exception {
    public InsufficientAccountBalanceException(String message) {
        super(message);
    }
}
