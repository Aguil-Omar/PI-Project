package com.pi_dev.models.GestionEvenement;

import java.time.LocalTime;

public class programme {
    private int id;
    private String activite;
    private LocalTime heurDebut;
    private LocalTime heurFin;
    private int evenementId;

    public programme(int id, String activite, LocalTime heurDebut, LocalTime heurFin, int evenementId) {
        this.id = id;
        this.activite = activite;
        this.heurDebut = heurDebut;
        this.heurFin = heurFin;
        this.evenementId = evenementId;
    }

    public programme(String activite, LocalTime heurDebut, LocalTime heurFin, int evenementId) {
        this.activite = activite;
        this.heurDebut = heurDebut;
        this.heurFin = heurFin;
        this.evenementId = evenementId;
    }

    public int getId() {
        return id;
    }

    public String getActivite() {
        return activite;
    }

    public LocalTime getHeurDebut() {
        return heurDebut;
    }

    public LocalTime getHeurFin() {
        return heurFin;
    }

    public int getEvenementId() {
        return evenementId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    public void setHeurDebut(LocalTime heurDebut) {
        this.heurDebut = heurDebut;
    }

    public void setHeurFin(LocalTime heurFin) {
        this.heurFin = heurFin;
    }

    public void setEvenementId(int evenementId) {
        this.evenementId = evenementId;
    }

    @Override
    public String toString() {
        return "programme{ " +
                "id=" + id +
                ", activite='" + activite + '\'' +
                ", heurDebut=" + heurDebut +
                ", heurFin=" + heurFin +
                ", evenementId=" + evenementId +
                '}';
    }
}
