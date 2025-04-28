package main.java.com.dao;

import main.java.com.config.DatabaseConnection;
import main.java.com.model.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.List; 

/**  
 * Classe DAO pour l'entité Article
 */
public class ArticleDAO implements GenericDAO<Article>  {
    private Connection connection; 

    public ArticleDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Article findById(int id) {
        Article article = null;
        String query = "SELECT * FROM article WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    article = extractArticleFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'article par ID : " + e.getMessage());
        }

        return article;
    }

    @Override
    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT * FROM article";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Article article = extractArticleFromResultSet(rs);
                articles.add(article);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les articles : " + e.getMessage());
        }

        return articles;
    }

    @Override
    public boolean create(Article article) {
        String query = "INSERT INTO article (nom, description, prix_unitaire, prix_gros, quantite_gros, stock, image_url) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, article.getNom());
            pstmt.setString(2, article.getDescription());
            pstmt.setDouble(3, article.getPrixUnitaire());

            if (article.getPrixGros() != null) {
                pstmt.setDouble(4, article.getPrixGros());
            } else {
                pstmt.setNull(4, Types.DECIMAL);
            }

            if (article.getQuantiteGros() != null) {
                pstmt.setInt(5, article.getQuantiteGros());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }

            pstmt.setInt(6, article.getStock());
            pstmt.setString(7, article.getImageUrl());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        article.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'article : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean update(Article article) {
        String query = "UPDATE article SET nom = ?, description = ?, prix_unitaire = ?, " +
                "prix_gros = ?, quantite_gros = ?, stock = ?, image_url = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, article.getNom());
            pstmt.setString(2, article.getDescription());
            pstmt.setDouble(3, article.getPrixUnitaire());

            if (article.getPrixGros() != null) {
                pstmt.setDouble(4, article.getPrixGros());
            } else {
                pstmt.setNull(4, Types.DECIMAL);
            }

            if (article.getQuantiteGros() != null) {
                pstmt.setInt(5, article.getQuantiteGros());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }

            pstmt.setInt(6, article.getStock());
            pstmt.setString(7, article.getImageUrl());
            pstmt.setInt(8, article.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'article : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM article WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'article : " + e.getMessage());
        }

        return false;
    }

    public List<Article> findByMarque(int marqueId) {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT a.* FROM article a " +
                "JOIN article_marque am ON a.id = am.article_id " +
                "WHERE am.marque_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, marqueId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Article article = extractArticleFromResultSet(rs);
                    articles.add(article);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des articles par marque : " + e.getMessage());
        }

        return articles;
    }

    public List<Article> findByNom(String nom) {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT * FROM article WHERE nom LIKE ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "%" + nom + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Article article = extractArticleFromResultSet(rs);
                    articles.add(article);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des articles par nom : " + e.getMessage());
        }

        return articles;
    }

    private Article extractArticleFromResultSet(ResultSet rs) throws SQLException {
        Article article = new Article();
        article.setId(rs.getInt("id"));
        article.setNom(rs.getString("nom"));
        article.setDescription(rs.getString("description"));
        article.setPrixUnitaire(rs.getDouble("prix_unitaire"));

        Double prixGros = rs.getDouble("prix_gros");
        if (!rs.wasNull()) {
            article.setPrixGros(prixGros);
        }

        Integer quantiteGros = rs.getInt("quantite_gros");
        if (!rs.wasNull()) {
            article.setQuantiteGros(quantiteGros);
        }

        article.setStock(rs.getInt("stock"));
        article.setImageUrl(rs.getString("image_url"));

        return article;
    }
}
