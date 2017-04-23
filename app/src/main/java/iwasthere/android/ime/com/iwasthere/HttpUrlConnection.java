package iwasthere.android.ime.com.iwasthere;

/**
 *
 * A complete Java class that shows how to open a URL, then read data (text) from that URL,
 * HttpURLConnection class (in combination with an InputStreamReader and BufferedReader).
 *
 * @author alvin alexander, http://alvinalexander.com.
 *
 */

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpUrlConnection {

    public static void main(String[] args) throws Exception  {
        new HttpUrlConnection();
    }

    public HttpUrlConnection()
    {
        try
        {
            String myUrl = "http://207.38.82.139:8001/student";
            myUrl = URLEncoder.encode(myUrl, "UTF-8");

            String results = doHttpUrlConnectionAction(myUrl);
            System.out.println(results);
        }
        catch (Exception e)
        {
            Log.e("Error", "Failed to acccess Web service", e);
        }
    }

    private String doHttpUrlConnectionAction(String desiredUrl) throws Exception {
        URL url = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder;

        try {
            // create the HttpURLConnection
            url = new URL(desiredUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            // uncomment this if you want to write output to this url
            //connection.setDoOutput(true);

            // give it 15 seconds to respond
            connection.setReadTimeout(15*1000);
            connection.connect();

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            return stringBuilder.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            // close the reader; this can throw an exception too, so
            // wrap it in another try/catch block.
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }
}

