package com.esprit.models;

public enum Disponibilite {
    DISPONIBLE,
    INDISPONIBLE;

    public String getStatus() {
        // Return a string representation of the enum
        switch (this) {
            case DISPONIBLE:
                return "Available";
            case INDISPONIBLE:
                return "Not Available";
            default:
                return "Unknown";
        }
    }
}
