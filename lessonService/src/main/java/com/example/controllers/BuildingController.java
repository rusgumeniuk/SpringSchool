package com.example.controllers;

import com.example.Building;
import com.example.Room;
import com.example.exceptions.BuildingDoesNotHaveRoomExceprion;
import com.example.exceptions.BuildingNotFoundException;
import com.example.services.BuildingService;
import com.example.services.RoomService;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/buildings")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;
    @Autowired
    private RoomService roomService;

    @GetMapping
    public List<Building> getBuildings() {
        return buildingService.getAll();
   
    }

    @GetMapping(value = "/{buildingId}", produces = "application/json; charset=UTF-8")
    public Building getBuilding(@PathVariable Integer buildingId) {
        return buildingService.getObjectById(buildingId);
    }

    @PostMapping
    public Building createBuilding(@Valid @RequestBody Building newBuilding){
        return buildingService.saveObject(newBuilding);
    }

    @PutMapping(value = "/{buildingId}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public Building updateBuilding(@Valid @RequestBody Building updatedBuilding, @PathVariable Integer buildingId){
        return buildingService.updateObject(updatedBuilding, buildingId);        
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
    public List<Room> getRoomsOfBuilding(@PathVariable Integer buildingId) {
        var building = buildingService.getObjectById(buildingId);
        return building.getRooms();
    }

    @DeleteMapping("/{buildingId}/rooms/{roomId}")
    public List<Room> removeRoomFromBuilding(@PathVariable Integer buildingId, @PathVariable Integer roomId) {
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