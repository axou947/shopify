package main.java.com.view.client;

import main.java.com.controller.PanierController;
import main.java.com.dao.DAOFactory;
import main.java.com.dao.MarqueDAO;
import main.java.com.model.ArticleMarque;
import main.java.com.model.LigneCommande;
import main.java.com.view.common.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Panel pour afficher et gérer le panier d'achat
 */
public class PanierPanel extends JPanel {
    private MainFrame mainFrame;
    private PanierController panierController;
    private MarqueDAO marqueDAO;

    private JTable panierTable;
    private PanierTableModel panierModel;
    private JLabel totalLabel;
    private JLabel remiseLabel;
    private JLabel netLabel;
    private JTextField codeRemiseField;
    private JButton appliquerRemiseButton;
    private JTextField noteField;
    private JButton validerCommandeButton;
    private JButton retourButton;
    private JButton viderPanierButton;

    /**
     * Constructeur du panel de panier
     * @param mainFrame La fenêtre principale
     */
    public PanierPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.marqueDAO = DAOFactory.getMarqueDAO();

        // Récupère le contrôleur de panier depuis le catalogue
        CataloguePanel cataloguePanel = (CataloguePanel) mainFrame.getCataloguePanel();
        if (cataloguePanel != null) {
            this.panierController = cataloguePanel.getPanierController();
        } else {
            this.panierController = new PanierController(); // Fallback si le catalogue n'est pas disponible
        }

