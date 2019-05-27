package com.example.listeners;

@FunctionalInterface
public interface RabbitListener<T> {
    public void receiveMessage(T msg);

}
