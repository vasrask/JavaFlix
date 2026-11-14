package javaflix.service;

import javaflix.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProgramsTest {
    @Test
    void titleSearchIsCaseInsensitiveAndSupportsPartialTitles() {
        Program movie = addMovie("Starlight " + UUID.randomUUID(), Genres.DRAMA,
                MaturityRatings.PG, 2020, 0);

        ArrayList<Program> results = Programs.returnProgramOfTitle("STARLIGHT");

        assertNotNull(results);
        assertTrue(results.contains(movie));
        assertNull(Programs.returnProgramOfTitle("no-such-title-" + UUID.randomUUID()));
    }

    @Test
    void yearSearchMatchesSeriesAiringRanges() {
        String title = "Series " + UUID.randomUUID();
        Series series = new Series(title, "Description", MaturityRatings.PG, Genres.DRAMA,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(Arrays.asList(new YearRange(2012, 2018))), new ArrayList<>());
        Programs.addProgram(series);

        ArrayList<Program> results = Programs.returnProgramsOfYear(2015);

        assertNotNull(results);
        assertTrue(results.contains(series));
    }

    @Test
    void combinedFiltersMustAllMatch() {
        Program match = addMovie("Matching title " + UUID.randomUUID(), Genres.DRAMA,
                MaturityRatings.PG, 2020, 4);
        addMovie("Matching title " + UUID.randomUUID(), Genres.COMEDY,
                MaturityRatings.PG, 2020, 5);
        addMovie("Different title " + UUID.randomUUID(), Genres.DRAMA,
                MaturityRatings.PG, 2020, 5);

        ArrayList<Program> results = Programs.returnProgramsByMultipleFilters(
                "matching title", MaturityRatings.PG, 2020, Genres.DRAMA, "", 3);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertSame(match, results.get(0));
    }

    @Test
    void noFiltersAndUnmatchedFiltersReturnNull() {
        assertNull(Programs.returnProgramsByMultipleFilters("", null, 0, null, "", 0));
        assertNull(Programs.returnProgramsByMultipleFilters(
                "unmatched-" + UUID.randomUUID(), null, 0, null, "", 0));
    }

    private Program addMovie(String title, Genres genre, MaturityRatings rating, int year, int stars) {
        Program movie = new Movie(title, "Description", rating, 100, genre, year,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        if (stars > 0) {
            User reviewer = new User("reviewer-" + UUID.randomUUID(), "reviewer@example.com", "secret",
                    UserCategory.SUBSCRIBER, new ArrayList<>());
            movie.addReview(new Review(stars, "Test review", reviewer));
        }
        Programs.addProgram(movie);
        return movie;
    }
}
