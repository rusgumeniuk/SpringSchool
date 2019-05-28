package com.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class LessonNotFoundException extends RuntimeException {
    public LessonNotFoundException(Integer id){
        super("Could not find lesson with ID: " + id);
    }
}
