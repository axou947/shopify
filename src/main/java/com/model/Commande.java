package main.java.com.model;

import java.util.Date;

/**
 * Classe représentant une commande dans le système de shopping
 */
public class Commande {
    private int id;
    private int clientId;
    private Date dateCommande;
    private String statut; // enum('en_cours', 'validee', 'expediee', 'livree', 'annulee')
    private double montantTotal;
    private double montantRemise; 
    private String note;

    /**
     * Constructeur par défaut
     */
    public Commande() {
        this.dateCommande = new Date(); // Par défaut, date de commande = aujourd'hui
        this.statut = "en_cours"; // Par défaut, statut = en cours
        this.montantRemise = 0.0; // Par défaut, pas de remise
    }

    /**
     * Constructeur avec paramètres
     * @param id Identifiant de la commande
     * @param clientId Identifiant du client
     * @param dateCommande Date de la commande
     * @param statut Statut de la commande
     * @param montantTotal Montant total de la commande
     * @param montantRemise Montant de la remise
     * @param note Note éventuelle
     */
    public Commande(int id, int clientId, Date dateCommande, String statut, double montantTotal, double montantRemise, String note) {
        this.id = id;
        this.clientId = clientId;
        this.dateCommande = dateCommande;
        this.statut = statut;
        this.montantTotal = montantTotal;
        this.montantRemise = montantRemise;
        this.note = note;
    }

    /**
     * Calcule le montant net (total - remise)
     * @return Le montant net
     */
    public double getMontantNet() {
        return montantTotal - montantRemise;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public double getMontantRemise() {
        return montantRemise;
    }

    public void setMontantRemise(double montantRemise) {
        this.montantRemise = montantRemise;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Commande #" + id + " (" + statut + ") - " + montantTotal + "€";
    }
}
