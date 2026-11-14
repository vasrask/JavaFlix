package javaflix.ui;

import javaflix.model.Program;
import javaflix.model.User;
import javaflix.service.Programs;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * SubscriberMenu designs the GUI as well as an API for the subscribers' functions.
 *
 * @author Vasiliki Raskopoulou
 */

public class SubscriberMenu extends JFrame {

    private JPanel favoritesPanel;
    private User user;

    public SubscriberMenu(User user) {
        this.user = user;
        initializeUserFavorites();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("JavaFlix");
        setSize(800, 800);
        setMinimumSize(new Dimension(600, 300));
        setLocationRelativeTo(null);
        setResizable(true);

        JTabbedPane pane = new JTabbedPane();

        new SearchPrograms(user, pane);

        favoritesPanel = new JPanel();
        new FavoritesPanel(user, pane, favoritesPanel);

        new Profile(user,pane);

        add(createMenuContent(pane));
        setVisible(true);
    }

    private JPanel createMenuContent(JTabbedPane pane) {
        JPanel content = new JPanel(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        JLabel appName = new JLabel("JavaFlix");
        appName.setFont(appName.getFont().deriveFont(Font.BOLD, 18f));
        header.add(appName, BorderLayout.WEST);
        header.add(LogOut.createButton(this), BorderLayout.EAST);

        content.add(header, BorderLayout.NORTH);
        content.add(pane, BorderLayout.CENTER);
        return content;
    }

    private void initializeUserFavorites() {
        ArrayList<Program> faves = new ArrayList<>(user.getFavorites());
        faves.removeIf(fav -> !Programs.getAllPrograms().contains(fav));
        user.setFavorites(faves);
    }

    public static void main(String[] args, User user) {
        new SubscriberMenu(user);
    }
}
