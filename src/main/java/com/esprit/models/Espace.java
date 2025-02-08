package com.esprit.models;

public class Espace {

    private int id;
    private String nom;
    private String titre;
    private String localisation;
    private Disponibilite etat;
    private categorieEspace categorie;

    public Espace(int id, String nom, String titre, String localisation, Disponibilite etat, categorieEspace categorie) {
        this.id = id;
        this.nom = nom;
        this.titre = titre;
        this.localisation = localisation;
        this.etat = etat;
        this.categorie = categorie;
    }

    public Espace(String nom, String titre, String localisation, Disponibilite etat, categorieEspace categorie) {
        this.nom = nom;
        this.titre = titre;
        this.localisation = localisation;
        this.etat = etat;
        this.categorie = categorie;
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

    public Disponibilite getetat() {
        return etat;
    }
    public categorieEspace getCategorie() {
        return categorie;
    }

    public void setetat(Disponibilite etat) {
        this.etat = etat;
    }

    public categorieEspace getCatégorie() {
        return categorie;
    }

    public void setCatégorie(categorieEspace catégorie) {
        this.categorie = catégorie;
    }

    @Override
    public String toString() {
        return "Espace{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", titre='" + titre + '\'' +
                ", localisation='" + localisation + '\'' +
                ", etat=" + etat +
                ", catégorie=" + categorie +
                '}';
    }
}


