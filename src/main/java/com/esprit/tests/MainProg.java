package com.esprit.tests;

import com.esprit.models.*;
import com.esprit.services.*;

import com.esprit.services.TypeEspaceService;

public class MainProg {
    public static void main(String[] args) {
        EspaceService es = new EspaceService();


       // es.ajouter(new Espace("Salle de conférence", "Réunion internationale", "3er étage",  Disponibilite.INDISPONIBLE, 2));



       // es.modifier(new Espace(3, "Salle A", "Réunion importante", "1er étage", Disponibilite.INDISPONIBLE, 2));



       // es.supprimer(new Espace(3, "", "", "", Disponibilite.DISPONIBLE, 3)); // assuming 3 is a valid type_espace_id


        System.out.println(es.rechercher());
        TypeEspaceService tes = new TypeEspaceService();



        //tes.ajouter(new TypeEspace("Salle de conférence", "Salle pour conférences et réunions"));



        //tes.modifier(new TypeEspace(1, "Salle de fetes ", "Salle pour grandes conférences"));


        // tes.supprimer(new TypeEspace(2, "", ""));



        System.out.println(tes.rechercher());
    }
}
