package javaflix.ui;

import javaflix.model.*;
import javaflix.service.Programs;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Renders search results as catalog cards.
 * 
 * @author Vasiliki Raskopoulou
 */
public class DisplaySearchResults extends JFrame {
    private static final Color BACKGROUND = new Color(245, 247, 250);
    private static final Color MUTED_TEXT = new Color(100, 110, 125);
    private static final Color POSTER_BACKGROUND = new Color(35, 48, 70);

    private JPanel resultsPanel;
    private User user;
    private ArrayList<Program> displayedPrograms;

    public DisplaySearchResults(User user, ArrayList<Program> programs, JTabbedPane pane, JPanel resultsPanel) {
        this.user = user;
        this.resultsPanel = resultsPanel;
        this.displayedPrograms = programs == null ? null : new ArrayList<>(programs);
        displayResults(programs);
    }

    private void displayResults(ArrayList<Program> programs) {
        resultsPanel.removeAll();
        resultsPanel.setBackground(BACKGROUND);

        if (programs == null || programs.isEmpty()) {
            resultsPanel.setLayout(new BorderLayout());
            JPanel emptyState = new JPanel();
            emptyState.setOpaque(false);
            emptyState.setLayout(new BoxLayout(emptyState, BoxLayout.Y_AXIS));
            JLabel title = new JLabel("No titles found");
            title.setFont(title.getFont().deriveFont(Font.BOLD, 17f));
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel hint = new JLabel("Try a different title or adjust your filters.");
            hint.setForeground(MUTED_TEXT);
            hint.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyState.add(title);
            emptyState.add(Box.createVerticalStrut(6));
            emptyState.add(hint);
            resultsPanel.add(emptyState, BorderLayout.CENTER);
        } else {
            resultsPanel.setLayout(new BorderLayout());
            JPanel cards = new JPanel(new GridLayout(0, 2, 14, 14));
            cards.setOpaque(false);
            for (Program program : programs) {
                cards.add(createProgramCard(program));
            }
            resultsPanel.add(cards, BorderLayout.NORTH);
        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private JPanel createProgramCard(Program program) {
        JPanel card = new JPanel(new BorderLayout(12, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 232)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        card.add(createPosterPlaceholder(program), BorderLayout.WEST);
        card.add(createProgramInformation(program), BorderLayout.CENTER);
        card.add(createActions(program), BorderLayout.SOUTH);
        card.setPreferredSize(new Dimension(340, 210));
        return card;
    }

    private JPanel createPosterPlaceholder(Program program) {
        JPanel poster = new JPanel(new BorderLayout(0, 8));
        poster.setBackground(POSTER_BACKGROUND);
        poster.setBorder(BorderFactory.createEmptyBorder(12, 8, 12, 8));
        poster.setPreferredSize(new Dimension(82, 124));
        poster.setMinimumSize(new Dimension(82, 124));

        JLabel brand = new JLabel("JF", SwingConstants.CENTER);
        brand.setForeground(Color.WHITE);
        brand.setFont(brand.getFont().deriveFont(Font.BOLD, 24f));
        JLabel type = new JLabel(program instanceof Series ? "SERIES" : "MOVIE", SwingConstants.CENTER);
        type.setForeground(new Color(205, 215, 230));
        type.setFont(type.getFont().deriveFont(Font.BOLD, 10f));

        poster.add(brand, BorderLayout.CENTER);
        poster.add(type, BorderLayout.SOUTH);
        return poster;
    }

    private JPanel createProgramInformation(Program program) {
        JPanel information = new JPanel();
        information.setOpaque(false);
        information.setLayout(new BoxLayout(information, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(toWrappedHtml(program.getTitle(), 190));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 15f));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel kind = new JLabel(program instanceof Series ? "Series" : "Movie");
        kind.setForeground(MUTED_TEXT);
        kind.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel yearAndGenre = new JLabel(program.getYear() + "  |  " + formatGenre(program.getGenre()));
        yearAndGenre.setAlignmentX(Component.LEFT_ALIGNMENT);

        String ratingText = program.getNumberOfReviews() == 0
                ? "Not rated yet"
                : String.format(Locale.ROOT, "Rating %.1f / 5", program.getNumberOfStars());
        JLabel rating = new JLabel(ratingText);
        rating.setFont(rating.getFont().deriveFont(Font.BOLD));
        rating.setAlignmentX(Component.LEFT_ALIGNMENT);

        information.add(title);
        information.add(Box.createVerticalStrut(5));
        information.add(kind);
        information.add(Box.createVerticalStrut(7));
        information.add(yearAndGenre);
        information.add(Box.createVerticalStrut(7));
        information.add(rating);
        return information;
    }

    private JPanel createActions(Program program) {
        JPanel actions = new JPanel(new GridLayout(user.getCategory() == UserCategory.SUBSCRIBER ? 2 : 1,
                user.getCategory() == UserCategory.SUBSCRIBER ? 2 : 1, 6, 6));
        actions.setOpaque(false);

        JButton watchButton = new JButton("Details");
        watchButton.addActionListener(e -> displayProgram(program));
        actions.add(watchButton);

        if (user.getCategory() == UserCategory.SUBSCRIBER) {
            JButton favoriteButton = new JButton("Favorite");
            favoriteButton.addActionListener(e -> addToFavorites(program));
            actions.add(favoriteButton);

            JButton reviewButton = new JButton(getReviewButtonText(program));
            reviewButton.addActionListener(e -> new AddReview(program, user, () -> displayResults(displayedPrograms)));
            actions.add(reviewButton);

            JButton deleteReviewButton = new JButton("Delete review");
            deleteReviewButton.setEnabled(getCurrentUserReview(program) != null);
            deleteReviewButton.addActionListener(e -> deleteReview(program));
            actions.add(deleteReviewButton);
        }

        return actions;
    }

    private String toWrappedHtml(String text, int width) {
        String escaped = text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        return "<html><body style='width:" + width + "px'>" + escaped + "</body></html>";
    }

    private String formatGenre(Genres genre) {
        if (genre == null) {
            return "Genre unavailable";
        }
        switch (genre) {
            case SCIFI:
                return "Sci-Fi";
            default:
                String value = genre.name().toLowerCase(Locale.ROOT);
                return Character.toUpperCase(value.charAt(0)) + value.substring(1);
        }
    }

    private void addToFavorites(Program program) {
        user.addFavorite(program);
        FavoritesPanel.updateFavoritesPanel();
    }

    private void deleteReview(Program program) {
        Review review = getCurrentUserReview(program);
        if (review != null) {
            program.deleteReview(review);
            displayResults(displayedPrograms);
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
        return getCurrentUserReview(program) == null ? "Add review" : "Edit review";
    }

    private void displayProgram(Program program) {
        Programs.displayProgram(program);
        new DisplayProgramDetails(program);
    }
}
