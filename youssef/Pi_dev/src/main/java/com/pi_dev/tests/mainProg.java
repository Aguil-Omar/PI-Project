package com.pi_dev.tests;

import com.pi_dev.models.GestionEvenement.programme;
import com.pi_dev.services.ProgService;
import com.pi_dev.utils.DataSource;

import java.time.LocalTime;
import java.util.List;

public class mainProg {
    public static void main(String[] args) {
        DataSource dataSource = DataSource.getInstance();
        ProgService service = new ProgService();

        // Correct the time format by converting strings to LocalTime
        programme prog = new programme("Yoga MatinalEE", LocalTime.parse("08:00"), LocalTime.parse("09:00"), 2);
        programme prog1 = new programme("Yoga SoiréeRRRR", LocalTime.parse("18:00"), LocalTime.parse("19:00"), 3);

        // Adding programmes
        service.ajouter(prog);
        service.ajouter(prog1);
        // Modify an existing programme (ensure that the ID exists in the database)
        programme progModifie = new programme(2, "Yoga Avancé", LocalTime.parse("09:00"), LocalTime.parse("10:30"), 2);
        service.modifier(progModifie);

        // Delete a programme (ensure the ID exists in the database)
        programme progASupprimer = new programme(2, "Yoga Matinal", LocalTime.parse("08:00"), LocalTime.parse("09:00"), 1);
        service.supprimer(progASupprimer);

        // Retrieve all programmes
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
