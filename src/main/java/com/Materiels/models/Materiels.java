package com.Materiels.models;

public class Materiels {

        private int id;
        private String nom;

        private float prix;
        private Disponibilte etat;


    public Materiels (int id, String nom , float prix , Disponibilte etat) {
        this.id = id;
        this.nom = nom;

        this.prix = prix;
        this.etat = etat;
    }
    public Materiels ( String nom,  float prix , Disponibilte etat) {
        this.nom = nom;

        this.prix = prix;
        this.etat = etat;
    }

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

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public Disponibilte getEtat() {
        return etat;
    }

    public void setEtat(Disponibilte etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "Materiels{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                ", etat=" + etat +
                '}';
    }


}

