package javaflix.service;

import javaflix.model.User;
import javaflix.model.UserCategory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UsersTest {
    @Test
    void loginRequiresMatchingUsernameAndPassword() {
        String username = "user-" + UUID.randomUUID();
        User user = new User(username, "user@example.com", "correct-password",
                UserCategory.SUBSCRIBER, new ArrayList<>());
        Users.addUser(user);

        assertSame(user, Users.loginUser(username, "correct-password"));
        assertNull(Users.loginUser(username, "wrong-password"));
        assertNull(Users.loginUser("missing-" + UUID.randomUUID(), "correct-password"));
    }

    @Test
    void usernameLookupRecognizesExistingAccounts() {
        String username = "existing-" + UUID.randomUUID();
        Users.addUser(new User(username, "user@example.com", "secret",
                UserCategory.SUBSCRIBER, new ArrayList<>()));

        assertTrue(Users.existingUsername(username));
        assertFalse(Users.existingUsername("missing-" + UUID.randomUUID()));
    }
}
