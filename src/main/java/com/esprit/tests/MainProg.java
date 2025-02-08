package com.esprit.tests;

import com.esprit.models.Espace;
import com.esprit.services.EspaceService;
import com.esprit.models.categorieEspace;
import com.esprit.models.Disponibilite;
public class MainProg {
    public static void main(String[] args) {
        EspaceService es = new EspaceService();


        // es.ajouter(new Espace("Salle B", "Salle de réunion", "3er étage", Disponibilite.DISPONIBLE, categorieEspace.INTERIEUR));


         es.modifier(new Espace(3, "Salle B", "Salle de conférence", "2ème étage", Disponibilite.INDISPONIBLE, categorieEspace.EXTERIEUR));


         //es.supprimer(new Espace(2, "", "", "", Disponibilite.DISPONIBLE, categorieEspace.INTERIEUR));


        System.out.println(es.rechercher());
    }
}