import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import static java.lang.Integer.parseInt;
/**
 *
 * @author Vasiliki Raskopoulou
 */
public class AddSeason extends JFrame{
    public AddSeason(int n, int year, int m, ArrayList<Episode> episodes, ArrayList<Season> seasons){
        setTitle("Add Details");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));

        for (int j = 1; j <= m; j++){
            panel.add(new JLabel("Episode " + j));
            JTextField durationField = new JTextField("Duration (in minutes)");
            panel.add(durationField);
            panel.add(new JLabel("Title"));
            JTextField titleField = new JTextField();
            panel.add(titleField);
            JButton addEpisodeButton = new JButton("Add Episode");
            int finalJ = j;
            addEpisodeButton.addActionListener(e -> addEpisode(finalJ, parseInt(durationField.getText()), titleField.getText(), episodes));

        }

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> addSeasonToList(n, year, episodes, seasons));
        panel.add(okButton);
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
    private void addEpisode(int n, int duration, String title, ArrayList<Episode> episodes) {
        Episode episode = new Episode(title, n - 1, duration);
        episodes.add(episode);
    }


        private void addSeasonToList(int n, int year, ArrayList<Episode> episodes, ArrayList<Season> seasons){
        Season season = new Season(n, year, episodes);
        seasons.add(season);
        JOptionPane.showMessageDialog(this, "Season added successfully!");

    }
}
