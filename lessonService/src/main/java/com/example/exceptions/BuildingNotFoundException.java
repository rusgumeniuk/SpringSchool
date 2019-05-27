package com.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BuildingNotFoundException extends RuntimeException {
    public BuildingNotFoundException(Integer id){
        super("Could not find building with ID: " + id);
    }
}
