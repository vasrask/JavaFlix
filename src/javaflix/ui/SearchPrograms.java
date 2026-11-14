package javaflix.ui;

import javaflix.model.*;
import javaflix.service.Programs;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Search and browse interface for movies and series.
 * 
 * @author Vasiliki Raskopoulou
 */
public class SearchPrograms extends JFrame {
    private static final Color BACKGROUND = new Color(245, 247, 250);
    private static final Color MUTED_TEXT = new Color(100, 110, 125);

    private JPanel searchPanel;
    private JPanel resultsPanel;
    private JTextField titleField;
    private JTextField yearField;
    private JTextField actorField;
    private JCheckBox[] maturityCheckboxes;
    private JCheckBox[] genreCheckboxes;
    private JCheckBox[] reviewCheckboxes;
    private JLabel validationLabel;
    private User user;
    private JTabbedPane pane;

    public SearchPrograms(User user, JTabbedPane pane) {
        this.user = user;
        this.pane = pane;
        searchPanel = createSearchPanel();
        pane.add("Browse", searchPanel);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 22, 18, 22));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(createHeader());
        content.add(Box.createVerticalStrut(16));
        content.add(createFiltersPanel());
        content.add(Box.createVerticalStrut(10));
        content.add(createSearchActions());

        resultsPanel = new JPanel(new GridLayout(0, 2, 14, 14));
        resultsPanel.setBackground(BACKGROUND);
        resultsPanel.setBorder(BorderFactory.createEmptyBorder(8, 2, 12, 2));
        showMessage("Ready to browse", "Enter a title or choose a filter to find something to watch.");

        JScrollPane resultsScrollPane = new JScrollPane(resultsPanel);
        resultsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        resultsScrollPane.getViewport().setBackground(BACKGROUND);
        resultsScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(content, BorderLayout.NORTH);
        panel.add(resultsScrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setOpaque(false);

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        JLabel heading = new JLabel("Browse movies and series");
        heading.setFont(heading.getFont().deriveFont(Font.BOLD, 22f));
        JLabel subtitle = new JLabel("Find your next favorite by title, cast, genre, or rating.");
        subtitle.setForeground(MUTED_TEXT);
        titles.add(heading);
        titles.add(Box.createVerticalStrut(4));
        titles.add(subtitle);
        header.add(titles, BorderLayout.CENTER);
        return header;
    }

    private JPanel createFiltersPanel() {
        JPanel filters = new JPanel(new GridLayout(1, 2, 12, 0));
        filters.setOpaque(false);
        filters.add(createTextFilterGroup());
        filters.add(createChoiceFilterGroup());
        return filters;
    }

    private JPanel createTextFilterGroup() {
        JPanel group = createFilterGroup("Title and details");
        group.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(4, 6, 4, 8);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        titleField = new JTextField();
        yearField = new JTextField();
        actorField = new JTextField();
        addTextFilterRow(group, constraints, 0, "Title", titleField);
        addTextFilterRow(group, constraints, 1, "Year", yearField);
        addTextFilterRow(group, constraints, 2, "Actor", actorField);

        return group;
    }

    private void addTextFilterRow(JPanel group, GridBagConstraints constraints, int row, String label, JTextField field) {
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0;
        group.add(new JLabel(label), constraints);
        constraints.gridx = 1;
        constraints.weightx = 1;
        field.setColumns(12);
        group.add(field, constraints);
    }

    private JPanel createChoiceFilterGroup() {
        JPanel group = createFilterGroup("Refine results");
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
        group.add(createChoiceRow("Maturity", new String[]{"G", "PG", "PG13", "R", "NC17"}, 0));
        group.add(createChoiceRow("Genre", new String[]{"Comedy", "Drama", "Sci-Fi", "Adventure", "Horror"}, 1));
        group.add(createChoiceRow("Minimum stars", new String[]{"1", "2", "3", "4", "5"}, 2));
        return group;
    }

    private JPanel createChoiceRow(String title, String[] labels, int filterType) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        row.setOpaque(false);
        row.add(new JLabel(title + ":"));

        JCheckBox[] checkboxes = new JCheckBox[labels.length];
        for (int i = 0; i < labels.length; i++) {
            checkboxes[i] = new JCheckBox(labels[i]);
            checkboxes[i].setOpaque(false);
            row.add(checkboxes[i]);
        }

        if (filterType == 0) {
            maturityCheckboxes = checkboxes;
        } else if (filterType == 1) {
            genreCheckboxes = checkboxes;
        } else {
            reviewCheckboxes = checkboxes;
        }
        return row;
    }

    private JPanel createFilterGroup(String title) {
        JPanel group = new JPanel();
        group.setBackground(Color.WHITE);
        group.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(220, 225, 232)),
                        title,
                        TitledBorder.LEADING,
                        TitledBorder.TOP
                ),
                BorderFactory.createEmptyBorder(6, 8, 8, 8)
        ));
        return group;
    }

    private JPanel createSearchActions() {
        JPanel actions = new JPanel(new BorderLayout(8, 0));
        actions.setOpaque(false);

        validationLabel = new JLabel(" ");
        validationLabel.setForeground(new Color(180, 45, 45));
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetFilters());

        JButton searchButton = new JButton("Search");
        searchButton.setFont(searchButton.getFont().deriveFont(Font.BOLD));
        searchButton.addActionListener(e -> searchFilters());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        buttons.add(resetButton);
        buttons.add(searchButton);
        actions.add(validationLabel, BorderLayout.CENTER);
        actions.add(buttons, BorderLayout.EAST);
        return actions;
    }

    private void resetFilters() {
        titleField.setText("");
        yearField.setText("");
        actorField.setText("");
        clearCheckboxes(maturityCheckboxes);
        clearCheckboxes(genreCheckboxes);
        clearCheckboxes(reviewCheckboxes);
        validationLabel.setText(" ");
        showMessage("Ready to browse", "Enter a title or choose a filter to find something to watch.");
    }

    private void clearCheckboxes(JCheckBox[] checkboxes) {
        for (JCheckBox checkbox : checkboxes) {
            checkbox.setSelected(false);
        }
    }

    private void searchFilters() {
        int year = 0;
        if (!yearField.getText().trim().isEmpty()) {
            try {
                year = Integer.parseInt(yearField.getText().trim());
            } catch (NumberFormatException exception) {
                validationLabel.setText("Enter a valid year.");
                yearField.requestFocusInWindow();
                yearField.selectAll();
                return;
            }
            if (year < 1) {
                validationLabel.setText("Year must be greater than zero.");
                yearField.requestFocusInWindow();
                yearField.selectAll();
                return;
            }
        }
        validationLabel.setText(" ");

        ArrayList<Program> programs = Programs.returnProgramsByMultipleFilters(
                titleField.getText().trim(),
                checkboxToRating(),
                year,
                checkboxToGenre(),
                actorField.getText().trim(),
                checkboxToReview()
        );
        new DisplaySearchResults(user, programs, pane, resultsPanel);
    }

    private void showMessage(String title, String description) {
        resultsPanel.removeAll();
        resultsPanel.setLayout(new BorderLayout());
        JPanel message = new JPanel();
        message.setOpaque(false);
        message.setLayout(new BoxLayout(message, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 17f));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setForeground(MUTED_TEXT);
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        message.add(titleLabel);
        message.add(Box.createVerticalStrut(6));
        message.add(descriptionLabel);
        resultsPanel.add(message, BorderLayout.CENTER);
        resultsPanel.revalidate();
        resultsPanel.repaint();
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
