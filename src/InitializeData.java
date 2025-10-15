import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * This class initializes all movies, series, and users in JavaFlix.
 * All movie and series details were taken from <a href="https://www.imdb.com/">IMDb</a>
 *
 * @author Vasiliki Raskopoulou
 */

public class InitializeData {

    public static void main(String[] args) throws IOException {
        // Initialize Movies
        String description1 = "An insomniac office worker and a devil-may-care soap maker form an underground fight club that evolves into much more.";
        ArrayList<String> mainCast1 = new ArrayList<>();
        mainCast1.add("Edward Norton");
        mainCast1.add("Brad Pitt");
        mainCast1.add("Helena Bonham Carter");
        Program object1 = new Movie("Fight Club", description1, MaturityRatings.R, 139, Genres.DRAMA, 1999, mainCast1, new ArrayList<>(), new ArrayList<>());

        String description2 = "A vampire tells his epic life story: love, betrayal, loneliness, and hunger.";
        ArrayList<String> mainCast2 = new ArrayList<>();
        mainCast2.add("Brad Pitt");
        mainCast2.add("Tom Cruise");
        mainCast2.add("Antonio Banderas");
        Movie object2 = new Movie("Interview with the Vampire: The Vampire Chronicles", description2, MaturityRatings.R, 123, Genres.HORROR, 1994, mainCast2, new ArrayList<>(), new ArrayList<>());

        String description3 = "The story of American actress Marilyn Monroe, covering her love and professional lives.";
        ArrayList<String> mainCast3 = new ArrayList<>();
        mainCast3.add("Ana de Armas");
        mainCast3.add("Lily Fisher");
        mainCast3.add("Julianne Nicholson");
        Movie object3 = new Movie("Blonde", description3, MaturityRatings.NC17, 167, Genres.DRAMA, 2022, mainCast3, new ArrayList<>(), new ArrayList<>());

        String description4 = "An insurance salesman discovers his whole life is actually a reality TV show.";
        ArrayList<String> mainCast4 = new ArrayList<>();
        mainCast4.add("Jim Carrey");
        mainCast4.add("Ed Harris");
        mainCast4.add("Laura Linney");
        Movie object4 = new Movie("The Truman Show", description4, MaturityRatings.PG, 110, Genres.COMEDY, 1998, mainCast4, new ArrayList<>(), new ArrayList<>());

        String description5 = "To save her father from death in the army, a young maiden secretly goes in his place and becomes one of China's greatest heroines in the process.";
        ArrayList<String> mainCast5 = new ArrayList<>();
        mainCast5.add("Ming-Na Wen");
        mainCast5.add("Eddie Murphy");
        mainCast5.add("BD Wong");
        Movie object5 = new Movie("Mulan", description5, MaturityRatings.G, 87, Genres.ADVENTURE, 1998, mainCast5, new ArrayList<>(), new ArrayList<>());

        String description6 = "When Earth becomes uninhabitable in the future, a farmer and ex-NASA pilot, Joseph Cooper, is tasked to pilot a spacecraft, along with a team of researchers, to find a new planet for humans.";
        ArrayList<String> mainCast6 = new ArrayList<>();
        mainCast6.add("Matthew McConaughey");
        mainCast6.add("Anne Hathaway");
        mainCast6.add("Jessica Chastain");
        Movie object6 = new Movie("Interstellar", description6, MaturityRatings.PG13, 169, Genres.SCIFI, 2014, mainCast6, new ArrayList<>(), new ArrayList<>());

        // Initialize Series
        ArrayList<Episode> friendsSeason1Episodes = new ArrayList<>();
        friendsSeason1Episodes.add(new Episode("The One Where Monica Gets a Roommate", 1, 22));
        friendsSeason1Episodes.add(new Episode("The One with the Sonogram at the End", 2, 22));

        ArrayList<Episode> friendsSeason2Episodes = new ArrayList<>();
        friendsSeason2Episodes.add(new Episode("The One with Ross's New Girlfriend", 1, 22));
        friendsSeason2Episodes.add(new Episode("The One with the Breast Milk", 2, 22));

        ArrayList<Season> friendsSeasons = new ArrayList<>();
        friendsSeasons.add(new Season(1, 1994, friendsSeason1Episodes));
        friendsSeasons.add(new Season(2, 1995, friendsSeason2Episodes));

        ArrayList<String> friendsMainCast = new ArrayList<>();
        friendsMainCast.add("Jennifer Aniston");
        friendsMainCast.add("Courteney Cox");
        friendsMainCast.add("Lisa Kudrow");
        ArrayList<YearRange> years = new ArrayList<>();
        years.add(new YearRange(1994,2004));
        Series friends = new Series("Friends", "Follows the personal and professional lives of six twenty to thirty-something-year-old friends living in Manhattan.", MaturityRatings.PG, Genres.COMEDY, friendsMainCast, new ArrayList<>(), friendsSeasons, years, new ArrayList<>());

        ArrayList<Episode> breakingBadSeason1Episodes = new ArrayList<>();
        breakingBadSeason1Episodes.add(new Episode("Pilot", 1, 58));
        breakingBadSeason1Episodes.add(new Episode("Cat's in the Bag...", 2, 48));

        ArrayList<Episode> breakingBadSeason2Episodes = new ArrayList<>();
        breakingBadSeason2Episodes.add(new Episode("Seven Thirty-Seven", 1, 47));
        breakingBadSeason2Episodes.add(new Episode("Grilled", 2, 47));

        ArrayList<Season> breakingBadSeasons = new ArrayList<>();
        breakingBadSeasons.add(new Season(1, 2008, breakingBadSeason1Episodes));
        breakingBadSeasons.add(new Season(2, 2009, breakingBadSeason2Episodes));

        ArrayList<String> breakingBadMainCast = new ArrayList<>();
        breakingBadMainCast.add("Bryan Cranston");
        breakingBadMainCast.add("Aaron Paul");
        breakingBadMainCast.add("Anna Gunn");
        ArrayList<YearRange> years2 = new ArrayList<>();
        years2.add(new YearRange(2008,2013));
        Series breakingBad = new Series("Breaking Bad", "A high school chemistry teacher turned methamphetamine manufacturer partners with a former student to secure his family's future.", MaturityRatings.R, Genres.DRAMA, breakingBadMainCast, new ArrayList<>(), breakingBadSeasons,years2, new ArrayList<>());

        // Initialize Users
        ArrayList<Program> favorites1 = new ArrayList<>();
        favorites1.add(object3);
        ArrayList<Program> favorites2 = new ArrayList<>();
        favorites2.add(object3);
        favorites2.add(object4);
        User object7 = new User("user1", "user1@gmail.com", "password1", UserCategory.SUBSCRIBER, favorites1);
        User object8 = new User("user2", "user2@gmail.com", "password2", UserCategory.SUBSCRIBER, favorites2);
        User object9 = new User("admin1", "admin1@gmail.com", "password1", UserCategory.ADMIN, new ArrayList<>());
        User object10 = new User("admin2", "admin2@gmail.com", "password2", UserCategory.ADMIN, new ArrayList<>());

        // Initialize Reviews
        Review review11 = new Review(4, "Very Good", object7);
        Review review12 = new Review(4, "Very Good", object8);
        object1.addReview(review11);
        object1.addReview(review12);

        Review review21 = new Review(2, "Good", object7);
        Review review22 = new Review(4, "Very Good", object8);
        object2.addReview(review21);
        object2.addReview(review22);

        Review review31 = new Review(5, "Great Movie", object7);
        Review review32 = new Review(5, "Amazing!", object8);
        object3.addReview(review31);
        object3.addReview(review32);

        Review review41 = new Review(1, "Didn't like it", object7);
        Review review42 = new Review(5, "Excellent!", object8);
        object4.addReview(review41);
        object4.addReview(review42);

        Review review51 = new Review(5, "Amazing!", object7);
        Review review52 = new Review(5, "Great Movie", object8);
        object5.addReview(review51);
        object5.addReview(review52);

        Review review61 = new Review(1, "Boring", object7);
        Review review62 = new Review(4, "Great", object8);
        object6.addReview(review61);
        object6.addReview(review62);

        // Write Movies and Series to file
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("programs.dat"))) {
            out.writeObject(object1);
            out.writeObject(object2);
            out.writeObject(object3);
            out.writeObject(object4);
            out.writeObject(object5);
            out.writeObject(object6);
            out.writeObject(friends);
            out.writeObject(breakingBad);
        }

        // Write Users to file
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            out.writeObject(object7);
            out.writeObject(object8);
            out.writeObject(object9);
            out.writeObject(object10);
        }
    }
}