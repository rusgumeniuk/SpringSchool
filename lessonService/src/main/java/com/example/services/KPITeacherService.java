package com.example.services;

import com.example.Teacher;
import com.example.exceptions.TeacherNotFoundException;
import com.example.repositories.TeacherRepository;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KPITeacherService implements TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public List<Teacher> getAll() {
        List<Teacher> allTeachers = teacherRepository.findAll();
        List<Teacher> notDeletedTeachers = new ArrayList<>();
        for (Teacher teacher : allTeachers) {
            if(!teacher.getDeleted())
                notDeletedTeachers.add(teacher);
        }
        return notDeletedTeachers;
    }

    @Override
    public Teacher getObjectById(Integer integer) {
        var foundTeacher = teacherRepository.findById(integer);
        if(foundTeacher.isPresent()){
            if (!foundTeacher.get().getDeleted())
                return foundTeacher.get();
        }
        throw new TeacherNotFoundException(integer);
    }

    @Override
    public Teacher saveObject(Teacher newObject) {
        return teacherRepository.save(newObject);
    }

    @Override
    public void deleteObject(Integer integer) {
        Teacher foundTeacher = getObjectById(integer);
        foundTeacher.delete();
        teacherRepository.save(foundTeacher);
    }

    @Override
    public Teacher updateObject(Teacher newObject, Integer integer) {
        Teacher foundTeacher = getObjectById(integer);
        foundTeacher.setFullName(newObject.getFullName());
        foundTeacher.setCathedra(newObject.getCathedra());
        foundTeacher.setTeacherRank(newObject.getTeacherRank());
        foundTeacher.setMentored_group(newObject.getMentored_group());
        return teacherRepository.save(foundTeacher);
    }
}
