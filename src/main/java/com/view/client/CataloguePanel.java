package main.java.com.view.client;

import main.java.com.controller.ArticleController;
import main.java.com.controller.PanierController;
import main.java.com.dao.DAOFactory;
import main.java.com.dao.MarqueDAO;
import main.java.com.model.Article;
import main.java.com.model.ArticleMarque;
import main.java.com.model.Marque;
import main.java.com.view.common.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * Panel pour afficher le catalogue d'articles
 */
public class CataloguePanel extends JPanel {
    private ArticleController articleController;
    private PanierController panierController;
    private MarqueDAO marqueDAO;
    private MainFrame mainFrame;

    private JList<Article> articleList;
    private DefaultListModel<Article> articleModel;
    private JList<Marque> marqueList;
    private DefaultListModel<Marque> marqueModel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton allArticlesButton;
    private JTextArea articleDetailArea;
    private JSpinner quantitySpinner;
    private JButton addToCartButton;
    private JButton viewCartButton;

    /**
     * Constructeur du panel de catalogue
     * @param mainFrame La fenêtre principale
     */
    public CataloguePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.articleController = new ArticleController();
        this.panierController = new PanierController();
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
        articleModel = new DefaultListModel<>();
        articleList = new JList<>(articleModel);
        articleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        marqueModel = new DefaultListModel<>();
        marqueList = new JList<>(marqueModel);
        marqueList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        searchField = new JTextField(20);
        searchButton = new JButton("Rechercher");
        allArticlesButton = new JButton("Tous les articles");
        articleDetailArea = new JTextArea(10, 30);
        articleDetailArea.setEditable(false);
        articleDetailArea.setLineWrap(true);
        articleDetailArea.setWrapStyleWord(true);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 1);
        quantitySpinner = new JSpinner(spinnerModel);

        addToCartButton = new JButton("Ajouter au panier");
        addToCartButton.setEnabled(false);
        viewCartButton = new JButton("Voir le panier");
    }

    /**
     * Configure la disposition des composants
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel du haut pour la recherche
        JPanel topPanel = new JPanel(new BorderLayout(5, 0));
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Recherche:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(allArticlesButton, BorderLayout.EAST);

        // Panel de gauche pour les marques
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Marques"));
        leftPanel.add(new JScrollPane(marqueList), BorderLayout.CENTER);

        // Panel central pour les articles
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Articles"));
        centerPanel.add(new JScrollPane(articleList), BorderLayout.CENTER);

        // Panel de droite pour les détails et actions
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Détails"));
        rightPanel.add(new JScrollPane(articleDetailArea), BorderLayout.CENTER);

        JPanel actionPanel = new JPanel();
        actionPanel.add(new JLabel("Quantité:"));
        actionPanel.add(quantitySpinner);
        actionPanel.add(addToCartButton);
        actionPanel.add(viewCartButton);
        rightPanel.add(actionPanel, BorderLayout.SOUTH);

        // Panel principal
        add(topPanel, BorderLayout.NORTH);

        // Panel qui contient les 3 colonnes
        JPanel contentPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        contentPanel.add(leftPanel);
        contentPanel.add(centerPanel);
        contentPanel.add(rightPanel);
        add(contentPanel, BorderLayout.CENTER);
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

        // Réinitialisation de la liste d'articles
        allArticlesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Article> articles = articleController.getAllArticles();
                updateArticleList(articles);
                marqueList.clearSelection();
            }
        });

        // Sélection d'une marque
        marqueList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Marque selectedMarque = marqueList.getSelectedValue();
                    if (selectedMarque != null) {
                        List<Article> articles = articleController.getArticlesByMarque(selectedMarque.getId());
                        updateArticleList(articles);
                    }
                }
            }
        });

        // Sélection d'un article
        articleList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Article selectedArticle = articleList.getSelectedValue();
                    if (selectedArticle != null) {
                        displayArticleDetails(selectedArticle);
                        addToCartButton.setEnabled(true);
                    } else {
                        articleDetailArea.setText("");
                        addToCartButton.setEnabled(false);
                    }
                }
            }
        });

        // Ajouter au panier
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Article selectedArticle = articleList.getSelectedValue();
                if (selectedArticle != null) {
                    int quantity = (int) quantitySpinner.getValue();

                    // Créer un ArticleMarque temporaire pour cet article
                    // Dans une implémentation réelle, il faudrait récupérer la relation exacte
                    ArticleMarque articleMarque = new ArticleMarque();
                    articleMarque.setId(selectedArticle.getId()); // Simplification, devrait être l'ID de la relation
                    articleMarque.setArticleId(selectedArticle.getId());
                    articleMarque.setArticle(selectedArticle);

                    if (panierController.ajouterArticle(articleMarque, quantity)) {
                        JOptionPane.showMessageDialog(CataloguePanel.this,
                                "Article ajouté au panier avec succès.",
                                "Panier",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(CataloguePanel.this,
                                "Impossible d'ajouter cet article au panier. Stock insuffisant.",
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Voir le panier
        viewCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showPanierPanel();
            }
        });
    }

    /**
     * Charge les données
     */
    private void loadData() {
        // Charge les marques
        List<Marque> marques = marqueDAO.findAll();
        for (Marque marque : marques) {
            marqueModel.addElement(marque);
        }

        // Charge tous les articles
        List<Article> articles = articleController.getAllArticles();
        updateArticleList(articles);
    }

    /**
     * Met à jour la liste des articles
     * @param articles Liste des articles à afficher
     */
    private void updateArticleList(List<Article> articles) {
        articleModel.clear();
        for (Article article : articles) {
            articleModel.addElement(article);
        }
    }

    /**
     * Affiche les détails d'un article
     * @param article L'article à afficher
     */
    private void displayArticleDetails(Article article) {
        StringBuilder details = new StringBuilder();
        details.append("Nom: ").append(article.getNom()).append("\n\n");
        details.append("Description: ").append(article.getDescription()).append("\n\n");
        details.append("Prix unitaire: ").append(article.getPrixUnitaire()).append(" €\n");

        if (article.getPrixGros() != null && article.getQuantiteGros() != null) {
            details.append("Prix en gros: ").append(article.getPrixGros())
                    .append(" € pour ").append(article.getQuantiteGros())
                    .append(" unités\n");
        }

        details.append("\nStock disponible: ").append(article.getStock()).append(" unités\n");

        // Affichage de l'image si disponible
        if (article.getImageUrl() != null && !article.getImageUrl().isEmpty()) {
            File imageFile = new File("src/main/resources/img/" + article.getImageUrl());
            if (imageFile.exists()) {
                details.append("\nImage disponible: ").append(article.getImageUrl());
                // Dans une implémentation plus complète, on pourrait afficher l'image
            }
        }

        articleDetailArea.setText(details.toString());
    }

    /**
     * Récupère le contrôleur de panier
     * @return Le contrôleur de panier
     */
    public PanierController getPanierController() {
        return panierController;
    }
}