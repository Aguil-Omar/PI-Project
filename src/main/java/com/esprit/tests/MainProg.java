package com.esprit.tests;

import com.esprit.models.Adresse;
import com.esprit.models.Role;
import com.esprit.models.Utilisateur;
import com.esprit.services.AdminService;

public class MainProg {

    public static void main(String[] args) {
        AdminService us = new AdminService();

        System.out.println("=== AJOUTER UTILISATEUR ===");

        System.out.println("\n=== RECHERCHER UTILISATEURS ===");
        us.rechercher().forEach(System.out::println);
    }
}
