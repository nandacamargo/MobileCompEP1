package iwasthere.android.ime.com.iwasthere;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by nanda on 22/04/17.
 * Based on http://www.codexpedia.com/android/
 */

public class HttpActivity extends Activity {

    Button btnFetchStudents;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);
        Log.d("HttpActivity", "Antes de estabelecer a conexão.");
        btnFetchStudents = (Button) findViewById(R.id.list_students);
        btnFetchStudents.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new FetchData().execute();
            }
        });

    }

    private class FetchData extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            StringBuilder stringBuilder;

            URL url = null;
            try {
                url = new URL("http://207.38.82.139:8001/student");
                Log.d("HttpActivity", "A URL é " + url);
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

                Log.d("HttpActivity", "Results =  " + stringBuilder.toString());
                return stringBuilder.toString();
            } catch (MalformedURLException e) {
                Log.d("HttpActivity", "Erro. My url " + url);
                e.printStackTrace();
                return  null;
            } catch (Exception e) {
                Log.e("httpActivity", Log.getStackTraceString(e));
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
    }
}