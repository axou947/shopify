package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public MainView() {
        setTitle("Application Shopping");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Création du menu
        JMenuBar menuBar = new JMenuBar();

        // Menu Articles
        JMenu articlesMenu = new JMenu("Articles");
        JMenuItem viewArticlesItem = new JMenuItem("Voir les articles");
        viewArticlesItem.addActionListener(e -> showArticlesView());
        articlesMenu.add(viewArticlesItem);

        // Menu Panier
        JMenu cartMenu = new JMenu("Panier");
        JMenuItem viewCartItem = new JMenuItem("Voir mon panier");
        viewCartItem.addActionListener(e -> showCartView());
        cartMenu.add(viewCartItem);

        // Menu Compte
        JMenu accountMenu = new JMenu("Mon Compte");
        JMenuItem logoutItem = new JMenuItem("Déconnexion");
        logoutItem.addActionListener(e -> logout());
        accountMenu.add(logoutItem);

        menuBar.add(articlesMenu);
        menuBar.add(cartMenu);
        menuBar.add(accountMenu);
        setJMenuBar(menuBar);

        // Panel principal avec CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Ajout des différentes vues
        mainPanel.add(new ArticleView(), "ARTICLES");
        mainPanel.add(new PanierView(), "PANIER");

        add(mainPanel);
        showArticlesView(); // Afficher la vue articles par défaut
    }

    private void showArticlesView() {
        cardLayout.show(mainPanel, "ARTICLES");
    }

    private void showCartView() {
        cardLayout.show(mainPanel, "PANIER");
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Êtes-vous sûr de vouloir vous déconnecter?",
                "Déconnexion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginView().setVisible(true);
        }
    }
}