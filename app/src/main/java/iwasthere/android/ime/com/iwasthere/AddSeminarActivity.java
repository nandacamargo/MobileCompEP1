package iwasthere.android.ime.com.iwasthere;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by nanda on 28/04/17.
 */

public class AddSeminarActivity extends AppCompatActivity {


    private EditText nameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_seminar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_add_seminar);

        nameView = (EditText) findViewById(R.id.name);
    }

    public void addSeminar(View v) {

        String name = nameView.getText().toString();

        if (name.length() < 1) {
            nameView.setError(getString(R.string.error_field_required));
            nameView.requestFocus();
        } else {

            SeminarAddTask seminar = new SeminarAddTask(name);
            seminar.execute();

            Intent i = new Intent(this, SeminarListActivity.class);
            startActivity(i);
            Log.d("AddSeminarActivity", "Click Add Activity");
        }
    }

    private class SeminarAddTask extends AsyncTask<Void, Void, Integer> {

        private final String mName;

        private final int SUCCESS = 0;


        SeminarAddTask(String name) {
            mName = name;
        }

        @Override
        protected Integer doInBackground(Void... params) {

            String stringURL = "http://207.38.82.139:8001/seminar/add";

            String s = "name=" + mName;
            Log.d("AddSeminarActivity", s);

            HttpUtil.doPost(stringURL, s);
            return SUCCESS;
        }
    }
}
