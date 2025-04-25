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
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

/**
 * Panel pour afficher le catalogue d'articles
 * Version améliorée suivant les principes du cours 7
 */
public class CataloguePanel extends JPanel {
    // Constantes
    private static final int DEFAULT_QUANTITY = 1;
    private static final int MAX_QUANTITY = 100;

    // Contrôleurs et DAO
    private ArticleController articleController;
    private PanierController panierController;
    private MarqueDAO marqueDAO;
    private MainFrame mainFrame;

    // Composants pour les listes
    private JList<Article> articleList;
    private DefaultListModel<Article> articleModel;
    private JList<Marque> marqueList;
    private DefaultListModel<Marque> marqueModel;

    // Composants pour la recherche
    private JTextField searchField;
    private JButton searchButton;
    private JButton allArticlesButton;

    // Composants pour les détails et actions
    private JTextArea articleDetailArea;
    private JSpinner quantitySpinner;
    private JButton addToCartButton;
    private JButton viewCartButton;

    // Panneau d'image
    private JPanel imagePanel;
    private JLabel imageLabel;

    /**
     * Constructeur du panel de catalogue
     *
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
        // Modèles et listes
        articleModel = new DefaultListModel<>();
        articleList = new JList<>(articleModel);
        articleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        articleList.setCellRenderer(new ArticleCellRenderer());

        marqueModel = new DefaultListModel<>();
        marqueList = new JList<>(marqueModel);
        marqueList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Recherche
        searchField = new JTextField(20);
        searchButton = new JButton("Rechercher");
        allArticlesButton = new JButton("Tous les articles");

        // Détails et actions
        articleDetailArea = new JTextArea(10, 30);
        articleDetailArea.setEditable(false);
        articleDetailArea.setLineWrap(true);
        articleDetailArea.setWrapStyleWord(true);

        // Spinner pour la quantité
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(DEFAULT_QUANTITY, 1, MAX_QUANTITY, 1);
        quantitySpinner = new JSpinner(spinnerModel);

        // Boutons d'action
        addToCartButton = new JButton("Ajouter au panier");
        addToCartButton.setEnabled(false);
        addToCartButton.setIcon(new ImageIcon("src/main/resources/img/cart.png"));

        viewCartButton = new JButton("Voir le panier");
        viewCartButton.setIcon(new ImageIcon("src/main/resources/img/cart.png"));

        // Panel d'image
        imagePanel = new JPanel(new BorderLayout());
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
    }

    /**
     * Configure la disposition des composants
     */
    private void setupLayout() {
        // Layout principal : BorderLayout
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel du haut pour la recherche et le titre
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));

        // Titre
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Catalogue des produits");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        topPanel.add(titlePanel, BorderLayout.NORTH);

        // Panel de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Recherche:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(allArticlesButton);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        // Panel central qui contiendra les 3 colonnes
        JPanel contentPanel = new JPanel(new GridLayout(1, 3, 10, 0));

        // Panel de gauche pour les marques
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(new TitledBorder("Marques"));

        // Ajout d'un JScrollPane pour la liste des marques
        JScrollPane marqueScrollPane = new JScrollPane(marqueList);
        marqueScrollPane.setPreferredSize(new Dimension(150, 0));
        leftPanel.add(marqueScrollPane, BorderLayout.CENTER);

        // Panel central pour les articles
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(new TitledBorder("Articles"));

        // Ajout d'un JScrollPane pour la liste des articles
        JScrollPane articleScrollPane = new JScrollPane(articleList);
        centerPanel.add(articleScrollPane, BorderLayout.CENTER);

        // Panel de droite pour les détails et actions
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setBorder(new TitledBorder("Détails"));

        // Panel d'informations
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(new JScrollPane(articleDetailArea), BorderLayout.CENTER);
        infoPanel.add(imagePanel, BorderLayout.SOUTH);

        // Panel d'actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actionPanel.add(new JLabel("Quantité:"));
        actionPanel.add(quantitySpinner);
        actionPanel.add(addToCartButton);
        actionPanel.add(viewCartButton);

        // Assemblage du panel droit
        rightPanel.add(infoPanel, BorderLayout.CENTER);
        rightPanel.add(actionPanel, BorderLayout.SOUTH);

        // Ajout des trois panels au panel central
        contentPanel.add(leftPanel);
        contentPanel.add(centerPanel);
        contentPanel.add(rightPanel);

        // Assemblage final
        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Configure les écouteurs d'événements
     */
    private void setupListeners() {
        // Recherche d'articles
        ActionListener searchAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText().trim();
                if (!searchTerm.isEmpty()) {
                    List<Article> articles = articleController.searchArticlesByName(searchTerm);
                    updateArticleList(articles);
                }
            }
        };

        searchButton.addActionListener(searchAction);

        // Recherche aussi en appuyant sur Entrée
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchButton.doClick();
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
                searchField.setText("");
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
                        imageLabel.setIcon(null);
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
     *
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
     *
     * @param article L'article à afficher
     */
    private void displayArticleDetails(Article article) {
        StringBuilder details = new StringBuilder();
        details.append("Nom: ").append(article.getNom()).append("\n\n");
        details.append("Description: ").append(article.getDescription()).append("\n\n");
        details.append("Prix unitaire: ").append(String.format("%.2f €", article.getPrixUnitaire())).append("\n");

        if (article.getPrixGros() != null && article.getQuantiteGros() != null) {
            details.append("Prix en gros: ").append(String.format("%.2f €", article.getPrixGros()))
                    .append(" pour ").append(article.getQuantiteGros())
                    .append(" unités\n");
        }

        details.append("\nStock disponible: ").append(article.getStock()).append(" unités\n");

        // Affichage de l'image si disponible
        if (article.getImageUrl() != null && !article.getImageUrl().isEmpty()) {
            File imageFile = new File("src/main/resources/img/" + article.getImageUrl());
            if (imageFile.exists()) {
                ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
                // Redimensionner l'image si elle est trop grande
                if (icon.getIconWidth() > 200) {
                    icon = new ImageIcon(icon.getImage().getScaledInstance(200, -1, Image.SCALE_SMOOTH));
                }
                imageLabel.setIcon(icon);
            } else {
                imageLabel.setIcon(null);
            }
        } else {
            imageLabel.setIcon(null);
        }

        articleDetailArea.setText(details.toString());
    }

    /**
     * Récupère le contrôleur de panier
     *
     * @return Le contrôleur de panier
     */
    public PanierController getPanierController() {
        return panierController;
    }

    /**
     * Renderer personnalisé pour afficher les articles dans la liste
     */
    private class ArticleCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);

            if (value instanceof Article) {
                Article article = (Article) value;
                label.setText(String.format("%s - %.2f €", article.getNom(), article.getPrixUnitaire()));

                // On peut ajouter une icône si nécessaire
                // label.setIcon(someIcon);
            }

            return label;
        }
    }
}