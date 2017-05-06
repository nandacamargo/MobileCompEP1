package iwasthere.android.ime.com.iwasthere;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nanda on 06/05/17.
 */

public class SendConfirmationActivity extends AppCompatActivity {

    private EditText nuspView;
    private EditText seminarIdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_confirmation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_send_confirmation);
        nuspView = (EditText) findViewById(R.id.nusp);
        //seminarIdView = (EditText) findViewById(R.id.seminarId);
    }

    public void sendPresenceConfirmation() {
        final String nusp = nuspView.getText().toString();
        final String seminarId = seminarIdView.getText().toString();

        if (nusp.length() < 2) {
            nuspView.setError(getString(R.string.error_invalid_nusp));
            nuspView.requestFocus();
        } else if (seminarId.length() < 1) {
           // seminarIdView.setError(getString(R.string.error_invalid_seminar_id));
            seminarIdView.requestFocus();
        } else {

            String url;
            url = "http://207.38.82.139:8001/attendence/submit";

            StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response: ", response);
                            JSONObject resp = HttpUtil.getJSONObject(response, "sendPresenceConfirmation");
                            if (HttpUtil.responseWasSuccess(resp)) {
                                Log.d("sendConfirmation", "SUCCESS");
                                postPresenceConfirmation();
                            }
                            else {
                                Snackbar.make(findViewById(android.R.id.content), "An error occurred. Please try again later.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                }
                            }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("nusp", nusp);
                    params.put("seminarId", seminarId);
                    return params;
                }
            };
            RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(strRequest);

        }
    }

    private void postPresenceConfirmation() {
        Intent i = new Intent(getApplicationContext(), AttendeesListActivity.class);
        startActivity(i);
    }

}
