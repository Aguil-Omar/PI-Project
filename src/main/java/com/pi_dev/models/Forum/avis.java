
package com.pi_dev.models.Forum;

import com.pi_dev.models.GestionEvenement.evenement;

import java.sql.Date;
import java.sql.Time;

public class avis {
    private int id;
    private int note;
    private Date date;
    private Time heur;
    private int evenement_id;
    private int utilisateur_id;

    public avis(int id, int note, Date date, Time heur) {
        this.id = id;
        this.note = note;
        this.date = date;
        this.heur = heur;
    }

    public avis(int id, int note) {
        this.id = id;
        this.note = note;
        this.date = new Date(System.currentTimeMillis());
        this.heur = new Time(System.currentTimeMillis());
    }

    public avis(int note) {
        this.note = note;
        this.date = new Date(System.currentTimeMillis());
        this.heur = new Time(System.currentTimeMillis());
    }



    //ajout avis
    public avis(int rate, int userId, int idEvenement) {
        this.note=rate;
        this.utilisateur_id = userId;
        this.evenement_id=idEvenement;
        this.date = new Date(System.currentTimeMillis());
        this.heur = new Time(System.currentTimeMillis());
    }

    public avis(int id, int note, int userId, int eventId) {
        this.id = id;
        this.note = note;
        this.utilisateur_id = userId;
        this.evenement_id=eventId;
        this.date = new Date(System.currentTimeMillis());
        this.heur = new Time(System.currentTimeMillis());
    }




    public int getId() {
        return this.id;
    }

    public int getNote() {
        return this.note;
    }

    public Date getDate() {
        return this.date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getHeur() {
        return this.heur;
    }

    public void setHeur(Time time) {
        this.heur = time;
    }

    public int getEvenement_id() {
        return evenement_id;
    }

    public void setEvenement_id(int evenement_id) {
        this.evenement_id = evenement_id;
    }

    public int getUtilisateur_id() {
        return utilisateur_id;
    }

    public void setUtilisateur_id(int utilisateur_id) {
        this.utilisateur_id = utilisateur_id;
    }

    @Override
    public String toString() {
        return "avis{" +
                "id=" + id +
                ", note=" + note +
                ", date=" + date +
                ", heur=" + heur +
                ", evenement_id=" + evenement_id +
                ", utilisateur_id=" + utilisateur_id +
                '}';
    }
}