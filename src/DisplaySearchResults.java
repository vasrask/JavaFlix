import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 *
 * @author Vasiliki Raskopoulou
 */

public class DisplaySearchResults extends JFrame {
    private JPanel resultsPanel;
    private User user;
    private JTabbedPane pane;
    public DisplaySearchResults(User user, ArrayList<Program> programs, JTabbedPane pane, JPanel resultsPanel){
        this.user = user;
        this.resultsPanel = resultsPanel;
        this.pane = pane;
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        if (user.getCategory().equals(UserCategory.SUBSCRIBER)){
            displaySubscriberSearchResults(programs);
        }
        else {
            displayAdminSearchResults(programs);
        }
    }
    private void displaySubscriberSearchResults(ArrayList<Program> programs) {
        resultsPanel.removeAll();
        if (programs == null || programs.isEmpty()) {
            JLabel noResults = new JLabel("No Movies Found");
            noResults.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultsPanel.add(noResults);
        } else {
            for (Program program : programs) {
                JPanel programPanel = createProgramPanel(program);
                programPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                resultsPanel.add(programPanel);
                resultsPanel.add(Box.createVerticalStrut(8));
            }
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private JPanel createProgramPanel(Program program) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.setMaximumSize(new Dimension(700, 45));

        JLabel titleLabel = new JLabel(program.getTitle());
        titleLabel.setPreferredSize(new Dimension(120, 25));
        panel.add(titleLabel);

        JButton watchButton = new JButton("Watch");
        watchButton.addActionListener(e -> displayProgram(program));
        panel.add(watchButton);

        JButton addToFavoritesButton = new JButton("Add To Favorites");
        addToFavoritesButton.addActionListener(e -> addToFavorites(program));
        panel.add(addToFavoritesButton);

        JButton addReviewButton = new JButton(getReviewButtonText(program));
        addReviewButton.addActionListener(e -> new AddReview(program, user));
        panel.add(addReviewButton);

        JButton deleteReviewButton = new JButton("Delete Review");
        deleteReviewButton.addActionListener(e -> deleteReview(program));
        panel.add(deleteReviewButton);

        return panel;
    }

    private JPanel createMinimalProgramPanel(Program program) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.setMaximumSize(new Dimension(700, 45));

        JLabel titleLabel = new JLabel(program.getTitle());
        titleLabel.setPreferredSize(new Dimension(220, 25));
        panel.add(titleLabel);

        JButton watchButton = new JButton("Watch");
        watchButton.addActionListener(e -> displayProgram(program));
        panel.add(watchButton);

        return panel;
    }
    private void addToFavorites(Program program) {
        user.addFavorite(program);
        FavoritesPanel.updateFavoritesPanel();
    }

    private void deleteReview(Program program) {
        Review review = getCurrentUserReview(program);
        if (review != null) {
            program.deleteReview(review);
        }
    }
    private Review getCurrentUserReview(Program program) {
        for (Review review : program.getUserReviews()) {
            if (review.getReviewer().equals(user)) {
                return review;
            }
        }
        return null;
    }

    private String getReviewButtonText(Program program) {
        return getCurrentUserReview(program) == null ? "Add Review" : "Edit Review";
    }

    private void displayAdminSearchResults(ArrayList<Program> programs) {
        resultsPanel.removeAll();
        if (programs == null || programs.isEmpty()) {
            JLabel noResults = new JLabel("No Movies Found");
            noResults.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultsPanel.add(noResults);
        } else {
            for (Program program : programs) {
                JPanel programPanel = createMinimalProgramPanel(program);
                programPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                resultsPanel.add(programPanel);
                resultsPanel.add(Box.createVerticalStrut(8));
            }
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private void displayProgram(Program program) {
        Programs.displayProgram(program);
        new DisplayProgramDetails(program);
    }
}
