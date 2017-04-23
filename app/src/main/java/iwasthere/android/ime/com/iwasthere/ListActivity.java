package iwasthere.android.ime.com.iwasthere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Created by dududcbier on 4/19/17.
 */

public class ListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }

    public void addTeacher(View v){
//        startActivityForResult( new Intent(this, TeacherActivity.class), 1);
        Log.d("list", "Click Add");
        /*Intent i = new Intent(this, HttpActivity.class);
        startActivity(i);*/
    }

    public void listStudents(View v) {
        Log.d("list", "Click List Students");
        Intent i = new Intent(this, HttpActivity.class);
        startActivity(i);

        //setContentView(R.layout.activity_signup);
    }
}
