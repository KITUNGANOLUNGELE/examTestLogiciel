package com.master.project.application;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;

import com.master.project.accessdata.MySQLPersonDAO;
import com.master.project.model.Person;
import com.master.project.model.PersonDAO;
import com.master.project.service.PersonService;

public class PersonGUI extends JFrame {
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField firstnameField, lastnameField, ageField, emailField, searchField;
    private JPasswordField passwordField;
    private JButton addButton, updateButton, deleteButton, clearButton;
    private JTable personTable;
    private DefaultTableModel tableModel;
    private PersonService service;
    private int selectedPersonId = -1;
    private JProgressBar passwordStrengthBar;

    public PersonGUI() {
        setTitle("👥 Person Management System");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ---------- INITIALISATION DU SERVICE ----------
        try {
            PersonDAO dao = new MySQLPersonDAO();
            service = new PersonService(dao);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error initializing DAO: " + e.getMessage());
            return;
        }

        // ---------- LAYOUT GLOBAL ----------
        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(new EmptyBorder(15, 15, 15, 15));
        content.setBackground(new Color(245, 247, 250));

        // ---------- FORMULAIRE ----------
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("User Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        firstnameField = createField(formPanel, gbc, "First Name:", 0);
        lastnameField = createField(formPanel, gbc, "Last Name:", 1);
        ageField = createField(formPanel, gbc, "Age:", 2);
        emailField = createField(formPanel, gbc, "Email:", 3);

        JLabel passLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(passLabel, gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Barre de force du mot de passe
        passwordStrengthBar = new JProgressBar(0, 5);
        passwordStrengthBar.setPreferredSize(new Dimension(200, 8));
        passwordStrengthBar.setForeground(Color.RED);
        gbc.gridy = 5;
        formPanel.add(passwordStrengthBar, gbc);

        // Mise à jour dynamique de la force du mot de passe
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                int strength = getPasswordStrength(new String(passwordField.getPassword()));
                passwordStrengthBar.setValue(strength);
                if (strength <= 2) passwordStrengthBar.setForeground(Color.RED);
                else if (strength == 3) passwordStrengthBar.setForeground(Color.ORANGE);
                else passwordStrengthBar.setForeground(new Color(76, 175, 80));
            }
        });

        // ---------- BOUTONS ----------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);

        addButton = createStyledButton("➕ Add", new Color(76, 175, 80));
        updateButton = createStyledButton("✏️ Update", new Color(33, 150, 243));
        deleteButton = createStyledButton("🗑️ Delete", new Color(244, 67, 54));
        clearButton = createStyledButton("🔄 Clear", new Color(158, 158, 158));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // ---------- TABLE ----------
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Registered Persons"));

        // Barre de recherche
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        JLabel searchLabel = new JLabel("🔍 Search:");
        searchField = new JTextField();
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        tableModel = new DefaultTableModel(new String[]{"ID", "First", "Last", "Age", "Email"}, 0);
        personTable = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        personTable.setRowSorter(sorter);
        personTable.setRowHeight(25);
        personTable.setSelectionBackground(new Color(220, 235, 255));
        JScrollPane scrollPane = new JScrollPane(personTable);
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // ---------- ASSEMBLAGE ----------
        content.add(formPanel, BorderLayout.NORTH);
        content.add(tablePanel, BorderLayout.CENTER);
        add(content);

        // ---------- ÉVÉNEMENTS ----------
        addButton.addActionListener(e -> addPerson());
        updateButton.addActionListener(e -> updatePerson());
        deleteButton.addActionListener(e -> deletePerson());
        clearButton.addActionListener(e -> clearFields());

        personTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { tableClicked(); }
        });

        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { filterTable(); }
        });

        // ---------- CHARGEMENT INITIAL ----------
        SwingUtilities.invokeLater(() -> {
            refreshTable();
            JOptionPane.showMessageDialog(this, "✅ Persons loaded successfully!");
        });
    }

    // --- Méthode pour créer un champ texte ---
    private JTextField createField(JPanel panel, GridBagConstraints gbc, String label, int y) {
        JLabel jLabel = new JLabel(label);
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(jLabel, gbc);
        JTextField field = new JTextField(20);
        gbc.gridx = 1;
        panel.add(field, gbc);
        return field;
    }

    // --- Bouton stylisé ---
    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --- Ajouter une personne ---
    private void addPerson() {
        try {
            String first = firstnameField.getText().trim();
            String last = lastnameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            String email = emailField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();

            if (first.isEmpty() || last.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ All fields must be filled!");
                return;
            }
            if (getPasswordStrength(pass) < 3) {
                JOptionPane.showMessageDialog(this, "🔒 Password too weak!");
                return;
            }

            Person p = new Person(first, last, age, email, pass);
            service.create(p);
            JOptionPane.showMessageDialog(this, "✅ Person added successfully!");
            clearFields();
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Age must be a number!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
        }
    }

    // --- Mettre à jour une personne ---
    private void updatePerson() {
        if (selectedPersonId <= 0) {
            JOptionPane.showMessageDialog(this, "⚠️ Select a person first!");
            return;
        }
        try {
            String first = firstnameField.getText().trim();
            String last = lastnameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            String email = emailField.getText().trim();
            String pass = new String(passwordField.getPassword()).trim();

            Person p = new Person(first, last, age, email, pass);
            p.setId(selectedPersonId);
            service.edit(p);
            JOptionPane.showMessageDialog(this, "✅ Person updated!");
            clearFields();
            refreshTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
        }
    }

    // --- Supprimer une personne ---
    private void deletePerson() {
        if (selectedPersonId <= 0) {
            JOptionPane.showMessageDialog(this, "⚠️ Select a person first!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this person?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.remove(selectedPersonId);
                JOptionPane.showMessageDialog(this, "🗑️ Person deleted!");
                clearFields();
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        }
    }

    // --- Lorsqu’on clique sur une ligne du tableau ---
    private void tableClicked() {
        int row = personTable.getSelectedRow();
        if (row >= 0) {
            selectedPersonId = (int) tableModel.getValueAt(row, 0);
            firstnameField.setText(tableModel.getValueAt(row, 1).toString());
            lastnameField.setText(tableModel.getValueAt(row, 2).toString());
            ageField.setText(tableModel.getValueAt(row, 3).toString());
            emailField.setText(tableModel.getValueAt(row, 4).toString());
            passwordField.setText("");
        }
    }

    // --- Actualiser la table ---
    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<Person> list = service.listPersons();
            for (Person p : list) {
                tableModel.addRow(new Object[]{p.getId(), p.getFirstName(), p.getLastName(), p.getAge(), p.getEmail()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error loading data: " + e.getMessage());
        }
    }

    // --- Filtrer la table (recherche) ---
    private void filterTable() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            sorter.setRowFilter(null); // réinitialise
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query)); // (?i) = insensible à la casse
        }
    }


    private void clearFields() {
        firstnameField.setText("");
        lastnameField.setText("");
        ageField.setText("");
        emailField.setText("");
        passwordField.setText("");
        passwordStrengthBar.setValue(0);
        selectedPersonId = -1;
    }

    private int getPasswordStrength(String password) {
        int strength = 0;
        if (password.length() >= 8) strength++;
        if (password.matches(".*[A-Z].*")) strength++;
        if (password.matches(".*[a-z].*")) strength++;
        if (password.matches(".*[0-9].*")) strength++;
        if (password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) strength++;
        return strength;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PersonGUI().setVisible(true));
    }
}
