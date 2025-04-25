package main.java.com.view.common;

import main.java.com.controller.LoginController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Panel de connexion pour l'application
 * Version améliorée suivant les principes du cours 7
 */
public class LoginPanel extends JPanel implements ActionListener {
    // Contrôleur
    private LoginController loginController;

    // Référence à la fenêtre principale
    private MainFrame mainFrame;

    // Composants graphiques
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel statusLabel;

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
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
    }

    /**
     * Configure la disposition des composants en utilisant GridBagLayout
     * pour un meilleur contrôle du positionnement
     */
    private void setupLayout() {
        // Utilisation d'un BorderLayout comme layout principal
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel de titre avec une bordure esthétique
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        JLabel titleLabel = new JLabel("Shopping App - Connexion");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Panel de formulaire avec GridBagLayout pour un alignement précis
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new TitledBorder("Identifiants"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setDisplayedMnemonic('E');  // Raccourci clavier Alt+E
        emailLabel.setLabelFor(emailField);    // Association du raccourci au champ
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(emailField, gbc);

        // Mot de passe
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordLabel.setDisplayedMnemonic('P');  // Raccourci clavier Alt+P
        passwordLabel.setLabelFor(passwordField); // Association du raccourci au champ
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(passwordField, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(statusLabel, gbc);

        // Panel de boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Assemblage des panels dans le panel principal avec BorderLayout
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Configure les écouteurs d'événements
     */
    private void setupListeners() {
        // Utilisation de "this" comme ActionListener puisque la classe implémente ActionListener
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);

        // Listener pour permettre la connexion en appuyant sur Entrée dans les champs
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    attemptLogin();
                }
            }
        };

        emailField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
    }

    /**
     * Gestion des événements d'action (implémentation de ActionListener)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            attemptLogin();
        } else if (e.getSource() == registerButton) {
            mainFrame.showRegistrationPanel();
        }
    }

    /**
     * Tente de connecter l'utilisateur
     */
    private void attemptLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        // Validation basique côté client
        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Veuillez remplir tous les champs");
            return;
        }

        // Tentative d'authentification
        int userId = loginController.authenticate(email, password);
        if (userId > 0) {
            String userType = loginController.getUserType(userId);
            mainFrame.onLoginSuccess(userId, userType);

            // Réinitialisation des champs et du statut
            emailField.setText("");
            passwordField.setText("");
            statusLabel.setText(" ");
        } else {
            statusLabel.setText("Email ou mot de passe incorrect");
            passwordField.setText("");  // Effacement du mot de passe par sécurité
            passwordField.requestFocus();  // Focus sur le champ mot de passe
        }
    }
}