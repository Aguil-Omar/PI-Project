package com.Materiels.tests;

import com.Materiels.models.Disponibilte;
import com.Materiels.models.Materiels;
import com.Materiels.models.TypeMateriels;
import com.Materiels.services.MaterielsServices;
import com.Materiels.services.TypeMaterielsServices;

public class MainProg {
    public static void main(String[] args) {
        MaterielsServices ps = new MaterielsServices();
        ps.ajouter(new Materiels("micro" , 250.0f, Disponibilte.DISPONIBLE));
        ps.modifier(new Materiels("micro", 250.0f , Disponibilte.valueOf("INDISPONIBLE")));
        ps.supprimer(new Materiels("micro",250.0f,Disponibilte.valueOf("DISPONIBLE")));
        System.out.println(ps.rechercher());
        System.out.println(Disponibilte.valueOf("INDISPONIBLE"));

    TypeMaterielsServices pst = new TypeMaterielsServices();
        pst.ajouter(new TypeMateriels("micro", "sans fils"));
        pst.modifier(new TypeMateriels( "hautparleur", "audio"));
        pst.supprimer(new TypeMateriels("chaise", "plasqtique"));
        System.out.println(pst.rechercher());
}}
