package com.Materiels.tests;

import com.Materiels.models.CategorieMateriel;
import com.Materiels.models.Disponibilte;
import com.Materiels.models.Materiels;
import com.Materiels.services.MaterielsServices;
public class MainProg {
    public static void main(String[] args) {
        MaterielsServices ps = new MaterielsServices();
        //ps.ajouter(new Materiels("micro", CategorieMateriel.VIDEO , 250.0f, Disponibilte.DISPONIBLE));
        //ps.modifier(new Materiels(1, "micro", 250.0f , Disponibilte.valueOf("INDISPONIBLE"), CategorieMateriel.valueOf("AUDIO")));
        //ps.supprimer(new Materiels(1,"micro",250.0f,Disponibilte.valueOf("DISPONIBLE"),CategorieMateriel.valueOf("AUDIO")));
        System.out.println(ps.rechercher());
        System.out.println(Disponibilte.valueOf("INDISPONIBLE"));
    }
}