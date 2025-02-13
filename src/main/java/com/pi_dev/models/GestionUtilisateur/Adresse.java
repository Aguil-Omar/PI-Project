package com.pi_dev.models.GestionUtilisateur;

public class Adresse {
    private int id;
    private int codePostal;
    private String region;

    public Adresse(int id, int codePostal, String region) {
        this.id = id;
        this.codePostal = codePostal;
        this.region = region;
    }

    public Adresse(int codePostal, String region) {
        this.codePostal = codePostal;
        this.region = region;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "Adresse{" +
                "id=" + id +
                ", codePostal=" + codePostal +
                ", region='" + region + '\'' +
                '}';
    }
}
