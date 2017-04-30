package iwasthere.android.ime.com.iwasthere;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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
        passwordView = (EditText) findViewById(R.id.pass);
        confirmPasswordView = (EditText) findViewById(R.id.confirm_pass);

        nameView.setText(user.getName());
        passwordView.setText("password");

        Button saveButton = (Button) findViewById(R.id.save_button);
        Button deleteButton = (Button) findViewById(R.id.delete_account_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(view);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //TODO delete account
            }
        });
    }

    public void updateProfile(View v) {

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

    public class EditProfileTask extends AsyncTask<Void, Void, Integer> {

        private final String mPassword;
        private final String mName;

        private final int SUCCESS = 0;


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
            URL url = null;

            String s = "nusp=" + user.getNusp() + "&pass=" + mPassword + "&name=" + mName;
            Log.d("SeminarAddActivity", s);

            try {
                url = new URL(stringURL);
                Log.d("httpGetRequest", "A URL é " + url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setReadTimeout(15 * 1000);
                connection.setConnectTimeout(15 * 1000);
                connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty( "charset", "utf-8");
                connection.setRequestProperty("Content-Length", "" + Integer.toString(s.getBytes().length));
                connection.connect();

                DataOutputStream os = new DataOutputStream(connection.getOutputStream());

                os.writeBytes(s);
                os.flush();
                os.close();

                Integer responseCode = connection.getResponseCode();
                Log.d("EditProfileActivity", responseCode.toString());


                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.i("EditProfileActivity", "POST efetuado com sucesso!");
                } else {
                    Log.i("EditProfileActivity", "POST não efetuado!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return SUCCESS;
        }
    }

}
