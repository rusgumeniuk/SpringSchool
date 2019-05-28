package com.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "buildings")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = "isDeleted", allowGetters = true)
public class Building extends com.example.Entity {

    @NotNull
    private int number;

    @NotNull
    private short countOfStoreys;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Room> rooms;

    public Building() {}
    public Building(@NotNull int number){
        this.number = number;
        countOfStoreys = 4;
    }
    public Building(@NotNull int number, @NotNull short countOfStoreys) {
        setNumber(number);
        setCountOfStoreys(countOfStoreys);
    }
    public Building(@NotNull int number, @NotNull short countOfStoreys, List<Room> rooms) {
        this(number, countOfStoreys);
        this.rooms = rooms;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        if(isNaturalNumber(number))
            this.number = number;
    }

    public short getCountOfStoreys() {
        return countOfStoreys;
    }

    public void setCountOfStoreys(short countOfStoreys) {
        if(isNaturalNumber(countOfStoreys))
            this.countOfStoreys = countOfStoreys;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    private boolean isNaturalNumber(int number){
        return number > 0;
    }

    public List<Room> addRoom(Room room){
        if(rooms.contains(room))
            throw new RuntimeException("Room already in this building!");
        rooms.add(room);
        return rooms;
    }
    
    public boolean removeRoom(Room room){
        return rooms.contains(room) && rooms.remove(room);
    }
    public boolean removeRoom(Integer id){
        for(int i = 0; i < rooms.size(); ++i){
            if(rooms.get(i).getId() == id)
                return rooms.remove(rooms.get(i));
        }
        return false;
    } 
}
