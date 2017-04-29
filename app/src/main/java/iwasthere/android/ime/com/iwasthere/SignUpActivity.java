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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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

    public class UserSignUpTask extends AsyncTask<Void, Void, Integer> {

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

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            StringBuilder stringBuilder;
            String stringURL = "http://207.38.82.139:8001/student/add";

            URL url = null;
            String s = null;
            try {
                s = "name=" + mName + "&nusp=" + mNusp + "&pass=" + mPassword;
            } catch (Exception e) {
                Log.e("Exception", "Exceprton");
            }
            Log.d("SignUpActivity", s);

            try {
                url = new URL(stringURL);
                Log.d("httpGetRequest", "A URL é " + url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setReadTimeout(15 * 1000);
                connection.setConnectTimeout(15 * 1000);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("charset", "utf-8");
                connection.setRequestProperty("Content-Length", "" + Integer.toString(s.getBytes().length));
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();

                DataOutputStream os = new DataOutputStream(connection.getOutputStream());

                os.writeBytes(s);
                os.flush();
                os.close();

                Integer responseCode = connection.getResponseCode();
                Log.d("SignUpActivity", responseCode.toString());

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    Log.i("SignUpActivity", "POST efetuado com sucesso!");
                    String line;
                    String response = "";
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line + "\n";
                        Log.i("SignUpActivity", line);
                    }

                    JSONObject resp = new JSONObject(response);
                    if (!resp.getBoolean("success")) {
                        return USER_ALREADY_EXISTS;
                    }
                } else {
                    Log.i("SignUpActivity", "POST efetuado com sucesso!");
                }
            } catch (MalformedURLException e) {
                Log.d("httpGetRequest", "Erro. My url " + url);
                e.printStackTrace();
                return null;
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

            return SUCCESS;
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
