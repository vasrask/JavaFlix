import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Vasiliki Raskopoulou
 */

public class YearRange implements Serializable {
    private int startYear;
    private int endYear;

    public YearRange(int startYear, int endYear) {
        this.startYear = startYear;
        this.endYear = endYear;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public boolean isYearInRange(int year) {
        return year >= startYear && year <= endYear;
    }

    @Override
    public String toString() {
        return startYear + "-" + endYear;
    }
    /**
     * Converts a string to a YearRange.
     *
     * @param input the input string in the format "YYYY-YYYY".
     * @return the corresponding YearRange object.
     * @throws IllegalArgumentException if the input string is invalid.
     */
    public static YearRange fromString(String input) {
        if (input == null || !input.matches("\\d{4}-\\d{4}")) {
            throw new IllegalArgumentException("Invalid format. Expected format: YYYY-YYYY");
        }

        String[] parts = input.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid format. Expected format: YYYY-YYYY");
        }

        try {
            int startYear = Integer.parseInt(parts[0]);
            int endYear = Integer.parseInt(parts[1]);

            if (startYear > endYear) {
                throw new IllegalArgumentException("Start year cannot be greater than end year.");
            }

            return new YearRange(startYear, endYear);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid year format. Years must be integers.", e);
        }
    }

    /**
     * Converts a string of year ranges into an ArrayList of YearRange objects.
     *
     * @param input the input string in the format "YYYY-YYYY, YYYY-YYYY, ...".
     * @return an ArrayList of YearRange objects.
     * @throws IllegalArgumentException if the input string is invalid.
     */
    public static ArrayList<YearRange> fromStringToYearRanges(String input) {
        ArrayList<YearRange> yearRanges = new ArrayList<>();

        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input string is null or empty.");
        }

        String[] ranges = input.split("\\s*,\\s*");

        for (String range : ranges) {
            range = range.trim();
            try {
                YearRange yearRange = YearRange.fromString(range);
                yearRanges.add(yearRange);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Error parsing year range: " + range, e);
            }
        }

        return yearRanges;
    }

}