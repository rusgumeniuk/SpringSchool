package com.example.services;

import java.util.List;
import java.util.Optional;

public interface Service<T, ID> {
    List<T> getAll();

    T getObjectById(ID id);

    T saveObject(T newObject);

    void deleteObject(ID id);

    T updateObject(T newObject, ID id);
}
