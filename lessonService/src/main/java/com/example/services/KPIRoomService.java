package com.example.services;

import com.example.Room;
import com.example.exceptions.RoomNotFoundException;
import com.example.repositories.RoomRepository;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KPIRoomService implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<Room> getAll() {
        return roomRepository.findAll()
                .stream()
                .filter(room -> !room.getDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Room getObjectById(Integer integer) {
        var foundRoom = roomRepository.findById(integer);
        if(foundRoom.isPresent()){
            if (!foundRoom.get().getDeleted())
                return foundRoom.get();
        }
        throw new RoomNotFoundException(integer);
    }

    @Override
    public Room saveObject(Room newObject) {
        return roomRepository.save(newObject);
    }

    @Override
    public void deleteObject(Integer integer) {
        Room foundRoom = getObjectById(integer);
        foundRoom.delete();
        roomRepository.save(foundRoom);
    }

    @Override
    public Room updateObject(Room newObject, Integer integer) {
        Room foundRoom = getObjectById(integer);
        foundRoom.setNumber(newObject.getNumber());
        return roomRepository.save(foundRoom);
    }
}
