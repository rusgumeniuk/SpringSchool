package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "messages")
@EntityListeners(AuditingEntityListener.class)
public class Message {
    private @Id
    @GeneratedValue
    Long msg_id;
    @NotNull
    private String description;
    @NotNull
    private OperationType operationType;
    @NotNull
    private String statusCode;
    @NotNull
    private String error;

    public Message(String description, OperationType operationType, String statusCode, String error) {
        this.description = description;
        this.operationType = operationType;
        this.statusCode = statusCode;
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
