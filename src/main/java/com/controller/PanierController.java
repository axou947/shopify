package main.java.com.controller;

import main.java.com.dao.ArticleDAO;
import main.java.com.dao.DAOFactory;
import main.java.com.model.Article;
import main.java.com.model.ArticleMarque;
import main.java.com.model.LigneCommande;
import main.java.com.model.Remise;
import main.java.com.util.ArticleMarqueUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Contrôleur pour gérer les opérations liées au panier d'achat
 */
public class PanierController {
    private List<LigneCommande> lignesPanier;
    private ArticleDAO articleDAO;
    private CommandeController commandeController;
    private double remiseTotal;
    private String codeRemise;

    // Variabme pour mémoriser le montant net de la dernière commande
    private double lastOrderNetAmount = 0;

    /**
     * Constructeur du contrôleur de panier
     */
    public PanierController() {
        this.lignesPanier = new ArrayList<>();
        this.articleDAO = DAOFactory.getArticleDAO();
        this.commandeController = new CommandeController();
        this.remiseTotal = 0;
    }

    /**
     * Ajoute un article au panier
     * @param articleMarque L'ArticleMarque à ajouter
     * @param quantite La quantité à ajouter
     * @return true si l'opération a réussi, false sinon
     */
    public boolean ajouterArticle(ArticleMarque articleMarque, int quantite) {
        // Vérifie la disponibilité
        Article article = articleMarque.getArticle();
        if (article == null || quantite <= 0 || article.getStock() < quantite) {
            return false;
        }

        // Vérifie si l'article est déjà dans le panier
        for (LigneCommande ligne : lignesPanier) {
            if (ligne.getArticleMarqueId() == articleMarque.getId()) {
                // Augmente la quantité
                int nouvelleQuantite = ligne.getQuantite() + quantite;
                if (article.getStock() < nouvelleQuantite) {
                    return false; // Stock insuffisant
                }
                ligne.setQuantite(nouvelleQuantite);
                // Recalcule le prix total
                double prix = getPrixUnitaire(articleMarque, nouvelleQuantite);
                ligne.setPrixUnitaire(prix);
                ligne.calculerPrixTotal();
                return true;
            }
        }

        // Article non présent dans le panier, on l'ajoute
        LigneCommande ligne = new LigneCommande();
        ligne.setArticleMarqueId(articleMarque.getId());
        ligne.setArticleMarque(articleMarque);
        ligne.setQuantite(quantite);
        double prix = getPrixUnitaire(articleMarque, quantite);
        ligne.setPrixUnitaire(prix);
        ligne.calculerPrixTotal();
        lignesPanier.add(ligne);

        return true;
    }

    /**
     * Calcule le prix unitaire en tenant compte des prix en gros
     * @param articleMarque L'ArticleMarque
     * @param quantite La quantité
     * @return Le prix unitaire calculé
     */
    private double getPrixUnitaire(ArticleMarque articleMarque, int quantite) {
        Article article = articleMarque.getArticle();

        // Si un prix spécifique est défini pour cet article-marque, on l'utilise
        if (articleMarque.getPrixSpecifique() != null) {
            return articleMarque.getPrixSpecifique();
        }

        // Sinon, on calcule en fonction des prix en gros
        if (article.getPrixGros() != null && article.getQuantiteGros() != null &&
                quantite >= article.getQuantiteGros()) {

            // Calcul du nombre de lots complets au prix en gros
            int lots = quantite / article.getQuantiteGros();
            int reste = quantite % article.getQuantiteGros();

            // Prix total = (nombre de lots * prix en gros) + (reste * prix unitaire)
            double prixTotal = (lots * article.getPrixGros()) + (reste * article.getPrixUnitaire());

            // Prix unitaire moyen
            return prixTotal / quantite;
        } else {
            return article.getPrixUnitaire();
        }
    }

    /**
     * Modifie la quantité d'un article dans le panier
     * @param index L'index de la ligne dans le panier
     * @param quantite La nouvelle quantité
     * @return true si l'opération a réussi, false sinon
     */
    public boolean modifierQuantite(int index, int quantite) {
        if (index < 0 || index >= lignesPanier.size() || quantite <= 0) {
            return false;
        }

        LigneCommande ligne = lignesPanier.get(index);
        ArticleMarque articleMarque = ligne.getArticleMarque();
        if (articleMarque == null || articleMarque.getArticle() == null) {
            return false;
        }

        // Vérifie la disponibilité
        Article article = articleMarque.getArticle();
        if (article.getStock() < quantite) {
            return false;
        }

        // Met à jour la quantité
        ligne.setQuantite(quantite);

        // Recalcule le prix unitaire et le prix total
        double prix = getPrixUnitaire(articleMarque, quantite);
        ligne.setPrixUnitaire(prix);
        ligne.calculerPrixTotal();

        return true;
    }

