package iwasthere.android.ime.com.iwasthere;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dududcbier on 30/04/17.
 */

public class HttpUtil {

    public static JSONObject doPost(String stringURL, String s) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(stringURL);
            Log.d("httpGetRequest", "A URL é " + url);
            connection = (HttpURLConnection) url.openConnection();

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
            Log.d("doPost", responseCode.toString());

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                Log.i("doPost", "POST efetuado com sucesso!");
                String line;
                String response = "";
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    response += line + "\n";
                    Log.i("doPost", line);
                }

                return new JSONObject(response);

            } else {
                Log.i("doPost", "POST efetuado com sucesso!");
            }
        } catch (MalformedURLException e) {
            Log.d("doPost", "Erro. My url " + stringURL);
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            Log.e("doPost", Log.getStackTraceString(e));
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    Log.e("doPost", "Error closing stream");
                    ioe.printStackTrace();
                }
            }
        }
        return null;
    }

    public static class HttpGetTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            StringBuilder stringBuilder;
            String stringURL = params[0];
            String result;

            URL url = null;
            Log.d("HttpGetTask", "Vou fazer o requesy");
            try {
                url = new URL(stringURL);
                Log.d("httpGetRequest", "A URL é " + url);
                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setReadTimeout(15*1000);
                connection.connect();

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                stringBuilder = new StringBuilder();

                String line;
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
                if (connection != null) {
                    connection.disconnect();
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
