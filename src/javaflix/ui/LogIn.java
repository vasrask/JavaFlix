
package javaflix.ui;

import javaflix.model.User;
import javaflix.model.UserCategory;
import javaflix.service.Users;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Vasiliki Raskopoulou
 */
public class LogIn extends JFrame {

    private JLabel failedLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LogIn() {
        setTitle("JavaFlix");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 200);
        setResizable(true);

        JPanel panel1 = new JPanel();
        GridLayout layout = new GridLayout(6, 1, 21, 2);
        panel1.setLayout(layout);
        TitledBorder border = BorderFactory.createTitledBorder(" Login ");
        panel1.setBorder(border);


        JLabel usernameLabel = new JLabel("Username:");
        panel1.add(usernameLabel);
        usernameField = new JTextField();
        usernameField.addActionListener(e -> loginCheck());
        usernameField.getDocument().addDocumentListener(createFeedbackListener());
        panel1.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        panel1.add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.addActionListener(e -> loginCheck());
        passwordField.getDocument().addDocumentListener(createFeedbackListener());
        panel1.add(passwordField);

        failedLabel = new JLabel(" ");
        failedLabel.setForeground(new Color(180, 45, 45));
        failedLabel.setVisible(false);
        panel1.add(failedLabel);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> loginCheck());
        panel1.add(loginButton);
        getRootPane().setDefaultButton(loginButton);

        add(panel1, BorderLayout.CENTER);

        JPanel panel2 = new JPanel();

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            new Register();
            dispose();
        });
        panel2.add(registerButton);

        add(panel2, BorderLayout.PAGE_END);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private DocumentListener createFeedbackListener() {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                clearFeedback();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                clearFeedback();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                clearFeedback();
            }
        };
    }

    private void clearFeedback() {
        failedLabel.setText(" ");
        failedLabel.setVisible(false);
    }

    public void loginCheck() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            failedLabel.setText("Enter both your username and password.");
            failedLabel.setVisible(true);
            return;
        }

        User user = Users.loginUser(username, password);
        if (user == null) {
            failedLabel.setText("Username or password is incorrect.");
            failedLabel.setVisible(true);
            passwordField.requestFocusInWindow();
            passwordField.selectAll();
            return;
        }

        if (user.getCategory() == UserCategory.SUBSCRIBER) {
            new SubscriberMenu(user);
        } else if (user.getCategory() == UserCategory.ADMIN) {
            new AdminMenu(user);
        } else {
            failedLabel.setText("This account type cannot sign in.");
            failedLabel.setVisible(true);
            return;
        }
        dispose();
    }

    public static void main(String[] args) {
        new LogIn();
    }
}
