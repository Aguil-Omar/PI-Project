package com.pi_dev.models;

import java.util.Date;

public class evenement {
    private int id;
    private String titre;
    private String description;
    private Date date;
    private String statut;
    private Categorie categorie;

    public evenement(String titre, String description, Date date, String statut, Categorie categorie) {
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.statut = statut;
        this.categorie = categorie;
    }
    public evenement(int id,String titre, String description, Date date, String statut, Categorie categorie) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.statut = statut;
        this.categorie = categorie;
    }

    public int getId() {
        return id;
    }
    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public String getStatut() {
        return statut;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    // Setters (facultatifs, si nécessaire)
    public void setId(int id) {
        this.id = id;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "evenement{" +
                "titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", statut='" + statut + '\'' +
                ", categorie=" + categorie +
                '}';
    }
}
