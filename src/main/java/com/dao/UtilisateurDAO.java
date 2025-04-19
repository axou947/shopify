package main.java.com.dao;

import main.java.com.config.DatabaseConnection;
import main.java.com.model.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO pour l'entité Utilisateur
 */
public class UtilisateurDAO implements GenericDAO<Utilisateur> {
    private Connection connection;

    public UtilisateurDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Utilisateur findById(int id) {
        Utilisateur utilisateur = null;
        String query = "SELECT * FROM utilisateur WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    utilisateur = extractUtilisateurFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'utilisateur par ID : " + e.getMessage());
        }

        return utilisateur;
    }

    @Override
    public List<Utilisateur> findAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT * FROM utilisateur";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Utilisateur utilisateur = extractUtilisateurFromResultSet(rs);
                utilisateurs.add(utilisateur);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les utilisateurs : " + e.getMessage());
        }

        return utilisateurs;
    }

    @Override
    public boolean create(Utilisateur utilisateur) {
        String query = "INSERT INTO utilisateur (email, mot_de_passe, type, nom, prenom, adresse, telephone, date_inscription) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, utilisateur.getEmail());
            pstmt.setString(2, utilisateur.getMotDePasse());
            pstmt.setString(3, utilisateur.getType());
            pstmt.setString(4, utilisateur.getNom());
            pstmt.setString(5, utilisateur.getPrenom());
            pstmt.setString(6, utilisateur.getAdresse());
            pstmt.setString(7, utilisateur.getTelephone());
            pstmt.setDate(8, new java.sql.Date(utilisateur.getDateInscription().getTime()));

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        utilisateur.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'utilisateur : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean update(Utilisateur utilisateur) {
        String query = "UPDATE utilisateur SET email = ?, mot_de_passe = ?, type = ?, nom = ?, " +
                "prenom = ?, adresse = ?, telephone = ?, date_inscription = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, utilisateur.getEmail());
            pstmt.setString(2, utilisateur.getMotDePasse());
            pstmt.setString(3, utilisateur.getType());
            pstmt.setString(4, utilisateur.getNom());
            pstmt.setString(5, utilisateur.getPrenom());
            pstmt.setString(6, utilisateur.getAdresse());
            pstmt.setString(7, utilisateur.getTelephone());
            pstmt.setDate(8, new java.sql.Date(utilisateur.getDateInscription().getTime()));
            pstmt.setInt(9, utilisateur.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM utilisateur WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }

        return false;
    }

    /**
     * Recherche un utilisateur par son email
     * @param email Email de l'utilisateur
     * @return L'utilisateur trouvé ou null
     */
    public Utilisateur findByEmail(String email) {
        Utilisateur utilisateur = null;
        String query = "SELECT * FROM utilisateur WHERE email = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    utilisateur = extractUtilisateurFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'utilisateur par email : " + e.getMessage());
        }

        return utilisateur;
    }

    /**
     * Extrait un utilisateur d'un ResultSet
     * @param rs ResultSet contenant les données
     * @return L'utilisateur créé
     * @throws SQLException Si une erreur SQL se produit
     */
    private Utilisateur extractUtilisateurFromResultSet(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(rs.getInt("id"));
        utilisateur.setEmail(rs.getString("email"));
        utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
        utilisateur.setType(rs.getString("type"));
        utilisateur.setNom(rs.getString("nom"));
        utilisateur.setPrenom(rs.getString("prenom"));
        utilisateur.setAdresse(rs.getString("adresse"));
        utilisateur.setTelephone(rs.getString("telephone"));
        utilisateur.setDateInscription(rs.getDate("date_inscription"));
        return utilisateur;
    }
}