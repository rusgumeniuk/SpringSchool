package com.example.loaders;

import com.example.Room;
import com.example.repositories.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class LoadDBRoom {
    @Bean
    public CommandLineRunner initDbRoom(RoomRepository repository){
        return args ->{
            List<Room> checkList = repository.findAll();
            if(checkList.isEmpty()){
                log.info("Preloading room " + repository.save((new Room(402))));
                log.info("Preloading room" + repository.save((new Room(518))));
            }
            else{
                log.info("Current rows in table room: ");
                repository.findAll().forEach(room -> log.info(room.getId() + ":" + room.getNumber() + " in " + room.getBuilding()));
            }
        };
    }
}