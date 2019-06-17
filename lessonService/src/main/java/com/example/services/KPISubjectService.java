package com.example.services;

import com.example.Subject;
import com.example.exceptions.SubjectNotFoundException;
import com.example.repositories.SubjectRepository;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KPISubjectService implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public List<Subject> getAll() {
        return subjectRepository.findAll()
                .stream()
                .filter(subject -> !subject.getDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Subject getObjectById(Integer integer) {
        var foundSubject = subjectRepository.findById(integer);
        if(foundSubject.isPresent()){
            if (!foundSubject.get().getDeleted())
                return foundSubject.get();
        }
        throw new SubjectNotFoundException(integer);
    }

    @Override
    public Subject saveObject(Subject newObject) {
        return subjectRepository.save(newObject);
    }

    @Override
    public void deleteObject(Integer integer) {
        Subject foundSubject = getObjectById(integer);
        foundSubject.delete();
        subjectRepository.save(foundSubject);
    }

    @Override
    public Subject updateObject(Subject newObject, Integer integer) {
        Subject foundSubject = getObjectById(integer);
        foundSubject.setTitle(newObject.getTitle());
        foundSubject.setControlType(newObject.getControlType());
        return subjectRepository.save(foundSubject);
    }
}
