package com.esprit.tests;

import com.esprit.models.Adresse;
import com.esprit.models.Role;
import com.esprit.models.Utilisateur;
import com.esprit.services.AdminService;

public class MainProg {

    public static void main(String[] args) {
        AdminService us = new AdminService();

        System.out.println("=== AJOUTER UTILISATEUR ===");
        us.ajouter(new Utilisateur(0, "Fedi", "Salah", "pass123", Role.ADMIN, new Adresse(1001, "Tunis"), "55512345"));
        us.ajouter(new Utilisateur(0, "Samir", "Ben Ali", "pass456", Role.PARTICIPANT, new Adresse(2001, "Sfax"), "98765432"));

        System.out.println("\n=== MODIFIER UTILISATEUR ===");
        us.modifier(new Utilisateur(1, "Fedi", "Samir", "newPass123", Role.ORGANISATEUR, new Adresse(1001, "Tunis"), "55599999"));

        System.out.println("\n=== SUPPRIMER UTILISATEUR ===");
        us.supprimer(new Utilisateur(1,"", "", "", null, null, ""));

        System.out.println("\n=== RECHERCHER UTILISATEURS ===");
        us.rechercher().forEach(System.out::println);
    }
}
