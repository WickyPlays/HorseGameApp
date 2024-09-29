package me.thienbao860.android.horsegameapp;

import java.util.Collections;
import java.util.List;

import me.thienbao860.android.horsegameapp.obj.User;

public class UserManager {

    public static List<User> USER_DATABASE = Collections.singletonList(new User("admin", "1"));;

    public static User login(String username, String password) {
        for (User user : USER_DATABASE) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }
}
