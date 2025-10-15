import javax.swing.*;
import java.util.ArrayList;

/**
 *
 * @author Vasiliki Raskopoulou
 */

public class FavoritesPanel extends JFrame {
    private static JPanel favoritesPanel;
    private static User user;
    ArrayList<Program> favorites;
    public FavoritesPanel(User user, JTabbedPane pane, JPanel favoritesPanel){
        this.user = user;
        this.favorites = user.getFavorites();
        this.favoritesPanel = favoritesPanel;
        favoritesPanel = createFavoritesPanel();
        pane.add("My Favorites", favoritesPanel);
    }

    private static JPanel createFavoritesPanel() {
        favoritesPanel = new JPanel();
        if (!user.getFavorites().isEmpty()) {
            updateFavoritesPanel();
        } else {
            favoritesPanel.add(new JLabel("You don't have any Favorites. Start exploring!"));
        }
        return favoritesPanel;
    }

    public static void updateFavoritesPanel() {
        favoritesPanel.removeAll();
        if (user.getFavorites().isEmpty()){
            System.out.println("no favorites");
        }
        for (Program program : user.getFavorites()) {
            favoritesPanel.add(new JLabel(program.getTitle()));
            JButton watchButton = new JButton("Watch");
            watchButton.addActionListener(e -> displayProgram(program));
            JButton removeButton = new JButton("Remove From Favorites");
            removeButton.addActionListener(e -> removeFromFavorites(program));
            favoritesPanel.add(watchButton);
            favoritesPanel.add(removeButton);
        }
        favoritesPanel.repaint();
        favoritesPanel.revalidate();
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
