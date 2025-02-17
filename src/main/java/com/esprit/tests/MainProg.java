package com.esprit.tests;

import com.esprit.services.utilisateur.AdminService;

public class MainProg {

    public static void main(String[] args) {
        AdminService us = new AdminService();

        System.out.println("=== AJOUTER UTILISATEUR ===");
        //us.modifier(new Utilisateur(5,"A", "B", "C", "D", Role.ADMIN, new Adresse(123, "Rue"), "E"));
        System.out.println("\n=== RECHERCHER UTILISATEURS ===");
        us.rechercher().forEach(System.out::println);
    }
}
