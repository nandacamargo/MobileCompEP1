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

public class SignUpActivity extends AppCompatActivity {

    private EditText nameView;
    private EditText nuspView;
    private EditText confirmPasswordView;
    private EditText passwordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
            confirmPasswordView.setError(getString(R.string.password_mismatch));
            confirmPasswordView.setText("");
            confirmPasswordView.requestFocus();
        } else {

            UserSignUpTask user = new UserSignUpTask(name, nusp, password);
            user.execute();

            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            Log.d("signup", "Click Create Account");
        }


    }

    public class UserSignUpTask extends AsyncTask<Void, Void, Integer> {

        private final String mName;
        private final String mNusp;
        private final String mPassword;

        private final int CONNECTION_FAILED = -1;
        private final int SUCCESS = 0;
        private final int POST_FAILED = -2;

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

            String s = "name=" + mName + "&nusp=" + mNusp + "&password=" + mPassword;
            Log.d("SignUpActivity", s);

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
                Log.d("SignUpActivity", responseCode.toString());

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    Log.i("SignUpActivity", "POST efetuado com sucesso!");
                    //String line;
//                    BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                    while ((line=br.readLine()) != null) {
//                        response+=line;
//                        Log.i("tag", line);
//                    }
                } else {
                    Log.i("SignUpActivity", "POST efetuado com sucesso!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return SUCCESS;
        }
    }

      /*  catch (MalformedURLException e) {
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
*/
        /*@Override
        protected void onPostExecute(Integer success) {

            mAuthTask = null;
            showProgress(false);
            String result = null;
            if (success == SUCCESS) {
                try {
                    result = new LoginActivity.GetSeminarTask().execute("http://207.38.82.139:8001/seminar").get();
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

        }*/
}
