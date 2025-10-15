import java.io.Serializable;

/**
 * Enumerator for the movie genres.
 *
 * @author Vasiliki Raskopoulou
 */
public enum Genres implements Serializable {
    COMEDY, DRAMA, SCIFI, ADVENTURE, HORROR;

    /**
     * Converts a string to a Genres enum.
     *
     * @param genre the genre as a string.
     * @return the corresponding Genres enum.
     * @throws IllegalArgumentException if the string does not match any genre.
     */
    public static Genres fromString(String genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }
        switch (genre.toLowerCase()) {
            case "comedy":
                return COMEDY;
            case "drama":
                return DRAMA;
            case "sci-fi":
            case "scifi":
                return SCIFI;
            case "horror":
                return HORROR;
            case "adventure":
                return ADVENTURE;
            default:
                throw new IllegalArgumentException("Unknown genre: " + genre);
        }
    }
}

