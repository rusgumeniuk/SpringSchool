package com.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TeacherNotFoundException extends RuntimeException {
    public TeacherNotFoundException(Integer id){
        super("Could not find teacher with ID: " + id);
    }
}
