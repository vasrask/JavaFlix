import java.io.Serializable;

/**
 * Sets the review parameters.
 *
 * @author Vasiliki Raskopoulou
 */
public class Review implements Serializable {
    private int ReviewStars;
    private String ReviewText;
    private User Reviewer;

    /**
     *
     * @param reviewStars      the review's stars.
     * @param reviewText       the review's text.
     * @param reviewer         the user who submits the review.
     *
     */

    public Review(int reviewStars,String reviewText,User reviewer) {
        this.ReviewStars = reviewStars;
        this.ReviewText = reviewText;
        this.Reviewer = reviewer;
    }

    /**
     * Sets the review's stars.
     *
     * @param numberOfStars the review's number of stars.
     */
    public void setReviewStars(int numberOfStars) {
        ReviewStars = numberOfStars;
    }

    /**
     * Sets the review's text.
     *
     * @param reviewText the review's text.
     */
    public void setReviewText(String reviewText) {
        ReviewText = reviewText;
    }

    /**
     * Sets the review's creator.
     *
     * @param reviewer the user who makes the review.
     */
    public void setReviewer(User reviewer) {
        Reviewer = reviewer;
    }


    /**
     * Returns the review's stars.
     *
     * @return review's number of stars.
     */
    public int getReviewStars() {
        return ReviewStars;
    }

    /**
     * Returns the review's text.
     *
     * @return review's text.
     */
    public String getReviewText() {
        return ReviewText;
    }

    /**
     * Returns the user who made the review.
     *
     * @return review's user.
     */
    public User getReviewer() {
        return Reviewer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review that = (Review) o;
        return ReviewStars == that.ReviewStars &&
                ReviewText.equals(that.ReviewText) &&
                Reviewer.equals(that.Reviewer);
    }
}
