package iwasthere.android.ime.com.iwasthere;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import static iwasthere.android.ime.com.iwasthere.HttpUtil.doPost;
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
//        deleteButton.setBackgroundColor(Color.parseColor("#F44336"));
    }

    private void updateProfile() {

        String name = nameView.getText().toString();
        String password = passwordView.getText().toString();
        String confirmPassword = confirmPasswordView.getText().toString();


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

            EditProfileTask register = new EditProfileTask(password, name);
            register.execute();

            user.setName(name);

            Intent i = new Intent(this, SeminarListActivity.class);
            i.putExtra("user", user);
            startActivity(i);
        }
    }

    private void deleteAccount() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(EditProfileActivity.this);
        dlgAlert.setMessage(R.string.delete_message);
        dlgAlert.setTitle(R.string.app_name);
        dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               new DeleteAccountTask().execute();
            }
        });
        dlgAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {}
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    private class EditProfileTask extends AsyncTask<Void, Void, Integer> {

        private final String mPassword;
        private final String mName;

        private final int SUCCESS = 1;
        private final int FAILURE = 0;


        EditProfileTask(String pass, String name) {
            mName = name;
            mPassword = pass;
        }

        @Override
        protected Integer doInBackground(Void... params) {

            String stringURL;
            if (user.isTeacher()) {
                stringURL = "http://207.38.82.139:8001/teacher/edit";
            } else {
                stringURL = "http://207.38.82.139:8001/student/edit";
            }

            String s = "nusp=" + user.getNusp() + "&pass=" + mPassword + "&name=" + mName;
            Log.d("SeminarAddActivity", s);

            JSONObject jObj = HttpUtil.doPost(stringURL, s);
            if (jObj != null) {
                try {
                    if (jObj.getBoolean("success")) {
                        return SUCCESS;
                    }
                } catch (JSONException e) {
                    Log.e("deleteAccountTask", "JSONException");
                }
            }
            return FAILURE;
        }
    }

    private class DeleteAccountTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            String stringURL = "http://207.38.82.139:8001/student/delete";

            String s = null;
            try {
                s = "nusp=" + user.getNusp();
            } catch (Exception e) {
                Log.e("Exception", "Exception");
            }
            Log.d("SignUpActivity", s);

            JSONObject jObj = doPost(stringURL, s);
            if (jObj != null) {
                try {
                    if (jObj.getBoolean("success")) {
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                    }
                } catch (JSONException e) {
                    Log.e("deleteAccountTask", "JSONException");
                }
            }
            return null;
        }
    }
}
