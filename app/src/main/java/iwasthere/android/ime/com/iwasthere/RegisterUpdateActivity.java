package iwasthere.android.ime.com.iwasthere;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by nanda on 28/04/17.
 */

public class RegisterUpdateActivity extends AppCompatActivity {

    private EditText idView;
    private EditText nameView;
    private EditText nuspView;
    private EditText passwordView;
    private EditText confirmPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_update);

        nameView = (EditText) findViewById(R.id.name);
        nuspView = (EditText) findViewById(R.id.nusp);
        passwordView = (EditText) findViewById(R.id.passwd);
        confirmPasswordView = (EditText) findViewById(R.id.confirmPasswd);

        nuspView.setText(getIntent().getStringExtra("nusp"));
        passwordView.setText(getIntent().getStringExtra("password"));
        nameView.setText(getIntent().getStringExtra("name"));
    }

    public void updateRegister(View v) {

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

            RegisterUpdateActivity.RegisterUpdateTask register = new RegisterUpdateActivity.RegisterUpdateTask(nusp, password, name);
            register.execute();

            /*Intent i = new Intent(this, RegisterUpdateActivity.class);*/
            Intent i = new Intent(this, AfterLoginActivity.class);
            startActivity(i);
            Log.d("RegisterUpdateActivity", "After Intent do RegisterUpdateActivity");
        }
    }

    public class RegisterUpdateTask extends AsyncTask<Void, Void, Integer> {

        private final String mNusp;
        private final String mPassword;
        private final String mName;

        private final int WRNG_PASSWD = 1;
        private final int USER_NOT_FOUND = 2;
        private final int CONNECTION_FAILED = -1;
        private final int SUCCESS = 0;


        RegisterUpdateTask(String nusp, String pass, String name) {
            mNusp = nusp;
            mName = name;
            mPassword = pass;
        }

        @Override
        protected Integer doInBackground(Void... params) {

            String stringURL = "http://207.38.82.139:8001/student/edit";
            URL url = null;

            String s = "nusp=" + mNusp + "&pass=" + mPassword + "&name=" + mName;
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
                Log.d("RegisterUpdateActivity", responseCode.toString());


                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.i("RegisterUpdateActivity", "POST efetuado com sucesso!");
                } else {
                    Log.i("RegisterUpdateActivity", "POST não efetuado!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return SUCCESS;
        }
    }

        /*@Override
        protected void onPostExecute(Integer success) {

        }*/
}
