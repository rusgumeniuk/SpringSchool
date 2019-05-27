package com.example.controllers;

import com.example.Building;
import com.example.Room;
import com.example.assemblers.BuildingResourcesAssembler;
import com.example.assemblers.RoomResourcesAssembler;
import com.example.exceptions.BuildingNotFoundException;
import com.example.exceptions.BuildingDoesNotHaveRoomExceprion;
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
@RequestMapping("/buildings")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;
    @Autowired
    private RoomService roomService;

    private final BuildingResourcesAssembler assembler;
    private final RoomResourcesAssembler roomAssembler;

    public BuildingController(BuildingResourcesAssembler assembler, RoomResourcesAssembler roomResourcesAssembler) {
        this.assembler = assembler;
        this.roomAssembler = roomResourcesAssembler;
    }

    @GetMapping
    public Resources<Resource<Building>> getBuildings() {
        List<Resource<Building>> list = buildingService.getAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(
                list,
                linkTo(methodOn(BuildingController.class).getBuildings()).withSelfRel()
        );
    }

    @GetMapping(value = "/{buildingId}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<ResourceSupport> getBuilding(@PathVariable Integer buildingId) {
        Building building = buildingService.getObjectById(buildingId);
        return ResponseEntity.ok(assembler.toResource(building));
    }

    @PostMapping
    public ResponseEntity<?> createBuilding(@Valid @RequestBody Building newBuilding) throws URISyntaxException {
        Resource<Building> resource = assembler.toResource(buildingService.saveObject(newBuilding));
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @PutMapping(value = "/{buildingId}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> updateBuilding(@Valid @RequestBody Building updatedBuilding, @PathVariable Integer buildingId) throws URISyntaxException {
        Building updatedObj = buildingService.updateObject(updatedBuilding, buildingId);

        Resource<Building> resource = assembler.toResource(updatedObj);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/{buildingId}/delall")
    public ResponseEntity<?> deleteBuildingAndRooms(@PathVariable Integer buildingId) {
        try{
            buildingService.deleteObject(buildingId);
        }
        catch (BuildingNotFoundException ex){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{buildingId}")
    public ResponseEntity<?> deleteBuildingAndSaveRooms(@PathVariable Integer buildingId) {
        Building building = buildingService.getObjectById(buildingId);
        Room[] rooms = new Room[building.getRooms().size()];
        rooms = building.getRooms().toArray(rooms);
        for (Room stud :
                rooms) {
            Room room = roomService.getObjectById(stud.getId());
            room.setBuilding(null);
            building.removeRoom(room);
            building.setRooms(building.getRooms());
            roomService.saveObject(room);
            buildingService.saveObject(building);
        }
        return deleteBuildingAndRooms(buildingId);
    }

    @GetMapping("/{buildingId}/rooms")
    public Resources<Resource<Room>> getRoomsOfBuilding(@PathVariable Integer buildingId) {
        var building = buildingService.getObjectById(buildingId);
        List<Resource<Room>> list = building.getRooms()
                .stream()
                .map(roomAssembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(
                list,
                linkTo(methodOn(BuildingController.class).getBuilding(buildingId)).withSelfRel(),
                linkTo(methodOn(RoomController.class).getRooms()).withSelfRel()
        );
    }

    @DeleteMapping("/{buildingId}/rooms/{roomId}")
    public Resources<Resource<Room>> removeRoomFromBuilding(@PathVariable Integer buildingId, @PathVariable Integer roomId) {
        Room room = roomService.getObjectById(roomId);
        Building building = buildingService.getObjectById(buildingId);
        if (!building.getRooms().contains(room))
            throw new BuildingDoesNotHaveRoomExceprion(building, room);
        else {
            room.setBuilding(null);
            building.removeRoom(room);
            building.setRooms(building.getRooms());
            buildingService.saveObject(building);
            roomService.saveObject(room);
        }
        return getRoomsOfBuilding(buildingId);
    }
}