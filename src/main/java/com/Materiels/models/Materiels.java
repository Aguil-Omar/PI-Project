package com.Materiels.models;

public class Materiels {

        private int id;
        private String nom;
        private CategorieMateriel categorie;
        private float prix;
        private Disponibilte etat;


    public Materiels (int id, String nom , float prix , Disponibilte etat, CategorieMateriel categorie) {
        this.id = id;
        this.nom = nom;
        this.categorie = categorie;
        this.prix = prix;
        this.etat = etat;
    }
    public Materiels ( String nom, CategorieMateriel categorie , float prix , Disponibilte etat) {
        this.nom = nom;
        this.categorie = categorie;
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

    public CategorieMateriel getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieMateriel categorie) {
        this.categorie = categorie;
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
                ", categorie=" + categorie +
                ", prix=" + prix +
                ", etat=" + etat +
                '}';
    }
}

