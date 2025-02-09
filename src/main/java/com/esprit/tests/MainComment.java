package com.esprit.tests;

import com.esprit.models.commentaire;
import com.esprit.services.commentaireService;

public class MainComment {
    public MainComment() {
    }

    public static void main(String[] args) {
        commentaireService c = new commentaireService();
        c.addLike(new commentaire(2, ""));
        c.addLike(new commentaire(2, ""));
        c.addLike(new commentaire(2, ""));
        c.addLike(new commentaire(2, ""));
        c.addLike(new commentaire(2, ""));
        c.addLike(new commentaire(2, ""));
        c.addLike(new commentaire(2, ""));
        c.addLike(new commentaire(2, ""));
        System.out.println(c.afficher());
    }
}
