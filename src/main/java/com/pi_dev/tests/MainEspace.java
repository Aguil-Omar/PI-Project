package com.pi_dev.tests;

import com.pi_dev.models.GestionEsapce.Espace;
import com.pi_dev.services.EspaceService;
import com.pi_dev.models.GestionEsapce.categorieEspace;
import com.pi_dev.models.GestionMateriels.Disponibilite;

public class MainEspace {
    public static void main(String[] args) {
        EspaceService es = new EspaceService();


         es.ajouter(new Espace("XXXXXXX", "Salle de réunion", "3er étage", Disponibilite.DISPONIBLE, categorieEspace.INTERIEUR));


        es.modifier(new Espace(3, "Salle B", "Salle de conférence", "2ème étage", Disponibilite.INDISPONIBLE, categorieEspace.EXTERIEUR));


        es.supprimer(new Espace(2, "", "", "", Disponibilite.DISPONIBLE, categorieEspace.INTERIEUR));


        System.out.println(es.rechercher());
    }
}