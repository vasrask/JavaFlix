import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 *
 * @author Vasiliki Raskopoulou
 */

public class SearchPrograms extends JFrame {
    private JPanel searchPanel, resultsPanel;
    private JTextField titleField, yearField, actorField;
    private JCheckBox[] maturityCheckboxes, genreCheckboxes, reviewCheckboxes;
    private JButton searchFiltersButton;
    private User user;
    private JTabbedPane pane;
    public SearchPrograms(User user, JTabbedPane pane) {
        this.user = user;
        this.pane = pane;
        searchPanel = createSearchPanel();
        pane.add("Search with multiple filters", searchPanel);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        titleField = addLabelAndTextField(panel, "Title");
        JLabel maturityRatingLabel = new JLabel("Maturity Rating");
        panel.add(maturityRatingLabel);
        panel.add(createMaturityRatingPanel());
        yearField = addLabelAndTextField(panel, "Year");
        JLabel genreLabel = new JLabel("Genre");
        panel.add(genreLabel);
        panel.add(createGenrePanel());
        actorField = addLabelAndTextField(panel, "Actor");
        JLabel reviewLabel = new JLabel("Stars");
        panel.add(reviewLabel);
        panel.add(createReviewPanel());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchFiltersButton = new JButton("Search");
        searchFiltersButton.setPreferredSize(new Dimension(120, 30));
        searchFiltersButton.addActionListener(e -> searchFilters());
        buttonPanel.add(searchFiltersButton);
        panel.add(buttonPanel);

        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setPreferredSize(new Dimension(700, 300));
        panel.add(scrollPane);

        return panel;
    }

    private JPanel createMaturityRatingPanel() {
        JPanel panel = new JPanel();
        String[] labels = {"G", "PG", "PG13", "R", "NC17"};
        maturityCheckboxes = createCheckboxes(panel, labels);
        return panel;
    }

    private JPanel createGenrePanel() {
        JPanel panel = new JPanel();
        String[] labels = {"Comedy", "Drama", "Sci-Fi", "Adventure", "Horror"};
        genreCheckboxes = createCheckboxes(panel, labels);
        return panel;
    }

    private JPanel createReviewPanel() {
        JPanel panel = new JPanel();
        String[] labels = {"1", "2", "3", "4", "5"};
        reviewCheckboxes = createCheckboxes(panel, labels);
        return panel;
    }
    private JCheckBox[] createCheckboxes(JPanel panel, String[] labels) {
        JCheckBox[] checkboxes = new JCheckBox[labels.length];
        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i]));
            checkboxes[i] = new JCheckBox();
            panel.add(checkboxes[i]);
        }
        return checkboxes;
    }

    private JTextField addLabelAndTextField(JPanel panel, String labelText) {
        panel.add(new JLabel(labelText));
        JTextField textField = new JTextField();
        panel.add(textField);
        return textField;
    }


    private void searchFilters() {
        resultsPanel.removeAll();
        int year = yearField.getText().isEmpty() ? 0 : parseInt(yearField.getText());
        ArrayList<Program> programs = Programs.returnProgramsByMultipleFilters(
                titleField.getText(),
                checkboxToRating(),
                year,
                checkboxToGenre(),
                actorField.getText(),
                checkboxToReview()
        );
        new DisplaySearchResults(user, programs, pane, resultsPanel);
    }

    private MaturityRatings checkboxToRating() {
        for (int i = 0; i < maturityCheckboxes.length; i++) {
            if (maturityCheckboxes[i].isSelected()) {
                return MaturityRatings.values()[i];
            }
        }
        return null;
    }

    private Genres checkboxToGenre() {
        for (int i = 0; i < genreCheckboxes.length; i++) {
            if (genreCheckboxes[i].isSelected()) {
                return Genres.values()[i];
            }
        }
        return null;
    }

    private int checkboxToReview() {
        for (int i = 0; i < reviewCheckboxes.length; i++) {
            if (reviewCheckboxes[i].isSelected()) {
                return i + 1;
            }
        }
        return 0;
    }
}
