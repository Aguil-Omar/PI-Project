package com.pi_dev.tests;
import com.pi_dev.models.GestionMateriels.Disponibilite;
import com.pi_dev.services.MaterielsServices;
import com.pi_dev.services.TypeMaterielsServices ;

public class MainMateriel {
    public static void main(String[] args) {
        MaterielsServices ps = new MaterielsServices();
        ps.ajouter(new com.pi_dev.models.GestionMateriels.Materiels("micro" , 250.0f, Disponibilite.DISPONIBLE));
        ps.modifier(new com.pi_dev.models.GestionMateriels.Materiels("micro", 250.0f , Disponibilite.valueOf("INDISPONIBLE")));
        ps.supprimer(new com.pi_dev.models.GestionMateriels.Materiels("micro",250.0f,Disponibilite .valueOf("DISPONIBLE")));
        System.out.println(ps.rechercher());
        System.out.println(Disponibilite.valueOf("INDISPONIBLE"));

        TypeMaterielsServices pst = new TypeMaterielsServices();
        pst.ajouter(new com.pi_dev.models.GestionMateriels.TypeMateriels("micro", "sans fils"));
        pst.modifier(new com.pi_dev.models.GestionMateriels.TypeMateriels( "hautparleur", "audio"));
        pst.supprimer(new com.pi_dev.models.GestionMateriels.TypeMateriels("chaise", "plasqtique"));
        System.out.println(pst.rechercher());
    }
}
