package com.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class GroupNotFoundException extends RuntimeException{
    public GroupNotFoundException(Integer id){
        super("Could not find group with ID: " + id);
    }
}
