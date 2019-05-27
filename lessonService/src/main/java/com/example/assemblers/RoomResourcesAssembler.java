package com.example.assemblers;

import com.example.Room;
import com.example.controllers.RoomController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class RoomResourcesAssembler implements ResourceAssembler<Room, Resource<Room>> {
    @Override
    public Resource<Room> toResource(Room room) {
        return new Resource<>(
                room,
                linkTo(methodOn(RoomController.class).getRoom(room.getId())).withSelfRel(),
                linkTo(methodOn(RoomController.class).getRooms()).withRel("rooms")
        );
    }
}
