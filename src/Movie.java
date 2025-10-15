
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class Movie inherits Program and defines some additional characteristics of the movie.
 *
 * @author Vasiliki Raskopoulou
 */

public class Movie extends Program implements Serializable{

    private int Duration;

    /**
     * @param duration  the movie's duration (in minutes).
     */

    public Movie(String title, String description, MaturityRatings maturityRating, int duration, Genres genre, int year, ArrayList<String> mainCast, ArrayList<Program> moreLikeThis, ArrayList<Review> userReviews) {
        super(title, description, maturityRating, genre, year, mainCast, moreLikeThis, userReviews);
        this.Duration = duration;
    }


    /**
     * Sets the movie's duration (in minutes).
     *
     * @param duration the duration (in minutes).
     */
    public void setDuration(int duration) {
        Duration = duration;
    }

    /**
     * Returns the movie's duration (in minutes).
     *
     * @return duration (in minutes).
     */
    public int getDuration() {
        return Duration;
    }


    /**
     * Allows administrators to edit the details of a program.
     *
     * @param title          the program's new title.
     * @param description    the program's new description.
     * @param maturityRating the program's new maturity rating.
     * @param genre          the program's new genre.
     * @param mainCast       the program's new main cast.
     * @param moreLikeThis   the program's new similar programs.
     */
    @Override
    public void editProgram(String title, String description, MaturityRatings maturityRating, Genres genre, ArrayList<String> mainCast, ArrayList<Program> moreLikeThis) {
        super.editProgram(title, description, maturityRating, genre, mainCast, moreLikeThis);
    }
    public void editProgram(String title, String description, MaturityRatings maturityRating, Genres genre, ArrayList<String> mainCast, ArrayList<Program> moreLikeThis, int year, int duration) {
        super.editProgram(title, description, maturityRating, genre, mainCast, moreLikeThis);
        super.setYear(year);
        this.Duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie that = (Movie) o;
        return  Duration == that.Duration;
    }
}
