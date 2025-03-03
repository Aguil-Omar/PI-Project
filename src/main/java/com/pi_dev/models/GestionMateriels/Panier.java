package com.pi_dev.models.GestionMateriels;

import java.util.Date;

public class Panier {
    private int id;
    private String utilisateur;
    private Date dateDebut;
    private Date dateFin;
    private int materielId;
    private int idEvenement;

    public void setIdEvenement(int idEvenement) {
        this.idEvenement = idEvenement;
    }



    public int getIdEvenement() {
        return idEvenement;
    }


    // Constructeur par défaut
    public Panier() {
    }

    // Constructeur avec tous les champs
    public Panier(int id, String utilisateur, Date dateDebut, Date dateFin, int materielId) {
        this.id = id;
        this.utilisateur = utilisateur;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.materielId = materielId;
    }
    // Constructeur avec tous les champs
    public Panier(int id, String utilisateur, Date dateDebut, Date dateFin, int materielId,int idEvenement) {
        this.id = id;
        this.utilisateur = utilisateur;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.materielId = materielId;
        this.idEvenement = idEvenement;
    }

    // Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
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

    public int getMaterielId() {
        return materielId;
    }

    public void setMaterielId(int materielId) {
        this.materielId = materielId;
    }

    // Méthode toString pour afficher les informations de l'objet
    @Override
    public String toString() {
        return "Panier{" +
                "id=" + id +
                ", utilisateur='" + utilisateur + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", materielId=" + materielId +
                '}';
    }
}