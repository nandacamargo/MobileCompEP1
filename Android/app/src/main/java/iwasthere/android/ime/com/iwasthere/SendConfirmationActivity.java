package iwasthere.android.ime.com.iwasthere;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nanda on 07/05/17.
 */

public class SendConfirmationActivity extends AppCompatActivity {

    private EditText nuspView;
    private Seminar seminar;

    private TextView tvScanFormat, tvScanContent;
    private LinearLayout llSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_confirmation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        seminar = SeminarSingleton.getInstance();
        nuspView = (EditText) findViewById(R.id.nusp);

        //getSupportActionBar().setTitle(R.string.title_send_confirmation);
        Log.d("SendConfirmation", "On function create of SendConfirmationActivity");

        tvScanFormat = (TextView) findViewById(R.id.tvScanFormat);
        tvScanContent = (TextView) findViewById(R.id.tvScanContent);
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
    }

    /*******************************************************/
    /*QR Code functions*/
   public void scanQR(View v) {

        Log.d("SendConfirmation", "On scanQR");
        llSearch.setVisibility(View.GONE);
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode or QRcode");
        integrator.setOrientationLocked(false);
        integrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                llSearch.setVisibility(View.GONE);
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                llSearch.setVisibility(View.VISIBLE);
                tvScanContent.setText(result.getContents());
                tvScanFormat.setText(result.getFormatName());
                /*sendScanResults(result.getContents());*/
                Log.d("SendConfirmation", "After scanning");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    /*******************************************************/

    public void sendScanResults(String results) {

        final String nusp = nuspView.getText().toString();
        final int seminarId;

        int value = Integer.parseInt(results);

        seminarId = seminar.getId();
        Log.d("SendConfirmation", "Results are: " + results);

        Log.d("PresenceConfirmation", "nusp: " + nusp + " seminarId:" + seminarId);

        if (nusp.length() < 2) {
            nuspView.setError(getString(R.string.error_invalid_nusp));
            nuspView.requestFocus();
        } else if (seminarId  < 0) {
            Log.e("PresenceConfirmation", "Invalid seminar id");
            finish();
        } else if (seminarId != value) {
            Log.e("PresenceConfirmation", "This QRCode don't corresponds to the seminar");
            finish();
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
                    params.put("seminarId", "" + seminarId);
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


    public void sendPdf() {

        Log.d("SendConfirmation", "After click sendPdf");
        Intent i = new Intent(getApplicationContext(), TextConfirmationActivity.class);
        startActivity(i);

    }
}
