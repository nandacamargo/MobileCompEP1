package iwasthere.android.ime.com.iwasthere;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static iwasthere.android.ime.com.iwasthere.R.id.login;

/**
 * A login screen that offers login via nusp/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mNuspView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox checkBox;

    @Override
    public void onBackPressed() {
        showProgress(false);
        super.onBackPressed();
    }

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
        // Reset errors.
        mNuspView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String nusp = mNuspView.getText().toString();
        final String password = mPasswordView.getText().toString();

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
            focusView.requestFocus();
        } else {
            String url;
            if (checkBox.isChecked()) {
                url = "http://207.38.82.139:8001/login/teacher";
            } else {
                url = "http://207.38.82.139:8001/login/student";
            }

            StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response: ", response);
                            JSONObject resp = HttpUtil.getJSONObject(response, "attemptLogin");
                            if (HttpUtil.responseWasSuccess(resp)) {
                                showProgress(true);
                                onLoginSuccess(nusp);
                            } else {
                                mPasswordView.setError(getString(R.string.error_incorrect_password));
                                mPasswordView.requestFocus();
                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("nusp", mNuspView.getText().toString());
                    params.put("pass", mPasswordView.getText().toString());
                    return params;
                }
            };
            RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(strRequest);
        }
    }

    private void onLoginSuccess(String nusp) {

        final Boolean isTeacher = checkBox.isChecked();
        String url;
        if (!isTeacher) {
             url = "http://207.38.82.139:8001/student/get/" + nusp;
        } else {
             url = "http://207.38.82.139:8001/teacher/get/" + nusp;
        }
        Log.d("URL", url);
        Log.d("TEACHER: ", isTeacher.toString());
        StringRequest strRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.d("Response: ", response);
                        JSONObject resp = HttpUtil.getJSONObject(response, "onLoginSuccess");
                        if (HttpUtil.responseWasSuccess(resp)) {
                            User user = new User(HttpUtil.getResponseDataString(resp), isTeacher);
                            Intent i = new Intent(getApplicationContext(), SeminarListActivity.class);
                            i.putExtra("user", user);
                            showProgress(false);
                            startActivity(i);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(strRequest);

    }

    private boolean isNuspValid(String nusp) {
        return nusp.length() > 0;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 2;
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
}

