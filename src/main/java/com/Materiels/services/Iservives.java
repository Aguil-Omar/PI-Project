package com.Materiels.services;

import java.util.List;

public interface Iservives<T> {

    void ajouter(T t);
    void modifier(T t);
    void supprimer(T t);
    List<T> rechercher();
}

