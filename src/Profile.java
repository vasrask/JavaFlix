import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 *
 * @author Vasiliki Raskopoulou
 */

public class Profile extends JFrame {
    private JPanel profilePanel;
    private JLabel messageLabel;
    private JButton validateButton, backButton;
    private User user;
    public Profile(User user, JTabbedPane pane) {
        this.user = user;
        profilePanel = new JPanel(new GridLayout(12, 3));
        profilePanel = createProfilePanel();
        pane.add("Show Profile", profilePanel);
    }
    private JPanel createProfilePanel() {
        profilePanel.add(new JLabel(user.getUsername()));
        profilePanel.add(createEditButton("Change", false, e -> editEmail()));
        profilePanel.add(new JLabel(user.getEmail()));
        profilePanel.add(createEditButton("Change", true, e -> editEmail()));
        profilePanel.add(new JLabel("****"));
        profilePanel.add(createEditButton("Change", true,  e -> editPassword()));
        return profilePanel;
    }
    private JButton createEditButton(String text, boolean enable, ActionListener action) {
        JButton button = new JButton(text);
        if (enable) {
            button.addActionListener(action);
        }
        else {
            button.setEnabled(false);
        }
        return button;
    }

    private void editEmail() {
        JTextField newEmailField = new JTextField(user.getEmail());
        showEditField("New Email", newEmailField, newAction -> {
            String newEmail = newEmailField.getText();
            if (newEmail.isEmpty()) newEmail = user.getEmail();
            Users.editEmail(user.getUsername(), user.getPassword(), newEmail);
            updateProfile();
        });
    }

    private void editPassword() {
        JPasswordField oldPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField newPasswordField2 = new JPasswordField();
        showEditPasswordField("New Password", oldPasswordField, newPasswordField, newPasswordField2, newAction -> {
            if (user.getPassword().equals(new String(oldPasswordField.getPassword()))){
                String newPassword = new String(newPasswordField.getPassword());
                if (newPassword.equals(new String(newPasswordField2.getPassword()))){
                    if (newPassword.equals(user.getPassword())) {
                        messageLabel.setText("New password can't be the same as old password");
                    } else {
                        if (newPassword.isEmpty()) newPassword = user.getPassword();
                        Users.editUserPassword(user.getUsername(), user.getPassword(), newPassword);
                        updateProfile();
                    }
                } else { messageLabel.setText("New passwords don't match");}
            } else {
                messageLabel.setText("Old password is incorrect");
            }

        });
    }

    private void updateProfile() {
        profilePanel.removeAll();
        profilePanel = createProfilePanel();
        profilePanel.revalidate();
        profilePanel.repaint();
    }

    private void showEditField(String labelText, JTextField field, ActionListener validateAction) {
        profilePanel.removeAll();
        profilePanel.add(new JLabel(labelText));
        profilePanel.add(field);
        validateButton = new JButton("Validate");
        validateButton.addActionListener(validateAction);
        profilePanel.add(validateButton);
        backButton = new JButton("Back");
        backButton.addActionListener(e -> updateProfile());
        profilePanel.add(backButton);
        profilePanel.revalidate();
        profilePanel.repaint();
    }
    private void showEditPasswordField(String labelText, JPasswordField old, JPasswordField field, JPasswordField field2, ActionListener validateAction) {
        profilePanel.removeAll();
        profilePanel.add(new JLabel("Old Password"));
        profilePanel.add(old);
        profilePanel.add(new JLabel(labelText));
        profilePanel.add(field);
        profilePanel.add(new JLabel("Repeat New Password"));
        profilePanel.add(field2);
        validateButton = new JButton("Validate");
        validateButton.addActionListener(validateAction);
        profilePanel.add(validateButton);
        backButton = new JButton("Back");
        backButton.addActionListener(e -> updateProfile());
        profilePanel.add(backButton);
        messageLabel = new JLabel();
        profilePanel.add(messageLabel);
        profilePanel.revalidate();
        profilePanel.repaint();
    }
}
