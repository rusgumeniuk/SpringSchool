package com.example.controllers;

import com.example.exceptions.LessonNotFoundException;
import com.example.lessons.Lesson;
import com.example.services.GroupService;
import com.example.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;
    @Autowired
    GroupService groupService;

    @GetMapping
    public List<Lesson> getLessons() {
        return lessonService.getAll();
    }

    @GetMapping("/{lessonId}")
    public Lesson getLesson(@PathVariable Integer lessonId) {
        return lessonService.getObjectById(lessonId);
    }

    @PostMapping
    public Lesson createLesson(@Valid @RequestBody Lesson newLesson){
        return lessonService.saveObject(newLesson);
    }

    @PutMapping("/{lessonId}")
    public Lesson updateLesson(@Valid @RequestBody Lesson updatedLesson, @PathVariable Integer lessonId){
        return lessonService.updateObject(updatedLesson, lessonId);
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<?> deleteLesson(@PathVariable Integer lessonId) {
        try {
            lessonService.deleteObject(lessonId);
        } catch (LessonNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception ex){
            System.out.println("ATTENTION " + ex.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}