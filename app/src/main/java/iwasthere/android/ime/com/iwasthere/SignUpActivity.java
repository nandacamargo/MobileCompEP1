package iwasthere.android.ime.com.iwasthere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by nanda on 22/04/17.
 */

public class SignUpActivity extends AppCompatActivity {

    EditText name, nusp, email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void signUpStudent(View v) {

        name = (EditText) findViewById(R.id.name);
        nusp = (EditText) findViewById(R.id.nusp);
        email =(EditText) findViewById(R.id.email);
        password =(EditText) findViewById(R.id.passwd);

        String s1 = name.getText().toString();
        String s2 = nusp.getText().toString();
        String s3 = email.getText().toString();
        String s4 = password.getText().toString();

        Intent i = new Intent(this, ListActivity.class);
        startActivity(i);
        Log.d("signup", "Click Create Account");
    }
}
