package main.java.com.controller;

import main.java.com.dao.ClientDAO;
import main.java.com.dao.DAOFactory;
import main.java.com.dao.UtilisateurDAO;
import main.java.com.model.Administrateur;
import main.java.com.model.Client;
import main.java.com.model.Utilisateur;
import main.java.com.util.PasswordHasher;

import java.util.Date;
import java.util.List;

/**
 * Contrôleur pour gérer les opérations liées aux clients
 */
public class ClientController {
    private ClientDAO clientDAO;
    private UtilisateurDAO utilisateurDAO;

    /**
     * Constructeur du contrôleur client
     */
    public ClientController() {
        this.clientDAO = DAOFactory.getClientDAO();
        this.utilisateurDAO = DAOFactory.getUtilisateurDAO();
    }

    /**
     * Récupère tous les clients
     *
     * @return Liste de tous les clients
     */
    public List<Client> getAllClients() {
        return clientDAO.findAll();
    }

    /**
     * Récupère un client par son ID
     *
     * @param id L'identifiant du client
     * @return Le client correspondant ou null
     */
    public Client getClientById(int id) {
        return clientDAO.findById(id);
    }

    /**
     * Récupère un client par l'ID de son utilisateur associé
     *
     * @param utilisateurId L'identifiant de l'utilisateur
     * @return Le client correspondant ou null
     */
    public Client getClientByUtilisateurId(int utilisateurId) {
        return clientDAO.findByUtilisateurId(utilisateurId);
    }

    /**
     * Récupère les informations d'un utilisateur associé à un client
     *
     * @param clientId L'identifiant du client
     * @return L'utilisateur correspondant ou null
     */
    public Utilisateur getUtilisateurByClientId(int clientId) {
        Client client = clientDAO.findById(clientId);
        if (client != null) {
            return utilisateurDAO.findById(client.getUtilisateurId());
        }
        return null;
    }

    /**
     * Inscrit un nouvel utilisateur (client ou administrateur)
     *
     * @param email      Email de l'utilisateur
     * @param motDePasse Mot de passe de l'utilisateur
     * @param nom        Nom de l'utilisateur
     * @param prenom     Prénom de l'utilisateur
     * @param adresse    Adresse de l'utilisateur
     * @param telephone  Téléphone de l'utilisateur
     * @param type       Type de l'utilisateur ("client" ou "admin")
     * @return ID de l'utilisateur créé, -1 si échec
     */
    public int inscrireUtilisateur(String email, String motDePasse, String nom, String prenom,
                                   String adresse, String telephone, String type) {
        // Vérifie si l'email existe déjà
        if (utilisateurDAO.findByEmail(email) != null) {
            return -1;
        }

        // Crée l'utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(email);
        utilisateur.setMotDePasse(PasswordHasher.hash(motDePasse));
        utilisateur.setType(type);
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        utilisateur.setAdresse(adresse);
        utilisateur.setTelephone(telephone);
        utilisateur.setDateInscription(new Date());

        if (utilisateurDAO.create(utilisateur)) {
            int userId = utilisateur.getId();

            // Selon le type, créer un client ou un administrateur
            if ("client".equals(type)) {
                Client client = new Client();
                client.setUtilisateurId(userId);
                client.setStatut("nouveau");

                if (clientDAO.create(client)) {
                    return client.getId();
                }
            } else if ("admin".equals(type)) {
                // Crée un administrateur lié à cet utilisateur
                Administrateur admin = new Administrateur();
                admin.setUtilisateurId(userId);
                admin.setRole("standard"); // Rôle par défaut

                // Dans une implémentation réelle, il faudrait avoir un DAOFactory.getAdministrateurDAO()
                // Pour simplifier, on retourne directement l'ID de l'utilisateur
                return userId;
            }
        }

        return -1;
    }
//
    /**
     * Inscrit un nouveau client
     *
     * @param email      Email du client
     * @param motDePasse Mot de passe du client
     * @param nom        Nom du client
     * @param prenom     Prénom du client
     * @param adresse    Adresse du client
     * @param telephone  Téléphone du client
     * @return ID du client créé, -1 si échec
     */
    public int inscrireClient(String email, String motDePasse, String nom, String prenom, String adresse, String telephone) {
        return inscrireUtilisateur(email, motDePasse, nom, prenom, adresse, telephone, "client");
    }

    /**
     * Met à jour le statut d'un client
     *
     * @param clientId L'identifiant du client
     * @param statut   Le nouveau statut
     * @return true si l'opération a réussi, false sinon
     */
    public boolean updateClientStatus(int clientId, String statut) {
        Client client = clientDAO.findById(clientId);
        if (client != null) {
            client.setStatut(statut);
            return clientDAO.update(client);
        }
        return false;
    }

    /**
     * Met à jour les informations d'un client
     *
     * @param clientId  L'identifiant du client
     * @param adresse   La nouvelle adresse
     * @param telephone Le nouveau téléphone
     * @return true si l'opération a réussi, false sinon
     */
    public boolean updateClientInfo(int clientId, String adresse, String telephone) {
        Client client = clientDAO.findById(clientId);
        if (client != null) {
            Utilisateur utilisateur = utilisateurDAO.findById(client.getUtilisateurId());
            if (utilisateur != null) {
                utilisateur.setAdresse(adresse);
                utilisateur.setTelephone(telephone);
                return utilisateurDAO.update(utilisateur);
            }
        }
        return false;
    }
}
