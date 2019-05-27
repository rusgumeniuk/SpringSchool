package com.example.exceptions;

import com.example.Room;
import com.example.Building;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BuildingDoesNotHaveRoomExceprion extends RuntimeException {
    public BuildingDoesNotHaveRoomExceprion(Building building, Room room){
        super("Building :" + building.getId() + " doesn't have room :" + room.getId());
    }

}
