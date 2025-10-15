import javax.swing.*;

public class LogOut extends JFrame {
    JFrame menu;
    public LogOut(JTabbedPane pane, JFrame menu) {
        this.menu = menu;
        JPanel logoutPanel = new JPanel();
        logoutPanel = createLogoutPanel();
        pane.add("LogOut", logoutPanel);
    }
    private JPanel createLogoutPanel() {
        JPanel panel = new JPanel();
        JLabel logoutLabel = new JLabel("Are you sure you want to logout?");
        panel.add(logoutLabel);
        JButton logoutButton = new JButton("LogOut");
        logoutButton.addActionListener(e -> logout());
        panel.add(logoutButton);
        return panel;
    }

    private void logout() {
        menu.dispose();
        new LogIn();
    }
}