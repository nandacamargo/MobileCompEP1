package iwasthere.android.ime.com.iwasthere;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static iwasthere.android.ime.com.iwasthere.R.id.pass;

public class EditProfileActivity extends AppCompatActivity {

    private User user;
    EditText nameView;
    EditText confirmPasswordView;
    EditText passwordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        user = getIntent().getParcelableExtra("user");

        nameView = (EditText) findViewById(R.id.name);
        passwordView = (EditText) findViewById(pass);
        confirmPasswordView = (EditText) findViewById(R.id.confirm_pass);

        nameView.setText(user.getName());
        passwordView.setText(R.string.password_placeholder);

        Button saveButton = (Button) findViewById(R.id.save_button);
        Button deleteButton = (Button) findViewById(R.id.delete_account_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAccount();
            }
        });
    }

    private void updateProfile() {

        final String name = nameView.getText().toString();
        final String password = passwordView.getText().toString();
        final String confirmPassword = confirmPasswordView.getText().toString();


        if (name.length() < 1) {
            nameView.setError(getString(R.string.error_field_required));
            nameView.requestFocus();
        } else if (password.length() < 3) {
            passwordView.setError(getString(R.string.error_invalid_password));
            passwordView.requestFocus();
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordView.setError(getString(R.string.error_password_mismatch));
            confirmPasswordView.setText("");
            confirmPasswordView.requestFocus();
        } else {
            String url;
            if (user.isTeacher()) url = "http://207.38.82.139:8001/teacher/edit";
            else url = "http://207.38.82.139:8001/student/edit";
            StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response: ", response);
                            JSONObject resp = HttpUtil.getJSONObject(response, "attemptLogin");
                            if (HttpUtil.responseWasSuccess(resp)) postUpdateProfile(name);
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
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("nusp", user.getNusp());
                    params.put("pass", passwordView.getText().toString());
                    params.put("name", nameView.getText().toString());
                    return params;
                }
            };
            RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(strRequest);
        }
    }

    private void postUpdateProfile(String name) {
        user.setName(name);
        Intent i = new Intent(this, SeminarListActivity.class);
        i.putExtra("user", user);
        startActivity(i);
    }

    private void deleteAccount() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(EditProfileActivity.this);
        dlgAlert.setMessage(R.string.delete_message);
        dlgAlert.setTitle(R.string.app_name);
        dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String url;
                if (user.isTeacher()) url = "http://207.38.82.139:8001/teacher/delete";
                else url = "http://207.38.82.139:8001/student/delete";
                StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response: ", response);
                                JSONObject resp = HttpUtil.getJSONObject(response, "attemptLogin");
                                if (HttpUtil.responseWasSuccess(resp)) postDeleteAccount();
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
                    protected Map<String, String> getParams()
                    {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("nusp", user.getNusp());
                        return params;
                    }
                };
                RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(strRequest);
            }
        });
        dlgAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {}
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    private void postDeleteAccount() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}
