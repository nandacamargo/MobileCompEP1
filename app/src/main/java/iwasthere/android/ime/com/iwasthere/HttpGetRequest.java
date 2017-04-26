package iwasthere.android.ime.com.iwasthere;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dududcbier on 26/04/17.
 */

public class HttpGetRequest extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder;
        String stringURL = params[0];
        String result;

        URL url = null;
        try {
            url = new URL(stringURL);
            Log.d("httpGetRequest", "A URL é " + url);
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

            Log.d("httpGetRequest", "Results =  " + stringBuilder.toString());

            result = stringBuilder.toString();

            return result;
        } catch (MalformedURLException e) {
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
    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }
}