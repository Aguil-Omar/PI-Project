

package com.pi_dev.models.Forum;

import java.sql.Date;
import java.sql.Time;

public class commentaire {
    private int id;
    private String comment;
    private Date date_comment;
    private Time time_comment;
    private int nbr_Likes;
    private int nbr_Dislikes;
    private int evenement_id;
    private int utilisateur_id;

    public commentaire(int id, String comment, Date date_comment, Time time_comment, int nbr_likes,int utilisateur_id,int nbr_Dislikes) {
        this.id = id;
        this.comment = comment;
        this.date_comment = date_comment;
        this.time_comment = time_comment;
        this.nbr_Likes = nbr_likes;
        this.utilisateur_id = utilisateur_id;
        this.nbr_Dislikes = nbr_Dislikes;
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

    public int getNbr_Dislikes() {
        return nbr_Dislikes;
    }

    public void setNbr_Dislikes(int nbr_Dislikes) {
        this.nbr_Dislikes = nbr_Dislikes;
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
        return "commentaire{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", date_comment=" + date_comment +
                ", time_comment=" + time_comment +
                ", nbr_Likes=" + nbr_Likes +
                ", nbr_Dislikes=" + nbr_Dislikes +
                ", evenement_id=" + evenement_id +
                ", utilisateur_id=" + utilisateur_id +
                '}';
    }
}
