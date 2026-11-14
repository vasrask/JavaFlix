package javaflix.ui;

import javax.swing.*;
import java.awt.*;

public final class LogOut {
    private LogOut() {
    }

    public static JButton createButton(JFrame menu) {
        JButton logoutButton = new JButton("Log out");
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    menu,
                    "Are you sure you want to log out?",
                    "Log out",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                menu.dispose();
                new LogIn();
            }
        });
        logoutButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        return logoutButton;
    }
}