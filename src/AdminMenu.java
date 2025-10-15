import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

/**
 * AdminMenu designs the GUI as well as an API for the administrators' functions.
 *
 * @author Vasiliki Raskopoulou
 */

public class AdminMenu <T extends Program> extends JFrame {
    private JPanel addPanel;
    private JTextField titleField, yearField, actorField, descriptionField, durationField;
    private JButton seasonsButton;
    private JCheckBox[] maturityCheckboxes, genreCheckboxes;
    private JComboBox<String> programTypeComboBox;
    private User user;
    ArrayList<Season> seasons = new ArrayList<>();

    public AdminMenu(User user) {
        this.user = user;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Admin Menu");
        setSize(800, 800);
        setMinimumSize(new Dimension(600, 300));
        setLocationRelativeTo(null);
        setResizable(true);

        JTabbedPane pane = new JTabbedPane();
        new SearchPrograms(user, pane);

        addPanel = createSelectPanel();
        pane.add("Add Program", addPanel);

        new Profile(user, pane);

        new LogOut(pane, this);

        add(pane);
        setVisible(true);
    }

    private JPanel createSelectPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1));

        String[] programTypes = {"Movie", "Series"};
        programTypeComboBox = new JComboBox<>(programTypes);
        programTypeComboBox.setSize(new Dimension(5, 20));
        panel.add(programTypeComboBox);

        addPanel = new JPanel();
        panel.add(addPanel);

        updateAddPanel("Movie");

        programTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) programTypeComboBox.getSelectedItem();
                updateAddPanel(selectedType);
            }
        });

        return panel;
    }

    private void updateAddPanel(String programType) {
        addPanel.removeAll();
        programTypeComboBox.setSize(new Dimension(5, 20));
        addPanel.add(programTypeComboBox);
        if (programType.equals("Movie")) {
            addPanel.add(createAddPanel((Class<T>) Movie.class));
        } else if (programType.equals("Series")) {
            addPanel.add(createAddPanel((Class<T>) Series.class));
        }

        addPanel.revalidate();
        addPanel.repaint();
    }

    private JPanel createAddPanel(Class<T> programType) {
        JPanel panel = new JPanel(new GridLayout(10, 2));
        titleField = addLabelAndTextField(panel, "Title");
        descriptionField = addLabelAndTextField(panel, "Description");

        if (programType == Movie.class) {
            durationField = addLabelAndTextField(panel, "Duration (in minutes)");
        } else if (programType == Series.class) {
            seasonsButton = addLabelAndButton(panel, "Seasons", "Add Seasons");
            seasonsButton.addActionListener(e -> addSeasons());
        }
        panel.add(new JLabel("Maturity Rating"));
        panel.add(createMaturityRatingPanel());

        panel.add(new JLabel("Genre"));
        panel.add(createGenrePanel());

        actorField = addLabelAndTextField(panel, "Actors");
        if (programType == Movie.class) {
            yearField = addLabelAndTextField(panel, "Year");
        }
        else if (programType == Series.class) {
            yearField = addLabelAndTextField(panel, "Years");
        }

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> addItem(programType));
        panel.add(addButton);
        return panel;
    }


    private JTextField addLabelAndTextField(JPanel panel, String labelText) {
        panel.add(new JLabel(labelText));
        JTextField textField = new JTextField();
        panel.add(textField);
        return textField;
    }

    private JButton addLabelAndButton(JPanel panel, String labelText, String buttonText) {
        panel.add(new JLabel(labelText));
        JButton button = new JButton(buttonText);
        panel.add(button);
        return button;
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

    private JCheckBox[] createCheckboxes(JPanel panel, String[] labels) {
        JCheckBox[] checkboxes = new JCheckBox[labels.length];
        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i]));
            checkboxes[i] = new JCheckBox();
            panel.add(checkboxes[i]);
        }
        return checkboxes;
    }

    private void addSeasons() {
        seasons.clear();
        new AddSeasons(seasons);
    }
    private void addItem(Class<T> programType) {
        if (programType == Movie.class) {
            addMovie();
        } else if (programType == Series.class) {
            addSeries();
        }
    }

    private void addMovie() {
        Movie movie = new Movie(
                titleField.getText(),
                descriptionField.getText(),
                checkboxToRating(),
                Integer.parseInt(durationField.getText()),
                checkboxToGenre(),
                Integer.parseInt(yearField.getText()),
                getActors(),
                new ArrayList<>(),  // More like this (empty for now)
                new ArrayList<>()   // User reviews (empty for now)
        );

        boolean exists = Programs.ProgramCatalog.values().stream()
                .anyMatch(p -> p.getTitle().equals(movie.getTitle()));

        if (!exists) {
            Programs.addProgram(movie);
            JOptionPane.showMessageDialog(this, "Movie added successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Movie already exists in the catalog.");
        }
    }

    private void addSeries() {
        Series series = new Series(
                titleField.getText(),
                descriptionField.getText(),
                checkboxToRating(),
                checkboxToGenre(),
                getActors(),
                new ArrayList<>(),
                new ArrayList<>(),
                YearRange.fromStringToYearRanges(yearField.getText()),
                new ArrayList<Program>() // More like this (empty for now)
        );

        boolean exists = Programs.ProgramCatalog.values().stream()
                .anyMatch(p -> p.getTitle().equals(series.getTitle()));

        if (!exists) {
            Programs.addProgram(series);
            JOptionPane.showMessageDialog(this, "Series added successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Series already exists in the catalog.");
        }
    }


    private ArrayList<String> getActors() {
        ArrayList<String> actors = new ArrayList<>();
        for (String actor : new ArrayList<String>(Arrays.asList(actorField.getText().split(",")))){
            actors.add(actor.trim());
        }
        return actors;
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
}
