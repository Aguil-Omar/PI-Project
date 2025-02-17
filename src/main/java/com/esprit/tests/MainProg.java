package com.esprit.tests;

import com.esprit.models.*;
import com.esprit.services.*;

public class MainProg {
    public static void main(String[] args) {
        EspaceService es = new EspaceService();


//       TypeEspace typeEspace = new TypeEspace("Salle de conférence", "Salle ");
//        Espace espace = new Espace("Salle OMNIA", "Ariana", Disponibilite.DISPONIBLE, typeEspace);
//        es.ajouter(espace);


        TypeEspace modifiedType = new TypeEspace(31, "Salle de fêtes", "Salle pour grandes conférences");
        Espace modifiedEspace = new Espace(34, "Salle ESPRIT", "MONASTIR", Disponibilite.INDISPONIBLE, modifiedType);
        es.modifier(modifiedEspace);

        // Deleting an espace (assuming ID exists)

        //Espace espaceToDelete = new Espace(33, "", "", Disponibilite.DISPONIBLE, modifiedType);
        //es.supprimer(espaceToDelete);

        // Displaying all espaces
        es.rechercher().forEach(System.out::println);
    }
}
