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
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * A login screen that offers login via nusp/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "8536148:hello", "12345:678"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mNuspView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

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
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mNuspSignInButton = (Button) findViewById(R.id.nusp_sign_in_button);
        mNuspSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid nusp, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
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
            mAuthTask = new UserLoginTask(nusp, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isNuspValid(String nusp) {
        //TODO: Replace this with your own logic
        return nusp.length() > 4;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 2;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
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
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

        private final String mNusp;
        private final String mPassword;

        private final int WRNG_PASSWD = 1;
        private final int USER_NOT_FOUND = 2;
        private final int CONNECTION_FAILED = -1;
        private final int SUCCESS = 0;


        UserLoginTask(String nusp, String password) {
            mNusp = nusp;
            mPassword = password;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(0);
            } catch (InterruptedException e) {
                return CONNECTION_FAILED;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mNusp)) {
                    // Account exists, return true if the password matches.
                    if (pieces[1].equals(mPassword)) return SUCCESS;
                    return WRNG_PASSWD;
                }
            }

            return USER_NOT_FOUND;
        }

        @Override
        protected void onPostExecute(Integer success) {

            mAuthTask = null;
            showProgress(false);
            String result = null;
            if (success == SUCCESS) {
                try {
                    result = new GetSeminarTask().execute("http://207.38.82.139:8001/seminar").get();
                } catch (InterruptedException e){
                    Log.e("UserLoginTask: ", "Interrupted!");
                } catch (ExecutionException e) {
                    Log.e("UserLoginTask: ", "Execution Exception!");
                }

                Intent i = new Intent(getApplicationContext(), SeminarListActivity.class);
                i.putExtra("result", result);
                startActivity(i);
            } else if (success == WRNG_PASSWD) {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            } else if (success == USER_NOT_FOUND){
                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                i.putExtra("nusp", mNusp);
                i.putExtra("password", mPassword);
                startActivity(i);
            }

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public class GetSeminarTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            StringBuilder stringBuilder;
            String stringURL = params[0];
            String result;

            URL url = null;
            try {
                url = new URL(stringURL);
                Log.d("httpGetRequest", "A URL é " + url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setReadTimeout(15*1000);
                connection.connect();

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                Log.d("httpGetRequest", "Results =  " + stringBuilder.toString());

                result = stringBuilder.toString();

                return result;
            } catch (MalformedURLException e) {
                Log.d("httpGetRequest", "Erro. My url " + url);
                e.printStackTrace();
                return  null;
            } catch (Exception e) {
                Log.e("httpGetRequest", Log.getStackTraceString(e));
                e.printStackTrace();
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ioe) {
                        Log.e("PlaceholderFragment", "Error closing stream");
                        ioe.printStackTrace();
                    }
                }
            }
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
        }
    }
}

