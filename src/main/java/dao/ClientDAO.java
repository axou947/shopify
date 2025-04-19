package dao;

import model.Client;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    private Connection connection;

    public ClientDAO() {
        this.connection = DBConnection.getConnection();
    }

    public boolean ajouterClient(Client client) {
        String query = "INSERT INTO Client (nom, prenom, email, mot_de_passe, type_client) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getEmail());
            ps.setString(4, client.getMotDePasse());
            ps.setString(5, client.getTypeClient());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du client: " + e.getMessage());
            return false;
        }
    }

    public Client authentifierClient(String email, String motDePasse) {
        String query = "SELECT * FROM Client WHERE email = ? AND mot_de_passe = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, motDePasse);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Client client = new Client();
                client.setIdClient(rs.getInt("id_client"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                client.setTypeClient(rs.getString("type_client"));
                client.setDateInscription(rs.getString("date_inscription"));
                return client;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification: " + e.getMessage());
        }
        return null;
    }

    public List<Client> listerClients() {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM Client";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Client client = new Client();
                client.setIdClient(rs.getInt("id_client"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                client.setTypeClient(rs.getString("type_client"));
                client.setDateInscription(rs.getString("date_inscription"));
                clients.add(client);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des clients: " + e.getMessage());
        }
        return clients;
    }
}