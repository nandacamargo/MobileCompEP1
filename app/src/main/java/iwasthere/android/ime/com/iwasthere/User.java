package iwasthere.android.ime.com.iwasthere;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dududcbier on 29/04/17.
 */

public class User {

    private String nusp;
    private String name;

    public User(String nusp, String name) {
        this.nusp = nusp;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getNusp() {
        return nusp;
    }

    public void setNusp(String nusp) {
        this.nusp = nusp;
    }

    public static ArrayList<User> getSeminars(JSONArray seminarsJSON) {
        ArrayList<User> seminars = new ArrayList<User>();
        for (int i = 0; i < seminarsJSON.length(); i++) {
            try {
                JSONObject user = seminarsJSON.getJSONObject(i);
                seminars.add(new User(user.getString("nusp"), user.getString("name")));
                Log.d("ListActivity: ", seminars.get(i).toString());
            } catch (JSONException e) {
                Log.e("ListActivity: ", "Invalid JSON object!");
            }
        }
        return seminars;
    }

    @Override
    public String toString() {
        return "User{" +
                "nusp='" + nusp + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
