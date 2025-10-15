import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Vasiliki Raskopoulou
 */

public class Season implements Serializable {
    private int No;
    private int Year;
    private ArrayList<Episode> Episodes;
    public Season(int no, int year, ArrayList<Episode> episodes){
        this.No = no;
        this.Year = year;
        this.Episodes = episodes;
    }
    @Override
    public String toString(){
        return "Season No: " + String.valueOf(No) + ", " + String.valueOf(Episodes.size()) + " Episodes";
    }
    /**
     * Parses a string of the form "Season No: 1, x Episodes, Season No: 2, y Episodes ..."
     * and returns an ArrayList of Season objects.
     *
     * @param input the string containing season information
     * @return an ArrayList of Season objects
     */
    public static ArrayList<Season> parseSeasons(String input) {
        ArrayList<Season> seasons = new ArrayList<>();
        String[] parts = input.split(", Season No: ");

        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("Season No: ")) {
                part = part.substring(11).trim(); // remove "Season No: " prefix
            }
            String[] details = part.split(", ");
            if (details.length == 2) {
                try {
                    int no = Integer.parseInt(details[0]);
                    int episodesCount = Integer.parseInt(details[1].split(" ")[0]);
                    ArrayList<Episode> episodes = new ArrayList<>(episodesCount);
                    for (int i = 0; i < episodesCount; i++) {
                        episodes.add(new Episode()); // Replace with actual Episode object if available
                    }
                    seasons.add(new Season(no,  0, episodes));
                } catch (NumberFormatException e) {
                    // Handle potential parsing error
                    System.err.println("Error parsing season details: " + part);
                }
            }
        }
        return seasons;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Season that = (Season) o;
        return Episodes.equals(that.Episodes) &&
                Year == that.Year &&
                No == that.No;

    }
}
