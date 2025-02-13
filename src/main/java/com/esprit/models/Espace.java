package com.esprit.models;

import java.sql.Time;  // For localisation field as Time

public class Espace {

    private int id;
    private String nom;
    private String titre;
    private String localisation;  // Change to String type
    private Disponibilite etat;
    private int type_espace_id;  // Add type_espace_id as an integer

    // Constructor with type_espace_id
    public Espace(int id, String nom, String titre, String localisation, Disponibilite etat, int type_espace_id) {
        this.id = id;
        this.nom = nom;
        this.titre = titre;
        this.localisation = localisation;
        this.etat = etat;
        this.type_espace_id = type_espace_id;
    }

    // Constructor without id (for creating new records)
    public Espace(String nom, String titre, String localisation, Disponibilite etat, int type_espace_id) {
        this.nom = nom;
        this.titre = titre;
        this.localisation = localisation;
        this.etat = etat;
        this.type_espace_id = type_espace_id;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public Disponibilite getEtat() {
        return etat;
    }

    public void setEtat(Disponibilite etat) {
        this.etat = etat;
    }

    public int getType_espace_id() {
        return type_espace_id;
    }

    public void setType_espace_id(int type_espace_id) {
        this.type_espace_id = type_espace_id;
    }

    @Override
    public String toString() {
        return "Espace{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", titre='" + titre + '\'' +
                ", localisation=" + localisation +
                ", etat=" + etat +
                ", type_espace_id=" + type_espace_id +
                '}';
    }
}
