package com.example.loaders;

import com.example.Building;
import com.example.repositories.BuildingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class LoadDBBuilding {
    @Bean
    public CommandLineRunner initDbBuilding(BuildingRepository repository){
        return args ->{
            List<Building> checkList = repository.findAll();
            if(checkList.isEmpty()){
                log.info("Preloading building " + repository.save((new Building(18, (short)5))));
                log.info("Preloading building" + repository.save((new Building(7, (short)9))));
            }
            else{
                log.info("Current rows in table building: ");
                repository.findAll().forEach(building -> log.info(building.getId() + ":" + building.getNumber()));
            }
        };
    }
}