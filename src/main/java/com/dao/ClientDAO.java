package main.java.com.dao;

import main.java.com.config.DatabaseConnection;
import main.java.com.model.Client;
import main.java.com.model.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO pour l'entité Client
 */
public class ClientDAO implements GenericDAO<Client> {
    private Connection connection;

    public ClientDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Client findById(int id) {
        Client client = null;
        String query = "SELECT * FROM client WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    client = extractClientFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du client par ID : " + e.getMessage());
        }

        return client;
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM client";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Client client = extractClientFromResultSet(rs);
                clients.add(client);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les clients : " + e.getMessage());
        }

        return clients;
    }

    @Override
    public boolean create(Client client) {
        String query = "INSERT INTO client (utilisateur_id, statut) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, client.getUtilisateurId());
            pstmt.setString(2, client.getStatut());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        client.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du client : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean update(Client client) {
        String query = "UPDATE client SET utilisateur_id = ?, statut = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, client.getUtilisateurId());
            pstmt.setString(2, client.getStatut());
            pstmt.setInt(3, client.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du client : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM client WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du client : " + e.getMessage());
        }

        return false;
    }

    /**
     * Recherche un client par l'ID de son utilisateur associé
     * @param utilisateurId L'ID de l'utilisateur
     * @return Le client trouvé ou null
     */
    public Client findByUtilisateurId(int utilisateurId) {
        Client client = null;
        String query = "SELECT * FROM client WHERE utilisateur_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, utilisateurId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    client = extractClientFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du client par utilisateur_id : " + e.getMessage());
        }

        return client;
    }

    /**
     * Crée un nouvel utilisateur et un client associé
     * @param utilisateur L'utilisateur à créer
     * @param client Le client à créer
     * @return true si l'opération a réussi, false sinon
     */
    public boolean createWithUtilisateur(Utilisateur utilisateur, Client client) {
        UtilisateurDAO utilisateurDAO = DAOFactory.getUtilisateurDAO();

        // Début de la transaction
        try {
            connection.setAutoCommit(false);

            // Création de l'utilisateur
            if (utilisateurDAO.create(utilisateur)) {
                // Associe l'ID de l'utilisateur créé au client
                client.setUtilisateurId(utilisateur.getId());

                // Création du client
                if (create(client)) {
                    connection.commit();
                    return true;
                }
            }

            // Si on arrive ici, c'est qu'il y a eu un problème
            connection.rollback();
            return false;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du client avec utilisateur : " + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Erreur lors du rollback : " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Erreur lors du rétablissement de l'autocommit : " + e.getMessage());
            }
        }
    }

    /**
     * Extrait un client d'un ResultSet
     * @param rs ResultSet contenant les données
     * @return Le client créé
     * @throws SQLException Si une erreur SQL se produit
     */
    private Client extractClientFromResultSet(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id"));
        client.setUtilisateurId(rs.getInt("utilisateur_id"));
        client.setStatut(rs.getString("statut"));
        return client;
    }
}