package com.johnburitto.tdddev.service;

import java.util.List;

public interface IService<T, V> {
    List<T> getAll();
    T getById(String id);
    T create(V v);
    T update(V v);
    void delete(String id);
}
