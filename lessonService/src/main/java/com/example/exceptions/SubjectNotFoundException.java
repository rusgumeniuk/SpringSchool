package com.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SubjectNotFoundException extends RuntimeException {
    public SubjectNotFoundException(Integer id){
        super("Could not find subject with ID: " + id);
    }
}
