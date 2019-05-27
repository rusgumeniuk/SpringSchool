package com.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "rooms")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = "isDeleted", allowGetters = true)
public class Room extends com.example.Entity {

    @NotNull
    private int number;

    public Room() {}

    public Room(@NotNull int number) {
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
