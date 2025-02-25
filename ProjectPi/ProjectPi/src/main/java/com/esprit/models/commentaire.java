//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.esprit.models;

import java.sql.Date;
import java.sql.Time;

public class commentaire {
    private int id;
    private String comment;
    private Date date_comment;
    private Time time_comment;
    private int nbr_Likes;

    public commentaire(int id, String comment, Date date_comment, Time time_comment, int nbr_likes) {
        this.id = id;
        this.comment = comment;
        this.date_comment = date_comment;
        this.time_comment = time_comment;
        this.nbr_Likes = nbr_likes;
    }

    public commentaire(String comment) {
        this.comment = comment;
        this.date_comment = new Date(System.currentTimeMillis());
        this.time_comment = new Time(System.currentTimeMillis());
        this.nbr_Likes = 0;
    }

    public commentaire(int id, String comment) {
        this.id = id;
        this.comment = comment;
        this.date_comment = new Date(System.currentTimeMillis());
        this.time_comment = new Time(System.currentTimeMillis());
        this.nbr_Likes = 0;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate_comment() {
        return this.date_comment;
    }

    public void setDate_comment(Date date_comment) {
        this.date_comment = date_comment;
    }

    public int getNbr_Likes() {
        return this.nbr_Likes;
    }

    public void setNbr_Likes(int nbr_Likes) {
        this.nbr_Likes = nbr_Likes;
    }

    public Time getTime_comment() {
        return this.time_comment;
    }

    public void setTime_comment(Time time_comment) {
        this.time_comment = time_comment;
    }

    public String toString() {
        return "commentaire{id=" + this.id + ", comment='" + this.comment + "', date_comment=" + this.date_comment + ", time_comment=" + this.time_comment + ", nbr_Likes=" + this.nbr_Likes + "}";
    }
}
