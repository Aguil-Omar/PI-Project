package com.pi_dev.tests;

import com.pi_dev.models.Categorie;
import com.pi_dev.services.IService;
import com.pi_dev.services.evenService;
import com.pi_dev.services.progService;
import com.pi_dev.models.evenement;
import com.pi_dev.models.programme;
import com.pi_dev.utils.DataSource;
import java.util.List;
import java.util.Date;
public class MainProg {
    public static void main(String[] args) {
        // Récupérer l'instance unique de DataSource
        DataSource dataSource = DataSource.getInstance();
        // Créer une instance de EvenementService
        evenService es = new evenService();
        progService service = new progService();

        // Ajouter un événement
        //es.ajouter(new evenement("Conférence sur anouar", "Une conférence sur les dernières avancées en IA.", new Date(), "Planifié", Categorie.Seminaire));

        // Afficher un message de confirmation
        //System.out.println("Événement ajouté avec succès !");
        // Modifier un événement
       //evenement evenementModifie = new evenement(1, "Nouveau Titre", "Nouvelle Description", new Date(), "En cours", Categorie.Seminaire);
       //es.modifier(evenementModifie);

        // Afficher un message de confirmation
        //System.out.println("Modification terminée !");
        //Créez un événement avec un ID que vous voulez supprimer (id=1 ici)
       // evenement evenementASupprimer = new evenement(1, "Titre de l'événement", "Description de l'événement", new java.util.Date(), "Planifié", Categorie.Seminaire);
        // Tester la suppression de l'événement
       //es.supprimer(evenementASupprimer);
        // Message de confirmation de la suppression
        //System.out.println("Tentative de suppression de l'événement avec l'ID 1.");
       /* List<evenement> evenements = es.rechercher();

        // Afficher les événements récupérés
        for (evenement evt : evenements) {
            System.out.println("ID: " + evt.getId() + ", Titre: " + evt.getTitre() +
                    ", Description: " + evt.getDescription() +
                    ", Date: " + evt.getDate() +
                    ", Statut: " + evt.getStatut() +
                    ", Catégorie: " + evt.getCategorie());
        }*/
        //programme prog = new programme("Yoga Matinal", "08:00", "09:00", 2);
        //programme prog1= new programme("Yoga soiree", "08:00", "09:00", 3);
        // Ajout du programme
        //service.ajouter(prog);
        //service.ajouter(prog1);
        // Créer un programme à modifier
        // Par exemple, un programme avec l'ID 1 et des nouvelles informations
        //programme prog = new programme(2, "Yoga Avancé", "09:00", "10:30", 2); // ID 1, nouvelle activité, horaires et événement
        // Créer un service pour le programme
        // Appeler la méthode modifier
        //service.modifier(prog);
        // Crée un objet programme avec l'ID du programme que tu veux supprimer
        //programme progASupprimer = new programme(2, "Yoga Matinal", "08:00", "09:00", 1);  // Remplacer 1 par l'ID du programme à supprimer

        // Appele la méthode supprimer de la progService pour supprimer le programme
        //service.supprimer(progASupprimer);
        // Récupérer la liste des programmes
       // List<programme> programmes = service.rechercher();

        // Afficher les programmes
        /*if (programmes.isEmpty()) {
            System.out.println("Aucun programme trouvé.");
        } else {
            System.out.println("Liste des programmes :");
            for (programme prog : programmes) {
                System.out.println(prog); // Utilise la méthode toString() de la classe programme
            }
        }*/





    }
    }

