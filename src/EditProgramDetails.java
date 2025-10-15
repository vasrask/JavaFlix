import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Integer.parseInt;

/**
 * EditProgramDetails class allows admins to edit details of a program.
 *
 * @author Vasiliki Raskopoulou
 */
public class EditProgramDetails extends JFrame {
    private JPanel panel1, panelMaturityRating, panelGenre;
    private JTextField titleField, descriptionField, yearField, durationField, mainCastField, moreLikeThisField, seasonsField, yearRangesField;
    private JLabel labelTitle, labelDescription, labelMaturityRating, labelYear, labelDuration, labelGenre, labelMainCast, labelMoreLikeThis;
    private JCheckBox GBox, PGBox, PG13Box, RBox, NC17Box, ComedyBox, SciFiBox, DramaBox, AdventureBox, HorrorBox;
    private JButton submitButton;

    public EditProgramDetails(Program program) {
        setupFrame(program.getTitle());
        initializeComponents(program);
        setupLayout(program);
        setVisible(true);
    }

    private void setupFrame(String title) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(title);
        setSize(700, 350);
        setMinimumSize(new Dimension(600, 300));
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void initializeComponents(Program program) {
        labelTitle = new JLabel("Title:");
        labelDescription = new JLabel("Description:");
        labelMaturityRating = new JLabel("Maturity Rating:");
        labelYear = new JLabel("Year:");
        labelDuration = new JLabel("Duration:");
        labelGenre = new JLabel("Genre:");
        labelMainCast = new JLabel("Main Cast:");
        labelMoreLikeThis = new JLabel("More Like This:");

        titleField = new JTextField(program.getTitle());
        descriptionField = new JTextField(program.getDescription());
        yearField = new JTextField(Integer.toString(program.getYear()));
        if (program instanceof Movie){
            Movie movie = (Movie) program;
        durationField = new JTextField(Integer.toString(movie.getDuration()));
        }
        if (program instanceof Series){
            Series series = (Series) program;
            ArrayList<String> seasons = new ArrayList<>();
            for (Season season : series.getSeasons()){
                seasons.add(season.toString());
            }
            seasonsField = new JTextField(String.join(";",seasons));
            ArrayList<String> yearRanges = new ArrayList<>();
            for (YearRange yearRange : series.getYears()){
                yearRanges.add(yearRange.toString());
            }
            yearRangesField = new JTextField(String.join(";",yearRanges));

        }
        mainCastField = new JTextField(String.join(", ", program.getMainCast()));
        moreLikeThisField = new JTextField("");

        GBox = new JCheckBox("G", program.getMaturityRating() == MaturityRatings.G);
        PGBox = new JCheckBox("PG", program.getMaturityRating() == MaturityRatings.PG);
        PG13Box = new JCheckBox("PG13", program.getMaturityRating() == MaturityRatings.PG13);
        RBox = new JCheckBox("R", program.getMaturityRating() == MaturityRatings.R);
        NC17Box = new JCheckBox("NC17", program.getMaturityRating() == MaturityRatings.NC17);

        ComedyBox = new JCheckBox("Comedy", program.getGenre() == Genres.COMEDY);
        DramaBox = new JCheckBox("Drama", program.getGenre() == Genres.DRAMA);
        SciFiBox = new JCheckBox("Sci-Fi", program.getGenre() == Genres.SCIFI);
        AdventureBox = new JCheckBox("Adventure", program.getGenre() == Genres.ADVENTURE);
        HorrorBox = new JCheckBox("Horror", program.getGenre() == Genres.HORROR);

        submitButton = new JButton("Submit Changes");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (program instanceof Movie){
                    Movie movie = (Movie) program;
                    movie.editProgram(
                            titleField.getText(),
                            descriptionField.getText(),
                            getSelectedMaturityRating(),
                            getSelectedGenre(),
                            new ArrayList<>(Arrays.asList(mainCastField.getText().split(","))),
                            new ArrayList<>(),
                            parseInt(yearField.getText()),
                            parseInt(durationField.getText())
                    );
                } else if (program instanceof Series) {
                    Series series = (Series) program;
                    series.editProgram(
                            titleField.getText(),
                            descriptionField.getText(),
                            getSelectedMaturityRating(),
                            getSelectedGenre(),
                            new ArrayList<>(Arrays.asList(mainCastField.getText().split(","))),
                            new ArrayList<>(),
                            Season.parseSeasons(seasonsField.getText()),
                            YearRange.fromStringToYearRanges(yearRangesField.getText())
                    );
            }
                dispose();
            }
        });
    }

    private void setupLayout(Program program) {
        panel1 = new JPanel(new GridLayout(10, 2));
        TitledBorder border = BorderFactory.createTitledBorder(program.getTitle());
        panel1.setBorder(border);
        add(panel1, BorderLayout.PAGE_START);

        panel1.add(labelTitle);
        panel1.add(titleField);

        panel1.add(labelDescription);
        panel1.add(descriptionField);

        panel1.add(labelMaturityRating);
        panelMaturityRating = new JPanel();
        panelMaturityRating.add(GBox);
        panelMaturityRating.add(PGBox);
        panelMaturityRating.add(PG13Box);
        panelMaturityRating.add(RBox);
        panelMaturityRating.add(NC17Box);
        panel1.add(panelMaturityRating);

        panel1.add(labelYear);
        panel1.add(yearField);

        panel1.add(labelDuration);
        panel1.add(durationField);

        panel1.add(labelGenre);
        panelGenre = new JPanel();
        panelGenre.add(ComedyBox);
        panelGenre.add(DramaBox);
        panelGenre.add(SciFiBox);
        panelGenre.add(AdventureBox);
        panelGenre.add(HorrorBox);
        panel1.add(panelGenre);

        panel1.add(labelMainCast);
        panel1.add(mainCastField);

        panel1.add(labelMoreLikeThis);
        panel1.add(moreLikeThisField);

        panel1.add(new JLabel());
        panel1.add(submitButton);
    }

    private MaturityRatings getSelectedMaturityRating() {
        if (GBox.isSelected()) return MaturityRatings.G;
        if (PGBox.isSelected()) return MaturityRatings.PG;
        if (PG13Box.isSelected()) return MaturityRatings.PG13;
        if (RBox.isSelected()) return MaturityRatings.R;
        if (NC17Box.isSelected()) return MaturityRatings.NC17;
        return null;
    }

    private Genres getSelectedGenre() {
        if (ComedyBox.isSelected()) return Genres.COMEDY;
        if (DramaBox.isSelected()) return Genres.DRAMA;
        if (SciFiBox.isSelected()) return Genres.SCIFI;
        if (AdventureBox.isSelected()) return Genres.ADVENTURE;
        if (HorrorBox.isSelected()) return Genres.HORROR;
        return null;
    }

    public static void main(String[] args, Program program) {
        new EditProgramDetails(program);
    }
}
