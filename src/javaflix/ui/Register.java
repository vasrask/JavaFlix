package javaflix.ui;

import javaflix.model.User;
import javaflix.model.UserCategory;
import javaflix.service.Users;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;

/**
 * Registration form for new subscribers.
 *
 * @author Vasiliki Raskopoulou
 */
public class Register extends JFrame {
    private JLabel validationLabel;
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    public Register() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("JavaFlix - Register");

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        "Create your account",
                        TitledBorder.LEADING,
                        TitledBorder.TOP
                ),
                new EmptyBorder(12, 14, 12, 14)
        ));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(6, 4, 6, 4);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField(22);
        emailField = new JTextField(22);
        passwordField = new JPasswordField(22);
        addFormRow(form, constraints, 0, "Username", nameField);
        addFormRow(form, constraints, 1, "Email", emailField);
        addFormRow(form, constraints, 2, "Password", passwordField);

        validationLabel = new JLabel(" ");
        validationLabel.setForeground(new Color(180, 45, 45));
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        form.add(validationLabel, constraints);
        addValidationClearListener(nameField);
        addValidationClearListener(emailField);
        addValidationClearListener(passwordField);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registerAction());
        JButton backButton = new JButton("Back to login");
        backButton.addActionListener(e -> {
            dispose();
            new LogIn();
        });

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(backButton);
        actions.add(registerButton);
        getRootPane().setDefaultButton(registerButton);

        add(form, BorderLayout.CENTER);
        add(actions, BorderLayout.PAGE_END);
        setSize(440, 280);
        setMinimumSize(new Dimension(400, 250));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addFormRow(JPanel form, GridBagConstraints constraints, int row, String label, JTextField field) {
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        form.add(new JLabel(label + ":"), constraints);
        constraints.gridx = 1;
        constraints.weightx = 1;
        form.add(field, constraints);
    }

    private void addValidationClearListener(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                clearValidation();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                clearValidation();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                clearValidation();
            }
        });
    }

    private void clearValidation() {
        validationLabel.setText(" ");
    }

    public void registerAction() {
        String username = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty()) {
            showValidation("Enter a username.", nameField);
            return;
        }
        if (email.isEmpty() || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            showValidation("Enter a valid email address.", emailField);
            return;
        }
        if (password.isEmpty()) {
            showValidation("Enter a password.", passwordField);
            return;
        }
        if (Users.existingUsername(username)) {
            showValidation("That username is already in use.", nameField);
            return;
        }

        Users.addUser(new User(username, email, password, UserCategory.SUBSCRIBER, new ArrayList<>()));
        dispose();
        new LogIn();
    }

    private void showValidation(String message, JTextField field) {
        validationLabel.setText(message);
        field.requestFocusInWindow();
        if (field instanceof JPasswordField) {
            field.selectAll();
        }
    }

    public static void main(String[] args) {
        new Register();
    }
}
