package main.java.com.model;

/**
 * Classe représentant une ligne de commande dans le système de shopping
 */
public class LigneCommande {
    private int id;
    private int commandeId;
    private int articleMarqueId;
    private int quantite;
    private double prixUnitaire;
    private double prixTotal;

    // Objet de relation 
    private ArticleMarque articleMarque;

    /**
     * Constructeur par défaut
     */
    public LigneCommande() {
    }

    /**
     * Constructeur avec paramètres
     * @param id Identifiant de la ligne de commande
     * @param commandeId Identifiant de la commande
     * @param articleMarqueId Identifiant de l'article-marque
     * @param quantite Quantité commandée
     * @param prixUnitaire Prix unitaire
     * @param prixTotal Prix total (quantité * prix unitaire)
     */
    public LigneCommande(int id, int commandeId, int articleMarqueId, int quantite, double prixUnitaire, double prixTotal) {
        this.id = id;
        this.commandeId = commandeId;
        this.articleMarqueId = articleMarqueId;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.prixTotal = prixTotal;
    }

    /**
     * Calcule le prix total à partir de la quantité et du prix unitaire
     */
    public void calculerPrixTotal() {
        this.prixTotal = this.quantite * this.prixUnitaire;
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

    public int getArticleMarqueId() {
        return articleMarqueId;
    }

    public void setArticleMarqueId(int articleMarqueId) {
        this.articleMarqueId = articleMarqueId;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
        // Recalcule le prix total si le prix unitaire est déjà défini
        if (this.prixUnitaire > 0) {
            calculerPrixTotal();
        }
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        // Recalcule le prix total si la quantité est déjà définie
        if (this.quantite > 0) {
            calculerPrixTotal();
        }
    }

    public double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public ArticleMarque getArticleMarque() {
        return articleMarque;
    }

    public void setArticleMarque(ArticleMarque articleMarque) {
        this.articleMarque = articleMarque;
    }

    @Override
    public String toString() {
        return quantite + " x " + prixUnitaire + "€ = " + prixTotal + "€";
    }
}
