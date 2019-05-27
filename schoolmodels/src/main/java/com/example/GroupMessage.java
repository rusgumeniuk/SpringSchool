package com.example;


public class GroupMessage {
    private String description;
    private OperationType operationType;
    private String statusCode;
    private String error;


    public GroupMessage(String description, OperationType operationType, String statusCode, String error) {
        this.description = description;
        this.operationType = operationType;
        this.statusCode = statusCode;
        this.error = error;
    }


    public GroupMessage(){

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
