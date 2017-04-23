package iwasthere.android.ime.com.iwasthere;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nanda on 22/04/17.
 */

public class Student implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private Number nusp;
    private String pass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Number getNusp() {
        return nusp;
    }

    public void setNusp(Number nusp) {
        this.nusp = nusp;
    }

    //Returns a list of students with info get from JSON
    private List<Student> getStudents(String jsonString) {
        List<Student> students = new ArrayList<Student>();
        try {
            JSONArray studentsJson = new JSONArray(jsonString);
            JSONObject student;

            for (int i = 0; i < studentsJson.length(); i++) {
                student = new JSONObject(studentsJson.getString(i));
                Log.i("Student found: ",
                        "name=" + student.getString("name"));

                Student objectStudent = new Student();
                objectStudent.setName(student.getString("name"));
                //objectStudent.setNusp(student.getNusp("nusp"));
                students.add(objectStudent);
            }

        } catch (JSONException e) {
            Log.e("Erro", "Erro no parsing do JSON", e);
        }
        return students;
    }

    @Override
    public String toString() {
        return name;
    }
}
