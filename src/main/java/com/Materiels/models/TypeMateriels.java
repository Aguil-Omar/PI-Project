package com.Materiels.models;

public class TypeMateriels {


        private int id;
        private String nomM;
    private String description;

    public TypeMateriels(int id, String nomM, String description) {
        this.id = id;
        this.nomM = nomM;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getNomM() {
        return nomM;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNomM(String nomM) {
        this.nomM = nomM;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return nomM;
    }

    public TypeMateriels(String nomM, String description) {
        this.nomM = nomM;
        this.description = description;
    }
}



