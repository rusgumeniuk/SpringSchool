package com.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {

    public ValidationException(List<String> validationMessages) {
        super("Invalid request: " + String.join(", ", validationMessages));
    }
}
