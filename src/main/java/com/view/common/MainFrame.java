package main.java.com.view.common;

import main.java.com.view.admin.GestionArticlesPanel;
import main.java.com.view.admin.GestionClientsPanel;
import main.java.com.view.admin.GestionRemisesPanel;
import main.java.com.view.admin.StatistiquesPanel;
import main.java.com.view.client.CataloguePanel;
import main.java.com.view.client.HistoriqueCommandesPanel;
import main.java.com.view.client.InscriptionPanel;
import main.java.com.view.client.PanierPanel;
import main.java.com.view.reporting.ReportingPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre principale de l'application
 */
public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private int currentUserId;
    private String currentUserType;

    // Panels de l'application
    private LoginPanel loginPanel;
    private InscriptionPanel inscriptionPanel;
    private CataloguePanel cataloguePanel;
    private PanierPanel panierPanel;
    private HistoriqueCommandesPanel historiquePanel;
    private GestionArticlesPanel gestionArticlesPanel;
    private GestionClientsPanel gestionClientsPanel;
    private GestionRemisesPanel gestionRemisesPanel;
    private StatistiquesPanel statistiquesPanel;
    private ReportingPanel reportingPanel;

    // Menus
    private JMenuBar menuBar;
    private JMenu menuFichier;
    private JMenu menuClient;
    private JMenu menuAdmin;

    /**
     * Constructeur de la fenêtre principale
     */
    public MainFrame() {
        setTitle("Shopping Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);

        initComponents();
        setupLayout();
        setupMenus();

        // Par défaut, affiche l'écran de connexion
        cardLayout.show(contentPanel, "login");
    }

    /**
     * Initialise les composants
     */
    private void initComponents() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Création des différents panels
        loginPanel = new LoginPanel(this);
        inscriptionPanel = new InscriptionPanel(this);

        // Les autres panels seront créés à la demande pour optimiser l'utilisation de la mémoire
    }

    /**
     * Configure la disposition des composants
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        // Ajout des panels au conteneur avec CardLayout
        contentPanel.add(loginPanel, "login");
        contentPanel.add(inscriptionPanel, "inscription");

        // Ajout du conteneur principal à la fenêtre
        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Configure les menus
     */
    private void setupMenus() {
        menuBar = new JMenuBar();

        // Menu Fichier
        menuFichier = new JMenu("Fichier");
        JMenuItem itemQuitter = new JMenuItem("Quitter");
        itemQuitter.addActionListener(e -> System.exit(0));
        JMenuItem itemDeconnexion = new JMenuItem("Déconnexion");
        itemDeconnexion.addActionListener(e -> logout());
        menuFichier.add(itemDeconnexion);
        menuFichier.addSeparator();
        menuFichier.add(itemQuitter);

        // Menu Client
        menuClient = new JMenu("Client");
        JMenuItem itemCatalogue = new JMenuItem("Catalogue");
        itemCatalogue.addActionListener(e -> showCataloguePanel());
        JMenuItem itemPanier = new JMenuItem("Panier");
        itemPanier.addActionListener(e -> showPanierPanel());
        JMenuItem itemHistorique = new JMenuItem("Historique des commandes");
        itemHistorique.addActionListener(e -> showHistoriquePanel());
        menuClient.add(itemCatalogue);
        menuClient.add(itemPanier);
        menuClient.add(itemHistorique);

        // Menu Admin
        menuAdmin = new JMenu("Administration");
        JMenuItem itemGestionArticles = new JMenuItem("Gestion des articles");
        itemGestionArticles.addActionListener(e -> showGestionArticlesPanel());
        JMenuItem itemGestionClients = new JMenuItem("Gestion des clients");
        itemGestionClients.addActionListener(e -> showGestionClientsPanel());
        JMenuItem itemGestionRemises = new JMenuItem("Gestion des remises");
        itemGestionRemises.addActionListener(e -> showGestionRemisesPanel());
        JMenuItem itemStatistiques = new JMenuItem("Statistiques");
        itemStatistiques.addActionListener(e -> showStatistiquesPanel());
        JMenuItem itemReporting = new JMenuItem("Reporting");
        itemReporting.addActionListener(e -> showReportingPanel());
        menuAdmin.add(itemGestionArticles);
        menuAdmin.add(itemGestionClients);
        menuAdmin.add(itemGestionRemises);
        menuAdmin.add(itemStatistiques);
        menuAdmin.add(itemReporting);

        // Ajout des menus à la barre de menu
        menuBar.add(menuFichier);
        menuBar.add(menuClient);
        menuBar.add(menuAdmin);

        // Désactive les menus client et admin par défaut
        menuClient.setEnabled(false);
        menuAdmin.setEnabled(false);

        // Ajout de la barre de menu à la fenêtre
        setJMenuBar(menuBar);
    }

    /**
     * Affiche le panel d'inscription
     */
    public void showRegistrationPanel() {
        cardLayout.show(contentPanel, "inscription");
    }

    /**
     * Affiche le panel de catalogue
     */
    public void showCataloguePanel() {
        if (cataloguePanel == null) {
            cataloguePanel = new CataloguePanel(this);
            contentPanel.add(cataloguePanel, "catalogue");
        }
        cardLayout.show(contentPanel, "catalogue");
    }

    /**
     * Affiche le panel de panier
     */
    public void showPanierPanel() {
        if (panierPanel == null) {
            panierPanel = new PanierPanel(this);
            contentPanel.add(panierPanel, "panier");
        } else {
            // Rafraîchit le panier pour s'assurer que les données sont à jour
            panierPanel.refreshPanier();
        }
        cardLayout.show(contentPanel, "panier");
    }

    /**
     * Affiche le panel d'historique des commandes
     */
    public void showHistoriquePanel() {
        if (historiquePanel == null) {
            historiquePanel = new HistoriqueCommandesPanel(this, currentUserId);
            contentPanel.add(historiquePanel, "historique");
        }
        cardLayout.show(contentPanel, "historique");
    }

    /**
     * Affiche le panel de gestion des articles
     */
    public void showGestionArticlesPanel() {
        if (gestionArticlesPanel == null) {
            gestionArticlesPanel = new GestionArticlesPanel(this);
            contentPanel.add(gestionArticlesPanel, "gestionArticles");
        }
        cardLayout.show(contentPanel, "gestionArticles");
    }

    /**
     * Affiche le panel de gestion des clients
     */
    public void showGestionClientsPanel() {
        if (gestionClientsPanel == null) {
            gestionClientsPanel = new GestionClientsPanel(this);
            contentPanel.add(gestionClientsPanel, "gestionClients");
        }
        cardLayout.show(contentPanel, "gestionClients");
    }

    /**
     * Affiche le panel de gestion des remises
     */
    public void showGestionRemisesPanel() {
        if (gestionRemisesPanel == null) {
            gestionRemisesPanel = new GestionRemisesPanel(this);
            contentPanel.add(gestionRemisesPanel, "gestionRemises");
        }
        cardLayout.show(contentPanel, "gestionRemises");
    }

    /**
     * Affiche le panel de statistiques
     */
    public void showStatistiquesPanel() {
        if (statistiquesPanel == null) {
            statistiquesPanel = new StatistiquesPanel();
            contentPanel.add(statistiquesPanel, "statistiques");
        }
        cardLayout.show(contentPanel, "statistiques");
    }

    /**
     * Affiche le panel de reporting
     */
    public void showReportingPanel() {
        if (reportingPanel == null) {
            reportingPanel = new ReportingPanel(this);
            contentPanel.add(reportingPanel, "reporting");
        }
        cardLayout.show(contentPanel, "reporting");
    }

    /**
     * Méthode appelée après une connexion réussie
     * @param userId ID de l'utilisateur connecté
     * @param userType Type d'utilisateur (client ou admin)
     */
    public void onLoginSuccess(int userId, String userType) {
        this.currentUserId = userId;
        this.currentUserType = userType;

        // Active les menus selon le type d'utilisateur
        if ("client".equals(userType)) {
            menuClient.setEnabled(true);
            menuAdmin.setEnabled(false);
            showCataloguePanel();
        } else if ("admin".equals(userType)) {
            menuClient.setEnabled(false);
            menuAdmin.setEnabled(true);
            showGestionArticlesPanel();
        }
    }

    /**
     * Déconnecte l'utilisateur
     */
    public void logout() {
        currentUserId = 0;
        currentUserType = null;

        // Désactive les menus
        menuClient.setEnabled(false);
        menuAdmin.setEnabled(false);

        // Retour à l'écran de connexion
        cardLayout.show(contentPanel, "login");
    }

    /**
     * Retourne l'ID de l'utilisateur courant
     * @return ID de l'utilisateur
     */
    public int getCurrentUserId() {
        return currentUserId;
    }

    /**
     * Retourne le type de l'utilisateur courant
     * @return Type d'utilisateur
     */
    public String getCurrentUserType() {
        return currentUserType;
    }

    /**
     * Retourne le panel de catalogue
     * @return Le panel de catalogue
     */
    public JPanel getCataloguePanel() {
        return cataloguePanel;
    }
}