package iwasthere.android.ime.com.iwasthere;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

        startActivityForResult( new Intent(this, HttpActivity.class), 1);
       //setContentView(R.layout.activity_signup);
    }

    public void listSeminars(View v) {
        Log.d("list", "Click List Seminars");

        startActivityForResult( new Intent(this, HttpActivity.class), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                String returnString = data.getData().toString();

                Log.d("List Activity: ", returnString);
                // set text view with string
                //TextView textView = (TextView) findViewById(R.id.name);
                //textView.setText(returnString);
            }
        }
    }
}