        initComponents();
        setupLayout();
        setupListeners();
        refreshPanier();
    }

    /**
     * Initialise les composants
     */
    private void initComponents() {
        panierModel = new PanierTableModel();
        panierTable = new JTable(panierModel);
        panierTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panierTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JTextField()));

        totalLabel = new JLabel("Total: 0.00 €");
        remiseLabel = new JLabel("Remise: 0.00 €");
        netLabel = new JLabel("Net à payer: 0.00 €");

        codeRemiseField = new JTextField(10);
        appliquerRemiseButton = new JButton("Appliquer");

        noteField = new JTextField(20);

        validerCommandeButton = new JButton("Valider la commande");
        retourButton = new JButton("Retour au catalogue");
        viderPanierButton = new JButton("Vider le panier");
    }

    /**
     * Configure la disposition des composants
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel du haut avec titre
        JPanel topPanel = new JPanel();
        JLabel titleLabel = new JLabel("Votre Panier");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel);

        // Panel central avec table
        JScrollPane scrollPane = new JScrollPane(panierTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Articles dans votre panier"));

        // Panel du bas avec totaux et actions
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Panel pour les totaux
        JPanel totalsPanel = new JPanel(new GridLayout(3, 1));
        totalsPanel.setBorder(BorderFactory.createTitledBorder("Résumé"));
        totalsPanel.add(totalLabel);
        totalsPanel.add(remiseLabel);
        totalsPanel.add(netLabel);

        // Panel pour la remise
        JPanel remisePanel = new JPanel();
        remisePanel.setBorder(BorderFactory.createTitledBorder("Code remise"));
        remisePanel.add(new JLabel("Code:"));
        remisePanel.add(codeRemiseField);
        remisePanel.add(appliquerRemiseButton);

        // Panel pour la note
        JPanel notePanel = new JPanel();
        notePanel.setBorder(BorderFactory.createTitledBorder("Note pour la commande"));
        notePanel.add(new JLabel("Note:"));
        notePanel.add(noteField);

        // Panel pour les totaux et remise
        JPanel rightBottomPanel = new JPanel(new GridLayout(2, 1));
        rightBottomPanel.add(totalsPanel);
        rightBottomPanel.add(remisePanel);

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viderPanierButton);
        buttonPanel.add(retourButton);
        buttonPanel.add(validerCommandeButton);

        // Organisation du panel du bas
        bottomPanel.add(rightBottomPanel, BorderLayout.EAST);
        bottomPanel.add(notePanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Ajout des panels au panel principal
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
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

        // Vider le panier
        viderPanierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(PanierPanel.this,
                        "Êtes-vous sûr de vouloir vider votre panier ?",
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {
                    panierController.viderPanier();
                    refreshPanier();
                }
            }
        });

        // Appliquer une remise
        appliquerRemiseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = codeRemiseField.getText().trim();
                if (code.isEmpty()) {
                    JOptionPane.showMessageDialog(PanierPanel.this,
                            "Veuillez entrer un code de remise.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Dans une implémentation réelle, il faudrait rechercher la remise dans la base
                // et l'appliquer au panier. Pour l'instant, nous simulons une remise de 10%.
                if (code.equals("REMISE10")) {
                    // Créer une remise fictive pour test
                    double remiseMontant = panierController.getMontantTotal() * 0.1;
                    panierController.annulerRemise();

                    // Simuler l'application d'une remise
                    JOptionPane.showMessageDialog(PanierPanel.this,
                            "Code de remise appliqué avec succès !",
                            "Remise",
                            JOptionPane.INFORMATION_MESSAGE);

                    updateTotals(remiseMontant);
                } else {
                    JOptionPane.showMessageDialog(PanierPanel.this,
                            "Code de remise invalide.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Valider la commande
        validerCommandeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panierController.getLignesPanier().isEmpty()) {
                    JOptionPane.showMessageDialog(PanierPanel.this,
                            "Votre panier est vide.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int clientId = mainFrame.getCurrentUserId();
                String note = noteField.getText().trim();

                int commandeId = panierController.validerPanier(clientId, note);
                if (commandeId > 0) {
                    JOptionPane.showMessageDialog(PanierPanel.this,
                            "Commande validée avec succès !\nNuméro de commande: " + commandeId,
                            "Commande",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshPanier();
                } else {
                    JOptionPane.showMessageDialog(PanierPanel.this,
                            "Erreur lors de la validation de la commande.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Rafraîchit l'affichage du panier
     */
    public void refreshPanier() {
        // Met à jour le modèle de table
        panierModel.fireTableDataChanged();

        // Met à jour les totaux
        updateTotals(panierController.getMontantRemise());
    }

    /**
     * Met à jour les totaux affichés
     * @param remise Le montant de la remise
     */
    private void updateTotals(double remise) {
        DecimalFormat df = new DecimalFormat("0.00");
        double total = panierController.getMontantTotal();
        double net = total - remise;

        totalLabel.setText("Total: " + df.format(total) + " €");
        remiseLabel.setText("Remise: " + df.format(remise) + " €");
        netLabel.setText("Net à payer: " + df.format(net) + " €");
    }

    /**
     * Modèle de table pour le panier
     */
    private class PanierTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Article", "Marque", "Prix unitaire", "Quantité", "Total"};

        @Override
        public int getRowCount() {
            return panierController.getLignesPanier().size();
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
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 3; // Seule la quantité est éditable
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            List<LigneCommande> lignes = panierController.getLignesPanier();
            if (rowIndex >= 0 && rowIndex < lignes.size()) {
                LigneCommande ligne = lignes.get(rowIndex);
                ArticleMarque articleMarque = ligne.getArticleMarque();

                switch (columnIndex) {
                    case 0: // Article
                        if (articleMarque != null && articleMarque.getArticle() != null) {
                            return articleMarque.getArticle().getNom();
                        }
                        return "Article inconnu";
                    case 1: // Marque
                        if (articleMarque != null && articleMarque.getMarque() != null) {
                            return articleMarque.getMarque().getNom();
                        } else if (articleMarque != null) {
                            // Essaie de charger la marque depuis la base de données
                            try {
                                return marqueDAO.findById(articleMarque.getMarqueId()).getNom();
                            } catch (Exception e) {
                                return "Marque inconnue";
                            }
                        }
                        return "Marque inconnue";
                    case 2: // Prix unitaire
                        return String.format("%.2f €", ligne.getPrixUnitaire());
                    case 3: // Quantité
                        return ligne.getQuantite();
                    case 4: // Total
                        return String.format("%.2f €", ligne.getPrixTotal());
                    default:
                        return null;
                }
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 3 && aValue instanceof String) { // Édition de la quantité
                try {
                    int newQuantity = Integer.parseInt((String) aValue);
                    if (newQuantity > 0) {
                        if (panierController.modifierQuantite(rowIndex, newQuantity)) {
                            fireTableDataChanged();
                            refreshPanier();
                        } else {
                            JOptionPane.showMessageDialog(PanierPanel.this,
                                    "Impossible de modifier la quantité. Stock insuffisant.",
                                    "Erreur",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(PanierPanel.this,
                                "La quantité doit être supérieure à zéro.",
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(PanierPanel.this,
                            "Veuillez entrer un nombre valide.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}