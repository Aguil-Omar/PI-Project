package com.pi_dev.services;

import com.pi_dev.models.Forum.commentaire;
import com.pi_dev.models.GestionEvenement.evenement;

import java.util.List;

public interface IService <T> {
    void ajouter(T t);
    void modifier(T t);
    void supprimer(T t);
    List<T> rechercher();

}