    /**
     * Supprime un article du panier
     * @param index L'index de la ligne dans le panier
     * @return true si l'opération a réussi, false sinon
     */
    public boolean supprimerArticle(int index) {
        if (index < 0 || index >= lignesPanier.size()) {
            return false;
        }

        lignesPanier.remove(index);
        return true;
    }

    /**
     * Vide le panier
     */
    public void viderPanier() {
        lignesPanier.clear();
        remiseTotal = 0;
        codeRemise = null;
        // Ne pas réinitialiser lastOrderNetAmount ici
    }

    /**
     * Applique un code de remise au panier
     * @param remise La remise à appliquer
     * @return true si la remise a été appliquée, false sinon
     */
    public boolean appliquerRemise(Remise remise) {
        if (remise == null || !remise.isValide(new Date())) {
            return false;
        }

        // Vérifie la quantité minimale
        int quantiteTotale = 0;
        for (LigneCommande ligne : lignesPanier) {
            quantiteTotale += ligne.getQuantite();
        }

        if (quantiteTotale < remise.getQuantiteMin()) {
            return false;
        }

        // Calcule le montant total avant remise
        double montantTotal = getMontantTotal();

        // Applique la remise
        remiseTotal = remise.calculerRemise(montantTotal);
        codeRemise = remise.getCode();

        return true;
    }

    /**
     * Annule la remise appliquée
     */
    public void annulerRemise() {
        remiseTotal = 0;
        codeRemise = null;
    }

    /**
     * Valide le panier et crée une commande
     * @param clientId L'ID du client
     * @param note Une note éventuelle pour la commande
     * @return L'ID de la commande créée, -1 si échec
     */
    public int validerPanier(int clientId, String note) {
        if (lignesPanier.isEmpty()) {
            return -1;
        }

        // Assurez-vous que chaque ArticleMarque existe dans la base de données
        for (LigneCommande ligne : lignesPanier) {
            ArticleMarque articleMarque = ligne.getArticleMarque();
            if (articleMarque != null) {
                // Utilise notre utilitaire pour garantir que la relation existe
                int articleMarqueId = ArticleMarqueUtil.ensureArticleMarqueExists(articleMarque);
                ligne.setArticleMarqueId(articleMarqueId);
            }
        }

        // Vérifie la disponibilité des articles
        if (!commandeController.checkArticlesAvailability(lignesPanier)) {
            return -1;
        }

        // Décrémente le stock
        if (!commandeController.decrementStock(lignesPanier)) {
            return -1;
        }

        // Mémoriser le montant net avant de vider le panier
        lastOrderNetAmount = getMontantNet();

        // Crée la commande
        int commandeId = commandeController.createCommande(clientId, lignesPanier, remiseTotal, note);

        // Si la commande a été créée avec succès, vide le panier
        if (commandeId > 0) {
            // On ne vide pas les variables mémorisées ici pour permettre
            // à la page de paiement d'y accéder après création de la commande
            lignesPanier.clear();
        }

        return commandeId;
    }
    /**
     * Récupère le montant net de la dernière commande validée
     * @return Le montant net de la dernière commande
     */
    public double getLastOrderNetAmount() {
        return lastOrderNetAmount;
    }

    /**
     * Récupère le montant total du panier (avant remise)
     * @return Le montant total
     */
    public double getMontantTotal() {
        double total = 0;
        for (LigneCommande ligne : lignesPanier) {
            total += ligne.getPrixTotal();
        }
        return total;
    }

    /**
     * Récupère le montant de la remise
     * @return Le montant de la remise
     */
    public double getMontantRemise() {
        return remiseTotal;
    }

    /**
     * Récupère le montant net (total - remise)
     * @return Le montant net
     */
    public double getMontantNet() {
        return getMontantTotal() - remiseTotal;
    }

    /**
     * Récupère le code de remise appliqué
     * @return Le code de remise ou null si aucune remise n'est appliquée
     */
    public String getCodeRemise() {
        return codeRemise;
    }

    /**
     * Récupère les lignes du panier
     * @return La liste des lignes du panier
     */
    public List<LigneCommande> getLignesPanier() {
        return lignesPanier;
    }
}
//
