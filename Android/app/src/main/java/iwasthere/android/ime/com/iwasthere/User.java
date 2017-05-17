package iwasthere.android.ime.com.iwasthere;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dududcbier on 29/04/17.
 */

public class User {

    private String nusp;
    private String name;
    private Boolean teacher;
    private Boolean selected = false;

    public User(String nusp, String name, Boolean teacher) {
        this.nusp = nusp;
        this.name = name;
        this.teacher = teacher;
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

    public User(String nusp, String name, Boolean teacher, Boolean selected) {
        this.nusp = nusp;
        this.name = name;
        this.teacher = teacher;
        this.selected = selected;
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

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
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


