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

        new LogOut(pane, this);
        add(pane);
        setVisible(true);
    }

    private void initializeUserFavorites() {
        ArrayList<Program> faves = new ArrayList<>(user.getFavorites());
        faves.removeIf(fav -> !Programs.ProgramCatalog.containsValue(fav));
        user.setFavorites(faves);
    }

    public static void main(String[] args, User user) {
        new SubscriberMenu(user);
    }
}

