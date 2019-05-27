package com.example.exceptions;


import com.example.Group;
import com.example.Student;

public class GroupNotHaveStudentsException extends RuntimeException {
    public GroupNotHaveStudentsException(Group group, Student student) {
        super("Group: " + group.toString() + " does not contain student: " + student.toString());
    }

    public GroupNotHaveStudentsException(Integer singerId, Integer songId) {
        super("Group with id: " + singerId + " does not contain student with id: " + songId);
    }
}
