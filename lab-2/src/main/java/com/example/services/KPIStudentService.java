package com.example.services;

import com.example.Student;
import com.example.exceptions.StudentNotFoundException;
import com.example.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.experimental.var;

@Service
public class KPIStudentService implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Student> getAll() {
        List<Student> allStudents = studentRepository.findAll();
        List<Student> notDeletedStudents = new ArrayList<>();
        for (Student student : allStudents) {
            if(!student.getDeleted())
                notDeletedStudents.add(student);
        }
        return notDeletedStudents;
    }

    @Override
    public Student getObjectById(Integer integer) {
        var foundStudent = studentRepository.findById(integer);
        if(foundStudent.isPresent()){
            if (!foundStudent.get().getDeleted())
                return foundStudent.get();
        }
        throw new StudentNotFoundException(integer);
    }

    @Override
    public Student saveObject(Student newObject) {
        return studentRepository.save(newObject);
    }

    @Override
    public void deleteObject(Integer integer) {
        Student foundStudent = getObjectById(integer);
        foundStudent.delete();
        studentRepository.save(foundStudent);
    }

    @Override
    public Student updateObject(Student newObject, Integer integer) {
        Student foundStudent = getObjectById(integer);
        foundStudent.setName(newObject.getName());
        foundStudent.setAge(newObject.getAge());
        foundStudent.setMale(newObject.getMale());
        foundStudent.setCity(newObject.getCity());
        return studentRepository.save(foundStudent);
    }
}
