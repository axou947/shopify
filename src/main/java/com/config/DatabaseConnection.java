package main.java.com.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe de connexion à la base de données
 * Utilise le pattern Singleton pour garantir une seule instance de connexion
 */
public class DatabaseConnection  {
    private static DatabaseConnection instance ;
    private Connection connection;

    // Configuration de la base de données avec paramètres supplémentaires pour résoudre les problèmes courants
    private static final String URL = "jdbc:mysql://localhost:3306/shopping_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "root"; // Adaptez selon votre configuration
    private static final String PASSWORD = ""; // Adaptez selon votre configuration

    private DatabaseConnection() {
        try {
            // Pour MySQL 8.0+, utilisez com.mysql.cj.jdbc.Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver MySQL chargé avec succès");

            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion à la base de données établie avec succès");
        } catch (ClassNotFoundException e) {
            System.err.println("ERREUR: Pilote MySQL non trouvé!");
            System.err.println("Assurez-vous d'avoir ajouté le connecteur MySQL à votre projet");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("ERREUR de connexion à la base de données MySQL:");
            System.err.println("URL: " + URL);
            System.err.println("User: " + USER);
            System.err.println("Message d'erreur: " + e.getMessage());
            System.err.println("Code SQL: " + e.getErrorCode());
            System.err.println("État SQL: " + e.getSQLState());
            e.printStackTrace();
        }
    }

    /**
     * Retourne l'instance unique de la connexion (pattern Singleton)
     * @return L'instance de DatabaseConnection
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Obtient une connexion à la base de données
     * Si la connexion est fermée ou null, tente de la rétablir
     * @return La connexion à la base de données
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Reconnexion à la base de données...");
                this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Reconnexion réussie");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la connexion : " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Vérifie si la connexion est valide
     * @return true si la connexion est valide, false sinon
     */
    public boolean isConnectionValid() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(5);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de la connexion : " + e.getMessage());
            return false;
        }
    }

    /**
     * Ferme la connexion à la base de données
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion à la base de données fermée");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Réinitialise l'instance pour forcer une nouvelle connexion
     */
    public static void resetInstance() {
        if (instance != null) {
            instance.closeConnection();
            instance = null;
        }
    }
}
