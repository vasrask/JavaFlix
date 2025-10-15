
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class Series inherits Program and defines some additional characteristics of the series.
 *
 * @author Vasiliki Raskopoulou
 */

public class Series extends Program{
    private ArrayList<Season> Seasons;
    private ArrayList<YearRange> YearRanges;
    /**
     * @param title          the series' title.
     * @param description    a short description of the series.
     * @param maturityRating the maturity rating of the series.
     * @param genre          the series' genre.
     * @param mainCast       a list of the main cast.
     * @param userReviews    a list of the series' reviews.
     * @param seasons       a list of the series' seasons.
     * @param moreLikeThis   a list of series similar to the series.
     */
    public Series(String title, String description, MaturityRatings maturityRating, Genres genre, ArrayList<String> mainCast, ArrayList<Review> userReviews,ArrayList<Season> seasons, ArrayList<YearRange> yearRanges, ArrayList<Program> moreLikeThis) {
        super(title, description, maturityRating, genre, yearRanges.get(0).getStartYear(), mainCast, moreLikeThis, userReviews);
        this.Seasons = seasons;
        this.YearRanges = yearRanges;
    }

    /**
     * Sets the seasons of the series.
     *
     * @param seasons the list of the series' seasons.
     */
    public void setSeasons(ArrayList<Season> seasons) {
        Seasons = seasons;
    }

    /**
     * Sets the years the series is airing.
     *
     * @param yearRanges the list of the year ranges the series was airing.
     */

    public void setYears(ArrayList<YearRange> yearRanges) {
        this.YearRanges = new ArrayList<>(yearRanges);
    }


    /**
     * Returns the seasons of the series.
     *
     * @return list of the series' seasons.
     */
    public ArrayList<Season> getSeasons() {
        return Seasons;
    }

    /**
     * Returns the years the series was airing.
     *
     * @return a list of the year ranges.
     */

    public ArrayList<YearRange> getYears() {
        return YearRanges;
    }

    public boolean isYearInAnyRange(int year) {
        for (YearRange range : YearRanges) {
            if (range.isYearInRange(year)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void editProgram(String title, String description, MaturityRatings maturityRating, Genres genre, ArrayList<String> mainCast, ArrayList<Program> moreLikeThis) {
        super.editProgram(title, description, maturityRating, genre, mainCast, moreLikeThis);
    }
    public void editProgram(String title, String description, MaturityRatings maturityRating, Genres genre, ArrayList<String> mainCast, ArrayList<Program> moreLikeThis, ArrayList<Season> seasons, ArrayList<YearRange> yearRanges) {
        super.editProgram(title, description, maturityRating, genre, mainCast, moreLikeThis);
        super.setYear(yearRanges.get(0).getStartYear());
        this.YearRanges = yearRanges;
        this.Seasons = seasons;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        if (!(o instanceof Series)) return false;
        Series series = (Series) o;
        return Objects.equals(YearRanges, series.YearRanges) &&
                Seasons.equals(series.Seasons);
    }
}
