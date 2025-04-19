package main.java.com.controller;

import main.java.com.dao.DAOFactory;
import main.java.com.dao.UtilisateurDAO;
import main.java.com.model.Utilisateur;
import main.java.com.util.PasswordHasher;

/**
 * Contrôleur pour gérer les opérations de connexion
 */
public class LoginController {
    private UtilisateurDAO utilisateurDAO;

    /**
     * Constructeur du contrôleur de login
     */
    public LoginController() {
        this.utilisateurDAO = DAOFactory.getUtilisateurDAO();
    }

    /**
     * Authentifie un utilisateur
     * @param email Email de l'utilisateur
     * @param password Mot de passe de l'utilisateur
     * @return ID de l'utilisateur si authentification réussie, -1 sinon
     */
    public int authenticate(String email, String password) {
        Utilisateur utilisateur = utilisateurDAO.findByEmail(email);
        if (utilisateur != null) {
            // Vérification du mot de passe
            if (PasswordHasher.verify(password, utilisateur.getMotDePasse())) {
                return utilisateur.getId();
            }
        }
        return -1;
    }

    /**
     * Récupère le type d'un utilisateur
     * @param userId ID de l'utilisateur
     * @return Type de l'utilisateur (client ou admin)
     */
    public String getUserType(int userId) {
        Utilisateur utilisateur = utilisateurDAO.findById(userId);
        if (utilisateur != null) {
            return utilisateur.getType();
        }
        return null;
    }

    /**
     * Crée un nouvel utilisateur
     * @param utilisateur L'utilisateur à créer
     * @return ID de l'utilisateur créé, -1 si échec
     */
    public int registerUser(Utilisateur utilisateur) {
        // Vérifie si l'email existe déjà
        if (utilisateurDAO.findByEmail(utilisateur.getEmail()) != null) {
            return -1;
        }

        // Hash du mot de passe avant stockage
        String hashedPassword = PasswordHasher.hash(utilisateur.getMotDePasse());
        utilisateur.setMotDePasse(hashedPassword);

        // Création de l'utilisateur
        if (utilisateurDAO.create(utilisateur)) {
            return utilisateur.getId();
        }

        return -1;
    }
}
