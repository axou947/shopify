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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Fenêtre principale de l'application
 * Version améliorée suivant les principes du cours 7
 */
public class MainFrame extends JFrame {
    // Constants for the frame
    private static final String TITLE = "Shopping Application";
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;

    // Layout manager
    private CardLayout cardLayout;
    private JPanel contentPanel;

    // User information
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

    // Menu items
    private JMenuItem itemDeconnexion;
    private JMenuItem itemQuitter;
    private JMenuItem itemCatalogue;
    private JMenuItem itemPanier;
    private JMenuItem itemHistorique;
    private JMenuItem itemGestionArticles;
    private JMenuItem itemGestionClients;
    private JMenuItem itemGestionRemises;
    private JMenuItem itemStatistiques;
    private JMenuItem itemReporting;

    /**
     * Constructeur de la fenêtre principale
     */
    public MainFrame() {
        // Configuration de base de la fenêtre
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);

        // Gestionnaire de fermeture personnalisé
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmAndExit();
            }
        });

        initComponents();
        setupLayout();
        setupMenus();
        setupListeners();

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

        // Initialisation des menus
        menuBar = new JMenuBar();
        menuFichier = new JMenu("Fichier");
        menuClient = new JMenu("Client");
        menuAdmin = new JMenu("Administration");

        // Initialisation des items de menu
        itemDeconnexion = new JMenuItem("Déconnexion");
        itemQuitter = new JMenuItem("Quitter");
        itemCatalogue = new JMenuItem("Catalogue");
        itemPanier = new JMenuItem("Panier");
        itemHistorique = new JMenuItem("Historique des commandes");
        itemGestionArticles = new JMenuItem("Gestion des articles");
        itemGestionClients = new JMenuItem("Gestion des clients");
        itemGestionRemises = new JMenuItem("Gestion des remises");
        itemStatistiques = new JMenuItem("Statistiques");
        itemReporting = new JMenuItem("Reporting");
    }

    /**
     * Configure la disposition des composants
     */
    private void setupLayout() {
        // Utilisation d'un BorderLayout pour la fenêtre principale
        setLayout(new BorderLayout());

        // Ajout des panels au conteneur avec CardLayout
        contentPanel.add(loginPanel, "login");
        contentPanel.add(inscriptionPanel, "inscription");

        // Ajout du conteneur principal à la fenêtre
        add(contentPanel, BorderLayout.CENTER);

        // Configuration du statut à afficher en bas de la fenêtre
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel statusLabel = new JLabel("Bienvenue dans l'application Shopping");
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);
    }

    /**
     * Configure les menus
     */
    private void setupMenus() {
        // Menu Fichier
        menuFichier.add(itemDeconnexion);
        menuFichier.addSeparator();
        menuFichier.add(itemQuitter);

        // Menu Client
        menuClient.add(itemCatalogue);
        menuClient.add(itemPanier);
        menuClient.add(itemHistorique);

        // Menu Admin
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
     * Configure les écouteurs d'événements
     */
    private void setupListeners() {
        // Écouteur pour la déconnexion
        itemDeconnexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        // Écouteur pour quitter
        itemQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmAndExit();
            }
        });

        // Écouteurs pour les menus client
        itemCatalogue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCataloguePanel();
            }
        });

        itemPanier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanierPanel();
            }
        });

        itemHistorique.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHistoriquePanel();
            }
        });

        // Écouteurs pour les menus admin
        itemGestionArticles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGestionArticlesPanel();
            }
        });

        itemGestionClients.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGestionClientsPanel();
            }
        });

        itemGestionRemises.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGestionRemisesPanel();
            }
        });

        itemStatistiques.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStatistiquesPanel();
            }
        });

        itemReporting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showReportingPanel();
            }
        });
    }

    /**
     * Demande confirmation avant de quitter l'application
     */
    private void confirmAndExit() {
        int response = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir quitter l'application ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
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