package com.master.project.application;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.master.project.accessdata.MySQLPersonDAO;
import com.master.project.model.Person;
import com.master.project.model.PersonDAO;
import com.master.project.service.PersonService;

public class PersonInsertGUI extends JFrame {
    private JTextField firstnameField, lastnameField, ageField, emailField, passwordField;
    private JButton submitButton;
    private PersonService service;

    public PersonInsertGUI() {
        setTitle("Insert Person");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            PersonDAO dao = new MySQLPersonDAO();
            service = new PersonService(dao);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error initializing DAO: " + e.getMessage());
            return;
        }

        // Panel principal avec padding
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels et champs
        firstnameField = createField(panel, gbc, "First Name:", 0);
        lastnameField = createField(panel, gbc, "Last Name:", 1);
        ageField = createField(panel, gbc, "Age:", 2);
        emailField = createField(panel, gbc, "Email:", 3);
        passwordField = createField(panel, gbc, "Password:", 4);

        // Bouton
        submitButton = new JButton("Insert Person");
        submitButton.setBackground(new Color(66, 133, 244));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(submitButton, gbc);

        add(panel);

        // Action du bouton
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertPerson();
            }
        });
    }

    // Méthode pour créer un champ avec label
    private JTextField createField(JPanel panel, GridBagConstraints gbc, String labelText, int yPos) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = yPos;
        gbc.gridwidth = 1;
        panel.add(label, gbc);

        JTextField field = new JTextField(20);
        gbc.gridx = 1;
        panel.add(field, gbc);

        return field;
    }

    // Méthode pour insérer une personne
    private void insertPerson() {
        try {
            String first = firstnameField.getText().trim();
            String last = lastnameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();

            Person person = new Person(first, last, age, email, password);
            service.create(person);

            JOptionPane.showMessageDialog(this, "Person inserted successfully! ID: " + person.getId());

            // Clear fields
            firstnameField.setText("");
            lastnameField.setText("");
            ageField.setText("");
            emailField.setText("");
            passwordField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number.", "Input Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PersonInsertGUI().setVisible(true);
        });
    }
}
