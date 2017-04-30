package iwasthere.android.ime.com.iwasthere;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static iwasthere.android.ime.com.iwasthere.R.id.login;

/**
 * A login screen that offers login via nusp/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuth = null;

    // UI references.
    private EditText mNuspView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mNuspView = (AutoCompleteTextView) findViewById(R.id.nusp);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mNuspSignInButton = (Button) findViewById(R.id.nusp_sign_in_button);
        Button mNuspSignUpButton = (Button) findViewById(R.id.nusp_register_button);
        mNuspSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mNuspSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpClick(mNuspView.getText().toString(), mPasswordView.getText().toString());
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        checkBox = (CheckBox) findViewById(R.id.checkbox);
        if (checkBox.isChecked()) {
            checkBox.setChecked(false);
        }
    }

    public void signUpClick(String nusp, String password) {
        Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
        i.putExtra("nusp", nusp);
        i.putExtra("password", password);
        startActivity(i);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid nusp, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuth != null) {
            return;
        }

        // Reset errors.
        mNuspView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String nusp = mNuspView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid nusp.
        if (TextUtils.isEmpty(nusp)) {
            mNuspView.setError(getString(R.string.error_field_required));
            focusView = mNuspView;
            cancel = true;
        } else if (!isNuspValid(nusp)) {
            mNuspView.setError(getString(R.string.error_invalid_nusp));
            focusView = mNuspView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            if (checkBox.isChecked()) {
                mAuth = new UserLoginTask(nusp, password, true, "http://207.38.82.139:8001/login/teacher");
                mAuth.execute((Void) null);
            } else {
                mAuth = new UserLoginTask(nusp, password, false, "http://207.38.82.139:8001/login/student");
                mAuth.execute((Void) null);
            }

        }
    }

    private boolean isNuspValid(String nusp) {
        return nusp.length() > 0;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 2;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mNusp;
        private final String mPassword;
        private final Boolean mTeacher;
        private final String stringURL;

        UserLoginTask(String nusp, String password, Boolean teacher, String url) {
            mNusp = nusp;
            mPassword = password;
            mTeacher = teacher;
            stringURL = url;
            Log.d("UserLoginTask", "mTeacher é " + mTeacher);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String postParams = "nusp=" + mNusp + "&pass=" + mPassword;

            JSONObject jObj = HttpUtil.doPost(stringURL, postParams);
            if (jObj == null) return false;
            Boolean r;
            try {
                r = jObj.getBoolean("success");
            } catch (JSONException e) {
                r = false;
            }
            return r;
        }

        @Override
        protected void onPostExecute(Boolean success) {

            mAuth = null;
            showProgress(false);
            String user = null;
            if (success) {
                try {
                    if (mTeacher)
                        user = new HttpUtil.HttpGetTask().execute("http://207.38.82.139:8001/teacher/get/" + mNusp).get();
                    else
                        user = new HttpUtil.HttpGetTask().execute("http://207.38.82.139:8001/student/get/" + mNusp).get();
                } catch (InterruptedException e){
                    Log.e("UserLoginTask: ", "Interrupted!");
                } catch (ExecutionException e) {
                    Log.e("UserLoginTask: ", "Execution Exception!");
                }
                Intent i = new Intent(getApplicationContext(), SeminarListActivity.class);
                try {
                    JSONObject userJSON = new JSONObject(user);
                    user = userJSON.getString("data");
                    i.putExtra("user", new User(user, mTeacher));
                } catch (JSONException e) {
                    Log.e("LoginActivity", "Incorrect JSON");
                }
                startActivity(i);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuth = null;
            showProgress(false);
        }
    }
}

