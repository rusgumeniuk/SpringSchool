package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "messages")
@EntityListeners(AuditingEntityListener.class)
public abstract class Message<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long msg_id;
    @NotNull
    private String description;
    @NotNull
    private HttpMethod httpMethod;
    @NotNull
    private String statusCode;
    @NotNull
    @Column(length = 1000)
    private String error;
    @NotNull
    private Timestamp dateTime;

    public Message(String description, HttpMethod httpMethod, String statusCode, Timestamp dateTime, String error) {
        this.description = description;
        this.httpMethod = httpMethod;
        this.statusCode = statusCode;
        this.dateTime = dateTime;
        this.error = error;
    }

    public Message(){

    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();

        String jsonString = "";
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonString = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonString;
    }
}
