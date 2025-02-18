package com.esprit.tests;

import com.esprit.models.Espace;
import com.esprit.models.Disponibilite;
import com.esprit.services.EspaceService;
import com.esprit.services.TypeEspaceService;
import com.esprit.models.TypeEspace;

public class MainProg {
    public static void main(String[] args) {
        EspaceService es = new EspaceService();

        // Adding new Espace objects
        es.ajouter(new Espace(1, "Salle de réunion", "3er étage", Disponibilite.DISPONIBLE, new TypeEspace(46)));  // Assuming type_espace_id = 1
        es.ajouter(new Espace(2, "Réunion internationale", "3er étage", Disponibilite.DISPONIBLE, new TypeEspace(2)));  // Assuming type_espace_id = 2

        // Modifying an existing Espace object
        //es.modifier(new Espace(3, "Salle B", "Salle de conférence", "2ème étage", Disponibilite.INDISPONIBLE, new TypeEspace(3)));  // Assuming type_espace_id = 3
       // es.modifier(new Espace(3, "Salle A", "Réunion importante", "1er étage", Disponibilite.INDISPONIBLE, new TypeEspace(2)));  // Assuming type_espace_id = 2

        // Deleting an Espace object
        //es.supprimer(new Espace(2, "", "", "", Disponibilite.DISPONIBLE, new TypeEspace(1)));  // Assuming type_espace_id = 1
       // es.supprimer(new Espace(3, "", "", "", Disponibilite.DISPONIBLE, new TypeEspace(3)));  // Assuming type_espace_id = 3

        // Displaying all Espace objects
        System.out.println(es.rechercher());

        // Working with TypeEspaceService
        TypeEspaceService tes = new TypeEspaceService();
        tes.ajouter(new TypeEspace("Salle de conférence", "Salle pour conférences et réunions"));

        // Modifying a TypeEspace object
        //tes.modifier(new TypeEspace(1, "Salle de fêtes", "Salle pour grandes conférences"));

        // Deleting a TypeEspace object
       // tes.supprimer(new TypeEspace(1, "", ""));

        // Displaying all TypeEspace objects
        System.out.println(tes.rechercher());
    }
}
