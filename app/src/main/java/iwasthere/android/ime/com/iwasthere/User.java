package iwasthere.android.ime.com.iwasthere;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
        Log.d("user: ", this.toString());
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

    private static ArrayList<User> getUsers(JSONArray usersJSON, Boolean teacher) {
        ArrayList<User> users = new ArrayList<>();
        Log.d("getUsers", usersJSON.toString());
        int size = usersJSON.length();
        for (int i = 0; i < size; i++) {
            JSONObject jObj;
            try {
                jObj = usersJSON.getJSONObject(i);
            } catch (JSONException e) {
                Log.e("getUsers: ", "Invalid JSON object!");
                jObj = null;
            }
            User user;
            try {
                user = getUserFromDB(jObj.getString("student_nusp"));
            } catch (JSONException e) {
                user = null;
            }
            if (user != null) users.add(user);
        }
        Log.d("getUsers res: ", users.toString());
        return users;
    }

    public static User getUserFromDB(String nusp) {
        String result = null;
        try {
            Log.d("GetUserFromDB", "comecei");
            HttpUtil.HttpGetTask request = new HttpUtil.HttpGetTask();
            result = request.execute("http://207.38.82.139:8001/student/get/" + nusp).get();
        } catch (InterruptedException e){
            Log.e("UserLoginTask: ", "Interrupted!");
        } catch (ExecutionException e) {
            Log.e("UserLoginTask: ", "Execution Exception!");
        }
        Log.d("GetUserFromDB", "Vou criar o user");

        try {
            JSONObject res = new JSONObject(result);
            return new User(nusp, res.getJSONObject("data").getString("name"), false);
        } catch (JSONException e){
            Log.e("getUserFromDB", "Invalid JSON object!");
        }
        return null;
    }

    public static ArrayList<User> getStudents(JSONArray users) {
        return getUsers(users, false);
    }

    public static ArrayList<User> getTeachers(JSONArray users) {
        return getUsers(users, true);
    }

    @Override
    public String toString() {
        return "User{" +
                "nusp='" + nusp + '\'' +
                ", name='" + name + '\'' +
                ", teacher='" + teacher + '\'' +
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return nusp.equals(user.nusp);
    }

    @Override
    public int hashCode() {
        return nusp.hashCode();
    }
}


