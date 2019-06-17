package com.example.controllers;

import com.example.Group;
import com.example.Student;

import com.example.exceptions.*;
import com.example.services.GroupService;
import com.example.services.StudentService;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;
    @Autowired
    private StudentService studentService;

    @GetMapping
    public Collection<Group> getGroups() {
        return groupService.getAll();
    }

    @GetMapping(value = "/{groupId}", produces = "application/json; charset=UTF-8")
    public Group getGroup(@PathVariable Integer groupId) {
        return groupService.getObjectById(groupId);
    }

    @PostMapping
    public Group createGroup(@Valid @RequestBody Group newGroup) {
        return groupService.saveObject(newGroup);
    }

    @PutMapping(value = "/{groupId}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public Group updateGroup(@Valid @RequestBody Group updatedGroup, @PathVariable Integer groupId) {
        return groupService.updateObject(updatedGroup, groupId);
    }

    @DeleteMapping("/{groupId}/delall")
    public ResponseEntity<?> deleteGroupAndStudents(@PathVariable Integer groupId) {
        try{
            groupService.deleteObject(groupId);
        }
        catch (GroupNotFoundException ex){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<?> deleteGroupAndSaveStudents(@PathVariable Integer groupId) {
        Group group = groupService.getObjectById(groupId);
        Student[] students = new Student[group.getStudents().size()];
        students = group.getStudents().toArray(students);
        for (Student stud :
                students) {
            Student student = studentService.getObjectById(stud.getId());
            student.setGroup(null);
            group.removeStudent(student);
            group.setStudents(group.getStudents());
            studentService.saveObject(student);
            groupService.saveObject(group);
        }
        return deleteGroupAndStudents(groupId);
    }

    @GetMapping("/{groupId}/students")
    public List<Student> getStudentsOfGroup(@PathVariable Integer groupId) {
        var group = groupService.getObjectById(groupId);
        return group.getStudents();
    }

    @DeleteMapping("/{groupId}/students/{studentId}")
    public List<Student> removeStudentFromGroup(@PathVariable Integer groupId, @PathVariable Integer studentId) {
        Student student = studentService.getObjectById(studentId);
        Group group = groupService.getObjectById(groupId);
        if (!group.getStudents().contains(student))
            throw new GroupNotHaveStudentsException(group, student);
        else {
            student.setGroup(null);
            group.removeStudent(student);
            group.setStudents(group.getStudents());
            groupService.saveObject(group);
            studentService.saveObject(student);
        }
        return getStudentsOfGroup(groupId);
    }
}