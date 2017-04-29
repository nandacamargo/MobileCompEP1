package iwasthere.android.ime.com.iwasthere;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SeminarListActivity extends AppCompatActivity {

    private ArrayList<Seminar> seminars;
    private ListView seminar_list;

    public class SeminarsAdapter extends ArrayAdapter<Seminar> {
        public SeminarsAdapter(Context context, ArrayList<Seminar> seminars) {
            super(context, 0, seminars);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Seminar seminar = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.seminar_item, parent, false);
            }
            TextView name = (TextView) convertView.findViewById(R.id.seminar_name);
            name.setText(seminar.getName());
            return convertView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seminar_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

   /*     FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Log.d("SeminarListActivity", "On the listener");
                Intent i = new Intent(SeminarListActivity.this, SeminarAddActivity.class);
                startActivity(i);
            }
        });

        String result = null;
        try {
            result = new GetSeminarTask().execute("http://207.38.82.139:8001/seminar").get();
        } catch (InterruptedException e) {
            Log.e("SeminarListActivity: ", "Interrupted!");
        } catch (ExecutionException e) {
            Log.e("SeminarListActivity: ", "Execution Exception!");
        }

        Log.d("SeminarListActivity", "Result: " + result);

        try {
            JSONObject jObj = new JSONObject(result);
            JSONArray seminarsJSON = jObj.getJSONArray("data");
            this.seminars = Seminar.getSeminars(seminarsJSON);
        } catch (JSONException e) {
            Log.e("ListActivity: ", "Invalid JSON returned from GET.");
        }

        this.seminar_list = (ListView) findViewById(R.id.seminar_list);
        SeminarsAdapter adapter = new SeminarsAdapter(this, this.seminars);
        this.seminar_list.setAdapter(adapter);
    }


    public class GetSeminarTask extends AsyncTask<String, Void, String> {

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
                connection.setReadTimeout(15 * 1000);
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
        }

    }
}
