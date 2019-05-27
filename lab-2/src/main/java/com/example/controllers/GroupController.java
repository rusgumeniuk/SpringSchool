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
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
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
    public Resources<Resource<Group>> getGroups() {
        List<Resource<Group>> list = groupService.getAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(
                list,
                linkTo(methodOn(GroupController.class).getGroups()).withSelfRel()
        );
    }

    @GetMapping(value = "/{groupId}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<ResourceSupport> getGroup(@PathVariable Integer groupId) {
        Group group = groupService.getObjectById(groupId);
        return ResponseEntity.ok(assembler.toResource(group));
    }

    @PostMapping
    public ResponseEntity<?> createGroup(@Valid @RequestBody Group newGroup) throws URISyntaxException {
        Resource<Group> resource = assembler.toResource(groupService.saveObject(newGroup));
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @PutMapping(value = "/{groupId}", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> updateGroup(@Valid @RequestBody Group updatedGroup, @PathVariable Integer groupId) throws URISyntaxException {
        Group updatedObj = groupService.updateObject(updatedGroup, groupId);

        Resource<Group> resource = assembler.toResource(updatedObj);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/{groupId}/delall")
    public ResponseEntity<?> deleteGroupAndStudents(@PathVariable Integer groupId) {
        groupService.deleteObject(groupId);
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
    public Resources<Resource<Student>> getStudentsOfGroup(@PathVariable Integer groupId) {
        var group = groupService.getObjectById(groupId);
        List<Resource<Student>> list = group.getStudents()
                .stream()
                .map(studentAssembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(
                list,
                linkTo(methodOn(GroupController.class).getGroup(groupId)).withSelfRel(),
                linkTo(methodOn(StudentController.class).getStudents()).withSelfRel()
        );
    }

    @DeleteMapping("/{groupId}/students/{studentId}")
    public Resources<Resource<Student>> removeStudentFromGroup(@PathVariable Integer groupId, @PathVariable Integer studentId) {
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

   /* @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ValidationError handleException(MethodArgumentNotValidException exception) {
        return createValidationError(exception);
    }

    private ValidationError createValidationError(MethodArgumentNotValidException e) {
        return ValidationErrorBuilder.fromBindingErrors(e.getBindingResult());
    }*/
}