package main.java.com.view.client;

import main.java.com.controller.CommandeController;
import main.java.com.model.Commande;
import main.java.com.model.LigneCommande;
import main.java.com.view.common.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel pour afficher l'historique des commandes d'un client
 */
public class HistoriqueCommandesPanel extends JPanel {
    private MainFrame mainFrame;
    private int clientId;
    private CommandeController commandeController;

    private JTable commandesTable;
    private CommandeTableModel commandeModel;
    private JTable lignesTable;
    private LigneCommandeTableModel ligneModel;
    private JButton retourButton;
    private JTextArea detailsArea;

    /**
     * Constructeur du panel d'historique des commandes
     * @param mainFrame La fenêtre principale
     * @param clientId L'ID du client
     */
    public HistoriqueCommandesPanel(MainFrame mainFrame, int clientId) {
        this.mainFrame = mainFrame;
        this.clientId = clientId;
        this.commandeController = new CommandeController();

        initComponents();
        setupLayout();
        setupListeners();
        loadData();
    }

    /**
     * Initialise les composants
     */
    private void initComponents() {
        commandeModel = new CommandeTableModel();
        commandesTable = new JTable(commandeModel);
        commandesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ligneModel = new LigneCommandeTableModel();
        lignesTable = new JTable(ligneModel);

        detailsArea = new JTextArea(5, 20);
        detailsArea.setEditable(false);

        retourButton = new JButton("Retour au catalogue");
    }

