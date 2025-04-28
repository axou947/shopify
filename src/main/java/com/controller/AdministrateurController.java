package main.java.com.controller;

import main.java.com.dao.AdministrateurDAO;
import main.java.com.dao.DAOFactory;
import main.java.com.dao.MarqueDAO;
import main.java.com.dao.UtilisateurDAO;
import main.java.com.model.Administrateur;
import main.java.com.model.Marque;
import main.java.com.model.Utilisateur;
import main.java.com.util.PasswordHasher;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Contrôleur pour gérer les opérations liées aux administrateurs
 */
public class AdministrateurController {
    private AdministrateurDAO administrateurDAO;
    private UtilisateurDAO utilisateurDAO;
    private MarqueDAO marqueDAO;

    // Map des permissions par rôle
    private static final Map<String, String[]> ROLE_PERMISSIONS = new HashMap<>();

    // Initialisation des permissions
    static {
        // Super admin a accès à tout
        ROLE_PERMISSIONS.put("super_admin", new String[] {
                "gestion_admin", "gestion_articles", "gestion_clients",
                "gestion_remises", "stats", "reporting"
        });

        // Admin standard a accès à tout sauf la gestion des administrateurs
        ROLE_PERMISSIONS.put("standard", new String[] {
                "gestion_articles", "gestion_clients", "gestion_remises",
                "stats", "reporting"
        });

        // Admin commercial a accès aux ventes et clients
        ROLE_PERMISSIONS.put("commercial", new String[] {
                "gestion_clients", "gestion_remises", "stats", "reporting"
        });

        // Admin inventaire a accès aux articles
        ROLE_PERMISSIONS.put("inventaire", new String[] {
                "gestion_articles"
        });
    }

    /**
     * Constructeur du contrôleur administrateur
     */
    public AdministrateurController() {
        this.administrateurDAO = DAOFactory.getAdministrateurDAO();
        this.utilisateurDAO = DAOFactory.getUtilisateurDAO();
        this.marqueDAO = DAOFactory.getMarqueDAO();
    }

