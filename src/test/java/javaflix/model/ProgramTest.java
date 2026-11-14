package javaflix.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProgramTest {
    @Test
    void addingReviewUpdatesAverageAndEditingReviewDoesNotAddDuplicate() {
        User reviewer = new User("reviewer", "reviewer@example.com", "secret",
                UserCategory.SUBSCRIBER, new ArrayList<>());
        Program program = new Movie("Test Movie", "Description", MaturityRatings.PG, 100,
                Genres.DRAMA, 2020, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        program.addReview(new Review(2, "Okay", reviewer));
        assertEquals(2.0, program.getNumberOfStars());
        assertEquals(1, program.getNumberOfReviews());

        program.addReview(new Review(4, "Better on rewatch", reviewer));

        assertEquals(4.0, program.getNumberOfStars());
        assertEquals(1, program.getNumberOfReviews());
        assertEquals(1, program.getUserReviews().size());
        assertEquals("Better on rewatch", program.getUserReviews().get(0).getReviewText());
    }

    @Test
    void deletingReviewRecalculatesAverage() {
        User firstReviewer = new User("first", "first@example.com", "secret",
                UserCategory.SUBSCRIBER, new ArrayList<>());
        User secondReviewer = new User("second", "second@example.com", "secret",
                UserCategory.SUBSCRIBER, new ArrayList<>());
        Program program = new Movie("Review Movie", "Description", MaturityRatings.PG, 100,
                Genres.DRAMA, 2020, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Review firstReview = new Review(2, "Low", firstReviewer);
        program.addReview(firstReview);
        program.addReview(new Review(4, "High", secondReviewer));

        program.deleteReview(firstReview);

        assertEquals(4.0, program.getNumberOfStars());
        assertEquals(1, program.getNumberOfReviews());
        assertEquals(1, program.getUserReviews().size());
    }
}
