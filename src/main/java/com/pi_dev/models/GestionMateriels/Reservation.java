package com.pi_dev.models.GestionMateriels;

import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import javafx.scene.control.Button;

import java.util.Date;

public class Reservation {

    private int id;
    private Date dateDebut;
    private Date dateFin;
    private Materiels materiel;
    private Utilisateur utilisateur;
    private Button actions;

    public Reservation(Date dateDebut, Date dateFin, Materiels materiel, Utilisateur utilisateur) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.materiel = materiel;
        this.utilisateur = utilisateur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Materiels getMateriel() {
        return materiel;
    }

    public void setMateriel(Materiels materiel) {
        this.materiel = materiel;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Button getActions() {
        return actions;
    }

    public void setActions(Button actions) {
        this.actions = actions;
    }

    public String getNom() {
        return materiel.getNom();
    }

    public Float getPrix() {
        return materiel.getPrix();
    }
}