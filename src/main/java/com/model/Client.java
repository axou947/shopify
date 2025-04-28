package main.java.com.model;

/**
 * Classe représentant un client dans le système de shopping
 */
public class Client {
    private int id; 
    private int utilisateurId;
    private String statut; // "nouveau" ou "ancien"

    /**
     * Constructeur par défaut
     */
    public Client() {
        this.statut = "nouveau"; // Par défaut, un client est nouveau
    }

    /**
     * Constructeur avec paramètres
     * @param id Identifiant du client
     * @param utilisateurId Identifiant de l'utilisateur associé
     * @param statut Statut du client (nouveau ou ancien)
     */
    public Client(int id, int utilisateurId, String statut) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.statut = statut;
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

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Client #" + id + " (" + statut + ")";
    }
}
