package iwasthere.android.ime.com.iwasthere;

import android.util.Log;

/**
 * Created by dududcbier on 30/04/17.
 */

public class UserSingleton {
    private static User ourInstance;

    public static User getInstance(String name, String nusp, Boolean teacher) {
        if (ourInstance == null) {
            ourInstance = new User(nusp, name, teacher);
        }
        return ourInstance;
    }

    public static User getInstance(String resp, Boolean teacher) {
        if (ourInstance == null) {
            ourInstance = new User(resp, teacher);
        }
        return ourInstance;
    }

    public static User getInstance() {
        if (ourInstance == null) {
            ourInstance = new User("-1", "-1", false);
            Log.d("UserSingleton", "WARNING: weird user");
        }
        return ourInstance;
    }

    public static void deleteInstance() {
        Log.d("UserSingleton", "Deleted");
        ourInstance = null;
    }

    private UserSingleton() {
    }
}
