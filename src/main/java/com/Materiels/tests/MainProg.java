package com.Materiels.tests;

import com.Materiels.models.Disponibilte;
import com.Materiels.models.Materiels;
import com.Materiels.models.TypeMateriels;
import com.Materiels.services.MaterielsServices;
import com.Materiels.services.TypeMaterielsServices;

public class MainProg {
    public static void main(String[] args) {
        // Initialisation des services
        MaterielsServices ps = new MaterielsServices();
        TypeMaterielsServices pst = new TypeMaterielsServices();

        // Création d'un TypeMateriels
        TypeMateriels typeMicro = new TypeMateriels("micro", "sans fils");
        pst.ajouter(typeMicro); // Ajout du type de matériel

        // Création d'un Materiels avec le TypeMateriels
        Materiels materielMicro = new Materiels("micro", 250.0f, Disponibilte.DISPONIBLE, typeMicro);
        ps.ajouter(materielMicro); // Ajout du matériel

        // Modification du matériel
        Materiels materielModifie = new Materiels("micro", 250.0f, Disponibilte.INDISPONIBLE, typeMicro);
        ps.modifier(materielModifie);

        // Suppression du matériel
        ps.supprimer(materielMicro);

        // Affichage de tous les matériels
        System.out.println("Liste des matériels :");
        System.out.println(ps.rechercher());

        // Test de l'énumération Disponibilte
        System.out.println("État du matériel : " + Disponibilte.valueOf("INDISPONIBLE"));

        // Création et gestion des TypeMateriels
        TypeMateriels typeHautParleur = new TypeMateriels("hautparleur", "audio");
        pst.ajouter(typeHautParleur); // Ajout d'un nouveau type de matériel

        TypeMateriels typeChaise = new TypeMateriels("chaise", "plastique");
        pst.ajouter(typeChaise); // Ajout d'un nouveau type de matériel

        // Modification d'un TypeMateriels
        TypeMateriels typeModifie = new TypeMateriels("hautparleur", "audio haute qualité");
        pst.modifier(typeModifie);

        // Suppression d'un TypeMateriels
        pst.supprimer(typeChaise);

        // Affichage de tous les TypeMateriels
        System.out.println("Liste des types de matériels :");
        System.out.println(pst.rechercher());
    }
}