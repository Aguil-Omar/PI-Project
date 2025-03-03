package com.pi_dev.models.GestionEvenement;

import java.util.List;

public class AdminReservationRow {
    private String evenement; // Titre de l'événement
    private String espaceReserve; // Espace réservé (nom de l'espace)
    private List<String> materielsReserves; // Liste des matériels réservés
    private List<String> utilisateurs; // Liste des utilisateurs qui ont réservé

    // Constructeur
    public AdminReservationRow(String evenement, String espaceReserve, List<String> materielsReserves, List<String> utilisateurs) {
        this.evenement = evenement;
        this.espaceReserve = espaceReserve;
        this.materielsReserves = materielsReserves;
        this.utilisateurs = utilisateurs;
    }

    // Getters et Setters
    public String getEvenement() {
        return evenement;
    }

    public void setEvenement(String evenement) {
        this.evenement = evenement;
    }

    public String getEspaceReserve() {
        return espaceReserve;
    }

    public void setEspaceReserve(String espaceReserve) {
        this.espaceReserve = espaceReserve;
    }

    public List<String> getMaterielsReserves() {
        return materielsReserves;
    }

    public void setMaterielsReserves(List<String> materielsReserves) {
        this.materielsReserves = materielsReserves;
    }

    public List<String> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(List<String> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }
}