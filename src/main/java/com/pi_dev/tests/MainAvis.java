package com.pi_dev.tests;

import com.pi_dev.models.Forum.avis;
import com.pi_dev.models.GestionEvenement.Categorie;
import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.services.avisService;
import com.pi_dev.utils.DataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainAvis {
    public MainAvis() {
    }

    public static void main(String[] args) throws ParseException {
        DataSource connection = DataSource.getInstance();
        avisService avis = new avisService();
       // avis.ajouter(new avis(4000));
        //avis.supprimer(new avis(3, 1));
        //avis.modifier(new avis(4, 22));
        String dateString = "2025-02-17"; // Date au format "yyyy-MM-dd"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date eventDate = dateFormat.parse(dateString);
        System.out.println(avis.getAvisByUserAndEvent(3,new evenement(55,"","",eventDate,"", Categorie.Atelier)));
    }
}
