package com.example.exceptions;

import com.example.Group;
import com.example.Student;

public class GroupNotExistStudentException extends RuntimeException {
    public GroupNotExistStudentException(Group group, Student student){
        super("Group: " + group.toString() + " does not contain student: " + student.toString());
    }
    public GroupNotExistStudentException(Integer groupId, Integer studentId){
        super("Group with id: " + groupId + " does not contain student with id: " + studentId);
    }
}
