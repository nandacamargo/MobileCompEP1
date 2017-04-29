package iwasthere.android.ime.com.iwasthere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by nanda on 28/04/17.
 */

public class AfterLoginActivity extends AppCompatActivity {

    String result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);
    }


    public void updateInfo (View v) {
        Intent i = new Intent(this, RegisterUpdateActivity.class);
        startActivity(i);
    }


    public void listSeminars(View v) {
        Intent i = new Intent(AfterLoginActivity.this, SeminarListActivity.class);
        startActivity(i);
    }

}
