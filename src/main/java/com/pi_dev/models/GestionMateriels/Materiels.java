package com.pi_dev.models.GestionMateriels;

public class Materiels {

        private int id;
        private String nom;

        private float prix;
        private Disponibilte etat;
         private TypeMateriels typeMateriel;
         private String imagePath;


    public Materiels (int id,String nom , float prix , Disponibilte etat , TypeMateriels typeMateriel, String imagePath ) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.etat = etat;
        this.typeMateriel  = typeMateriel;
        this.imagePath = imagePath;

    }
    public Materiels ( String nom,  float prix , Disponibilte etat, TypeMateriels typeMateriel,String imagePath ) {
        this.nom = nom;
        this.prix = prix;
        this.etat = etat;
        this.typeMateriel  = typeMateriel ;
        this.imagePath = imagePath;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public Disponibilte getEtat() {
        return etat;
    }

    public void setEtat(Disponibilte etat) {
        this.etat = etat;
    }
    public TypeMateriels getTypeMateriel () {
        return typeMateriel ;
    }
    public void setTypeMateriel(TypeMateriels typeMateriel) {
        this.typeMateriel = typeMateriel;
    }
    public String getImagePath() {
        return imagePath;
    }


    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    @Override
    public String toString() {
        return "Materiels{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                ", etat=" + etat +
                ", TypeMateriel =" + typeMateriel  +
                ", imagePath='" + imagePath + '\'' +

                '}';
    }


}

