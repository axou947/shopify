package main.java.com.view.client;

import main.java.com.controller.PanierController;
import main.java.com.dao.DAOFactory;
import main.java.com.dao.MarqueDAO;
import main.java.com.model.ArticleMarque;
import main.java.com.model.LigneCommande;
import main.java.com.view.common.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * Panel pour afficher et gérer le panier d'achat
 * Version améliorée suivant les principes du cours 7
 */
public class PanierPanel extends JPanel implements ActionListener, TableModelListener {
    // Constantes
    private static final int COLUMN_ARTICLE = 0;
    private static final int COLUMN_MARQUE = 1;
    private static final int COLUMN_PRIX_UNITAIRE = 2;
    private static final int COLUMN_QUANTITE = 3;
    private static final int COLUMN_TOTAL = 4;

    // Références
    private MainFrame mainFrame;
    private PanierController panierController;
    private MarqueDAO marqueDAO;

    // Composants de l'interface
    private JTable panierTable;
    private PanierTableModel panierModel;
    private JLabel totalLabel;
    private JLabel remiseLabel;
    private JLabel netLabel;
    private JTextField codeRemiseField;
    private JButton appliquerRemiseButton;
    private JTextArea noteField;
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
        // Table du panier
        panierModel = new PanierTableModel();
        panierModel.addTableModelListener(this);

        panierTable = new JTable(panierModel);
        panierTable.setRowHeight(30); // Hauteur des lignes pour meilleure lisibilité
        panierTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Configuration des largeurs et renderers des colonnes
        TableColumn quantityColumn = panierTable.getColumnModel().getColumn(COLUMN_QUANTITE);
        quantityColumn.setCellEditor(new DefaultCellEditor(new JTextField()));

        // Format des nombres
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        panierTable.getColumnModel().getColumn(COLUMN_PRIX_UNITAIRE).setCellRenderer(rightRenderer);
        panierTable.getColumnModel().getColumn(COLUMN_QUANTITE).setCellRenderer(rightRenderer);
        panierTable.getColumnModel().getColumn(COLUMN_TOTAL).setCellRenderer(rightRenderer);

