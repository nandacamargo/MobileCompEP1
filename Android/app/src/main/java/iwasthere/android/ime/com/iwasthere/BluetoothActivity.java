package iwasthere.android.ime.com.iwasthere;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by nanda on 29/04/17.
 */


public class BluetoothActivity extends AppCompatActivity {

    Button b1;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        b1 = (Button) findViewById(R.id.closeSubmission);

        BA = BluetoothAdapter.getDefaultAdapter();
        //lv = (ListView)findViewById(R.id.listView);
    }

    public void on(View v){
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
        }
    }

    public void off(View v){
        BA.disable();
        Toast.makeText(getApplicationContext(), "Turned off" ,Toast.LENGTH_LONG).show();
    }

    public  void visible(View v){
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }


    public void list(View v){
        pairedDevices = BA.getBondedDevices();

        ArrayList list = new ArrayList();

        for(BluetoothDevice bt : pairedDevices) list.add(bt.getName());
        Toast.makeText(getApplicationContext(), "Showing Paired Devices",Toast.LENGTH_SHORT).show();

        final ArrayAdapter adapter = new  ArrayAdapter(this,android.R.layout.simple_list_item_1, list);

        lv.setAdapter(adapter);
    }

    /*****************************************
     * This task only applies to the teacher *
     * ***************************************/

    public class SendConfirmationTask extends AsyncTask<Void, Void, Integer> {

        private final int mId;
        private final String mNusp;

        private final int USER_NOT_FOUND = 2;
        private final int CONNECTION_FAILED = -1;
        private final int SUCCESS = 0;


        SendConfirmationTask(int id, String nusp) {
            mId = id;
            mNusp = nusp;
        }

        @Override
        protected Integer doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            StringBuilder stringBuilder;
            String stringURL = "http://207.38.82.139:8001/attendence/submit";

            URL url = null;

            String s = "nusp=" + mNusp + "&id=" + mId;
            Log.d("BluetoothActivity", s);

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
                Log.d("BluetoothActivity", responseCode.toString());


                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.i("BluetoothActivity", "POST efetuado com sucesso!");
                } else {
                    Log.i("BluetoothActivity", "POST não efetuado!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return SUCCESS;
        }
    }


    /*************************************************
    *Stops accepting student's presence confirmation *
    **************************************************/
    public void closeSubmissionClick() {
        /*TODO: Turn the bluetooth off*/

        Intent i = new Intent(this, SeminarListActivity.class);
        startActivity(i);

    }

}