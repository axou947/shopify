package main.java.com.view.client;

import main.java.com.controller.ClientController;
import main.java.com.view.common.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel d'inscription pour les nouveaux clients
 */
public class InscriptionPanel extends JPanel {
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField adresseField;
    private JTextField telephoneField;
    private JButton inscriptionButton;
    private JButton annulerButton;

    private ClientController clientController;
    private MainFrame mainFrame;

    /**
     * Constructeur du panel d'inscription
     * @param mainFrame La fenêtre principale
     */
    public InscriptionPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.clientController = new ClientController();
        initComponents();
        setupLayout();
        setupListeners();
    }

    /**
     * Initialise les composants
     */
    private void initComponents() {
        nomField = new JTextField(20);
        prenomField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        adresseField = new JTextField(20);
        telephoneField = new JTextField(20);
        inscriptionButton = new JButton("S'inscrire");
        annulerButton = new JButton("Annuler");
    }

    /**
     * Configure la disposition des composants
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        // Panel de titre
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Inscription");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Panel de formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Nom
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nom:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(nomField, gbc);

        // Prénom
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Prénom:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(prenomField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(emailField, gbc);

        // Mot de passe
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Mot de passe:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(passwordField, gbc);

        // Confirmation mot de passe
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Confirmer mot de passe:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(confirmPasswordField, gbc);

        // Adresse
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Adresse:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(adresseField, gbc);

        // Téléphone
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Téléphone:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(telephoneField, gbc);

        // Panel de boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(inscriptionButton);
        buttonPanel.add(annulerButton);

        // Ajout des panels au panel principal
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Configure les écouteurs d'événements
     */
    private void setupListeners() {
        inscriptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateForm()) {
                    String nom = nomField.getText();
                    String prenom = prenomField.getText();
                    String email = emailField.getText();
                    String password = new String(passwordField.getPassword());
                    String adresse = adresseField.getText();
                    String telephone = telephoneField.getText();

                    int clientId = clientController.inscrireClient(email, password, nom, prenom, adresse, telephone);
                    if (clientId > 0) {
                        JOptionPane.showMessageDialog(InscriptionPanel.this,
                                "Inscription réussie. Vous pouvez maintenant vous connecter.",
                                "Inscription",
                                JOptionPane.INFORMATION_MESSAGE);
                        mainFrame.logout(); // Retour à l'écran de connexion
                    } else {
                        JOptionPane.showMessageDialog(InscriptionPanel.this,
                                "Erreur lors de l'inscription. Cet email est peut-être déjà utilisé.",
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        annulerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.logout(); // Retour à l'écran de connexion
            }
        });
    }

    /**
     * Valide le formulaire d'inscription
     * @return true si le formulaire est valide, false sinon
     */
    private boolean validateForm() {
        // Vérifie que tous les champs obligatoires sont remplis
        if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() ||
                emailField.getText().isEmpty() || passwordField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez remplir tous les champs obligatoires (Nom, Prénom, Email, Mot de passe)",
                    "Erreur de validation",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Vérifie que l'email est valide
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!emailField.getText().matches(emailRegex)) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez entrer une adresse email valide",
                    "Erreur de validation",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Vérifie que les mots de passe correspondent
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Les mots de passe ne correspondent pas",
                    "Erreur de validation",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Vérifie que le mot de passe a une longueur minimale
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this,
                    "Le mot de passe doit contenir au moins 6 caractères",
                    "Erreur de validation",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}