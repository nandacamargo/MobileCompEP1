package iwasthere.android.ime.com.iwasthere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameView;
    private EditText nuspView;
    private EditText confirmPasswordView;
    private EditText passwordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameView = (EditText) findViewById(R.id.name);
        nuspView = (EditText) findViewById(R.id.nusp);
        passwordView = (EditText) findViewById(R.id.passwd);
        confirmPasswordView = (EditText) findViewById(R.id.confirmPasswd);

        nuspView.setText(getIntent().getStringExtra("nusp"));
        passwordView.setText(getIntent().getStringExtra("password"));

    }

    public void signUpStudent(View v) {

        String name = nameView.getText().toString();
        String nusp = nuspView.getText().toString();
        String password = passwordView.getText().toString();
        String confirmPassword = confirmPasswordView.getText().toString();

        if (name.length() < 1) {
            nameView.setError(getString(R.string.error_field_required));
            nameView.requestFocus();
        } else if (nusp.length() < 4) {
            nuspView.setError(getString(R.string.error_invalid_nusp));
            nuspView.requestFocus();
        } else if (password.length() < 3) {
            passwordView.setError(getString(R.string.error_invalid_password));
            passwordView.requestFocus();
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordView.setError(getString(R.string.password_mismatch));
            confirmPasswordView.setText("");
            confirmPasswordView.requestFocus();
        } else {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            Log.d("signup", "Click Create Account");
        }
    }
}
