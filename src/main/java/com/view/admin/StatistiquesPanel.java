package main.java.com.view.admin;

import main.java.com.dao.ArticleDAO;
import main.java.com.dao.CommandeDAO;
import main.java.com.dao.DAOFactory;
import main.java.com.dao.MarqueDAO;
import main.java.com.model.Article;
import main.java.com.model.Commande;
import main.java.com.model.LigneCommande;
import main.java.com.model.Marque;
import main.java.com.view.reporting.ChartGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Panel pour afficher les statistiques de ventes
 */
public class StatistiquesPanel extends JPanel {
    private CommandeDAO commandeDAO;
    private ArticleDAO articleDAO;
    private MarqueDAO marqueDAO;

    private JTabbedPane tabbedPane;
    private JPanel ventesMensuellesPanel;
    private JPanel ventesParArticlePanel;
    private JPanel ventesParMarquePanel;

    /**
     * Constructeur du panel de statistiques
     */
    public StatistiquesPanel() {
        this.commandeDAO = DAOFactory.getCommandeDAO();
        this.articleDAO = DAOFactory.getArticleDAO();
        this.marqueDAO = DAOFactory.getMarqueDAO();

        initComponents();
        setupLayout();
        loadData();
    }

    /**
     * Initialise les composants
     */
    private void initComponents() {
        tabbedPane = new JTabbedPane();
        ventesMensuellesPanel = new JPanel(new BorderLayout());
        ventesParArticlePanel = new JPanel(new BorderLayout());
        ventesParMarquePanel = new JPanel(new BorderLayout());
    }

    /**
     * Configure la disposition des composants
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Configuration des onglets
        tabbedPane.addTab("Ventes Mensuelles", ventesMensuellesPanel);
        tabbedPane.addTab("Ventes par Article", ventesParArticlePanel);
        tabbedPane.addTab("Ventes par Marque", ventesParMarquePanel);

        // Ajout du panneau d'onglets au panel principal
        add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * Charge les données et génère les graphiques
     */
    private void loadData() {
        // Génération du graphique de ventes mensuelles
        generateMonthlySalesChart();

        // Génération du graphique de ventes par article
        generateSalesByArticleChart();

        // Génération du graphique de ventes par marque
        generateSalesByBrandChart();
    }

    /**
     * Génère le graphique des ventes mensuelles
     */
    private void generateMonthlySalesChart() {
        // Dans une implémentation réelle, ces données seraient chargées depuis la base de données
        // Ici, nous utilisons des données fictives pour l'exemple
        Map<String, List<Double>> data = new HashMap<>();
        List<Double> salesValues = Arrays.asList(12500.0, 14200.0, 15800.0, 16900.0, 18300.0, 20100.0);
        data.put("Ventes", salesValues);

        List<String> months = Arrays.asList("Janvier", "Février", "Mars", "Avril", "Mai", "Juin");

        // Création du graphique
        JPanel chartPanel = ChartGenerator.createLineChart(
                "Évolution des ventes mensuelles",
                "Mois",
                "Montant (€)",
                data,
                months
        );

        // Ajout du graphique au panel
        ventesMensuellesPanel.add(chartPanel, BorderLayout.CENTER);
    }

    /**
     * Génère le graphique des ventes par article
     */
    private void generateSalesByArticleChart() {
        // Dans une implémentation réelle, ces données seraient chargées depuis la base de données
        // Ici, nous utilisons des données fictives pour l'exemple
        Map<String, Double> data = new HashMap<>();
        data.put("Briquet", 5200.0);
        data.put("Stylo", 3800.0);
        data.put("Cahier", 2900.0);
        data.put("Clé USB", 7500.0);
        data.put("Écouteurs", 6300.0);

        // Création du graphique
        JPanel chartPanel = ChartGenerator.createBarChart(
                "Ventes par article",
                "Article",
                "Montant (€)",
                data
        );

        // Ajout du graphique au panel
        ventesParArticlePanel.add(chartPanel, BorderLayout.CENTER);
    }

    /**
     * Génère le graphique des ventes par marque
     */
    private void generateSalesByBrandChart() {
        // Dans une implémentation réelle, ces données seraient chargées depuis la base de données
        // Ici, nous utilisons des données fictives pour l'exemple
        Map<String, Double> data = new HashMap<>();
        data.put("BIC", 8900.0);
        data.put("Oxford", 5700.0);
        data.put("Samsung", 12300.0);
        data.put("Sony", 9800.0);
        data.put("Anker", 6500.0);

        // Création du graphique
        JPanel chartPanel = ChartGenerator.createPieChart(
                "Répartition des ventes par marque",
                data
        );

        // Ajout du graphique au panel
        ventesParMarquePanel.add(chartPanel, BorderLayout.CENTER);
    }
}