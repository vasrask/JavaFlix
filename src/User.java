

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class User defines the characteristics of the user and methods for managing their Favorites' list.
 *
 * @author Vasiliki Raskopoulou
 */

public class User implements Serializable {
    private final String Username;
    private String Email;
    private String Password;
    private final UserCategory Category;
    private ArrayList<Program> Favorites;

    /**
     *
     * @param username   the user's username.
     * @param email    the user's email.
     * @param password the user's password.
     * @param category the user's category.
     */
    public User(String username, String email, String password, UserCategory category, ArrayList<Program> favorites) {
        this.Email = email;
        this.Username = username;
        this.Password = password;
        this.Category = category;
        this.Favorites = favorites;
    }

    /**
     * Returns the user's username.
     *
     * @return the user's username.
     */
    public String getUsername() {
        return this.Username;
    }

    /**
     * Returns the user's email.
     *
     * @return the user's email.
     */
    public String getEmail() {
        return Email;
    }

    /**
     * Returns the user's password.
     *
     * @return the user's password.
     */
    public String getPassword() {
        return Password;
    }

    /**
     * Returns the user's category.
     *
     * @return the user's category.
     */
    public UserCategory getCategory() {
        return Category;
    }

    public ArrayList<Program> getFavorites() {
        return Favorites;
    }

    /**
     * Stores a new entry in the Favorites' list.
     *
     * @param program the program to be inserted in Favorites.
     */
    public void addFavorite(Program program) {

        if (!Favorites.contains(program)) {
            Favorites.add(program);
        }
    }

    /**
     * Removes an entry from the Favorites' list.
     *
     * @param program the program to be removed from Favorites.
     */
    public void removeFavorite(Program program) {
        Favorites.remove(program);
    }

    /**
     * Sets the user's email.
     *
     * @param email the email.
     */
    public void setEmail(String email) {
        Email = email;
    }
    /**
     * Sets the user's favorite programs.
     *
     * @param favorites a list of the user's favortire programs.
     */
    public void setFavorites(ArrayList<Program> favorites) {
        Favorites = favorites;
    }
    /**
     * Sets the user's password.
     *
     * @param password the password.
     */
    public void setPassword(String password) {
        Password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return Username.equals(that.Username);
    }

}
