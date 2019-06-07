package com.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(Long id){
        super("Could not find message with ID: " + id);
    }
}
