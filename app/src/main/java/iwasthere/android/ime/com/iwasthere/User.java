package iwasthere.android.ime.com.iwasthere;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dududcbier on 29/04/17.
 */

public class User implements Parcelable{

    private String nusp;
    private String name;
    private Boolean teacher;

    public User(String nusp, String name, Boolean teacher) {
        this.nusp = nusp;
        this.name = name;
        this.teacher = teacher;
        Log.d("Novo user com teacher ", this.teacher.toString());
    }

    public User(String user, Boolean teacher){
        try {
            JSONObject jObject = new JSONObject(user);
            this.nusp = jObject.getString("nusp");
            this.name = jObject.getString("name");
            this.teacher = teacher;
        } catch (JSONException e) {
            Log.e("User", "Incorrect JSON");
        }
        Log.d("Novo user com teacher ", this.teacher.toString());
    }

    public User(Parcel p){
        String[] data = new String[3];

        p.readStringArray(data);
        this.nusp = data[0];
        this.name = data[1];
        this.teacher = Boolean.valueOf(data[2]);
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

    public Boolean isTeacher() {
        return this.teacher;
    }

    private static ArrayList<User> getUsers(JSONArray users, Boolean teacher) {
        ArrayList<User> seminars = new ArrayList<>();
        for (int i = 0; i < users.length(); i++) {
            try {
                JSONObject user = users.getJSONObject(i);
                seminars.add(new User(user.getString("nusp"), user.getString("name"), teacher));
                Log.d("ListActivity: ", seminars.get(i).toString());
            } catch (JSONException e) {
                Log.e("ListActivity: ", "Invalid JSON object!");
            }
        }
        return seminars;
    }

    public ArrayList<User> getStudents(JSONArray users) {
        return getUsers(users, false);
    }

    public ArrayList<User> getTeachers(JSONArray users) {
        return getUsers(users, true);
    }

    @Override
    public String toString() {
        return "User{" +
                "nusp='" + nusp + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.nusp, this.name, String.valueOf(this.teacher)});
    }

    public static final Parcelable.Creator<User> CREATOR= new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

}


