package com.pi_dev.models.GestionEvenement;

import java.util.Date;

public class Reservation {
    private int id;
    private int evenementId; // Correspond à id_evenement dans la table
    private String emailClient; // Correspond à email dans la table
    private int idUtilisateur; // Correspond à id_utilisateur dans la table
    private int nombrePlaces; // Correspond à nombre_places dans la table
    private Date dateReservation; // Correspond à date_reservation dans la table

    // Constructeur principal
    public Reservation(int id, int evenementId, String emailClient, int idUtilisateur, int nombrePlaces, Date dateReservation) {
        this.id = id;
        this.evenementId = evenementId;
        this.emailClient = emailClient;
        this.idUtilisateur = idUtilisateur;
        this.nombrePlaces = nombrePlaces;
        this.dateReservation = dateReservation;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEvenementId() {
        return evenementId;
    }

    public void setEvenementId(int evenementId) {
        this.evenementId = evenementId;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public void setEmailClient(String emailClient) {
        this.emailClient = emailClient;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public int getNombrePlaces() {
        return nombrePlaces;
    }

    public void setNombrePlaces(int nombrePlaces) {
        this.nombrePlaces = nombrePlaces;
    }

    public Date getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }
}