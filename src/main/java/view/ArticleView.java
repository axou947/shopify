package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import model.Article;

public class ArticleView extends JPanel {
    private JPanel articlesPanel;
    private List<Article> articles;

    public ArticleView() {
        setLayout(new BorderLayout());

        // Simuler des données d'articles
        articles = new ArrayList<>();
        articles.add(new Article(1, "Briquet", "Briquet jetable", 0.50, 100, 4.00, 10, "Bic"));
        articles.add(new Article(2, "Cahier", "Cahier 96 pages", 3.50, 50, 30.00, 10, "Oxford"));
        articles.add(new Article(3, "Stylo", "Stylo bille bleu", 1.20, 200, 10.00, 10, "Bic"));
        articles.add(new Article(4, "Gomme", "Gomme blanche", 0.80, 150, 6.00, 10, "Maped"));
        articles.add(new Article(5, "Règle", "Règle 30cm", 1.50, 80, 12.00, 10, "Maped"));
        articles.add(new Article(6, "Trousse", "Trousse scolaire", 5.00, 30, 45.00, 10, "Eastpak"));

        // Panel de recherche
        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Rechercher");
        searchPanel.add(new JLabel("Rechercher:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Panel des articles
        articlesPanel = new JPanel();
        articlesPanel.setLayout(new GridLayout(0, 3, 10, 10));
        updateArticlesDisplay(articles);

        JScrollPane scrollPane = new JScrollPane(articlesPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void updateArticlesDisplay(List<Article> articlesToDisplay) {
        articlesPanel.removeAll();

        for (Article article : articlesToDisplay) {
            JPanel cardPanel = createArticleCard(article);
            articlesPanel.add(cardPanel);
        }

        articlesPanel.revalidate();
        articlesPanel.repaint();
    }

    private JPanel createArticleCard(Article article) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setPreferredSize(new Dimension(200, 250));

        // Image de l'article (simulée)
        JLabel imageLabel = new JLabel(new ImageIcon(
                new ImageIcon("src/main/resources/images/products.png")
                        .getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        card.add(imageLabel, BorderLayout.NORTH);

        // Détails de l'article
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1));
        detailsPanel.add(new JLabel(article.getNom(), JLabel.CENTER));
        detailsPanel.add(new JLabel("Marque: " + article.getMarque()));
        detailsPanel.add(new JLabel("Prix: " + article.getPrixUnitaire() + "€"));

        if (article.getPrixVenteEnGros() > 0) {
            detailsPanel.add(new JLabel("En gros: " + article.getQuantitePourVenteGros() +
                    " pour " + article.getPrixVenteEnGros() + "€"));
        }

        card.add(detailsPanel, BorderLayout.CENTER);

        // Bouton d'ajout au panier
        JButton addButton = new JButton("Ajouter au panier");
        addButton.addActionListener(e -> {
            // Logique d'ajout au panier
            JOptionPane.showMessageDialog(this,
                    article.getNom() + " ajouté au panier!",
                    "Panier", JOptionPane.INFORMATION_MESSAGE);
        });

        card.add(addButton, BorderLayout.SOUTH);
        return card;
    }
}