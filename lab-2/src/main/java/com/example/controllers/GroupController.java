package com.example.controllers;

import com.example.Group;
import com.example.Student;
import com.example.assemblers.GroupResourcesAssembler;
import com.example.assemblers.StudentResourcesAssembler;
import com.example.exceptions.*;
import com.example.services.GroupService;
import com.example.services.StudentService;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;
    @Autowired
    private StudentService studentService;

    private final GroupResourcesAssembler assembler;
    private final StudentResourcesAssembler studentAssembler;

    public GroupController(GroupResourcesAssembler assembler, StudentResourcesAssembler studentResourcesAssembler) {
        this.assembler = assembler;
        this.studentAssembler = studentResourcesAssembler;
    }

    @GetMapping
    public Collection<Group> getGroups() {
        return groupService.getAll().stream()
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{groupId}", produces = "application/json; charset=UTF-8")
    public Group getGroup(@PathVariable Integer groupId) {
        return groupService.getObjectById(groupId);
    }

    @PostMapping
    public Group createGroup(@Valid @RequestBody Group newGroup) throws URISyntaxException {
        return groupService.saveObject(newGroup);
    }

    @PutMapping(value = "/{groupId}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public Group updateGroup(@Valid @RequestBody Group updatedGroup, @PathVariable Integer groupId) throws URISyntaxException {
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
        return group.getStudents()
                .stream()
                .collect(Collectors.toList());
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