        // Labels des montants
        totalLabel = new JLabel("Total: 0,00 €", JLabel.RIGHT);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));

        remiseLabel = new JLabel("Remise: 0,00 €", JLabel.RIGHT);
        remiseLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        netLabel = new JLabel("Net à payer: 0,00 €", JLabel.RIGHT);
        netLabel.setFont(new Font("Arial", Font.BOLD, 16));
        netLabel.setForeground(new Color(0, 100, 0)); // Vert foncé

        // Zone de remise
        codeRemiseField = new JTextField(10);
        appliquerRemiseButton = new JButton("Appliquer");

        // Zone de note
        noteField = new JTextArea(3, 20);
        noteField.setLineWrap(true);
        noteField.setWrapStyleWord(true);

        // Boutons d'action
        validerCommandeButton = new JButton("Valider la commande");
        validerCommandeButton.setFont(new Font("Arial", Font.BOLD, 14));
        validerCommandeButton.setBackground(new Color(46, 139, 87)); // SeaGreen
        validerCommandeButton.setForeground(Color.WHITE);

        retourButton = new JButton("Retour au catalogue");
        viderPanierButton = new JButton("Vider le panier");
    }

    /**
     * Configure la disposition des composants
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel du haut avec titre
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Votre Panier", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        // Panel central avec table
        JScrollPane scrollPane = new JScrollPane(panierTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder("Articles dans votre panier"),
                new EmptyBorder(5, 5, 5, 5)
        ));

        // Panel du bas avec les totaux et actions
        JPanel bottomPanel = new JPanel(new BorderLayout(20, 10));
        bottomPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Panel pour les totaux
        JPanel totalsPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        totalsPanel.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder("Résumé"),
                new EmptyBorder(5, 5, 5, 5)
        ));
        totalsPanel.add(totalLabel);
        totalsPanel.add(remiseLabel);
        totalsPanel.add(netLabel);

        // Panel pour la remise
        JPanel remisePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        remisePanel.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder("Code remise"),
                new EmptyBorder(5, 5, 5, 5)
        ));
        remisePanel.add(new JLabel("Code:"));
        remisePanel.add(codeRemiseField);
        remisePanel.add(appliquerRemiseButton);

        // Panel pour les totaux et remise (partie droite)
        JPanel rightBottomPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        rightBottomPanel.add(totalsPanel);
        rightBottomPanel.add(remisePanel);

        // Panel pour la note
        JPanel notePanel = new JPanel(new BorderLayout(5, 5));
        notePanel.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder("Note pour la commande"),
                new EmptyBorder(5, 5, 5, 5)
        ));
        notePanel.add(new JScrollPane(noteField), BorderLayout.CENTER);

        // Panel pour les boutons d'action
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttonPanel.add(viderPanierButton);
        buttonPanel.add(retourButton);
        buttonPanel.add(validerCommandeButton);

        // Organisation du panel du bas
        JPanel leftBottomPanel = new JPanel(new BorderLayout(0, 10));
        leftBottomPanel.add(notePanel, BorderLayout.CENTER);

        bottomPanel.add(leftBottomPanel, BorderLayout.CENTER);
        bottomPanel.add(rightBottomPanel, BorderLayout.EAST);
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
        // Utilisation de "this" comme ActionListener car la classe implémente ActionListener
        retourButton.addActionListener(this);
        viderPanierButton.addActionListener(this);
        appliquerRemiseButton.addActionListener(this);
        validerCommandeButton.addActionListener(this);
    }

    /**
     * Gestion des événements d'action
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == retourButton) {
            mainFrame.showCataloguePanel();
        }
        else if (source == viderPanierButton) {
            int response = JOptionPane.showConfirmDialog(this,
                    "Êtes-vous sûr de vouloir vider votre panier ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                panierController.viderPanier();
                refreshPanier();
            }
        }
        else if (source == appliquerRemiseButton) {
            String code = codeRemiseField.getText().trim();
            if (code.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez entrer un code de remise.",
                        "Code manquant",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Dans une implémentation réelle, il faudrait rechercher la remise dans la base
            // et l'appliquer au panier. Pour l'instant, nous simulons une remise de 10%.
            if (code.equals("REMISE10")) {
                // Créer une remise fictive pour test
                double remiseMontant = panierController.getMontantTotal() * 0.1;
                panierController.annulerRemise();

                // Simuler l'application d'une remise
                JOptionPane.showMessageDialog(this,
                        "Code de remise appliqué avec succès !",
                        "Remise appliquée",
                        JOptionPane.INFORMATION_MESSAGE);

                updateTotals(remiseMontant);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Code de remise invalide.",
                        "Code invalide",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (source == validerCommandeButton) {
            if (panierController.getLignesPanier().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Votre panier est vide.",
                        "Panier vide",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int clientId = mainFrame.getCurrentUserId();
            String note = noteField.getText().trim();

            int commandeId = panierController.validerPanier(clientId, note);
            if (commandeId > 0) {
                JOptionPane.showMessageDialog(this,
                        "Commande validée avec succès !\nNuméro de commande: " + commandeId,
                        "Commande validée",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshPanier();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la validation de la commande.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Gestion des événements de modification du modèle de table
     */
    @Override
    public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == COLUMN_QUANTITE) {
            refreshPanier();
        }
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
        private NumberFormat currencyFormatter = new DecimalFormat("#,##0.00 €");

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
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case COLUMN_PRIX_UNITAIRE:
                case COLUMN_TOTAL:
                    return Double.class;
                case COLUMN_QUANTITE:
                    return Integer.class;
                default:
                    return String.class;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == COLUMN_QUANTITE; // Seule la quantité est éditable
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            List<LigneCommande> lignes = panierController.getLignesPanier();
            if (rowIndex >= 0 && rowIndex < lignes.size()) {
                LigneCommande ligne = lignes.get(rowIndex);
                ArticleMarque articleMarque = ligne.getArticleMarque();

                switch (columnIndex) {
                    case COLUMN_ARTICLE: // Article
                        if (articleMarque != null && articleMarque.getArticle() != null) {
                            return articleMarque.getArticle().getNom();
                        }
                        return "Article inconnu";

                    case COLUMN_MARQUE: // Marque
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

                    case COLUMN_PRIX_UNITAIRE: // Prix unitaire
                        return ligne.getPrixUnitaire();

                    case COLUMN_QUANTITE: // Quantité
                        return ligne.getQuantite();

                    case COLUMN_TOTAL: // Total
                        return ligne.getPrixTotal();

                    default:
                        return null;
                }
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == COLUMN_QUANTITE) {
                try {
                    int newQuantity;
                    if (aValue instanceof Integer) {
                        newQuantity = (Integer) aValue;
                    } else if (aValue instanceof String) {
                        newQuantity = Integer.parseInt((String) aValue);
                    } else {
                        return;
                    }

                    if (newQuantity > 0) {
                        if (panierController.modifierQuantite(rowIndex, newQuantity)) {
                            fireTableRowsUpdated(rowIndex, rowIndex);
                        } else {
                            JOptionPane.showMessageDialog(PanierPanel.this,
                                    "Impossible de modifier la quantité. Stock insuffisant.",
                                    "Stock insuffisant",
                                    JOptionPane.WARNING_MESSAGE);
                            // Restaure l'ancienne valeur dans la vue
                            fireTableRowsUpdated(rowIndex, rowIndex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(PanierPanel.this,
                                "La quantité doit être supérieure à zéro.",
                                "Quantité invalide",
                                JOptionPane.WARNING_MESSAGE);
                        // Restaure l'ancienne valeur dans la vue
                        fireTableRowsUpdated(rowIndex, rowIndex);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(PanierPanel.this,
                            "Veuillez entrer un nombre valide.",
                            "Format invalide",
                            JOptionPane.WARNING_MESSAGE);
                    // Restaure l'ancienne valeur dans la vue
                    fireTableRowsUpdated(rowIndex, rowIndex);
                }
            }
        }
    }
}