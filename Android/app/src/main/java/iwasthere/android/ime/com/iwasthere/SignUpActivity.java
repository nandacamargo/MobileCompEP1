package iwasthere.android.ime.com.iwasthere;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        passwordView = (EditText) findViewById(R.id.password);
        confirmPasswordView = (EditText) findViewById(R.id.confirm_password);

        nuspView.setText(getIntent().getStringExtra("nusp"));
        passwordView.setText(getIntent().getStringExtra("password"));

    }

    public void signUpStudent(View v) {
        signUpUser(false);
    }

    public void signUpUser(Boolean teacher) {
        final String name = nameView.getText().toString();
        final String nusp = nuspView.getText().toString();
        final String password = passwordView.getText().toString();
        final String confirmPassword = confirmPasswordView.getText().toString();

        if (name.length() < 1) {
            nameView.setError(getString(R.string.error_field_required));
            nameView.requestFocus();
        } else if (nusp.length() < 2) {
            nuspView.setError(getString(R.string.error_invalid_nusp));
            nuspView.requestFocus();
        } else if (password.length() < 1) {
            passwordView.setError(getString(R.string.error_invalid_password));
            passwordView.requestFocus();
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordView.setError(getString(R.string.error_password_mismatch));
            confirmPasswordView.setText("");
            confirmPasswordView.requestFocus();
        } else {

        String url;
        if (teacher) url = "http://207.38.82.139:8001/teacher/add";
        else url = "http://207.38.82.139:8001/student/add";
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response: ", response);
                        JSONObject resp = HttpUtil.getJSONObject(response, "signUpUser");
                        if (HttpUtil.responseWasSuccess(resp)) {
                            Log.d("signUpUser", "SUCCESS");
                            postSignUpUser();
                        }
                        else {
                            if (resp.has("message")) {
                                Log.d("signUpUser", "WTF");
                                nuspView.setError(getString(R.string.error_user_exists));
                                nuspView.requestFocus();
                            }
                            else {
                                Snackbar.make(findViewById(android.R.id.content), "An error occurred. Please try again later.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("nusp", nusp);
                        params.put("name", name);
                        params.put("pass", password);
                        return params;
                    }
                };
        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(strRequest);

        }
    }

    private void postSignUpUser() {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
    }
}
