package main.java.com.view.admin;

import main.java.com.controller.ArticleController;
import main.java.com.dao.DAOFactory;
import main.java.com.dao.MarqueDAO;
import main.java.com.model.Article;
import main.java.com.model.ArticleMarque;
import main.java.com.model.Marque;
import main.java.com.view.common.MainFrame;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;


/**
 * Panel pour la gestion des articles
 */
public class GestionArticlesPanel extends JPanel {

    
    private MainFrame mainFrame;
    private ArticleController articleController;
    private MarqueDAO marqueDAO;

    private JTable articleTable;
    private ArticleTableModel articleModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JTextField searchField;
    private JButton searchButton;

    /**
     * Constructeur du panel de gestion des articles
     * @param mainFrame La fenêtre principale
     */
    public GestionArticlesPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.articleController = new ArticleController();
        this.marqueDAO = DAOFactory.getMarqueDAO();

        initComponents();
        setupLayout();
        setupListeners();
        loadData();
    }

    /**
     * Initialise les composants
     */
    private void initComponents() {
        articleModel = new ArticleTableModel();
        articleTable = new JTable(articleModel);
        articleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        articleTable.getTableHeader().setReorderingAllowed(false);

        // Configuration des largeurs de colonnes
        TableColumn column;
        for (int i = 0; i < articleTable.getColumnCount(); i++) {
            column = articleTable.getColumnModel().getColumn(i);
            if (i == 1) { // Description
                column.setPreferredWidth(200);
            } else {
                column.setPreferredWidth(80);
            }
        }

        addButton = new JButton("Ajouter");
        editButton = new JButton("Modifier");
        deleteButton = new JButton("Supprimer");
        refreshButton = new JButton("Actualiser");
        searchField = new JTextField(20);
        searchButton = new JButton("Rechercher");
    }

    /**
     * Configure la disposition des composants
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel de titre
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Gestion des Articles");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Panel de recherche
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Rechercher:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Panel supérieur qui combine titre et recherche
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        // Panel de table avec scrolling
        JScrollPane scrollPane = new JScrollPane(articleTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        // Panel de boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Assemblage des panels
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Configure les écouteurs d'événements
     */
    private void setupListeners() {
        // Recherche d'articles
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText().trim();
                if (!searchTerm.isEmpty()) {
                    List<Article> articles = articleController.searchArticlesByName(searchTerm);
                    updateArticleList(articles);
                }
            }
        });

        // Rafraîchissement de la liste
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });

        // Ajouter un article
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showArticleDialog(null);
            }
        });

        // Modifier un article
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = articleTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Article article = articleController.getArticleById(
                            (Integer) articleTable.getValueAt(selectedRow, 0));
                    showArticleDialog(article);
                } else {
                    JOptionPane.showMessageDialog(GestionArticlesPanel.this,
                            "Veuillez sélectionner un article à modifier.",
                            "Modification",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Supprimer un article
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = articleTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int articleId = (Integer) articleTable.getValueAt(selectedRow, 0);
                    int response = JOptionPane.showConfirmDialog(GestionArticlesPanel.this,
                            "Êtes-vous sûr de vouloir supprimer cet article ?",
                            "Confirmation",
                            JOptionPane.YES_NO_OPTION);

                    if (response == JOptionPane.YES_OPTION) {
                        if (articleController.deleteArticle(articleId)) {
                            loadData();
                        } else {
                            JOptionPane.showMessageDialog(GestionArticlesPanel.this,
                                    "Erreur lors de la suppression de l'article.",
                                    "Erreur",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(GestionArticlesPanel.this,
                            "Veuillez sélectionner un article à supprimer.",
                            "Suppression",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    /**
     * Charge les données
     */
    private void loadData() {
        List<Article> articles = articleController.getAllArticles();
        updateArticleList(articles);
    }

    /**
     * Met à jour la liste des articles
     * @param articles Liste des articles à afficher
     */
    private void updateArticleList(List<Article> articles) {
        articleModel.setArticles(articles);
        articleModel.fireTableDataChanged();
    }

    /**
     * Affiche la boîte de dialogue pour ajouter/modifier un article
     * @param article L'article à modifier ou null pour un nouvel article
     */
    private void showArticleDialog(Article article) {
        boolean isEditing = (article != null);

        // Création de la boîte de dialogue
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                isEditing ? "Modifier un article" : "Ajouter un article",
                true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);

        // Création du formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Champs du formulaire
        JTextField nomField = new JTextField(20);
        JTextArea descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);

        JFormattedTextField prixUnitaireField = new JFormattedTextField(NumberFormat.getNumberInstance());
        prixUnitaireField.setColumns(10);

        JFormattedTextField prixGrosField = new JFormattedTextField(NumberFormat.getNumberInstance());
        prixGrosField.setColumns(10);

        JSpinner quantiteGrosSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));
        JSpinner stockSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        JTextField imageUrlField = new JTextField(20);

        // Pré-remplissage des champs si en mode édition
        if (isEditing) {
            nomField.setText(article.getNom());
            descriptionArea.setText(article.getDescription());
            prixUnitaireField.setValue(article.getPrixUnitaire());

            if (article.getPrixGros() != null) {
                prixGrosField.setValue(article.getPrixGros());
            }

            if (article.getQuantiteGros() != null) {
                quantiteGrosSpinner.setValue(article.getQuantiteGros());
            }

            stockSpinner.setValue(article.getStock());
            imageUrlField.setText(article.getImageUrl());
        }

        // Ajout des champs au formulaire
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nom:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(nomField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(descriptionScroll, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Prix unitaire:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(prixUnitaireField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Prix en gros:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(prixGrosField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Quantité en gros:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(quantiteGrosSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Stock:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(stockSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("URL de l'image:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(imageUrlField, gbc);

        // Boutons de validation et d'annulation
        JButton okButton = new JButton(isEditing ? "Modifier" : "Ajouter");
        JButton cancelButton = new JButton("Annuler");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Gestionnaires d'événements pour les boutons
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateForm(nomField.getText(), prixUnitaireField.getValue())) {
                    // Création ou mise à jour de l'article
                    Article articleToSave = isEditing ? article : new Article();
                    articleToSave.setNom(nomField.getText());
                    articleToSave.setDescription(descriptionArea.getText());

                    Number prixUnitaireNumber = (Number) prixUnitaireField.getValue();
                    articleToSave.setPrixUnitaire(prixUnitaireNumber.doubleValue());

                    Number prixGrosNumber = (Number) prixGrosField.getValue();
                    if (prixGrosNumber != null) {
                        articleToSave.setPrixGros(prixGrosNumber.doubleValue());
                        articleToSave.setQuantiteGros((Integer) quantiteGrosSpinner.getValue());
                    } else {
                        articleToSave.setPrixGros(null);
                        articleToSave.setQuantiteGros(null);
                    }

                    articleToSave.setStock((Integer) stockSpinner.getValue());
                    articleToSave.setImageUrl(imageUrlField.getText());

                    boolean success;
                    if (isEditing) {
                        success = articleController.updateArticle(articleToSave);
                    } else {
                        success = articleController.addArticle(articleToSave);
                    }

                    if (success) {
                        dialog.dispose();
                        loadData();
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                                "Erreur lors de l'enregistrement de l'article.",
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        // Assemblage final de la boîte de dialogue
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(contentPanel);
        dialog.setVisible(true);
    }

    /**
     * Valide le formulaire d'article
     * @param nom Nom de l'article
     * @param prixUnitaire Prix unitaire
     * @return true si le formulaire est valide, false sinon
     */
    private boolean validateForm(String nom, Object prixUnitaire) {
        // Vérification du nom
        if (nom == null || nom.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Le nom de l'article est obligatoire.",
                    "Erreur de validation",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Vérification du prix
        if (prixUnitaire == null) {
            JOptionPane.showMessageDialog(this,
                    "Le prix unitaire est obligatoire.",
                    "Erreur de validation",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Number prixUnitaireNumber = (Number) prixUnitaire;
        if (prixUnitaireNumber.doubleValue() <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Le prix unitaire doit être supérieur à zéro.",
                    "Erreur de validation",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Modèle de table pour les articles
     */
    private class ArticleTableModel extends AbstractTableModel {
        private final String[] columnNames = {"ID", "Nom", "Description", "Prix unitaire", "Prix gros", "Quantité gros", "Stock"};
        private List<Article> articles;

        public ArticleTableModel() {
            this.articles = new ArrayList<>();
        }

        public void setArticles(List<Article> articles) {
            this.articles = articles;
        }

        @Override
        public int getRowCount() {
            return articles.size();
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
            if (rowIndex >= 0 && rowIndex < articles.size()) {
                Article article = articles.get(rowIndex);

                switch (columnIndex) {
                    case 0: // ID
                        return article.getId();
                    case 1: // Nom
                        return article.getNom();
                    case 2: // Description
                        return article.getDescription();
                    case 3: // Prix unitaire
                        return String.format("%.2f €", article.getPrixUnitaire());
                    case 4: // Prix gros
                        return article.getPrixGros() != null ?
                                String.format("%.2f €", article.getPrixGros()) : "-";
                    case 5: // Quantité gros
                        return article.getQuantiteGros() != null ?
                                article.getQuantiteGros() : "-";
                    case 6: // Stock
                        return article.getStock();
                    default:
                        return null;
                }
            }
            return null;
        }
    }
}
