package com.esprit.models;

public class Espace {
    private int id;
    private String nom;
    private String localisation;
    private Disponibilite etat;
    private TypeEspace typeEspace; // Store TypeEspace instead of just an ID

    // Constructor with ID
    public Espace(int id, String nom, String localisation, Disponibilite etat, TypeEspace typeEspace) {
        this.id = id;
        this.nom = nom;
        this.localisation = localisation;
        this.etat = etat;
        this.typeEspace = typeEspace;
    }

    // Constructor without ID (for adding new espaces)
    public Espace( String nom, String localisation, Disponibilite etat, TypeEspace typeEspace) {
        this.nom = nom;
        this.localisation = localisation;
        this.etat = etat;
        this.typeEspace = typeEspace;
    }

    public Espace(String nom, String localisation, Disponibilite etat) {
        this.nom = nom;
        this.localisation = localisation;
        this.etat = etat;
    }


    // Getters and Setters
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

    public TypeEspace getTypeEspace() {
        return typeEspace;
    }

    public void setTypeEspace(TypeEspace typeEspace) {
        this.typeEspace = typeEspace;
    }

    @Override
    public String toString() {
        return "Espace{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", localisation='" + localisation + '\'' +
                ", etat=" + etat +
                ", typeEspace=" + typeEspace +
                '}';
    }
}
