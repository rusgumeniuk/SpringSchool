package com.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "rooms")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = "isDeleted", allowGetters = true)
public class Room extends com.example.Entity {

    @NotNull
    private int number;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "fk_building_id", referencedColumnName = "id")
    private Building building;


    public Room() {}
    public Room(int number) {
        setNumber(number);
    }
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        if(number > 0)
            this.number = number;
        else
            throw new RuntimeException("Number of room can not be: " + number);
    }
}
