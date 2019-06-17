package com.example.services;

import com.example.Group;
import com.example.exceptions.GroupNotFoundException;
import com.example.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.experimental.var;

@Service
public class KPIGroupService implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public List<Group> getAll() {
        return groupRepository.findAll()
                .stream()
                .filter(group -> !group.getDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Group getObjectById(Integer integer) {
        var foundGroup = groupRepository.findById(integer);

        if (foundGroup.isPresent()) {
            if (!foundGroup.get().getDeleted())
                return foundGroup.get();
        }
        throw new GroupNotFoundException(integer);
    }

    @Override
    public Group saveObject(Group newObject) {
        return groupRepository.save(newObject);
    }

    @Override
    public void deleteObject(Integer integer) {
        Group foundGroup = getObjectById(integer);
        foundGroup.delete();
        groupRepository.save(foundGroup);
    }

    @Override
    public Group updateObject(Group newObject, Integer integer) {
        Group foundGroup = getObjectById(integer);
        foundGroup.setTitle(newObject.getTitle());
        foundGroup.setCathedra(newObject.getCathedra());
        foundGroup.setStartYear(newObject.getStartYear());
        return groupRepository.save(foundGroup);
    }
}
