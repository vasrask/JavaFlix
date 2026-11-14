package javaflix.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    @Test
    void favoritesCanBeAddedOnceAndRemoved() {
        User user = new User("subscriber", "subscriber@example.com", "secret",
                UserCategory.SUBSCRIBER, new ArrayList<>());
        Program favorite = new Movie("Favorite", "Description", MaturityRatings.PG, 100,
                Genres.COMEDY, 2021, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        user.addFavorite(favorite);
        user.addFavorite(favorite);
        assertEquals(1, user.getFavorites().size());

        user.removeFavorite(favorite);
        assertEquals(0, user.getFavorites().size());
    }
}
