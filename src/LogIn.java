
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Vasiliki Raskopoulou
 */
public class LogIn extends JFrame{

    private static JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel failedLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LogIn() {
        setTitle("JavaFlix");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 200);
        setResizable(true);

        JPanel panel1 = new JPanel();
        GridLayout layout = new GridLayout(6, 1, 21, 2);
        panel1.setLayout(layout);
        TitledBorder border = BorderFactory.createTitledBorder(" Login ");
        panel1.setBorder(border);


        usernameLabel = new JLabel("Username:");
        panel1.add(usernameLabel);
        usernameField = new JTextField("");
        usernameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginCheck();
            }
        });
        panel1.add(usernameField);

        passwordLabel = new JLabel("Password:");
        panel1.add(passwordLabel);
        passwordField = new JPasswordField("");
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginCheck();
            }
        });
        panel1.add(passwordField);

        failedLabel = new JLabel("Login failed");
        failedLabel.setVisible(false);
        panel1.add(failedLabel);

        loginButton = new JButton();
        loginButton.setText("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginCheck();
            }
        });
        panel1.add(loginButton);

        add(panel1, BorderLayout.CENTER);

        JPanel panel2 = new JPanel();

        registerButton = new JButton();
        registerButton.setText("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Register();
                dispose();
            }
        });
        panel2.add(registerButton);

        add(panel2, BorderLayout.PAGE_END);

        setVisible(true);

    }


    public void loginCheck () {
        String username = usernameField.getText();
        String password = String.valueOf((passwordField.getPassword()));

        User user = Users.loginUser(username, password);
        if (user == null) {
            failedLabel.setVisible(true);
        }
        if (user.getCategory() == UserCategory.SUBSCRIBER) {
            new SubscriberMenu(user);
        } else if (user.getCategory() == UserCategory.ADMIN) {
            new AdminMenu(user);
            failedLabel.setVisible(true);
        } else {
            failedLabel.setVisible(true);
        }
        dispose();

    }

    public static void main(String[] args) {
        new LogIn();
    }
}
