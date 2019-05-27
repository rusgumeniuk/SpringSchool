package com.example.services;

import com.example.Building;
import com.example.exceptions.BuildingNotFoundException;
import com.example.repositories.BuildingRepository;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KPIBuildingService implements BuildingService {

    @Autowired
    private BuildingRepository buildingRepository;

    @Override
    public List<Building> getAll() {
        List<Building> allBuildings = buildingRepository.findAll();
        List<Building> notDeletedBuildings = new ArrayList<>();
        for (Building building : allBuildings) {
            if(!building.getDeleted())
                notDeletedBuildings.add(building);
        }
        return notDeletedBuildings;
    }

    @Override
    public Building getObjectById(Integer integer) {
        var foundBuilding = buildingRepository.findById(integer);
        if(foundBuilding.isPresent()){
            if (!foundBuilding.get().getDeleted())
                return foundBuilding.get();
        }
        throw new BuildingNotFoundException(integer);
    }

    @Override
    public Building saveObject(Building newObject) {
        return buildingRepository.save(newObject);
    }

    @Override
    public void deleteObject(Integer integer) {
        Building foundBuilding = getObjectById(integer);
        foundBuilding.delete();
        buildingRepository.save(foundBuilding);
    }

    @Override
    public Building updateObject(Building newObject, Integer integer) {
        Building foundBuilding = getObjectById(integer);
        foundBuilding.setNumber(newObject.getNumber());
        foundBuilding.setCountOfStoreys(newObject.getCountOfStoreys());
        foundBuilding.setRooms(newObject.getRooms());
        return buildingRepository.save(foundBuilding);
    }
}
