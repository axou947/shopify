package main.java.com.view.admin;

import main.java.com.dao.DAOFactory;
import main.java.com.model.Article;
import main.java.com.model.ArticleMarque;
import main.java.com.model.Remise;
import main.java.com.view.common.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Panel pour la gestion des remises
 */
public class GestionRemisesPanel extends JPanel {
    private MainFrame mainFrame;
    private List<Remise> remises;

    private JTable remiseTable;
    private RemiseTableModel remiseModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;

    /**
     * Constructeur du panel de gestion des remises
     * @param mainFrame La fenêtre principale
     */
    public GestionRemisesPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.remises = new ArrayList<>(); // Remplacer par un appel au DAO dans une implémentation réelle

        initComponents();
        setupLayout();
        setupListeners();
        loadData();
    }

    /**
     * Initialise les composants
     */
    private void initComponents() {
        remiseModel = new RemiseTableModel();
        remiseTable = new JTable(remiseModel);
        remiseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addButton = new JButton("Ajouter");
        editButton = new JButton("Modifier");
        deleteButton = new JButton("Supprimer");
        refreshButton = new JButton("Actualiser");
    }

    /**
     * Configure la disposition des composants
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel de titre
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Gestion des Remises");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Panel de table
        JScrollPane tableScrollPane = new JScrollPane(remiseTable);

        // Panel de boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Assemblage final
        add(titlePanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Configure les écouteurs d'événements
     */
    private void setupListeners() {
        // Actualisation
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });

        // Ajout d'une remise
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRemiseDialog(null);
            }
        });

        // Modification d'une remise
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = remiseTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Remise remise = remiseModel.getRemiseAt(selectedRow);
                    showRemiseDialog(remise);
                } else {
                    JOptionPane.showMessageDialog(GestionRemisesPanel.this,
                            "Veuillez sélectionner une remise à modifier.",
                            "Modification",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Suppression d'une remise
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = remiseTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Remise remise = remiseModel.getRemiseAt(selectedRow);

                    int response = JOptionPane.showConfirmDialog(GestionRemisesPanel.this,
                            "Êtes-vous sûr de vouloir supprimer la remise " + remise.getCode() + " ?",
                            "Confirmation",
                            JOptionPane.YES_NO_OPTION);

                    if (response == JOptionPane.YES_OPTION) {
                        // Dans une implémentation réelle, supprimer la remise de la base de données
                        remises.remove(selectedRow);
                        remiseModel.fireTableDataChanged();
                    }
                } else {
                    JOptionPane.showMessageDialog(GestionRemisesPanel.this,
                            "Veuillez sélectionner une remise à supprimer.",
                            "Suppression",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    /**
     * Charge les données
     */
    private void loadData() {
        // Normalement, charger depuis la base via DAO
        // Pour le test, on crée des remises fictives
        remises = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Remise remise1 = new Remise();
            remise1.setId(1);
            remise1.setCode("SUMMER10");
            remise1.setPourcentage(10.0);
            remise1.setDateDebut(dateFormat.parse("01/06/2025"));
            remise1.setDateFin(dateFormat.parse("31/08/2025"));
            remise1.setQuantiteMin(1);
            remises.add(remise1);

            Remise remise2 = new Remise();
            remise2.setId(2);
            remise2.setCode("BULK20");
            remise2.setPourcentage(20.0);
            remise2.setDateDebut(dateFormat.parse("01/01/2025"));
            remise2.setDateFin(dateFormat.parse("31/12/2025"));
            remise2.setQuantiteMin(5);
            remises.add(remise2);

            Remise remise3 = new Remise();
            remise3.setId(3);
            remise3.setCode("NEWCLIENT");
            remise3.setPourcentage(5.0);
            remise3.setDateDebut(dateFormat.parse("01/01/2025"));
            remise3.setDateFin(dateFormat.parse("31/12/2025"));
            remise3.setQuantiteMin(1);
            remises.add(remise3);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        remiseModel.setRemises(remises);
        remiseModel.fireTableDataChanged();
    }

    /**
     * Affiche la boîte de dialogue pour ajouter/modifier une remise
     * @param remise La remise à modifier ou null pour une nouvelle remise
     */
    private void showRemiseDialog(Remise remise) {
        boolean isEditing = (remise != null);

        // Création de la fenêtre de dialogue
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                isEditing ? "Modifier une remise" : "Ajouter une remise",
                true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        // Création du formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Champs du formulaire
        JTextField codeField = new JTextField(15);
        JTextField pourcentageField = new JTextField(15);
        JTextField montantFixeField = new JTextField(15);
        JFormattedTextField dateDebutField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        dateDebutField.setColumns(15);
        JFormattedTextField dateFinField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
        dateFinField.setColumns(15);
        JSpinner quantiteMinSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        // Pré-remplissage des champs si en mode édition
        if (isEditing) {
            codeField.setText(remise.getCode());
            pourcentageField.setText(String.valueOf(remise.getPourcentage()));
            montantFixeField.setText(String.valueOf(remise.getMontantFixe()));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            if (remise.getDateDebut() != null) {
                dateDebutField.setValue(dateFormat.format(remise.getDateDebut()));
            }
            if (remise.getDateFin() != null) {
                dateFinField.setValue(dateFormat.format(remise.getDateFin()));
            }
            quantiteMinSpinner.setValue(remise.getQuantiteMin());
        } else {
            // Valeurs par défaut pour une nouvelle remise
            pourcentageField.setText("0");
            montantFixeField.setText("0");
            dateDebutField.setValue(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

            // Date de fin par défaut = 3 mois plus tard
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 3);
            dateFinField.setValue(new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime()));
        }

        // Ajout des champs au formulaire
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Code:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(codeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Pourcentage (%):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(pourcentageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Montant fixe (€):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(montantFixeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Date début (jj/mm/aaaa):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(dateDebutField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Date fin (jj/mm/aaaa):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(dateFinField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Quantité minimale:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(quantiteMinSpinner, gbc);

        // Boutons
        JButton okButton = new JButton(isEditing ? "Modifier" : "Ajouter");
        JButton cancelButton = new JButton("Annuler");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Gestionnaires d'événements pour les boutons
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateForm(codeField.getText())) {
                    try {
                        // Création ou mise à jour de la remise
                        Remise remiseToSave = isEditing ? remise : new Remise();
                        remiseToSave.setCode(codeField.getText());
                        remiseToSave.setPourcentage(Double.parseDouble(pourcentageField.getText()));
                        remiseToSave.setMontantFixe(Double.parseDouble(montantFixeField.getText()));

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        remiseToSave.setDateDebut(dateFormat.parse(dateDebutField.getText()));
                        remiseToSave.setDateFin(dateFormat.parse(dateFinField.getText()));

                        remiseToSave.setQuantiteMin((Integer) quantiteMinSpinner.getValue());

                        if (!isEditing) {
                            // Générer un ID pour la nouvelle remise (dans une impl. réelle, ce serait fait par la BD)
                            remiseToSave.setId(remises.size() + 1);
                            remises.add(remiseToSave);
                        }

                        remiseModel.fireTableDataChanged();
                        dialog.dispose();
                    } catch (ParseException | NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog,
                                "Erreur de format dans les champs. Veuillez vérifier les valeurs entrées.",
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        // Assemblage final
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(contentPanel);
        dialog.setVisible(true);
    }

    /**
     * Valide le formulaire de remise
     * @param code Code de la remise
     * @return true si le formulaire est valide, false sinon
     */
    private boolean validateForm(String code) {
        if (code == null || code.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Le code de remise est obligatoire.",
                    "Erreur de validation",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Modèle de table pour les remises
     */
    private class RemiseTableModel extends AbstractTableModel {
        private final String[] columnNames = {"ID", "Code", "Pourcentage", "Montant fixe", "Date début", "Date fin", "Quantité min"};
        private List<Remise> remises;

        public RemiseTableModel() {
            this.remises = new ArrayList<>();
        }

        public void setRemises(List<Remise> remises) {
            this.remises = remises;
        }

        public Remise getRemiseAt(int rowIndex) {
            if (rowIndex >= 0 && rowIndex < remises.size()) {
                return remises.get(rowIndex);
            }
            return null;
        }

        @Override
        public int getRowCount() {
            return remises.size();
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
            if (rowIndex >= 0 && rowIndex < remises.size()) {
                Remise remise = remises.get(rowIndex);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                switch (columnIndex) {
                    case 0: // ID
                        return remise.getId();
                    case 1: // Code
                        return remise.getCode();
                    case 2: // Pourcentage
                        return remise.getPourcentage() + " %";
                    case 3: // Montant fixe
                        return remise.getMontantFixe() + " €";
                    case 4: // Date début
                        return remise.getDateDebut() != null ? dateFormat.format(remise.getDateDebut()) : "-";
                    case 5: // Date fin
                        return remise.getDateFin() != null ? dateFormat.format(remise.getDateFin()) : "-";
                    case 6: // Quantité min
                        return remise.getQuantiteMin();
                    default:
                        return null;
                }
            }
            return null;
        }
    }
}