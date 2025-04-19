package view;

import controller.ClientController;
import model.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private ClientController clientController;

    public LoginView() {
        this.clientController = new ClientController();
        initUI();
    }

    private void initUI() {
        setTitle("Connexion - Système de Shopping");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Titre
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(new JLabel("Connexion au système"), gbc);

        // Email
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);

        // Mot de passe
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel("Mot de passe:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        // Boutons
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Se connecter");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                Client client = clientController.authentifierClient(email, password);
                if (client != null) {
                    JOptionPane.showMessageDialog(LoginView.this, "Connexion réussie!");
                    // Ouvrir la vue appropriée selon le type d'utilisateur
                    dispose();
                    new ClientView(client).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(LoginView.this,
                            "Email ou mot de passe incorrect", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(loginButton, gbc);

        // Lien d'inscription
        gbc.gridy = 4;
        JButton registerButton = new JButton("Créer un nouveau compte");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegistrationView().setVisible(true);
            }
        });
        panel.add(registerButton, gbc);

        add(panel);
    }
}