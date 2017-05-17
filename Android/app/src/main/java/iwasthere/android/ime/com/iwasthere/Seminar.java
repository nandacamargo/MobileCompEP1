package iwasthere.android.ime.com.iwasthere;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dududcbier on 26/04/17.
 */

public class Seminar implements Comparable{

    private String name;
    private Integer id;

    public Seminar(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static ArrayList<Seminar> getSeminars(JSONArray seminarsJSON) {
        ArrayList<Seminar> seminars = new ArrayList<>();
        for (int i = 0; i < seminarsJSON.length(); i++) {
            try {
                JSONObject seminar = seminarsJSON.getJSONObject(i);
                seminars.add(new Seminar(seminar.getString("name"), seminar.getInt("id")));
                Log.d("ListActivity: ", seminars.get(i).toString());
            } catch (JSONException e) {
                Log.e("ListActivity: ", "Invalid JSON object!");
            }
        }
        return seminars;
    }

    public String toString() {
        return "Name: " + this.name + ", ID: " + this.id;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Seminar s = (Seminar) o;
        return this.name.compareTo(s.getName());
    }
}


