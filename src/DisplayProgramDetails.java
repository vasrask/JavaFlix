import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
/**
 * DisplayProgramDetails displays all the details of a movie or a series.
 *
 * @author Vasiliki Raskopoulou
 */

public class DisplayProgramDetails extends JFrame {

    private Program program; // Assuming Program is a common base class for Movie and Series

    public DisplayProgramDetails(Program program) {
        this.program = program;

        setTitle(program.getTitle() + " - Details");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));

        // Display common details
        panel.add(new JLabel("Title: " + program.getTitle()));
        panel.add(new JLabel("Release Year: " + program.getYear()));
        panel.add(new JLabel(program.getDescription()));
        panel.add(new JLabel("Genre: " + program.getGenre()));
        panel.add(new JLabel("Maturity Rating: " + program.getMaturityRating()));
        panel.add(new JLabel("MainCast: " + String.join(", ",program.getMainCast())));
        panel.add(new JLabel("Review: " + getReviewText()));

        // Display type-specific details
        if (program instanceof Movie) {
            Movie movie = (Movie) program;
            panel.add(new JLabel("Duration: " + movie.getDuration()));
            panel.add(new JLabel("Type: Movie"));
        } else if (program instanceof Series) {
            Series series = (Series) program;
            panel.add(new JLabel("Seasons: " + series.getSeasons()));
            ArrayList<String> years = new ArrayList<>();
            for (YearRange yearRange: series.getYears()){
                years.add(yearRange.toString());
            }
            panel.add(new JLabel("Years: " + String.join(", ", years)));
            panel.add(new JLabel("Type: Series"));
        }

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panel.add(closeButton);

        add(panel);
        setVisible(true);
    }

    private String getReviewText() {
        ArrayList<Review> reviews = program.getUserReviews();
        if (reviews == null){
            return "No reviews available";
        } else {
            JLabel StarsLabel = new JLabel(String.format("%.1f",program.getNumberOfStars()));
            JPanel panel1 = new JPanel();
            panel1.add(StarsLabel);
            ArrayList<String> reviewTexts = new ArrayList<>();
            for (Review review : program.getUserReviews()){
                JLabel reviewLabel = new JLabel(review.getReviewer().getUsername()+ " : " + review.getReviewText());
                panel1.add(reviewLabel);
                reviewTexts.add(", " + review.getReviewer().getUsername()+ " : " + review.getReviewText());
            }
            return String.format("%.1f",program.getNumberOfStars()) + String.join(", ", reviewTexts);
        }
    }
}
