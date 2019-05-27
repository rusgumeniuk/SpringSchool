package com.example.controllers;

import com.example.Building;
import com.example.Room;
import com.example.assemblers.BuildingResourcesAssembler;
import com.example.assemblers.RoomResourcesAssembler;
import com.example.exceptions.RoomNotFoundException;
import com.example.services.BuildingService;
import com.example.services.RoomService;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    BuildingService buildingService;
    private final RoomResourcesAssembler assembler;
    private final BuildingResourcesAssembler buildingAssembler;

    public RoomController(RoomResourcesAssembler assembler, BuildingResourcesAssembler buildingResourcesAssembler) {
        this.assembler = assembler;
        this.buildingAssembler = buildingResourcesAssembler;
    }

    @GetMapping
    public Resources<Resource<Room>> getRooms() {
        List<Resource<Room>> list = roomService.getAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(
                list,
                linkTo(methodOn(RoomController.class).getRooms()).withSelfRel()
        );
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ResourceSupport> getRoom(@PathVariable Integer roomId) {
        var room = roomService.getObjectById(roomId);
        return ResponseEntity.ok(assembler.toResource(room));
    }

    @PostMapping
    public ResponseEntity<?> createRoom(@Valid @RequestBody Room newRoom) throws URISyntaxException {
        Resource<Room> resource = assembler.toResource(roomService.saveObject(newRoom));
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<?> updateRoom(@Valid @RequestBody Room updatedRoom, @PathVariable Integer roomId) throws URISyntaxException {
        Room updatedObj = roomService.updateObject(updatedRoom, roomId);
        Resource<Room> resource = assembler.toResource(updatedObj);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable Integer roomId) {
        try {
            changeRoomBuilding(roomId, -2);
            roomService.deleteObject(roomId);
        } catch (RoomNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception ex){
            System.out.println("ATTENTION " + ex.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{roomId}/building")
    public ResponseEntity<ResourceSupport> getBuildingOfRoom(@PathVariable Integer roomId) {
        Room room = roomService.getObjectById(roomId);
        if (room.getBuilding() == null)
            return ResponseEntity.noContent().build();
        var building = buildingService.getObjectById(room.getBuilding().getId());
        return  ResponseEntity
                .ok(buildingAssembler.toResource(building));
    }


    @PostMapping("/{roomId}/building/{newBuildingId}")
    public Resources<Resource<Room>> changeRoomBuilding(@PathVariable Integer roomId, @PathVariable Integer newBuildingId) {
        Room room = roomService.getObjectById(roomId);
        removeRoomFromBuilding(room);
        return addRoomToBuilding(room, newBuildingId);
    }

    private void removeRoomFromBuilding(Room room) {
        if (room.getBuilding() != null) {
            Building roomBuilding = buildingService.getObjectById(room.getBuilding().getId());
            roomBuilding.removeRoom(room);
            roomBuilding.setRooms(roomBuilding.getRooms());
            buildingService.saveObject(roomBuilding);
            roomService.saveObject(room);
        }
    }

    private Resources<Resource<Room>> addRoomToBuilding(Room room, Integer buildingId) {
        Room stud = roomService.getObjectById(room.getId());

        if (buildingId == -2)
            return null;
        Building building = buildingService.getObjectById(buildingId);
        stud.setBuilding(building);
        building.addRoom(stud);
        building.setRooms(building.getRooms());
        buildingService.saveObject(building);
        roomService.saveObject(stud);
        return getRoomsOfBuilding(buildingId);
    }

    private Resources<Resource<Room>> getRoomsOfBuilding(Integer buildingId) {
        var building = buildingService.getObjectById(buildingId);
        List<Resource<Room>> list = building.getRooms()
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(
                list,
                linkTo(methodOn(BuildingController.class).getBuilding(buildingId)).withSelfRel(),
                linkTo(methodOn(RoomController.class).getRooms()).withSelfRel()
        );
    }
}