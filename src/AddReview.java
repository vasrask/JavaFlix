import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * AddReview class allows users to add reviews for movies.
 *
 * @author Vasiliki Raskopoulou
 */
public class AddReview extends JFrame {
    private JCheckBox[] starCheckBoxes;
    private JLabel[] starLabels;
    private JLabel starsLabel, reviewTextLabel;
    private JPanel starPanel;
    private JButton submitReviewButton;
    private JTextField reviewTextField;
    public static Review review;

    public AddReview(Program program, User user) {
        setupFrame( program.getTitle());
        initializeComponents();

        JPanel mainPanel = createMainPanel();
        addComponentsToPanel(mainPanel,  program, user);

        add(mainPanel, BorderLayout.PAGE_START);
        setVisible(true);
    }

    private void setupFrame(String title) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(title);
        setSize(350, 350);
        setMinimumSize(new Dimension(600, 300));
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void initializeComponents() {
        starsLabel = new JLabel("Stars: ");
        reviewTextLabel = new JLabel("Review: ");
        reviewTextField = new JTextField();

        starCheckBoxes = new JCheckBox[5];
        starLabels = new JLabel[5];
        for (int i = 0; i < 5; i++) {
            starLabels[i] = new JLabel(String.valueOf(i + 1));
            starCheckBoxes[i] = new JCheckBox();
        }

        submitReviewButton = new JButton("Submit");
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        starPanel = new JPanel();

        for (int i = 0; i < 5; i++) {
            starPanel.add(starLabels[i]);
            starPanel.add(starCheckBoxes[i]);
        }

        return panel;
    }

    private void addComponentsToPanel(JPanel panel, Program  program, User user) {
        panel.add(starsLabel);
        panel.add(starPanel);
        panel.add(reviewTextLabel);
        panel.add(reviewTextField);
        panel.add(submitReviewButton);

        submitReviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                review = new Review(getSelectedStars(), reviewTextField.getText(), user);
                program.addReview(review);
                dispose();
            }
        });
    }

    private int getSelectedStars() {
        for (int i = 0; i < starCheckBoxes.length; i++) {
            if (starCheckBoxes[i].isSelected()) {
                return i + 1;
            }
        }
        return 0;
    }

    public static void main(String[] args, Movie movie, User user) {
        new AddReview(movie, user);
    }
}
