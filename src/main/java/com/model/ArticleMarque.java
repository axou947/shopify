package main.java.com.model;

/**
 * Classe représentant une relation entre un article et une marque
 */
public class ArticleMarque {
    private int id;
    private int articleId;
    private int marqueId;
    private Double prixSpecifique; 

    // Objets de relation
    private Article article;
    private Marque marque;

    /**
     * Constructeur par défaut
     */
    public ArticleMarque() {
    }

    /**
     * Constructeur avec paramètres
     *
     * @param id             Identifiant de la relation
     * @param articleId      Identifiant de l'article
     * @param marqueId       Identifiant de la marque
     * @param prixSpecifique Prix spécifique pour cet article de cette marque
     */
    public ArticleMarque(int id, int articleId, int marqueId, Double prixSpecifique) {
        this.id = id;
        this.articleId = articleId;
        this.marqueId = marqueId;
        this.prixSpecifique = prixSpecifique;
    }

    /**
     * Obtient le prix à utiliser (spécifique ou prix standard de l'article)
     *
     * @return Le prix à utiliser
     */
    public double getPrix() {
        if (prixSpecifique != null) {
            return prixSpecifique;
        } else if (article != null) {
            return article.getPrixUnitaire();
        }
        return 0;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getMarqueId() {
        return marqueId;
    }

    public void setMarqueId(int marqueId) {
        this.marqueId = marqueId;
    }

    public Double getPrixSpecifique() {
        return prixSpecifique;
    }

    public void setPrixSpecifique(Double prixSpecifique) {
        this.prixSpecifique = prixSpecifique;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Marque getMarque() {
        return marque;
    }

    public void setMarque(Marque marque) {
        this.marque = marque;
    }

    @Override
    public String toString() {
        String articleNom = article != null ? article.getNom() : "Article #" + articleId;
        String marqueNom = marque != null ? marque.getNom() : "Marque #" + marqueId;
        return articleNom + " - " + marqueNom + " (" + (prixSpecifique != null ? prixSpecifique : "prix standard") + ")";
    }
}
