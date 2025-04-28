package main.java.com.controller;

import main.java.com.dao.DAOFactory;
import main.java.com.dao.PaiementDAO;
import main.java.com.model.Commande;
import main.java.com.model.Paiement;

import java.util.Date;
import java.util.List;

/**
 * Contrôleur pour gérer les opérations liées aux paiements
 */
public class PaiementController {
    private PaiementDAO paiementDAO;
    private CommandeController commandeController;

    /**
     * Constructeur du contrôleur de paiement
     */
    public PaiementController() {
        this.paiementDAO = new PaiementDAO();
        this.commandeController = new CommandeController();
    }

    /**
     * Récupère tous les paiements
     * @return Liste de tous les paiements
     */
    public List<Paiement> getAllPaiements() {
        return paiementDAO.findAll();
    }

    /**
     * Récupère un paiement par son ID
     * @param id L'identifiant du paiement
     * @return Le paiement correspondant ou null
     */
    public Paiement getPaiementById(int id) {
        return paiementDAO.findById(id);
    }

    /**
     * Récupère les paiements d'une commande
     * @param commandeId L'identifiant de la commande
     * @return Liste des paiements de la commande
     */
    public List<Paiement> getPaiementsByCommande(int commandeId) {
        return paiementDAO.findByCommande(commandeId);
    }

    /**
     * Crée un nouveau paiement
     * @param commandeId L'identifiant de la commande
     * @param montant Le montant du paiement
     * @param methode La méthode de paiement
     * @return ID du paiement créé, -1 si échec
     */
    public int createPaiement(int commandeId, double montant, String methode) {
        // Vérification de la commande
        Commande commande = commandeController.getCommandeById(commandeId);
        if (commande == null) {
            return -1;
        }

        // Création du paiement
        Paiement paiement = new Paiement();
        paiement.setCommandeId(commandeId);
        paiement.setMontant(montant);
        paiement.setDatePaiement(new Date());
        paiement.setMethode(methode);
        paiement.setStatut("en_attente");

        if (paiementDAO.create(paiement)) {
            return paiement.getId();
        } else {
            return -1;
        }
    }

    /**
     * Simule le traitement de paiement (délai d'attente puis validation)
     * @param paiementId L'identifiant du paiement
     * @return true si le paiement est validé, false sinon
     */
    public boolean traiterPaiement(int paiementId) {
        Paiement paiement = paiementDAO.findById(paiementId);
        if (paiement == null) {
            return false;
        }

        // Mise à jour du statut en "valide"
        if (paiementDAO.updateStatus(paiementId, "valide")) {
            // Mise à jour du statut de la commande en "validee"
            return commandeController.updateCommandeStatus(paiement.getCommandeId(), "validee");
        }

        return false;
    }

    /**
     * Met à jour le statut d'un paiement
     * @param paiementId L'identifiant du paiement
     * @param statut Le nouveau statut
     * @return true si l'opération a réussi, false sinon
     */
    public boolean updatePaiementStatus(int paiementId, String statut) {
        return paiementDAO.updateStatus(paiementId, statut);
    }

    /**
     * Annule un paiement
     * @param paiementId L'identifiant du paiement
     * @return true si l'opération a réussi, false sinon
     */
    public boolean cancelPaiement(int paiementId) {
        Paiement paiement = paiementDAO.findById(paiementId);
        if (paiement == null || !"en_attente".equals(paiement.getStatut())) {
            return false;
        }

        return paiementDAO.updateStatus(paiementId, "refuse");
    }
}
//
