package com.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "groups")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = "isDeleted", allowGetters = true)
public class Group extends com.example.Entity {
    @NotBlank(message ="TITLE can not be blank")
    private String title;

    @NotBlank(message = "Group has to be connected with some cathedra")
    private String cathedra;

    @NotNull(message = "Group had to start studying in some year")
    private int startYear;

    @JsonIgnore
    private boolean isDeleted;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Student> students;

    public Group(){}
    public Group(String title){
        setTitle(title);
        cathedra = "unknown";
        startYear = 2018;
    }

    public List<Student> addStudent(Student student){
        if(students.contains(student))
            throw new RuntimeException("Student already in this group!");
        students.add(student);
        return students;
    }

    public boolean removeStudent(Student student){
        return students.contains(student) && students.remove(student);
    }
    public boolean removeStudent(Integer id){
        for(int i = 0; i < students.size(); ++i){
            if(students.get(i).getId() == id)
                return students.remove(students.get(i));
        }
        return false;
    }

    public void delete(){
        isDeleted = true;
    }
    public boolean getDeleted(){
        return isDeleted;
    }

    public void setStartYear(int startYear){
        if(startYear < 1950 || startYear > 2019)
            System.out.println("Oooops, wrong start year");
        else
            this.startYear = startYear;
    }
}