
package com.pi_dev.models.Forum;

import java.sql.Date;
import java.sql.Time;

public class avis {
    private int id;
    private int note;
    private Date date;
    private Time heur;

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

    public String toString() {
        return "avis{id=" + this.id + ", note=" + this.note + ", date=" + this.date + ", time=" + this.heur + "}";
    }
}