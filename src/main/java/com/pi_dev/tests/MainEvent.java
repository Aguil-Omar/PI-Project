package com.pi_dev.tests;

import com.pi_dev.models.GestionEvenement.Categorie;
import com.pi_dev.services.EvenService;
import com.pi_dev.services.ProgService;
import com.pi_dev.models.GestionEvenement.evenement;
import com.pi_dev.utils.DataSource;
import java.util.List;
import java.util.Date;
public class MainEvent {
    public static void main(String[] args) {
        DataSource dataSource = DataSource.getInstance();
        EvenService es = new EvenService();
        ProgService service = new ProgService();
        es.ajouter(new evenement("Conférence sur said", "Une conférence sur les dernières avancées en IA.", new Date(), "Planifié", Categorie.Seminaire));
        System.out.println("Événement ajouté avec succès !");
        evenement evenementModifie = new evenement(1, "Nouveau Titre", "Nouvelle Description", new Date(), "En cours", Categorie.Seminaire);
        es.modifier(evenementModifie);
        System.out.println("Modification terminée !");
        evenement evenementASupprimer = new evenement(1, "Titre de l'événement", "Description de l'événement", new java.util.Date(), "Planifié", Categorie.Seminaire);
        es.supprimer(evenementASupprimer);
        System.out.println("Tentative de suppression de l'événement avec l'ID 1.");
        List<evenement> evenements = es.rechercher();
        for (evenement evt : evenements) {
            System.out.println("ID: " + evt.getId() + ", Titre: " + evt.getTitre() +
                    ", Description: " + evt.getDescription() +
                    ", Date: " + evt.getDate() +
                    ", Statut: " + evt.getStatut() +
                    ", Catégorie: " + evt.getCategorie());

        }


    }
    }

