import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class Program defines the characteristics of the program and handles its reviews.
 *
 * @author Vasiliki Raskopoulou
 */
public class Program implements Serializable {
    private String Title;
    private String Description;
    private MaturityRatings MaturityRating;
    private Genres Genre;
    private int Year;
    private ArrayList<String> MainCast;
    private ArrayList<Program> MoreLikeThis;
    private ArrayList<Review> UserReviews;
    private int NumberOfReviews;
    private double TotalStars;


    /**
     *
     * @param title          the program's title.
     * @param description    a short description of the program.
     * @param maturityRating the maturity rating of the program.
     * @param genre          the program's genre.
     * @param year           the year of the program.
     * @param mainCast       a list of the main cast.
     * @param userReviews      a list of the program's reviews.
     */

    public Program(String title, String description, MaturityRatings maturityRating, Genres genre, int year, ArrayList<String> mainCast, ArrayList<Program> moreLikeThis, ArrayList<Review> userReviews) {
        this.Title = title;
        this.Description = description;
        this.MaturityRating = maturityRating;
        this.Genre = genre;
        this.Year = year;
        this.MainCast = mainCast;
        this.MoreLikeThis = moreLikeThis;
        this.UserReviews = userReviews;
        this.NumberOfReviews = 0;
        this.TotalStars = 0.0;
    }


    /**
     * Sets the program's title.
     *
     * @param title the title.
     */
    public void setTitle(String title) {
        Title = title;
    }

    /**
     * Sets the program's description.
     *
     * @param description the description.
     */
    public void setDescription(String description) {
        Description = description;
    }

    /**
     * Sets the program's maturity rating.
     *
     * @param maturityRating the maturity rating.
     */
    public void setMaturityRating(MaturityRatings maturityRating) {
        MaturityRating = maturityRating;
    }



    /**
     * Sets the program's genre.
     *
     * @param genre the genre.
     */
    public void setGenre(Genres genre) {
        Genre = genre;
    }

    /**
     * Sets the program's release year.
     *
     * @param year the year.
     */
    public void setYear(int year) {
        Year = year;
    }

    /**
     * Sets the program's main cast.
     *
     * @param mainCast the list of actors of the main cast.
     */
    public void setMainCast(ArrayList<String> mainCast) {
        MainCast = mainCast;
    }

    /**
     * Sets the program's similar programs.
     *
     * @param moreLikeThis the list of programs similar to the program.
     */
    public void setMoreLikeThis(ArrayList<Program> moreLikeThis) {
        MoreLikeThis = moreLikeThis;
    }

    /**
     * Sets the program's reviews.
     *
     * @param reviews the list of reviews.
     */
    public void setUserReviews(ArrayList<Review> reviews) {
        UserReviews = reviews;
    }


    /**
     * Returns the program's title.
     *
     * @return title.
     */
    public String getTitle() {
        return Title;
    }

    /**
     * Returns the program's description.
     *
     * @return description.
     */
    public String getDescription() {
        return Description;
    }

    /**
     * Returns the program's maturity rating.
     *
     * @return maturity rating.
     */
    public MaturityRatings getMaturityRating() {
        return MaturityRating;
    }


    /**
     * Returns the program's genre.
     *
     * @return genre.
     */
    public Genres getGenre() {
        return Genre;
    }

    /**
     * Returns the release year of the program.
     *
     * @return release year of the program.
     */
    public int getYear() { return Year; }

    /**
     * Returns the main cast of the program.
     *
     * @return list of the main cast.
     */
    public ArrayList<String> getMainCast() { return MainCast; }

    /**
     * Returns the program's similar programs.
     *
     * @return list of programs similar to this program.
     */
    public ArrayList<Program> getMoreLikeThis() { return MoreLikeThis; }

    /**
     * Returns the program's reviews.
     *
     * @return list of reviews.
     */
    public ArrayList<Review> getUserReviews() { return UserReviews; }

    /**
     * Returns the number of reviews of the program.
     *
     * @return number of reviews.
     */
    public int getNumberOfReviews() { return NumberOfReviews; }
    /**
     * Returns the number of stars of the program.
     *
     * @return  number of stars.
     */
    public double getNumberOfStars() { return TotalStars; }
    /**
     * Adds a review to the program. Recalculates the mean of total stars.
     * @param review the new review to be added.
     */
    public void addReview(Review review){
        boolean editReview = false;
        int stars = review.getReviewStars();
        for (Review rvw : UserReviews ){
            if (rvw.getReviewer().equals(review.getReviewer())){
                TotalStars = ((TotalStars * NumberOfReviews - rvw.getReviewStars()) + stars)/(NumberOfReviews);
                rvw.setReviewText(review.getReviewText());
                rvw.setReviewStars(stars);
                editReview = true;
                break;
            }
        }
        if(!editReview){
            UserReviews.add(review);
            if (NumberOfReviews == 0){
                TotalStars = stars;
                NumberOfReviews = 1;
            }
            else{
                TotalStars = (TotalStars * NumberOfReviews + stars)/(NumberOfReviews + 1);
                NumberOfReviews = NumberOfReviews +1;
            }

        }

    }
    /**
     * Removes a review from the program. Recalculates the mean of total stars.
     * @param review the review to be removed.
     */
    public void deleteReview(Review review) {
        int stars = review.getReviewStars();
        for (Review rvw: UserReviews){
            if (rvw.equals(review)){
                UserReviews.remove(review);
                break;}
        }
        NumberOfReviews = NumberOfReviews - 1;
        if (NumberOfReviews == 0) {
            TotalStars = 0;
        }
        else {
            TotalStars = (TotalStars * (NumberOfReviews + 1) - stars) / (NumberOfReviews);
        }
    }
    /**
     * Allows administrators to edit the details of a program.
     *
     * @param title          the program's new title.
     * @param description    the program's new description.
     * @param maturityRating the program's new maturity rating.
     * @param genre          the program's new genre.
     * @param mainCast       the program's new main cast.
     * @param moreLikeThis   the program's similar programs.
     */
    public void editProgram(String title, String description, MaturityRatings maturityRating, Genres genre,  ArrayList<String> mainCast, ArrayList<Program> moreLikeThis) {
        Title = title;
        Description = description;
        MaturityRating = maturityRating;
        Genre = genre;
        MainCast = mainCast;
        MoreLikeThis = moreLikeThis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Program that = (Program) o;
        return Title.equals(that.Title) &&
                Description.equals(that.Description) &&
                MaturityRating.equals(that.MaturityRating) &&
                Genre.equals(that.Genre) &&
                Year == that.Year &&
                MainCast.equals(that.MainCast) &&
                UserReviews.equals(that.UserReviews) &&
                MoreLikeThis.equals(that.MoreLikeThis);
    }
}
