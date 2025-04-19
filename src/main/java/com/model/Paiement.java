package main.java.com.model;

import java.util.Date;

/**
 * Classe représentant un paiement dans le système de shopping
 */
public class Paiement {
    private int id;
    private int commandeId;
    private double montant;
    private Date datePaiement;
    private String methode; // enum('carte', 'paypal', 'virement', 'especes')
    private String statut; // enum('en_attente', 'valide', 'refuse', 'rembourse')

    /**
     * Constructeur par défaut
     */
    public Paiement() {
        this.datePaiement = new Date(); // Par défaut, date de paiement = maintenant
        this.statut = "en_attente"; // Par défaut, statut = en attente
    }

    /**
     * Constructeur avec paramètres
     * @param id Identifiant du paiement
     * @param commandeId Identifiant de la commande associée
     * @param montant Montant du paiement
     * @param datePaiement Date du paiement
     * @param methode Méthode de paiement
     * @param statut Statut du paiement
     */
    public Paiement(int id, int commandeId, double montant, Date datePaiement, String methode, String statut) {
        this.id = id;
        this.commandeId = commandeId;
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.methode = methode;
        this.statut = statut;
    }

    /**
     * Vérifie si le paiement est validé
     * @return true si le paiement est validé, false sinon
     */
    public boolean isValidated() {
        return "valide".equals(this.statut);
    }

    /**
     * Vérifie si le paiement peut être remboursé
     * @return true si le paiement peut être remboursé, false sinon
     */
    public boolean isRefundable() {
        return "valide".equals(this.statut);
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(int commandeId) {
        this.commandeId = commandeId;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Date getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }

    public String getMethode() {
        return methode;
    }

    public void setMethode(String methode) {
        this.methode = methode;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Paiement #" + id + " (" + statut + ") - " + montant + "€";
    }
}