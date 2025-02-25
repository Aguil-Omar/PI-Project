package com.esprit.tests;

import com.esprit.models.avis;
import com.esprit.services.avisService;
import com.esprit.utils.DataSource;

public class MainAvis {
    public MainAvis() {
    }

    public static void main(String[] args) {
        DataSource connection = DataSource.getInstance();

        avisService A = new avisService();

        avis x =new avis(1);
        A.ajouter(x);
        A.supprimer(new avis(3, 1));
        A.modifier(new avis(4, 22));
        System.out.println(A.afficher());

    }
}
