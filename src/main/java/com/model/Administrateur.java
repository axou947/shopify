package main.java.com.model;

/**
 * Classe représentant un administrateur dans le système de shopping
 */
public class Administrateur {
    private int id;
    private int utilisateurId; 
    private String role;

    /**
     * Constructeur par défaut
     */
    public Administrateur() {
    }

    /**
     * Constructeur avec paramètres
     * @param id Identifiant de l'administrateur
     * @param utilisateurId Identifiant de l'utilisateur associé
     * @param role Rôle de l'administrateur
     */
    public Administrateur(int id, int utilisateurId, String role) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.role = role;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Administrateur #" + id + " (" + role + ")";
    }
}
