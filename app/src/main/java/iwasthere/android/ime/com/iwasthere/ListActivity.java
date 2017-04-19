package iwasthere.android.ime.com.iwasthere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    }
}
