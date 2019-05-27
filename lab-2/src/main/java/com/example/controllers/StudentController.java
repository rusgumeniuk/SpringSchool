package com.example.controllers;

import com.example.Group;
import com.example.Student;
import com.example.assemblers.GroupResourcesAssembler;
import com.example.assemblers.StudentResourcesAssembler;
import com.example.services.GroupService;
import com.example.services.StudentService;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    GroupService groupService;
    private final StudentResourcesAssembler assembler;
    private final GroupResourcesAssembler groupAssembler;

    public StudentController(StudentResourcesAssembler assembler, GroupResourcesAssembler groupResourcesAssembler) {
        this.assembler = assembler;
        this.groupAssembler = groupResourcesAssembler;
    }

    @GetMapping
    public Resources<Resource<Student>> getStudents() {
        List<Resource<Student>> list = studentService.getAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(
                list,
                linkTo(methodOn(StudentController.class).getStudents()).withSelfRel()
        );
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<ResourceSupport> getStudent(@PathVariable Integer studentId) {
        var student = studentService.getObjectById(studentId);
        return ResponseEntity.ok(assembler.toResource(student));
    }

    @PostMapping
    public ResponseEntity<?> createStudent(@Valid @RequestBody Student newStudent) throws URISyntaxException {
        Resource<Student> resource = assembler.toResource(studentService.saveObject(newStudent));
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<?> updateStudent(@Valid @RequestBody Student updatedStudent, @PathVariable Integer studentId) throws URISyntaxException {
        Student updatedObj = studentService.updateObject(updatedStudent, studentId);
        Resource<Student> resource = assembler.toResource(updatedObj);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable Integer studentId) {
        try {
            changeStudentGroup(studentId, -2);
            studentService.deleteObject(studentId);
        } catch (Exception ex) {
            System.out.println("ATTENTION " + ex.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{studentId}/group")
    public ResponseEntity<ResourceSupport> getGroupOfStudent(@PathVariable Integer studentId) {
        Student student = studentService.getObjectById(studentId);
        if (student.getGroup() == null)
            return ResponseEntity.noContent().build();
        var group = groupService.getObjectById(student.getGroup().getId());
        return  ResponseEntity
                .ok(groupAssembler.toResource(group));
    }


    @PostMapping("/{studentId}/group/{newGroupId}")
    public Resources<Resource<Student>> changeStudentGroup(@PathVariable Integer studentId, @PathVariable Integer newGroupId) {
        Student student = studentService.getObjectById(studentId);
        removeStudentFromGroup(student);
        return addStudentToGroup(student, newGroupId);
    }

    private void removeStudentFromGroup(Student student) {
        if (student.getGroup() != null) {
            Group studentGroup = groupService.getObjectById(student.getGroup().getId());
            studentGroup.removeStudent(student);
            studentGroup.setStudents(studentGroup.getStudents());
            groupService.saveObject(studentGroup);
            studentService.saveObject(student);
        }
    }

    private Resources<Resource<Student>> addStudentToGroup(Student student, Integer groupId) {
        Student stud = studentService.getObjectById(student.getId());
                
        if (groupId == -2)
            return null;
        Group group = groupService.getObjectById(groupId);
        stud.setGroup(group);
        group.addStudent(stud);
        group.setStudents(group.getStudents());
        groupService.saveObject(group);
        studentService.saveObject(stud);
        return getStudentsOfGroup(groupId);
    }

    private Resources<Resource<Student>> getStudentsOfGroup(Integer groupId) {
         var group = groupService.getObjectById(groupId);
        List<Resource<Student>> list = group.getStudents()
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(
                list,
                linkTo(methodOn(GroupController.class).getGroup(groupId)).withSelfRel(),
                linkTo(methodOn(StudentController.class).getStudents()).withSelfRel()
        );
    }

    /*@ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ValidationError handleException(MethodArgumentNotValidException exception) {
        return createValidationError(exception);
    }

    private ValidationError createValidationError(MethodArgumentNotValidException e) {
        return ValidationErrorBuilder.fromBindingErrors(e.getBindingResult());
    }*/
}

