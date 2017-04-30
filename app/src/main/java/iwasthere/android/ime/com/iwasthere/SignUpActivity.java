package iwasthere.android.ime.com.iwasthere;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameView;
    private EditText nuspView;
    private EditText confirmPasswordView;
    private EditText passwordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_sign_up);

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
            confirmPasswordView.setError(getString(R.string.error_password_mismatch));
            confirmPasswordView.setText("");
            confirmPasswordView.requestFocus();
        } else {

            UserSignUpTask user = new UserSignUpTask(name, nusp, password);
            user.execute();
        }


    }

    private class UserSignUpTask extends AsyncTask<Void, Void, Integer> {

        private final String mName;
        private final String mNusp;
        private final String mPassword;

        private final int CONNECTION_FAILED = -1;
        private final int SUCCESS = 0;
        private final int USER_ALREADY_EXISTS = -2;

        UserSignUpTask(String name, String nusp, String password) {
            mName = name;
            mNusp = nusp;
            mPassword = password;
        }

        @Override
        protected Integer doInBackground(Void... params) {

            String stringURL = "http://207.38.82.139:8001/student/add";

            String s = null;
            try {
                s = "name=" + mName + "&nusp=" + mNusp + "&pass=" + mPassword;
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("SignUpActivity", s);

            JSONObject jObj =  HttpUtil.doPost(stringURL, s);
            try {
                if (jObj != null && jObj.getBoolean("success")) {
                    return SUCCESS;
                }
                else {
                    return USER_ALREADY_EXISTS;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return CONNECTION_FAILED;
        }

        @Override
        protected void onPostExecute(Integer success) {

            switch (success) {
                case SUCCESS:
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                    Log.d("signup", "Click Create Account");
                    break;
                case USER_ALREADY_EXISTS:
                    nuspView.setError(getString(R.string.error_user_exists));
                    nuspView.requestFocus();
                    break;
                default:
                    Snackbar.make(findViewById(android.R.id.content), "An error occurred. Please try again later.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }

        }
    }
}
