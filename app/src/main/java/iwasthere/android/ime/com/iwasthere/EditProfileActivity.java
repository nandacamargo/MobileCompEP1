package iwasthere.android.ime.com.iwasthere;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }

    public class EditProfileTask extends AsyncTask<Void, Void, Integer> {

        private final String mNusp;
        private final String mPassword;
        private final String mName;

        private final int WRNG_PASSWD = 1;
        private final int USER_NOT_FOUND = 2;
        private final int CONNECTION_FAILED = -1;
        private final int SUCCESS = 0;


        EditProfileTask(String nusp, String pass, String name) {
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

}
