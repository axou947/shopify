package main.java.com.view.admin;

import main.java.com.controller.ClientController;
import main.java.com.model.Client;
import main.java.com.model.Utilisateur;
import main.java.com.view.common.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Panel pour la gestion des clients
 */
public class GestionClientsPanel extends JPanel {
    private MainFrame mainFrame;
    private ClientController clientController;

    private JTable clientTable;
    private ClientTableModel clientModel;
    private JButton refreshButton;
    private JButton changeStatusButton;
    private JTextField searchField;
    private JButton searchButton;
    private JTextArea detailsArea;

    /**
     * Constructeur du panel de gestion des clients
     * @param mainFrame La fenêtre principale
     */
    public GestionClientsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.clientController = new ClientController();

        initComponents();
        setupLayout();
        setupListeners();
        loadData();
    }

    /**
     * Initialise les composants
     */
    private void initComponents() {
        clientModel = new ClientTableModel();
        clientTable = new JTable(clientModel);
        clientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        refreshButton = new JButton("Actualiser");
        changeStatusButton = new JButton("Changer statut");
        searchField = new JTextField(20);
        searchButton = new JButton("Rechercher");

        detailsArea = new JTextArea(10, 30);
        detailsArea.setEditable(false);
    }

    /**
     * Configure la disposition des composants
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel de titre
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Gestion des Clients");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Panel de recherche
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Rechercher:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Panel supérieur
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        // Panel central avec table et détails
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        // Table des clients
        JScrollPane tableScrollPane = new JScrollPane(clientTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Liste des clients"));
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Zone de détails
        JScrollPane detailsScrollPane = new JScrollPane(detailsArea);
        detailsScrollPane.setBorder(BorderFactory.createTitledBorder("Détails du client"));
        centerPanel.add(detailsScrollPane, BorderLayout.SOUTH);

        // Panel de boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(changeStatusButton);

        // Assemblage final
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Configure les écouteurs d'événements
     */
    private void setupListeners() {
        // Actualisation de la liste
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });

        // Changement de statut
        changeStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = clientTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Client client = clientModel.getClientAt(selectedRow);
                    String currentStatus = client.getStatut();
                    String newStatus = "nouveau".equals(currentStatus) ? "ancien" : "nouveau";

                    int response = JOptionPane.showConfirmDialog(GestionClientsPanel.this,
                            "Voulez-vous changer le statut de ce client de \"" + currentStatus + "\" à \"" + newStatus + "\" ?",
                            "Changement de statut",
                            JOptionPane.YES_NO_OPTION);

                    if (response == JOptionPane.YES_OPTION) {
                        if (clientController.updateClientStatus(client.getId(), newStatus)) {
                            loadData();
                        } else {
                            JOptionPane.showMessageDialog(GestionClientsPanel.this,
                                    "Erreur lors de la mise à jour du statut.",
                                    "Erreur",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(GestionClientsPanel.this,
                            "Veuillez sélectionner un client.",
                            "Aucune sélection",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Sélection d'un client
        clientTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = clientTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Client client = clientModel.getClientAt(selectedRow);
                    displayClientDetails(client);
                }
            }
        });
    }

    /**
     * Charge les données
     */
    private void loadData() {
        List<Client> clients = clientController.getAllClients();
        clientModel.setClients(clients);
        clientModel.fireTableDataChanged();

        // Vide la zone de détails
        detailsArea.setText("");
    }

    /**
     * Affiche les détails d'un client
     * @param client Le client à afficher
     */
    private void displayClientDetails(Client client) {
        if (client == null) {
            detailsArea.setText("");
            return;
        }

        // Récupère les informations de l'utilisateur associé
        Utilisateur utilisateur = clientController.getUtilisateurByClientId(client.getId());
        if (utilisateur == null) {
            detailsArea.setText("Impossible de récupérer les informations de l'utilisateur.");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder details = new StringBuilder();

        details.append("ID: ").append(client.getId()).append("\n");
        details.append("Statut: ").append(client.getStatut()).append("\n\n");
        details.append("Nom: ").append(utilisateur.getNom()).append("\n");
        details.append("Prénom: ").append(utilisateur.getPrenom()).append("\n");
        details.append("Email: ").append(utilisateur.getEmail()).append("\n");
        details.append("Adresse: ").append(utilisateur.getAdresse() != null ? utilisateur.getAdresse() : "Non renseignée").append("\n");
        details.append("Téléphone: ").append(utilisateur.getTelephone() != null ? utilisateur.getTelephone() : "Non renseigné").append("\n");
        details.append("Date d'inscription: ").append(dateFormat.format(utilisateur.getDateInscription())).append("\n");

        detailsArea.setText(details.toString());
    }

    /**
     * Modèle de table pour les clients
     */
    private class ClientTableModel extends AbstractTableModel {
        private final String[] columnNames = {"ID", "Nom", "Prénom", "Email", "Téléphone", "Statut", "Date d'inscription"};
        private List<Client> clients;
        private List<Utilisateur> utilisateurs;

        public ClientTableModel() {
            this.clients = new ArrayList<>();
            this.utilisateurs = new ArrayList<>();
        }

        public void setClients(List<Client> clients) {
            this.clients = clients;
            this.utilisateurs = new ArrayList<>();

            // Récupère les informations des utilisateurs associés
            for (Client client : clients) {
                Utilisateur utilisateur = clientController.getUtilisateurByClientId(client.getId());
                utilisateurs.add(utilisateur);
            }
        }

        public Client getClientAt(int rowIndex) {
            if (rowIndex >= 0 && rowIndex < clients.size()) {
                return clients.get(rowIndex);
            }
            return null;
        }

        @Override
        public int getRowCount() {
            return clients.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex >= 0 && rowIndex < clients.size() && rowIndex < utilisateurs.size()) {
                Client client = clients.get(rowIndex);
                Utilisateur utilisateur = utilisateurs.get(rowIndex);

                // Si l'utilisateur n'a pas été trouvé
                if (utilisateur == null) {
                    if (columnIndex == 0) {
                        return client.getId();
                    } else if (columnIndex == 5) {
                        return client.getStatut();
                    } else {
                        return "N/A";
                    }
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                switch (columnIndex) {
                    case 0: // ID
                        return client.getId();
                    case 1: // Nom
                        return utilisateur.getNom();
                    case 2: // Prénom
                        return utilisateur.getPrenom();
                    case 3: // Email
                        return utilisateur.getEmail();
                    case 4: // Téléphone
                        return utilisateur.getTelephone() != null ? utilisateur.getTelephone() : "-";
                    case 5: // Statut
                        return client.getStatut();
                    case 6: // Date d'inscription
                        return utilisateur.getDateInscription() != null ?
                                dateFormat.format(utilisateur.getDateInscription()) : "-";
                    default:
                        return null;
                }
            }
            return null;
        }
    }
}