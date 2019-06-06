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
import javax.validation.constraints.NotNull;

@Data
@javax.persistence.Entity
@Table(name = "students")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = "isDeleted", allowGetters = true)
public class Student extends com.example.Entity {
    @NotBlank(message ="NAME can not be blank")
    private String name;

    @NotNull(message = "Age can not be empty")
    private int age;

    @NotNull(message = "Each person has male!")
    private Male male;

    @NotBlank(message = "Does this student have not home?")
    private String city;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "fk_group_id", referencedColumnName = "id")
    private Group group;

    public Student(){}

    public Student(@NotBlank String name){
        this.name = name;
    }
    public Student(@NotBlank String name, @NotBlank int age, @NotBlank Male male, @NotBlank String city){
        this(name);
        setCity(city);
        setMale(male);
        setAge(age);
    }

    public void setAge(int age){
        if(age >= 16)
            this.age = age;
        else
            System.out.println("Oooops, student should be elder 15");
    }
    public int getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public Male getMale() {
        return male;
    }
    public void setMale(Male male) {
        this.male = male;
    }
}