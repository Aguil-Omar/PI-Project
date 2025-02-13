package com.pi_dev.tests;


import com.pi_dev.models.GestionEsapce.Disponibilite;
import com.pi_dev.models.GestionEsapce.Espace;
import com.pi_dev.models.GestionEsapce.TypeEspace;
import com.pi_dev.services.*;

import com.pi_dev.services.TypeEspaceService;

public class MainEspace {
    public static void main(String[] args) {
        EspaceService es = new EspaceService();


        //es.ajouter(new Espace("Salle de press", "seminaire tayara", "3er étage",  Disponibilite.DISPONIBLE, 1));



        // es.modifier(new Espace(3, "Salle A", "Réunion importante", "1er étage", Disponibilite.INDISPONIBLE, 2));



        // es.supprimer(new Espace(3, "", "", "", Disponibilite.DISPONIBLE, 3)); // assuming 3 is a valid type_espace_id


        System.out.println(es.rechercher());
        TypeEspaceService tes = new TypeEspaceService();



       // tes.ajouter(new TypeEspace("Salle de conférence", "Salle pour conférences et réunions"));



        //tes.modifier(new TypeEspace(1, "Salle de fetes ", "Salle pour grandes conférences"));


        //tes.supprimer(new TypeEspace(1, "", ""));



        System.out.println(tes.rechercher());
    }
}
