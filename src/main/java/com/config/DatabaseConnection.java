package main.java.com.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;






/**
 * Classe de connexion à la base de données
 * Utilise le pattern Singleton pour garantir une seule instance de connexion
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    // Configuration de la base de données
    private static final String URL = "jdbc:mysql://localhost:3306/shopping_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // WAMP par défaut n'a pas de mot de passe

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion à la base de données établie avec succès");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la connexion : " + e.getMessage());
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion à la base de données fermée");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }
}
