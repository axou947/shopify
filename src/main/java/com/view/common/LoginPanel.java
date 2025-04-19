package main.java.com.view.common;

import main.java.com.controller.LoginController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel de connexion pour l'application
 */
public class LoginPanel extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private LoginController loginController;
    private MainFrame mainFrame;

    /**
     * Constructeur du panel de connexion
     * @param mainFrame La fenêtre principale
     */
    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.loginController = new LoginController();
        initComponents();
        setupLayout();
        setupListeners();
    }

    /**
     * Initialise les composants
     */
    private void initComponents() {
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Se connecter");
        registerButton = new JButton("S'inscrire");
    }

    /**
     * Configure la disposition des composants
     */
    private void setupLayout() {
        setLayout(new BorderLayout());

        // Panel de titre
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Shopping App - Connexion");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Panel de formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(emailField, gbc);

        // Mot de passe
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Mot de passe:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(passwordField, gbc);

        // Panel de boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Ajout des panels au panel principal
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Configure les écouteurs d'événements
     */
    private void setupListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginPanel.this,
                            "Veuillez remplir tous les champs",
                            "Erreur de connexion",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int userId = loginController.authenticate(email, password);
                if (userId > 0) {
                    String userType = loginController.getUserType(userId);
                    mainFrame.onLoginSuccess(userId, userType);
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this,
                            "Email ou mot de passe incorrect",
                            "Erreur de connexion",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showRegistrationPanel();
            }
        });
    }
}