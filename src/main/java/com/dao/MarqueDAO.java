package main.java.com.dao;

import main.java.com.config.DatabaseConnection;
import main.java.com.model.Marque;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO pour l'entité Marque
 */
public class MarqueDAO implements GenericDAO<Marque> {
    private Connection connection;

    public MarqueDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Marque findById(int id) {
        Marque marque = null;
        String query = "SELECT * FROM marque WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    marque = extractMarqueFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la marque par ID : " + e.getMessage());
        }

        return marque;
    }

    @Override
    public List<Marque> findAll() {
        List<Marque> marques = new ArrayList<>();
        String query = "SELECT * FROM marque ORDER BY nom";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Marque marque = extractMarqueFromResultSet(rs);
                marques.add(marque);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de toutes les marques : " + e.getMessage());
        }

        return marques;
    }

    @Override
    public boolean create(Marque marque) {
        String query = "INSERT INTO marque (nom, description, logo_url) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, marque.getNom());
            pstmt.setString(2, marque.getDescription());
            pstmt.setString(3, marque.getLogoUrl());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        marque.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la marque : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean update(Marque marque) {
        String query = "UPDATE marque SET nom = ?, description = ?, logo_url = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, marque.getNom());
            pstmt.setString(2, marque.getDescription());
            pstmt.setString(3, marque.getLogoUrl());
            pstmt.setInt(4, marque.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la marque : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM marque WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la marque : " + e.getMessage());
        }

        return false;
    }

    /**
     * Recherche une marque par son nom
     * @param nom Le nom de la marque
     * @return La marque trouvée ou null
     */
    public Marque findByNom(String nom) {
        Marque marque = null;
        String query = "SELECT * FROM marque WHERE nom = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, nom);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    marque = extractMarqueFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la marque par nom : " + e.getMessage());
        }

        return marque;
    }

    /**
     * Récupère les marques associées à un article
     * @param articleId L'ID de l'article
     * @return Liste des marques associées à l'article
     */
    public List<Marque> findByArticle(int articleId) {
        List<Marque> marques = new ArrayList<>();
        String query = "SELECT m.* FROM marque m " +
                "INNER JOIN article_marque am ON m.id = am.marque_id " +
                "WHERE am.article_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, articleId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Marque marque = extractMarqueFromResultSet(rs);
                    marques.add(marque);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des marques par article : " + e.getMessage());
        }

        return marques;
    }

    /**
     * Extrait une marque d'un ResultSet
     * @param rs ResultSet contenant les données
     * @return La marque créée
     * @throws SQLException Si une erreur SQL se produit
     */
    private Marque extractMarqueFromResultSet(ResultSet rs) throws SQLException {
        Marque marque = new Marque();
        marque.setId(rs.getInt("id"));
        marque.setNom(rs.getString("nom"));
        marque.setDescription(rs.getString("description"));
        marque.setLogoUrl(rs.getString("logo_url"));
        return marque;
    }
}