package iwasthere.android.ime.com.iwasthere;

import android.util.Log;

/**
 * Created by nanda on 09/05/17.
 */

public class SeminarSingleton {

    private static Seminar ourInstance;

    public static Seminar getInstance(String name, int id) {
        if (ourInstance == null) {
            ourInstance = new Seminar(name, id);
        }
        return ourInstance;
    }

    public static Seminar updateInstance(String name, int id) {
        ourInstance = new Seminar(name, id);
        return ourInstance;
    }

    public static Seminar getInstance() {
        if (ourInstance == null) {
            ourInstance = new Seminar("-1", -1);
            Log.d("SeminarSingleton", "WARNING: weird seminar");
        }
        return ourInstance;
    }

    public static Seminar deleteInstance() {
        Log.d("SeminarSingleton", "Deleted");
        ourInstance = null;

        return ourInstance;
    }

    private SeminarSingleton() {
    }

}
