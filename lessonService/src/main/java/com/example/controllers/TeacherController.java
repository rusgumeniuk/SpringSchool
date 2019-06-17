package com.example.controllers;

import com.example.Group;
import com.example.Teacher;
import com.example.exceptions.TeacherDontMentorThisGroup;
import com.example.exceptions.TeacherNotFoundException;
import com.example.services.GroupService;
import com.example.services.TeacherService;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
@RequestMapping("/teachers")
public class TeacherController {


    @Autowired
    private TeacherService teacherService;
    @Autowired
    private GroupService groupService;

    @GetMapping
    public List<Teacher> getTeachers() {
        return teacherService.getAll();
    }

    @GetMapping(value = "/{teacherId}", produces = "application/json; charset=UTF-8")
    public Teacher getTeacher(@PathVariable Integer teacherId) {
        return teacherService.getObjectById(teacherId);        
    }

    @PostMapping
    public Teacher createTeacher(@Valid @RequestBody Teacher newTeacher){
        return teacherService.saveObject(newTeacher);
    }

    @PutMapping(value = "/{teacherId}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public Teacher updateTeacher(@Valid @RequestBody Teacher updatedTeacher, @PathVariable Integer teacherId){
        return teacherService.updateObject(updatedTeacher, teacherId);
    }

    @DeleteMapping("/{teacherId}/delall")
    public ResponseEntity<?> deleteTeacherAndMentoredGroup(@PathVariable Integer teacherId) {
        try{
            teacherService.deleteObject(teacherId);
        }
        catch (TeacherNotFoundException ex){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{teacherId}")
    public ResponseEntity<?> deleteTeacherAndSaveMentoredGroup(@PathVariable Integer teacherId) {
        Teacher teacher = teacherService.getObjectById(teacherId);
        if(teacher.getMentored_group() != null){
            Group mentoredGroup = groupService.getObjectById(teacher.getMentored_group().getId());
            removeGroupFromMentor(teacherId, teacher.getMentored_group().getId());
        }
        return deleteTeacherAndMentoredGroup(teacherId);
    }

    @GetMapping("/{teacherId}/group")
    public Group getMentoredGroupOfTeacher(@PathVariable Integer teacherId) {
        var teacher = teacherService.getObjectById(teacherId);
        return teacher.getMentored_group();
    }


    @DeleteMapping("/{teacherId}/group/{mentoredGroupId}")
    public Group removeGroupFromMentor(@PathVariable Integer teacherId, @PathVariable Integer mentoredGroupId) {
        Group group = groupService.getObjectById(mentoredGroupId);
        Teacher teacher = teacherService.getObjectById(teacherId);
        if (!(teacher.getMentored_group().getId() == group.getId()))
            throw new TeacherDontMentorThisGroup(teacher, group);
        else {
            group.setMentor(teacher);
            teacher.setMentored_group(group);
            teacherService.saveObject(teacher);
            groupService.saveObject(group);
        }
        return getMentoredGroupOfTeacher(teacherId);
    }

    @PostMapping("/{teacherId}/group/{mentoredGroupId}")
    public Group addGroupToMentor(@PathVariable Integer teacherId, @PathVariable Integer mentoredGroupId) {
        Group group = groupService.getObjectById(mentoredGroupId);
        Teacher teacher = teacherService.getObjectById(teacherId);
        teacher.setMentored_group(group);
        group.setMentor(teacher);
        teacherService.saveObject(teacher);
        groupService.saveObject(group);
        return getMentoredGroupOfTeacher(teacherId);
    }
}
