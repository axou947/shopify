package main.java.com.view.reporting;

import main.java.com.controller.CommandeController;
import main.java.com.dao.ArticleDAO;
import main.java.com.dao.DAOFactory;
import main.java.com.dao.MarqueDAO;
import main.java.com.model.Article;
import main.java.com.model.Commande;
import main.java.com.model.Marque;
import main.java.com.view.common.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Panel pour générer des rapports
 */
public class ReportingPanel extends JPanel {
    private MainFrame mainFrame;
    private CommandeController commandeController;
    private ArticleDAO articleDAO;
    private MarqueDAO marqueDAO;

    private JTabbedPane tabbedPane;
    private JComboBox<String> reportTypeComboBox;
    private JButton generateButton;
    private JButton exportButton;
    private JPanel chartPanel;

    /**
     * Constructeur du panel de reporting
     * @param mainFrame La fenêtre principale
     */
    public ReportingPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.commandeController = new CommandeController();
        this.articleDAO = DAOFactory.getArticleDAO();
        this.marqueDAO = DAOFactory.getMarqueDAO();

        initComponents();
        setupLayout();
        setupListeners();
    }

    /**
     * Initialise les composants
     */
    private void initComponents() {
        tabbedPane = new JTabbedPane();

        String[] reportTypes = {
                "Ventes par catégorie",
                "Ventes par marque",
                "Évolution des ventes"
        };

        reportTypeComboBox = new JComboBox<>(reportTypes);
        generateButton = new JButton("Générer");
        exportButton = new JButton("Exporter");
        exportButton.setEnabled(false);

        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder("Graphique"));
    }

    /**
     * Configure la disposition des composants
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel du haut
        JPanel topPanel = new JPanel();
        JLabel titleLabel = new JLabel("Reporting");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel);

        // Panel de contrôle
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Type de rapport:"));
        controlPanel.add(reportTypeComboBox);
        controlPanel.add(generateButton);
        controlPanel.add(exportButton);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(chartPanel, BorderLayout.CENTER);

        // Assemblage final
        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Configure les écouteurs d'événements
     */
    private void setupListeners() {
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportReport();
            }
        });
    }

    /**
     * Génère un rapport selon le type sélectionné
     */
    private void generateReport() {
        String reportType = (String) reportTypeComboBox.getSelectedItem();

        // Vide le panel du graphique
        chartPanel.removeAll();

        if ("Ventes par catégorie".equals(reportType)) {
            generateSalesByCategoryReport();
        } else if ("Ventes par marque".equals(reportType)) {
            generateSalesByBrandReport();
        } else if ("Évolution des ventes".equals(reportType)) {
            generateSalesEvolutionReport();
        }

        // Rafraîchit le panel
        chartPanel.revalidate();
        chartPanel.repaint();

        // Active le bouton d'export
        exportButton.setEnabled(true);
    }

    /**
     * Génère un rapport de ventes par catégorie
     */
    private void generateSalesByCategoryReport() {
        // Dans une implémentation réelle, récupérer les données de la base de données
        Map<String, Double> data = new HashMap<>();
        data.put("Bureautique", 4800.0);
        data.put("Électronique", 9200.0);
        data.put("Accessoires", 3500.0);
        data.put("Vêtements", 7800.0);

        // Création du graphique
        JPanel barChart = ChartGenerator.createBarChart(
                "Ventes par catégorie",
                "Catégorie",
                "Montant (€)",
                data
        );

        // Ajout du graphique au panel
        chartPanel.add(barChart, BorderLayout.CENTER);
    }

    /**
     * Génère un rapport de ventes par marque
     */
    private void generateSalesByBrandReport() {
        // Dans une implémentation réelle, récupérer les données de la base de données
        Map<String, Double> data = new HashMap<>();
        data.put("BIC", 8900.0);
        data.put("Oxford", 5700.0);
        data.put("Samsung", 12300.0);
        data.put("Sony", 9800.0);
        data.put("Anker", 6500.0);

        // Création du graphique
        JPanel pieChart = ChartGenerator.createPieChart(
                "Répartition des ventes par marque",
                data
        );

        // Ajout du graphique au panel
        chartPanel.add(pieChart, BorderLayout.CENTER);
    }

    /**
     * Génère un rapport d'évolution des ventes
     */
    private void generateSalesEvolutionReport() {
        // Dans une implémentation réelle, récupérer les données de la base de données
        Map<String, List<Double>> data = new HashMap<>();
        List<Double> salesValues = Arrays.asList(12500.0, 14200.0, 15800.0, 16900.0, 18300.0, 20100.0);
        data.put("Ventes", salesValues);

        List<String> months = Arrays.asList("Janvier", "Février", "Mars", "Avril", "Mai", "Juin");

        // Création du graphique
        JPanel lineChart = ChartGenerator.createLineChart(
                "Évolution des ventes mensuelles",
                "Mois",
                "Montant (€)",
                data,
                months
        );

        // Ajout du graphique au panel
        chartPanel.add(lineChart, BorderLayout.CENTER);
    }

    /**
     * Exporte le rapport courant
     */
    private void exportReport() {
        String reportType = (String) reportTypeComboBox.getSelectedItem();

        // Boîte de dialogue pour choisir l'emplacement du fichier
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exporter le rapport");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setSelectedFile(new File(reportType.replace(" ", "_") + ".csv"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try (FileWriter writer = new FileWriter(file)) {
                // Écrit l'en-tête du fichier CSV
                writer.write("Rapport: " + reportType + "\n");
                writer.write("Date de génération: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()) + "\n\n");

                // Écrit les données selon le type de rapport
                if ("Ventes par catégorie".equals(reportType)) {
                    writer.write("Catégorie,Montant\n");
                    writer.write("Bureautique,4800.0\n");
                    writer.write("Électronique,9200.0\n");
                    writer.write("Accessoires,3500.0\n");
                    writer.write("Vêtements,7800.0\n");
                } else if ("Ventes par marque".equals(reportType)) {
                    writer.write("Marque,Montant\n");
                    writer.write("BIC,8900.0\n");
                    writer.write("Oxford,5700.0\n");
                    writer.write("Samsung,12300.0\n");
                    writer.write("Sony,9800.0\n");
                    writer.write("Anker,6500.0\n");
                } else if ("Évolution des ventes".equals(reportType)) {
                    writer.write("Mois,Montant\n");
                    writer.write("Janvier,12500.0\n");
                    writer.write("Février,14200.0\n");
                    writer.write("Mars,15800.0\n");
                    writer.write("Avril,16900.0\n");
                    writer.write("Mai,18300.0\n");
                    writer.write("Juin,20100.0\n");
                }

                JOptionPane.showMessageDialog(this,
                        "Rapport exporté avec succès dans " + file.getAbsolutePath(),
                        "Export réussi",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de l'export du rapport: " + e.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}