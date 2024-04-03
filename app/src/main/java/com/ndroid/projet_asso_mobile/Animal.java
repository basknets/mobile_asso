package com.ndroid.projet_asso_mobile;


public class Animal {
    private String animal_id; // Variable d'instance pour stocker l'ID de l'animal
    private String nom;
    private String espece;
    private String description;

    // Constructeur pour initialiser l'objet Animal avec l'ID, le nom, l'espèce et la description
    public Animal(String animal_id, String nom, String espece, String description) {
        this.animal_id = animal_id;
        this.nom = nom;
        this.espece = espece;
        this.description = description;
    }

    // Méthode pour récupérer l'ID de l'animal
    public String getId() {
        return animal_id;
    }

    // Méthodes pour récupérer le nom, l'espèce et la description de l'animal
    public String getNom() {
        return nom;
    }

    public String getEspece() {
        return espece;
    }

    public String getDescription() {
        return description;
    }
}

