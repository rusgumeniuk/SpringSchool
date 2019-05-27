package com.example.services;

import com.example.Group;
import com.example.exceptions.GroupNotFoundException;
import com.example.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.experimental.var;

@Service
public class KPIGroupService implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public List<Group> getAll() {
        List<Group> allGroups = groupRepository.findAll();
        List<Group> notDeletedGroup = new ArrayList<>();
        for (Group group : allGroups) {
            if (!group.getDeleted())
                notDeletedGroup.add(group);
        }
        return notDeletedGroup;
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
        Group foundGroup = groupRepository.getOne(integer);

        if (foundGroup.getDeleted())
            throw new GroupNotFoundException(foundGroup.getId());

        foundGroup.delete();
        groupRepository.save(foundGroup);
    }

    @Override
    public Group updateObject(Group newObject, Integer integer) {
        Optional<Group> foundGroup = groupRepository.findById(integer);
        if (foundGroup.get().getDeleted())
            throw new GroupNotFoundException(integer);

        if (foundGroup.isPresent()) {
            foundGroup
                    .map(group -> {
                        group.setTitle(newObject.getTitle());
                        group.setStudents(newObject.getStudents());
                        group.setCathedra(newObject.getCathedra());
                        group.setStartYear(newObject.getStartYear());
                        group.setId(newObject.getId());
                        return groupRepository.save(group);
                    })
                    .orElseGet(() -> {
                        newObject.setId(integer);
                        return groupRepository.save(newObject);
                    });
        }
        throw new GroupNotFoundException(integer);
    }
}
