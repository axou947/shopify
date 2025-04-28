package main.java.com.model;

/**
 * Classe représentant une marque dans le système de shopping
 */
public class Marque {
    private int id;
    private String nom;
    private String description; 
    private String logoUrl;

    /**
     * Constructeur par défaut
     */
    public Marque() {
    }

    /**
     * Constructeur avec paramètres
     * @param id Identifiant de la marque
     * @param nom Nom de la marque
     * @param description Description de la marque
     * @param logoUrl URL du logo de la marque
     */
    public Marque(int id, String nom, String description, String logoUrl) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.logoUrl = logoUrl;
    }

    // Getters et Setters
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @Override
    public String toString() {
        return nom;
    }
}
