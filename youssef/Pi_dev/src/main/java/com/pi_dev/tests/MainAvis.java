package com.pi_dev.tests;

import com.pi_dev.models.Forum.avis;
import com.pi_dev.services.avisService;
import com.pi_dev.utils.DataSource;

public class MainAvis {
    public MainAvis() {
    }

    public static void main(String[] args) {
        DataSource connection = DataSource.getInstance();
        avisService avis = new avisService();
        avis.ajouter(new avis(4000));
        avis.supprimer(new avis(3, 1));
        avis.modifier(new avis(4, 22));
        System.out.println(avis.rechercher());
    }
}
