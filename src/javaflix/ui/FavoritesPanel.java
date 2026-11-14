package javaflix.ui;

import javaflix.model.Program;
import javaflix.model.User;
import javaflix.service.Programs;
import javax.swing.*;
import java.awt.*;

/**
 * Displays and manages a subscriber's favorite programs.
 * 
 * @author Vasiliki Raskopoulou
 */
public class FavoritesPanel extends JFrame {
    private static JPanel favoritesPanel;
    private static User user;

    public FavoritesPanel(User user, JTabbedPane pane, JPanel panel) {
        FavoritesPanel.user = user;
        favoritesPanel = panel;
        favoritesPanel.setLayout(new BorderLayout());
        updateFavoritesPanel();
        pane.add("My Favorites", favoritesPanel);
    }

    public static void updateFavoritesPanel() {
        favoritesPanel.removeAll();
        if (user.getFavorites().isEmpty()) {
            JPanel emptyState = new JPanel();
            emptyState.setLayout(new BoxLayout(emptyState, BoxLayout.Y_AXIS));
            JLabel title = new JLabel("Your favorites list is empty");
            title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel hint = new JLabel("Browse the catalog and add titles you want to keep close.");
            hint.setAlignmentX(Component.CENTER_ALIGNMENT);
            hint.setForeground(new Color(100, 110, 125));
            emptyState.add(title);
            emptyState.add(Box.createVerticalStrut(8));
            emptyState.add(hint);
            favoritesPanel.add(emptyState, BorderLayout.CENTER);
        } else {
            JPanel list = new JPanel();
            list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
            list.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

            for (Program program : user.getFavorites()) {
                list.add(createFavoriteRow(program));
                list.add(Box.createVerticalStrut(8));
            }

            JScrollPane scrollPane = new JScrollPane(list);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            favoritesPanel.add(scrollPane, BorderLayout.CENTER);
        }
        favoritesPanel.revalidate();
        favoritesPanel.repaint();
    }

    private static JPanel createFavoriteRow(Program program) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 232)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        JLabel title = new JLabel(program.getTitle());
        title.setFont(title.getFont().deriveFont(Font.BOLD));
        row.add(title, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton watchButton = new JButton("Details");
        watchButton.addActionListener(e -> displayProgram(program));
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> removeFromFavorites(program));
        actions.add(watchButton);
        actions.add(removeButton);
        row.add(actions, BorderLayout.EAST);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        return row;
    }

    private static void displayProgram(Program program) {
        Programs.displayProgram(program);
        new DisplayProgramDetails(program);
    }

    private static void removeFromFavorites(Program program) {
        user.removeFavorite(program);
        updateFavoritesPanel();
    }
}
