package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import model.Article;

public class PanierView extends JPanel {
    private JTable cartTable;
    private JLabel totalLabel;
    private List<Article> cartItems;

    public PanierView() {
        setLayout(new BorderLayout());
        cartItems = new ArrayList<>();

        // Simuler des articles dans le panier
        cartItems.add(new Article(1, "Briquet", "", 0.50, 12, 4.00, 10, "Bic"));
        cartItems.add(new Article(3, "Stylo", "", 1.20, 5, 10.00, 10, "Bic"));

        // Panel du haut avec titre
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Mon Panier", JLabel.LEFT));
        add(topPanel, BorderLayout.NORTH);

        // Tableau des articles du panier
        String[] columnNames = {"Article", "Marque", "Quantité", "Prix unitaire", "Total"};
        Object[][] data = new Object[cartItems.size()][5];

        for (int i = 0; i < cartItems.size(); i++) {
            Article item = cartItems.get(i);
            data[i][0] = item.getNom();
            data[i][1] = item.getMarque();
            data[i][2] = item.getQuantiteStock(); // Ici, quantiteStock représente la quantité dans le panier
            data[i][3] = String.format("%.2f€", item.getPrixUnitaire());

            // Calcul du prix avec remise en gros si applicable
            double total = calculateItemTotal(item);
            data[i][4] = String.format("%.2f€", total);
        }

        cartTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(cartTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel du bas avec total et boutons
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Calcul du total
        double total = cartItems.stream()
                .mapToDouble(this::calculateItemTotal)
                .sum();

        totalLabel = new JLabel("Total: " + String.format("%.2f€", total), JLabel.RIGHT);
        bottomPanel.add(totalLabel, BorderLayout.NORTH);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton removeButton = new JButton("Supprimer");
        removeButton.addActionListener(e -> {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow >= 0) {
                cartItems.remove(selectedRow);
                updateCartDisplay();
            }
        });

        JButton checkoutButton = new JButton("Passer la commande");
        checkoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Commande passée avec succès!",
                    "Confirmation", JOptionPane.INFORMATION_MESSAGE);
        });

        buttonPanel.add(removeButton);
        buttonPanel.add(checkoutButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private double calculateItemTotal(Article item) {
        int quantity = item.getQuantiteStock(); // Quantité dans le panier
        double unitPrice = item.getPrixUnitaire();
        double bulkPrice = item.getPrixVenteEnGros();
        int bulkQuantity = item.getQuantitePourVenteGros();

        if (bulkPrice > 0 && quantity >= bulkQuantity) {
            int bulkSets = quantity / bulkQuantity;
            int remainder = quantity % bulkQuantity;
            return (bulkSets * bulkPrice) + (remainder * unitPrice);
        }
        return quantity * unitPrice;
    }

    private void updateCartDisplay() {
        // Mise à jour de l'affichage du panier
        // (implémentation similaire au constructeur)
    }
}