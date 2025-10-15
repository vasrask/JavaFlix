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
public class AddSeasons extends JFrame{
    private JTextField numberOfSeasonsField = new JTextField();
    private JTextField[] numberOfEpisodesFields;
    private JButton[] okButtons;
    public AddSeasons(ArrayList<Season> seasons){

        setTitle("Add Details");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));


        panel.add(new JLabel("How many Seasons are there?"));
        panel.add(numberOfSeasonsField);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> drawPanel(seasons, panel));
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
    private void drawPanel(ArrayList<Season> seasons, JPanel panel){
        int n = parseInt(numberOfSeasonsField.getText());
        numberOfEpisodesFields = new JTextField[n];
        okButtons = new JButton[n];
        for (int i = 0; i < n; i++){
            ArrayList<Episode> episodes = new ArrayList<>();
            int displayNumber = i + 1;
            panel.add(new JLabel("Season " + displayNumber));
            panel.add(new JLabel(""));
            panel.add(new JLabel("Year"));
            JTextField yearField = new JTextField();
            panel.add(yearField);
            int year = 0;
            panel.add(new JLabel("NumberOfEpisodes"));
            numberOfEpisodesFields[i] = new JTextField();
            panel.add(numberOfEpisodesFields[i]);
            int numberOfEpisodes = 0;
            okButtons[i] = new JButton();
            okButtons[i].setText("Add Episodes");
            panel.add(okButtons[i]);
            int finalI = i;
            okButtons[i].addActionListener(e -> addSeason(year, finalI, episodes, yearField, seasons));

        }
        JButton closeButton = new JButton("OK");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panel.add(closeButton);

    }
    private void addSeason(int year, int n, ArrayList<Episode> episodes, JTextField yearField, ArrayList<Season> seasons){
        year = parseInt(yearField.getText());
        new AddSeason(n, year, parseInt(numberOfEpisodesFields[n].getText()), episodes, seasons);
    }

}
