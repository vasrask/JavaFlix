
import java.io.Serializable;
import java.util.HashMap;

/**
 * Manages users through a HashMap. Accessing and editing of users occurs here.
 *
 * @author Vasiliki Raskopoulou
 */
public class Users extends Programs implements Serializable {

    protected static HashMap<Integer, User> UserCatalog = new HashMap<>();

    /**
     * Adds a user to the platform.
     * @param k id the user to be added.
     */
    public static void addUser(User k){
        int Id=setUserId(1);
        UserCatalog.put(Id,k);
    }

    /**
     * Returns the next available ID in the user's catalog.
     * @param id the starting ID number.
     * @return the unique ID.
     */
    public static int setUserId(int id){
        while(UserCatalog.containsKey(id)){
            id++;
        }
        return id;
    }


    /**
     * Returns the user's category based on their credentials.
     * @param username the user's username
     * @param password the user's password
     * @return one of the two categories (admin, subscriber) or null
     * in case no user with such credentials exists.
     */
    public static User loginUser(String username, String password){
        for (User user : UserCatalog.values()){
            if (username.equals(user.getUsername()) && password.equals(user.getPassword())){
                return user;
            }
        }
        return null;
    }


    /**
     * Checks if the username given at registration already exists.
     *
     * @param givenUsername the username given by the user.
     * @return true if the username already exists, false differently.
     */
    public static boolean existingUsername(String givenUsername){
        for (User user:UserCatalog.values()){
            if (user.getUsername().equals(givenUsername))
                return true;
        }
        return false;
    }


    /**
     * Allows user to change username
     * @param username the user's username
     * @param password the user's password
     * @param newEmail the user's new email.
     */
    public static void editEmail(String username,String password, String newEmail){
        for (User user : UserCatalog.values()){
            if (username.equals(user.getUsername()) && password.equals(user.getPassword())){
                user.setEmail(newEmail);
            }
        }
    }

    /**
     * Allows user to change password
     * @param username the user's username
     * @param password the user's password
     * @param newPassword the user's new password.
     */
    public static void editUserPassword(String username,String password, String newPassword){
        for (User user : UserCatalog.values()){
            if (username.equals(user.getUsername()) && password.equals(user.getPassword())){
                user.setPassword(newPassword);
            }
        }
    }

}