package main.java.com.model;

/**
 * Classe représentant un article dans le système de shopping
 */
public class Article {
    private int id;
    private String nom;
    private String description; 
    private double prixUnitaire;
    private Double prixGros;
    private Integer quantiteGros;
    private int stock;
    private String imageUrl;

    /**
     * Constructeur par défaut
     */
    public Article() {
    }

    /**
     * Constructeur avec paramètres
     * @param id Identifiant de l'article
     * @param nom Nom de l'article
     * @param description Description de l'article
     * @param prixUnitaire Prix unitaire de l'article
     * @param prixGros Prix en gros de l'article (peut être null)
     * @param quantiteGros Quantité minimale pour le prix en gros (peut être null)
     * @param stock Stock disponible de l'article
     * @param imageUrl URL de l'image de l'article
     */
    public Article(int id, String nom, String description, double prixUnitaire, Double prixGros, Integer quantiteGros, int stock, String imageUrl) {
        this.id = id ;
        this.nom = nom;
        this.description = description;
        this.prixUnitaire = prixUnitaire;
        this.prixGros = prixGros;
        this.quantiteGros = quantiteGros;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    /**
     * Calcule le prix pour une quantité donnée en tenant compte des prix en gros
     * @param quantite La quantité d'articles
     * @return Le prix total calculé
     */
    public double calculerPrix(int quantite) {
        if (prixGros != null && quantiteGros != null && quantite >= quantiteGros) {
            // Calcul du nombre de lots complets au prix en gros
            int lots = quantite / quantiteGros;
            int reste = quantite % quantiteGros;

            // Prix total = (nombre de lots * prix en gros) + (reste * prix unitaire)
            return (lots * prixGros) + (reste * prixUnitaire);
        } else {
            // Pas de prix en gros ou quantité inférieure à la quantité en gros
            return quantite * prixUnitaire;
        }
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

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Double getPrixGros() {
        return prixGros;
    }

    public void setPrixGros(Double prixGros) {
        this.prixGros = prixGros;
    }

    public Integer getQuantiteGros() {
        return quantiteGros;
    }

    public void setQuantiteGros(Integer quantiteGros) {
        this.quantiteGros = quantiteGros;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return nom + " - " + prixUnitaire + "€";
    }
}
