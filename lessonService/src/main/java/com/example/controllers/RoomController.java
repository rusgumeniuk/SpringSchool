package com.example.controllers;

import com.example.Building;
import com.example.Room;
import com.example.exceptions.RoomNotFoundException;
import com.example.services.BuildingService;
import com.example.services.RoomService;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    BuildingService buildingService;
    @GetMapping
    public List<Room> getRooms() {
       return roomService.getAll();        
    }

    @GetMapping("/{roomId}")
    public Room getRoom(@PathVariable Integer roomId) {
        return roomService.getObjectById(roomId);        
    }

    @PostMapping
    public Room createRoom(@Valid @RequestBody Room newRoom){
        return roomService.saveObject(newRoom);
    }

    @PutMapping("/{roomId}")
    public Room updateRoom(@Valid @RequestBody Room updatedRoom, @PathVariable Integer roomId){
       return roomService.updateObject(updatedRoom, roomId);        
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
    public Building getBuildingOfRoom(@PathVariable Integer roomId) {
        Room room = roomService.getObjectById(roomId);
        if (room.getBuilding() == null)
            return null;
        return buildingService.getObjectById(room.getBuilding().getId());
    }

    @PostMapping("/{roomId}/building/{newBuildingId}")
    public List<Room> changeRoomBuilding(@PathVariable Integer roomId, @PathVariable Integer newBuildingId) {
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

    private List<Room> addRoomToBuilding(Room room, Integer buildingId) {
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

    private List<Room> getRoomsOfBuilding(Integer buildingId) {
        var building = buildingService.getObjectById(buildingId);
        return building.getRooms();
    }
}