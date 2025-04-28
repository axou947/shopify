package main.java.com.controller;

import main.java.com.dao.ArticleDAO;
import main.java.com.dao.DAOFactory;
import main.java.com.model.Article;

import java.util.List;




/**
 * Contrôleur pour gérer les opérations liées aux articles
 */
public class ArticleController {
    private ArticleDAO articleDAO; 

    public ArticleController() {
        this.articleDAO = DAOFactory.getArticleDAO();
    }

    /**
     * Récupère tous les articles
     * @return Liste de tous les articles
     */
    public List<Article> getAllArticles() {
        return articleDAO.findAll();
    }

    /**
     * Récupère un article par son ID
     * @param id L'identifiant de l'article
     * @return L'article correspondant ou null
     */
    public Article getArticleById(int id) {
        return articleDAO.findById(id);
    }

    /**
     * Recherche des articles par nom
     * @param nom Le nom ou partie du nom à rechercher
     * @return Liste des articles correspondants
     */
    public List<Article> searchArticlesByName(String nom) {
        return articleDAO.findByNom(nom);
    }

    /**
     * Recherche des articles par marque
     * @param marqueId L'identifiant de la marque
     * @return Liste des articles de cette marque
     */
    public List<Article> getArticlesByMarque(int marqueId) {
        return articleDAO.findByMarque(marqueId);
    }

    /**
     * Ajoute un nouvel article
     * @param article L'article à ajouter
     * @return true si l'opération a réussi, false sinon
     */
    public boolean addArticle(Article article) {
        return articleDAO.create(article);
    }

    /**
     * Met à jour un article existant
     * @param article L'article à mettre à jour
     * @return true si l'opération a réussi, false sinon
     */
    public boolean updateArticle(Article article) {
        return articleDAO.update(article);
    }

    /**
     * Supprime un article
     * @param articleId L'identifiant de l'article à supprimer
     * @return true si l'opération a réussi, false sinon
     */
    public boolean deleteArticle(int articleId) {
        return articleDAO.delete(articleId);
    }

    /**
     * Calcule le prix pour une quantité donnée d'un article
     * @param articleId L'identifiant de l'article
     * @param quantite La quantité demandée
     * @return Le prix calculé ou -1 si l'article n'est pas trouvé
     */
    public double calculatePrice(int articleId, int quantite) {
        Article article = articleDAO.findById(articleId);
        if (article != null) {
            return article.calculerPrix(quantite);
        }
        return -1;
    }

    /**
     * Vérifie si un article est disponible en stock
     * @param articleId L'identifiant de l'article
     * @param quantite La quantité demandée
     * @return true si l'article est disponible en quantité suffisante
     */
    public boolean isArticleAvailable(int articleId, int quantite) {
        Article article = articleDAO.findById(articleId);
        return article != null && article.getStock() >= quantite;
    }

    /**
     * Met à jour le stock d'un article
     * @param articleId L'identifiant de l'article
     * @param quantite La quantité à soustraire du stock
     * @return true si l'opération a réussi, false sinon
     */
    public boolean updateStock(int articleId, int quantite) {
        Article article = articleDAO.findById(articleId);
        if (article != null && article.getStock() >= quantite) {
            article.setStock(article.getStock() - quantite);
            return articleDAO.update(article);
        }
        return false;
    }
}
