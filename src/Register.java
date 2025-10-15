
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.border.TitledBorder;
/**
 *
 * @author Vasiliki Raskopoulou
 */

public class Register extends JFrame {
    private JLabel registerFailedLabel;
    private JButton registerButton, backButton;
    private JTextField nameField, emailField;
    private JPasswordField passwordField;

    public Register() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setSize(500,500);
        setTitle("JavaFlix");
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel panel1 = new JPanel();
        GridLayout layout1 = new GridLayout(8, 1);
        panel1.setLayout(layout1);
        TitledBorder border = BorderFactory.createTitledBorder(" Register ");
        panel1.setBorder(border);
        add(panel1,BorderLayout.PAGE_START);
        panel1.setLayout(layout1);

        JLabel nameLabel = new JLabel("Name:");
        panel1.add(nameLabel);

        nameField = new JTextField();
        panel1.add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        panel1.add(emailLabel);

        emailField = new JTextField();
        panel1.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        panel1.add(passwordLabel);

        passwordField = new JPasswordField();
        panel1.add(passwordField);

        add(panel1,BorderLayout.CENTER);

        JPanel panel3 = new JPanel();
        registerFailedLabel = new JLabel("Username already exists.");
        registerFailedLabel.setVisible(false);
        panel1.add(registerFailedLabel);
        registerButton = new JButton();
        registerButton.setText("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerAction();
            }
        });
        panel3.add(registerButton);
        backButton = new JButton();
        backButton.setText("Go Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LogIn();
            }
        });
        panel3.add(backButton);

        add(panel3,BorderLayout.PAGE_END);

        setVisible(true);
        pack();

    }


    public void registerAction (){

        String username = nameField.getText();
        String email = emailField.getText();
        String pass = String.valueOf((passwordField.getPassword()));

        if (Users.existingUsername(username)){
            registerFailedLabel.setVisible(true);
        }
        else {
            Users.addUser(new User(username,email,pass, UserCategory.SUBSCRIBER,new ArrayList<>()));
            new LogIn();
            dispose();
        }
    }



    public static void main(String[] args) {
        Register start = new Register();
    }
}
