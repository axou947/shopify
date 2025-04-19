package main.java.com.controller;

import main.java.com.dao.CommandeDAO;
import main.java.com.dao.DAOFactory;
import main.java.com.model.Article;
import main.java.com.model.ArticleMarque;
import main.java.com.model.Commande;
import main.java.com.model.LigneCommande;

import java.util.Date;
import java.util.List;

/**
 * Contrôleur pour gérer les opérations liées aux commandes
 */
public class CommandeController {
    private CommandeDAO commandeDAO;
    private ArticleController articleController;

    /**
     * Constructeur du contrôleur de commande
     */
    public CommandeController() {
        this.commandeDAO = DAOFactory.getCommandeDAO();
        this.articleController = new ArticleController();
    }

    /**
     * Récupère toutes les commandes
     * @return Liste de toutes les commandes
     */
    public List<Commande> getAllCommandes() {
        return commandeDAO.findAll();
    }

    /**
     * Récupère une commande par son ID
     * @param id L'identifiant de la commande
     * @return La commande correspondante ou null
     */
    public Commande getCommandeById(int id) {
        return commandeDAO.findById(id);
    }

    /**
     * Récupère les commandes d'un client
     * @param clientId L'identifiant du client
     * @return Liste des commandes du client
     */
    public List<Commande> getCommandesByClient(int clientId) {
        return commandeDAO.findByClient(clientId);
    }

    /**
     * Récupère les lignes de commande d'une commande
     * @param commandeId L'identifiant de la commande
     * @return Liste des lignes de commande
     */
    public List<LigneCommande> getLignesCommande(int commandeId) {
        return commandeDAO.findLignesCommande(commandeId);
    }

    /**
     * Crée une nouvelle commande
     * @param clientId L'identifiant du client
     * @param lignesCommande Liste des lignes de commande
     * @param montantRemise Montant de la remise appliquée
     * @param note Note éventuelle pour la commande
     * @return ID de la commande créée, -1 si échec
     */
    public int createCommande(int clientId, List<LigneCommande> lignesCommande, double montantRemise, String note) {
        // Calcul du montant total
        double montantTotal = 0;
        for (LigneCommande ligne : lignesCommande) {
            montantTotal += ligne.getPrixTotal();
        }

        // Création de la commande
        Commande commande = new Commande();
        commande.setClientId(clientId);
        commande.setDateCommande(new Date());
        commande.setStatut("en_cours");
        commande.setMontantTotal(montantTotal);
        commande.setMontantRemise(montantRemise);
        commande.setNote(note);

        // Sauvegarde de la commande et de ses lignes
        return commandeDAO.createWithLignes(commande, lignesCommande);
    }

    /**
     * Met à jour le statut d'une commande
     * @param commandeId L'identifiant de la commande
     * @param statut Le nouveau statut
     * @return true si l'opération a réussi, false sinon
     */
    public boolean updateCommandeStatus(int commandeId, String statut) {
        Commande commande = commandeDAO.findById(commandeId);
        if (commande != null) {
            commande.setStatut(statut);
            return commandeDAO.update(commande);
        }
        return false;
    }

    /**
     * Annule une commande
     * @param commandeId L'identifiant de la commande
     * @return true si l'opération a réussi, false sinon
     */
    public boolean cancelCommande(int commandeId) {
        Commande commande = commandeDAO.findById(commandeId);
        if (commande != null && !"annulee".equals(commande.getStatut())) {
            // Change le statut
            commande.setStatut("annulee");

            // Récupère les lignes de commande
            List<LigneCommande> lignes = commandeDAO.findLignesCommande(commandeId);

            // Restitue les articles au stock
            for (LigneCommande ligne : lignes) {
                ArticleMarque articleMarque = ligne.getArticleMarque();
                if (articleMarque != null) {
                    Article article = articleMarque.getArticle();
                    if (article != null) {
                        // Remet la quantité en stock
                        article.setStock(article.getStock() + ligne.getQuantite());
                        articleController.updateArticle(article);
                    }
                }
            }

            return commandeDAO.update(commande);
        }
        return false;
    }

    /**
     * Vérifie la disponibilité des articles pour une commande
     * @param lignesCommande Liste des lignes de commande à vérifier
     * @return true si tous les articles sont disponibles, false sinon
     */
    public boolean checkArticlesAvailability(List<LigneCommande> lignesCommande) {
        for (LigneCommande ligne : lignesCommande) {
            ArticleMarque articleMarque = ligne.getArticleMarque();
            if (articleMarque != null) {
                Article article = articleMarque.getArticle();
                if (article != null) {
                    if (article.getStock() < ligne.getQuantite()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Décremente le stock des articles pour une commande
     * @param lignesCommande Liste des lignes de commande
     * @return true si l'opération a réussi, false sinon
     */
    public boolean decrementStock(List<LigneCommande> lignesCommande) {
        for (LigneCommande ligne : lignesCommande) {
            ArticleMarque articleMarque = ligne.getArticleMarque();
            if (articleMarque != null) {
                Article article = articleMarque.getArticle();
                if (article != null) {
                    // Vérifie la disponibilité
                    if (article.getStock() < ligne.getQuantite()) {
                        return false;
                    }

                    // Décremente le stock
                    article.setStock(article.getStock() - ligne.getQuantite());
                    if (!articleController.updateArticle(article)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}