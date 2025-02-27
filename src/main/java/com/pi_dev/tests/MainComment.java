package com.pi_dev.tests;

import com.pi_dev.models.Forum.commentaire;
import com.pi_dev.services.commentaireService;

public class MainComment {
    public MainComment() {
    }

    public static void main(String[] args) {
        commentaireService c = new commentaireService();
        c.ajouter(new commentaire( "comments comments  comments  comments "));
        c.modifier(new commentaire(1,"comments 123456789 comments 123456789"));
        c.supprimer(new commentaire(1,"comments 123456789 comments 123456789"));
        c.addLike(new commentaire(2, ""));
        System.out.println(c.rechercher());
    }
}
