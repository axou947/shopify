package view;

import javax.swing.*;
import java.awt.*;

public class AdminView extends JFrame {
    private JTabbedPane tabbedPane;

    public AdminView() {
        setTitle("Administration - Système de Shopping");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        // Onglet Gestion des articles
        JPanel articlesPanel = new JPanel();
        articlesPanel.add(new JLabel("Gestion des articles"));
        tabbedPane.addTab("Articles", articlesPanel);

        // Onglet Gestion des clients
        JPanel clientsPanel = new JPanel();
        clientsPanel.add(new JLabel("Gestion des clients"));
        tabbedPane.addTab("Clients", clientsPanel);

        // Onglet Statistiques
        JPanel statsPanel = new JPanel();
        statsPanel.add(new JLabel("Statistiques de vente"));
        tabbedPane.addTab("Statistiques", statsPanel);

        add(tabbedPane);

        // Menu de déconnexion
        JMenuBar menuBar = new JMenuBar();
        JMenu adminMenu = new JMenu("Admin");
        JMenuItem logoutItem = new JMenuItem("Déconnexion");
        logoutItem.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });
        adminMenu.add(logoutItem);
        menuBar.add(adminMenu);
        setJMenuBar(menuBar);
    }
}