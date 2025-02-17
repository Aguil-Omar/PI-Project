package com.pi_dev.tests;

import com.pi_dev.models.GestionUtilisateur.Adresse;
import com.pi_dev.models.GestionUtilisateur.Role;
import com.pi_dev.models.GestionUtilisateur.Utilisateur;
import com.pi_dev.services.AdminService;

public class MainUtilisateur {

    public static void main(String[] args) {
        AdminService us = new AdminService();

        //System.out.println("=== AJOUTER UTILISATEUR ===");
        //us.ajouter(new Utilisateur( "SAID", "Salah", "pass123", Role.ADMIN, new Adresse(1001, "Tunis"), "55512345"));
       // us.ajouter(new Utilisateur( "Samir", "Ben Ali", "pass456", Role.PARTICIPANT, new Adresse(2001, "Sfax"), "98765432"));

       /* System.out.println("\n=== MODIFIER UTILISATEUR ===");
        us.modifier(new Utilisateur(1, "Fedi", "Samir", "newPass123", Role.ORGANISATEUR, new Adresse(1001, "Tunis"), "55599999"));

        System.out.println("\n=== SUPPRIMER UTILISATEUR ===");
        us.supprimer(new Utilisateur(1));*/

        System.out.println("\n=== RECHERCHER UTILISATEURS ===");
        us.rechercher().forEach(System.out::println);
    }
}
