package com.pi_dev.tests;

import com.pi_dev.models.GestionEvenement.programme;
import com.pi_dev.services.ProgService;
import com.pi_dev.utils.DataSource;

import java.util.List;

public class mainProg {
    public static void main(String[] args) {
        DataSource dataSource = DataSource.getInstance();
        ProgService service = new ProgService();

        programme prog = new programme("Yoga MatinalEE", "08:00", "09:00", 2);
        programme prog1 = new programme("Yoga SoiréeRRRR", "18:00", "19:00", 3);
        service.ajouter(prog);
        service.ajouter(prog1);
        programme progModifie = new programme(2, "Yoga Avancé", "09:00", "10:30", 2);
        service.modifier(progModifie);

        programme progASupprimer = new programme(2, "Yoga Matinal", "08:00", "09:00", 1);
        service.supprimer(progASupprimer);

        List<programme> programmes = service.rechercher();
        if (programmes.isEmpty()) {
            System.out.println("Aucun programme trouvé.");
        } else {
            System.out.println("Liste des programmes :");
            for (programme p : programmes) {
                System.out.println(p);
            }
        }
    }
}
