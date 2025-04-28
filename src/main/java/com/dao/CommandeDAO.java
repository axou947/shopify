package main.java.com.dao;

import main.java.com.config.DatabaseConnection;
import main.java.com.model.ArticleMarque;
import main.java.com.model.Commande;
import main.java.com.model.LigneCommande;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO pour l'entité Commande
 */
public class CommandeDAO implements GenericDAO<Commande> {
    private Connection connection;

    public CommandeDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Commande findById(int id) {
        Commande commande = null;
        String query = "SELECT * FROM commande WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    commande = extractCommandeFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la commande par ID : " + e.getMessage());
        }

        return commande;
    }

    @Override
    public List<Commande> findAll() {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT * FROM commande ORDER BY date_commande DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Commande commande = extractCommandeFromResultSet(rs);
                commandes.add(commande);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de toutes les commandes : " + e.getMessage());
        }

        return commandes;
    }

    @Override
    public boolean create(Commande commande) {
        String query = "INSERT INTO commande (client_id, date_commande, statut, montant_total, montant_remise, note) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, commande.getClientId());
            pstmt.setTimestamp(2, new Timestamp(commande.getDateCommande().getTime()));
            pstmt.setString(3, commande.getStatut());
            pstmt.setDouble(4, commande.getMontantTotal());
            pstmt.setDouble(5, commande.getMontantRemise());
            pstmt.setString(6, commande.getNote());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        commande.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la commande : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean update(Commande commande) {
        String query = "UPDATE commande SET client_id = ?, date_commande = ?, statut = ?, " +
                "montant_total = ?, montant_remise = ?, note = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, commande.getClientId());
            pstmt.setTimestamp(2, new Timestamp(commande.getDateCommande().getTime()));
            pstmt.setString(3, commande.getStatut());
            pstmt.setDouble(4, commande.getMontantTotal());
            pstmt.setDouble(5, commande.getMontantRemise());
            pstmt.setString(6, commande.getNote());
            pstmt.setInt(7, commande.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la commande : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM commande WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la commande : " + e.getMessage());
        }

        return false;
    }

    /**
     * Recherche des commandes par client
     * @param clientId L'identifiant du client
     * @return Liste des commandes du client
     */
    public List<Commande> findByClient(int clientId) {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT * FROM commande WHERE client_id = ? ORDER BY date_commande DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, clientId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Commande commande = extractCommandeFromResultSet(rs);
                    commandes.add(commande);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des commandes par client : " + e.getMessage());
        }

        return commandes;
    }

    /**
     * Recherche les lignes de commande d'une commande
     * @param commandeId L'identifiant de la commande
     * @return Liste des lignes de commande
     */
    public List<LigneCommande> findLignesCommande(int commandeId) {
        List<LigneCommande> lignes = new ArrayList<>();
        String query = "SELECT lc.*, am.article_id, am.marque_id FROM ligne_commande lc " +
                "JOIN article_marque am ON lc.article_marque_id = am.id " +
                "WHERE lc.commande_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, commandeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LigneCommande ligne = extractLigneCommandeFromResultSet(rs);
                    lignes.add(ligne);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des lignes de commande : " + e.getMessage());
        }

        return lignes;
    }

    /**
     * Crée une commande et ses lignes dans une transaction
     * @param commande La commande à créer
     * @param lignes Les lignes de commande à créer
     * @return ID de la commande créée, -1 si échec
     */
    public int createWithLignes(Commande commande, List<LigneCommande> lignes) {
        // Début de la transaction
        try {
            connection.setAutoCommit(false);

            // Création de la commande
            if (create(commande)) {
                // Création des lignes de commande
                for (LigneCommande ligne : lignes) {
                    ligne.setCommandeId(commande.getId());
                    if (!createLigneCommande(ligne)) {
                        connection.rollback();
                        return -1;
                    }
                }

                connection.commit();
                return commande.getId();
            }

            connection.rollback();
            return -1;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la commande avec lignes : " + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Erreur lors du rollback : " + ex.getMessage());
            }
            return -1;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Erreur lors du rétablissement de l'autocommit : " + e.getMessage());
            }
        }
    }

    /**
     * Crée une ligne de commande
     * @param ligne La ligne de commande à créer
     * @return true si l'opération a réussi, false sinon
     */
    private boolean createLigneCommande(LigneCommande ligne) {
        String query = "INSERT INTO ligne_commande (commande_id, article_marque_id, quantite, prix_unitaire, prix_total) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, ligne.getCommandeId());
            pstmt.setInt(2, ligne.getArticleMarqueId());
            pstmt.setInt(3, ligne.getQuantite());
            pstmt.setDouble(4, ligne.getPrixUnitaire());
            pstmt.setDouble(5, ligne.getPrixTotal());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ligne.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la ligne de commande : " + e.getMessage());
        }

        return false;
    }

    /**
     * Extrait une commande d'un ResultSet
     * @param rs ResultSet contenant les données
     * @return La commande créée
     * @throws SQLException Si une erreur SQL se produit
     */
    private Commande extractCommandeFromResultSet(ResultSet rs) throws SQLException {
        Commande commande = new Commande();
        commande.setId(rs.getInt("id"));
        commande.setClientId(rs.getInt("client_id"));
        commande.setDateCommande(rs.getTimestamp("date_commande"));
        commande.setStatut(rs.getString("statut"));
        commande.setMontantTotal(rs.getDouble("montant_total"));
        commande.setMontantRemise(rs.getDouble("montant_remise"));
        commande.setNote(rs.getString("note"));
        return commande;
    }

    /**
     * Extrait une ligne de commande d'un ResultSet
     * @param rs ResultSet contenant les données
     * @return La ligne de commande créée
     * @throws SQLException Si une erreur SQL se produit
     */
    private LigneCommande extractLigneCommandeFromResultSet(ResultSet rs) throws SQLException {
        LigneCommande ligne = new LigneCommande();
        ligne.setId(rs.getInt("id"));
        ligne.setCommandeId(rs.getInt("commande_id"));
        ligne.setArticleMarqueId(rs.getInt("article_marque_id"));
        ligne.setQuantite(rs.getInt("quantite"));
        ligne.setPrixUnitaire(rs.getDouble("prix_unitaire"));
        ligne.setPrixTotal(rs.getDouble("prix_total"));

        // Création de l'ArticleMarque associé
        ArticleMarque articleMarque = new ArticleMarque();
        articleMarque.setId(rs.getInt("article_marque_id"));
        articleMarque.setArticleId(rs.getInt("article_id"));
        articleMarque.setMarqueId(rs.getInt("marque_id"));
        ligne.setArticleMarque(articleMarque);

        return ligne;
    }
}
