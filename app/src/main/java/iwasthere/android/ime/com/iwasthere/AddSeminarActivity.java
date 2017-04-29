package iwasthere.android.ime.com.iwasthere;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class AddSeminarActivity extends AppCompatActivity{


    private EditText nameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_seminar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_add_seminar);

        nameView = (EditText) findViewById(R.id.name);
    }

    public void addSeminar(View v) {

        String name = nameView.getText().toString();

        if (name.length() < 1) {
            nameView.setError(getString(R.string.error_field_required));
            nameView.requestFocus();
        } else {

            SeminarAddTask seminar = new SeminarAddTask(name);
            seminar.execute();

            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            Log.d("AddSeminarActivity", "Click Add Activity");
        }
    }

    public class SeminarAddTask extends AsyncTask<Void, Void, Integer> {

        private final String mName;

        private final int SUCCESS = 0;


        SeminarAddTask(String name) {
            mName = name;
        }

        @Override
        protected Integer doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            StringBuilder stringBuilder;
            String stringURL = "http://207.38.82.139:8001/seminar/add";

            URL url = null;

            String s = "name=" + mName;
            Log.d("AddSeminarActivity", s);

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
                Log.d("AddSeminarActivity", responseCode.toString());


                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.i("AddSeminarActivity", "POST efetuado com sucesso!");
                } else {
                    Log.i("AddSeminarActivity", "POST não efetuado!");
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
