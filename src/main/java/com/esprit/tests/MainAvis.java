package com.esprit.tests;

import com.esprit.models.avis;
import com.esprit.services.avisService;
import com.esprit.utils.DataSource;

public class MainAvis {
    public MainAvis() {
    }

    public static void main(String[] args) {
        DataSource connection = DataSource.getInstance();
        avisService avis = new avisService();
        avis.ajouter(new avis(1));
        avis.supprimer(new avis(3, 1));
        avis.modifier(new avis(4, 22));
        System.out.println(avis.afficher());
    }
}