    /**
     * Vérifie si un administrateur a accès à une fonctionnalité
     * @param adminId ID de l'administrateur
     * @param permission La permission à vérifier
     * @return true si l'accès est autorisé, false sinon
     */
    public boolean hasAccess(int adminId, String permission) {
        Administrateur admin = administrateurDAO.findById(adminId);
        if (admin == null) {
            return false;
        }

        String role = admin.getRole();

        // Les permissions du rôle
        String[] permissions = ROLE_PERMISSIONS.get(role);
        if (permissions == null) {
            return false;
        }

        // Vérifie si la permission est dans la liste
        for (String perm : permissions) {
            if (perm.equals(permission)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Vérifie si un utilisateur a accès à une fonctionnalité
     * @param userId ID de l'utilisateur
     * @param permission La permission à vérifier
     * @return true si l'accès est autorisé, false sinon
     */
    public boolean userHasAccess(int userId, String permission) {
        // Vérifie que l'utilisateur est bien un admin
        Utilisateur utilisateur = utilisateurDAO.findById(userId);
        if (utilisateur == null || !"admin".equals(utilisateur.getType())) {
            return false;
        }

        // Récupère l'admin correspondant
        Administrateur admin = administrateurDAO.findByUtilisateurId(userId);
        if (admin == null) {
            return false;
        }

        // Vérifie la permission
        return hasAccess(admin.getId(), permission);
    }

    /**
     * Récupère tous les administrateurs
     *
     * @return Liste de tous les administrateurs
     */
    public List<Administrateur> getAllAdministrateurs() {
        return administrateurDAO.findAll();
    }

    /**
     * Récupère un administrateur par son ID
     *
     * @param id L'identifiant de l'administrateur
     * @return L'administrateur correspondant ou null
     */
    public Administrateur getAdministrateurById(int id) {
        return administrateurDAO.findById(id);
    }

    /**
     * Récupère un administrateur par l'ID de son utilisateur associé
     *
     * @param utilisateurId L'identifiant de l'utilisateur
     * @return L'administrateur correspondant ou null
     */
    public Administrateur getAdministrateurByUtilisateurId(int utilisateurId) {
        return administrateurDAO.findByUtilisateurId(utilisateurId);
    }

    /**
     * Récupère les informations d'un utilisateur associé à un administrateur
     *
     * @param adminId L'identifiant de l'administrateur
     * @return L'utilisateur correspondant ou null
     */
    public Utilisateur getUtilisateurByAdminId(int adminId) {
        Administrateur administrateur = administrateurDAO.findById(adminId);
        if (administrateur != null) {
            return utilisateurDAO.findById(administrateur.getUtilisateurId());
        }
        return null;
    }

    /**
     * Génère un nom de marque unique basé sur le nom et prénom
     *
     * @param nom Nom de l'admin
     * @param prenom Prénom de l'admin
     * @return Un nom de marque unique
     */
    private String generateUniqueMarqueName(String nom, String prenom) {
        // Base du nom de marque
        String baseName = nom + " " + prenom;

        // Vérifier si une marque avec ce nom existe déjà
        Marque existingMarque = marqueDAO.findByNom(baseName);
        if (existingMarque == null) {
            return baseName;
        }

        // Si le nom existe déjà, ajouter un suffixe unique
        String uniqueName = baseName + " " + UUID.randomUUID().toString().substring(0, 8);
        return uniqueName;
    }

    /**
     * Inscrit un nouvel administrateur
     *
     * @param email      Email de l'administrateur
     * @param motDePasse Mot de passe de l'administrateur
     * @param nom        Nom de l'administrateur
     * @param prenom     Prénom de l'administrateur
     * @param adresse    Adresse de l'administrateur
     * @param telephone  Téléphone de l'administrateur
     * @param role       Rôle de l'administrateur
     * @param marque     Marque associée à l'administrateur (optionnel)
     * @return ID de l'administrateur créé, -1 si échec
     */
    public int inscrireAdministrateur(String email, String motDePasse, String nom, String prenom,
                                      String adresse, String telephone, String role, String marque) {
        // Vérifie si l'email existe déjà
        if (utilisateurDAO.findByEmail(email) != null) {
            return -1;
        }

        // Crée l'utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(email);
        utilisateur.setMotDePasse(PasswordHasher.hash(motDePasse));
        utilisateur.setType("admin");
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        utilisateur.setAdresse(adresse);
        utilisateur.setTelephone(telephone);
        utilisateur.setDateInscription(new Date());

        // Si aucune marque n'est spécifiée ou si la marque est vide, générer un nom unique
        if (marque == null || marque.trim().isEmpty()) {
            marque = generateUniqueMarqueName(nom, prenom);
        } else {
            // Vérifier si la marque existe déjà et la rendre unique si nécessaire
            Marque existingMarque = marqueDAO.findByNom(marque);
            if (existingMarque != null) {
                marque = marque + " " + UUID.randomUUID().toString().substring(0, 8);
            }
        }

        // Crée l'administrateur
        Administrateur administrateur = new Administrateur();
        administrateur.setRole(role);
        administrateur.setMarque(marque);

        // Utilise la méthode createWithUtilisateur et vérifie le résultat
        if (administrateurDAO.createWithUtilisateur(utilisateur, administrateur)) {
            return administrateur.getId(); // Retourne l'ID de l'administrateur créé
        } else {
            return -1; // Échec
        }
    }

    /**
     * Inscrit un nouvel administrateur (surcharge sans spécifier la marque)
     *
     * @param email      Email de l'administrateur
     * @param motDePasse Mot de passe de l'administrateur
     * @param nom        Nom de l'administrateur
     * @param prenom     Prénom de l'administrateur
     * @param adresse    Adresse de l'administrateur
     * @param telephone  Téléphone de l'administrateur
     * @param role       Rôle de l'administrateur
     * @return ID de l'administrateur créé, -1 si échec
     */
    public int inscrireAdministrateur(String email, String motDePasse, String nom, String prenom,
                                      String adresse, String telephone, String role) {
        return inscrireAdministrateur(email, motDePasse, nom, prenom, adresse, telephone, role, null);
    }

    /**
     * Met à jour le rôle d'un administrateur
     *
     * @param adminId L'identifiant de l'administrateur
     * @param role    Le nouveau rôle
     * @return true si l'opération a réussi, false sinon
     */
    public boolean updateAdministrateurRole(int adminId, String role) {
        Administrateur administrateur = administrateurDAO.findById(adminId);
        if (administrateur != null) {
            administrateur.setRole(role);
            return administrateurDAO.update(administrateur);
        }
        return false;
    }

    /**
     * Met à jour la marque d'un administrateur
     *
     * @param adminId L'identifiant de l'administrateur
     * @param marque  La nouvelle marque
     * @return true si l'opération a réussi, false sinon
     */
    public boolean updateAdministrateurMarque(int adminId, String marque) {
        Administrateur administrateur = administrateurDAO.findById(adminId);
        if (administrateur != null) {
            // Vérifier si la marque existe déjà et la rendre unique si nécessaire
            Marque existingMarque = marqueDAO.findByNom(marque);
            if (existingMarque != null) {
                marque = marque + " " + UUID.randomUUID().toString().substring(0, 8);
            }

            administrateur.setMarque(marque);
            return administrateurDAO.update(administrateur);
        }
        return false;
    }

    /**
     * Met à jour les informations d'un administrateur
     *
     * @param adminId   L'identifiant de l'administrateur
     * @param adresse   La nouvelle adresse
     * @param telephone Le nouveau téléphone
     * @return true si l'opération a réussi, false sinon
     */
    public boolean updateAdministrateurInfo(int adminId, String adresse, String telephone) {
        Administrateur administrateur = administrateurDAO.findById(adminId);
        if (administrateur != null) {
            Utilisateur utilisateur = utilisateurDAO.findById(administrateur.getUtilisateurId());
            if (utilisateur != null) {
                utilisateur.setAdresse(adresse);
                utilisateur.setTelephone(telephone);
                return utilisateurDAO.update(utilisateur);
            }
        }
        return false;
    }
}
