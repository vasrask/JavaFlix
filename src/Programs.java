
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Manages programs through a HashMap. Accessing of programs occurs here.
 *
 * @author Vasiliki Raskopoulou
 */

public class Programs implements Serializable {

    static HashMap<Integer, Program> ProgramCatalog = new HashMap<>();

    /**
     * Adds a program to the catalog.
     *
     * @param program the program to be added.
     */
    public static void addProgram(Program program) {
        int Id = setProgramId(1);
        ProgramCatalog.put(Id, program);
    }

    /**
     * Removes a program from the catalog.
     *
     * @param program the program to be removed.
     */
    public static void deleteProgram(Program program) {
        int Id = ProgramCatalog.size();
        for (Map.Entry entry : ProgramCatalog.entrySet()) {
            if (program.equals((Program) entry.getValue())) {
                Id = (Integer) entry.getKey();
            }
        }
        ProgramCatalog.remove(Id, program);
    }
    /**
     * Returns the next available ID in the program's catalog.
     *
     * @param id the starting ID number.
     * @return the unique ID.
     */
    public static int setProgramId(int id) {
        while (ProgramCatalog.containsKey(id)) {
            id++;
        }
        return id;
    }


    /**
     * Returns all programs of the given title.
     *
     * @param title the title given by the user.
     * @return list of programs of the given title.
     */
    public static ArrayList<Program> returnProgramOfTitle(String title) {
        ArrayList<Program> programs = new ArrayList<>();
        boolean FoundProgram = false;
        String lowerCaseTitle = title.toLowerCase();
        for (Program program : ProgramCatalog.values()) {
            String programTitle = program.getTitle().toLowerCase();
            if (programTitle.contains(lowerCaseTitle)) {
                FoundProgram = true;
                programs.add(program);
            }
        }
        if (!FoundProgram) {
            return null;
        } else {
            return programs;
        }
    }
    /**
     * Returns all programs of the given year.
     *
     * @param year the year given by the user.
     * @return list of programs of the given year.
     */
    public static ArrayList<Program> returnProgramsOfYear(int year) {
        ArrayList<Program> programs = new ArrayList<>();
        boolean FoundProgram = false;
        for (Program program : ProgramCatalog.values()) {
            if (program instanceof Series) {
                Series series = (Series) program;
                if (series.isYearInAnyRange(year)) {
                    programs.add(series);
                    FoundProgram = true;
                }
            } else {
                if (program.getYear() == year) {
                    programs.add(program);
                    FoundProgram = true;
                }
            }
        }

        if (!FoundProgram) {
            return null;
        } else {
            return programs;
        }
    }

    /**
     * Returns all programs of the given genre.
     *
     * @param genre the genre set by the user.
     * @return list of programs of the given genre.
     */
    public static ArrayList<Program> returnProgramsOfGenre(Genres genre) {
        ArrayList<Program> programs = new ArrayList<>();
        boolean FoundProgram = false;
        for (Program program : ProgramCatalog.values()) {
            if (program.getGenre() == genre) {
                FoundProgram = true;
                programs.add(program);
            }
        }
        if (!FoundProgram) {
            return null;
        }
        else {
            return programs;
        }
    }
    /**
     * Return all programs of an actor given by the user.
     *
     * @param actor the actor's name given by the user.
     * @return list of programs with given actor.
     */
    public static ArrayList<Program> returnProgramsWithActor(String actor) {
        ArrayList<Program> programs = new ArrayList<>();
        boolean FoundProgram = false;
        for (Program program : ProgramCatalog.values()) {
            if (program.getMainCast().contains(actor)) {
                FoundProgram = true;
                programs.add(program);
            }
        }
        if (!FoundProgram) {
            return null;
        }
        else {
            return programs;
        }
    }
    /**
     * Returns all programs of the given maturity rating.
     *
     * @param maturityRating the maturityRating given by the user.
     * @return list of programs of the given maturityRating.
     */
    public static ArrayList<Program> returnProgramsOfMaturityRating(MaturityRatings maturityRating) {
        ArrayList<Program> programs = new ArrayList<>();
        boolean FoundProgram = false;
        for (Program program : ProgramCatalog.values()) {
            if (program.getMaturityRating()  == maturityRating) {
                FoundProgram = true;
                programs.add(program);
            }
        }
        if (!FoundProgram) {
            return null;
        }
        else {
            return programs;
        }
    }
    /**
     * Returns all programs with number of stars equal or greater than the given one.
     *
     * @param stars the review stars set by the user.
     * @return list of programs with equal or higher review rating.
     */
    public static ArrayList<Program> returnProgramsWithReview(int stars) {
        ArrayList<Program> programs = new ArrayList<>();
        boolean FoundProgram = false;
        for (Program program : ProgramCatalog.values()) {
            if (program.getNumberOfStars()  >= stars) {
                FoundProgram = true;
                programs.add(program);
            }
        }
        if (!FoundProgram) {
            return null;
        }
        else {
            return programs;
        }
    }
    /**
     * Returns all programs that match the filters given.
     * @param title the desired title of the program.
     * @param maturityRating the desired maturity rating of the program(s).
     * @param year the desired year of the program(s).
     * @param genre the desired genre of the program(s).
     * @param actor the desired actor of the program(s).
     * @param stars the desired stars of the program(s).
     * @return list of programs matching the filters.
     *
     */
    public static ArrayList<Program> returnProgramsByMultipleFilters(String title, MaturityRatings maturityRating, int year, Genres genre, String actor,int stars) {
        if (!title.isEmpty()) {
            return returnProgramOfTitle(title);
        }
        else if (!(maturityRating == null)) {
            return returnProgramsOfMaturityRating(maturityRating);
        }
        else if (!(year == 0)) {
            return returnProgramsOfYear(year);
        }
        else if (!(genre == null))
        {
            return returnProgramsOfGenre(genre);
        }
        else if (!actor.equals(""))
        {
            return returnProgramsWithActor(actor);
        }
        else if (!(stars == 0))
        {
            return returnProgramsWithReview(stars);
        }
        return null;
    }
    /**
     *
     * Displays the program's details in the system's standard output stream.
     * @param program the program whose details are displayed.
     */
    public static void displayProgram(Program program) {
        System.out.println("Title: " + program.getTitle());
        System.out.println("Description: " + program.getDescription());
        System.out.println("Maturity Rating: " + program.getMaturityRating());
        System.out.println("Genre: " + program.getGenre());
        System.out.print("Main Cast: ");
        for (String actor : program.getMainCast()) {
            System.out.print(actor + ' ');
        }
        System.out.println();
        for (Review review : program.getUserReviews()) {
            System.out.print(review.getReviewer().getUsername() + ':' + review.getReviewText() + ',' + review.getReviewStars());
            System.out.println();
        }
        System.out.println("Stars: "+String.format("%.1f",program.getNumberOfStars())+" Number of Reviews: " + program.getNumberOfReviews());
        for (Program similarprogram : program.getMoreLikeThis()) {
            System.out.print(similarprogram.getTitle() + ',');
        }
        System.out.println();

    }

}