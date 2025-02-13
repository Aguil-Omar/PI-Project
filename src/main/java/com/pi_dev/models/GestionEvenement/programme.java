package com.pi_dev.models.GestionEvenement;

public class programme {
    private int id;
    private String activite;
    private String heurDebut;
    private String heurFin;
    private int evenementId;

    public programme(int id, String activite, String heurDebut, String heurFin, int evenementId) {
        this.id = id;
        this.activite = activite;
        this.heurDebut = heurDebut;
        this.heurFin = heurFin;
        this.evenementId = evenementId;
    }

    public programme(String activite, String heurDebut, String heurFin, int evenementId) {
        this.activite = activite;
        this.heurDebut = heurDebut;
        this.heurFin = heurFin;
        this.evenementId = evenementId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivite() {
        return activite;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    public String getHeurDebut() {
        return heurDebut;
    }

    public void setHeurDebut(String heurDebut) {
        this.heurDebut = heurDebut;
    }

    public String getHeurFin() {
        return heurFin;
    }

    public void setHeurFin(String heurFin) {
        this.heurFin = heurFin;
    }

    public int getEvenementId() {
        return evenementId;
    }

    public void setEvenementId(int evenementId) {
        this.evenementId = evenementId;
    }

    @Override
    public String toString() {
        return "programme{ " +
                "id=" + id +
                ", activite='" + activite + '\'' +
                ", heurDebut='" + heurDebut + '\'' +
                ", heurFin='" + heurFin + '\'' +
                ", evenementId=" + evenementId +
                '}';
    }
}
