package com.esprit.models;

public class TypeEspace {
    private int id;
    private String type;
    private String description;

    // Constructor
    public TypeEspace(int id, String type, String description) {
        this.id = id;
        this.type = type;
        this.description = description;
    }

    // Constructor without ID (for insertion)
    public TypeEspace(String type, String description) {
        this.type = type;
        this.description = description;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "TypeEspace{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}



