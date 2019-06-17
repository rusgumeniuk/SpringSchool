package com.example.services;

import com.example.exceptions.LessonNotFoundException;
import com.example.lessons.Lesson;
import com.example.repositories.LessonRepository;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KPILessonService implements LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public List<Lesson> getAll() {
        return lessonRepository.findAll()
                .stream()
                .filter(lesson -> !lesson.getDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Lesson getObjectById(Integer integer) {
        var foundLesson = lessonRepository.findById(integer);
        if(foundLesson.isPresent()){
            if (!foundLesson.get().getDeleted())
                return foundLesson.get();
        }
        throw new LessonNotFoundException(integer);
    }

    @Override
    public Lesson saveObject(Lesson newObject) {
        return lessonRepository.save(newObject);
    }

    @Override
    public void deleteObject(Integer integer) {
        Lesson foundLesson = getObjectById(integer);
        foundLesson.delete();
        lessonRepository.save(foundLesson);
    }

    @Override
    public Lesson updateObject(Lesson newObject, Integer integer) {
        Lesson foundLesson = getObjectById(integer);
        foundLesson.setDayOfWeek(newObject.getDayOfWeek());
        foundLesson.setLessonNumber(newObject.getLessonNumber());
        foundLesson.setLessonType(newObject.getLessonType());
        foundLesson.setWeekMode(newObject.getWeekMode());
        foundLesson.setGroup(newObject.getGroup());
        foundLesson.setRoom(newObject.getRoom());
        foundLesson.setTeacher(newObject.getTeacher());
        foundLesson.setSubject(newObject.getSubject());
        return lessonRepository.save(foundLesson);
    }
}