    /**
     * Configure la disposition des composants
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel du haut avec titre
        JPanel topPanel = new JPanel();
        JLabel titleLabel = new JLabel("Historique des commandes");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel);

        // Panel pour les tables
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        // Table des commandes
        JScrollPane commandesScrollPane = new JScrollPane(commandesTable);
        commandesScrollPane.setBorder(BorderFactory.createTitledBorder("Vos commandes"));
        centerPanel.add(commandesScrollPane, BorderLayout.NORTH);

        // Table des lignes de commande
        JScrollPane lignesScrollPane = new JScrollPane(lignesTable);
        lignesScrollPane.setBorder(BorderFactory.createTitledBorder("Détails de la commande sélectionnée"));
        centerPanel.add(lignesScrollPane, BorderLayout.CENTER);

        // Zone de détails
        JScrollPane detailsScrollPane = new JScrollPane(detailsArea);
        detailsScrollPane.setBorder(BorderFactory.createTitledBorder("Informations supplémentaires"));
        centerPanel.add(detailsScrollPane, BorderLayout.SOUTH);

        // Panel du bas avec bouton de retour
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(retourButton);

        // Ajout des panels au panel principal
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Configure les écouteurs d'événements
     */
    private void setupListeners() {
        // Retour au catalogue
        retourButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showCataloguePanel();
            }
        });

        // Sélection d'une commande
        commandesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = commandesTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int commandeId = commandeModel.getCommandeIdAt(selectedRow);
                    loadLignesCommande(commandeId);

                    // Affiche les informations supplémentaires
                    Commande commande = commandeModel.getCommandeAt(selectedRow);
                    displayCommandeDetails(commande);
                }
            }
        });
    }

    /**
     * Charge les données
     */
    private void loadData() {
        List<Commande> commandes = commandeController.getCommandesByClient(clientId);
        commandeModel.setCommandes(commandes);
        commandeModel.fireTableDataChanged();

        // Vide la table des lignes de commande
        ligneModel.setLignes(new ArrayList<>());
        ligneModel.fireTableDataChanged();

        // Vide la zone de détails
        detailsArea.setText("");
    }

    /**
     * Charge les lignes de commande pour une commande
     * @param commandeId L'ID de la commande
     */
    private void loadLignesCommande(int commandeId) {
        List<LigneCommande> lignes = commandeController.getLignesCommande(commandeId);
        ligneModel.setLignes(lignes);
        ligneModel.fireTableDataChanged();
    }

    /**
     * Affiche les détails d'une commande
     * @param commande La commande à afficher
     */
    private void displayCommandeDetails(Commande commande) {
        if (commande == null) {
            detailsArea.setText("");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        StringBuilder details = new StringBuilder();

        details.append("Commande n°").append(commande.getId()).append("\n");
        details.append("Date: ").append(dateFormat.format(commande.getDateCommande())).append("\n");
        details.append("Statut: ").append(commande.getStatut()).append("\n");
        details.append("Total: ").append(String.format("%.2f €", commande.getMontantTotal())).append("\n");

        if (commande.getMontantRemise() > 0) {
            details.append("Remise: ").append(String.format("%.2f €", commande.getMontantRemise())).append("\n");
            details.append("Net à payer: ").append(String.format("%.2f €", commande.getMontantNet())).append("\n");
        }

        if (commande.getNote() != null && !commande.getNote().isEmpty()) {
            details.append("\nNote: ").append(commande.getNote()).append("\n");
        }

        detailsArea.setText(details.toString());
    }

    /**
     * Modèle de table pour les commandes
     */
    private class CommandeTableModel extends AbstractTableModel {
        private final String[] columnNames = {"N°", "Date", "Statut", "Montant total", "Remise", "Net à payer"};
        private List<Commande> commandes;

        public CommandeTableModel() {
            this.commandes = new ArrayList<>();
        }

        public void setCommandes(List<Commande> commandes) {
            this.commandes = commandes;
        }

        public int getCommandeIdAt(int rowIndex) {
            if (rowIndex >= 0 && rowIndex < commandes.size()) {
                return commandes.get(rowIndex).getId();
            }
            return -1;
        }

        public Commande getCommandeAt(int rowIndex) {
            if (rowIndex >= 0 && rowIndex < commandes.size()) {
                return commandes.get(rowIndex);
            }
            return null;
        }

        @Override
        public int getRowCount() {
            return commandes.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex >= 0 && rowIndex < commandes.size()) {
                Commande commande = commandes.get(rowIndex);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                switch (columnIndex) {
                    case 0: // N°
                        return commande.getId();
                    case 1: // Date
                        return dateFormat.format(commande.getDateCommande());
                    case 2: // Statut
                        return commande.getStatut();
                    case 3: // Montant total
                        return String.format("%.2f €", commande.getMontantTotal());
                    case 4: // Remise
                        return String.format("%.2f €", commande.getMontantRemise());
                    case 5: // Net à payer
                        return String.format("%.2f €", commande.getMontantNet());
                    default:
                        return null;
                }
            }
            return null;
        }
    }

    /**
     * Modèle de table pour les lignes de commande
     */
    private class LigneCommandeTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Article", "Marque", "Prix unitaire", "Quantité", "Prix total"};
        private List<LigneCommande> lignes;

        public LigneCommandeTableModel() {
            this.lignes = new ArrayList<>();
        }

        public void setLignes(List<LigneCommande> lignes) {
            this.lignes = lignes;
        }

        @Override
        public int getRowCount() {
            return lignes.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex >= 0 && rowIndex < lignes.size()) {
                LigneCommande ligne = lignes.get(rowIndex);

                switch (columnIndex) {
                    case 0: // Article
                        return ligne.getArticleMarque() != null && ligne.getArticleMarque().getArticle() != null ?
                                ligne.getArticleMarque().getArticle().getNom() : "Article inconnu";
                    case 1: // Marque
                        return ligne.getArticleMarque() != null && ligne.getArticleMarque().getMarque() != null ?
                                ligne.getArticleMarque().getMarque().getNom() : "Marque inconnue";
                    case 2: // Prix unitaire
                        return String.format("%.2f €", ligne.getPrixUnitaire());
                    case 3: // Quantité
                        return ligne.getQuantite();
                    case 4: // Prix total
                        return String.format("%.2f €", ligne.getPrixTotal());
                    default:
                        return null;
                }
            }
            return null;
        }
    }
}