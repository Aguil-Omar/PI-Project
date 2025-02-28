package com.pi_dev.models.GestionEsapce;

public class Espace {
    private int id;
    private String nom;
    private String localisation;
    private Disponibilite etat;
    private com.pi_dev.models.GestionEsapce.TypeEspace typeEspace;
    private String imageUrl;



    // Constructor with ID
    public Espace(int id, String nom, String localisation, Disponibilite etat, TypeEspace typeEspace, String imageUrl) {
        this.id = id;
        this.nom = nom;
        this.localisation = localisation;
        this.etat = etat;
        this.typeEspace = typeEspace;
        this.imageUrl = imageUrl;
    }

    // Constructor without ID (for adding new espaces)
    public Espace( String nom, String localisation, Disponibilite etat, com.pi_dev.models.GestionEsapce.TypeEspace typeEspace, String imageUrl) {
        this.nom = nom;
        this.localisation = localisation;
        this.etat = etat;
        this.typeEspace = typeEspace;
        this.imageUrl = imageUrl;
    }
    public Espace() {}

    public Espace(String nom, String localisation, Disponibilite etat) {
        this.nom = nom;
        this.localisation = localisation;
        this.etat = etat;
    }

    public Espace(int id, String nom, Disponibilite etat, TypeEspace typeEspace) {
        this.id = id;
        this.nom = nom;
        this.etat = etat;
        this.typeEspace = typeEspace;

    }





    public void setimageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getimageUrl() {
        return imageUrl;
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

    public com.pi_dev.models.GestionEsapce.TypeEspace getTypeEspace() {
        return typeEspace;
    }

    public void setTypeEspace(com.pi_dev.models.GestionEsapce.TypeEspace typeEspace) {
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
