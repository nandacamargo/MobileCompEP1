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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by nanda on 04/05/17.
 */

public class ScanQrCodeActivity  extends AppCompatActivity {

    private ZXingScannerView mScannerView;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    private EditText nuspView;
    private EditText seminarIdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Programmatically initialize the scanner view
        mScannerView = new ZXingScannerView(this);
        // Set the scanner view as the content view
        setContentView(mScannerView);

        nuspView = (EditText) findViewById(R.id.nusp);
        //seminarIdView = (EditText) findViewById(R.id.seminarId);

    }

   /* @Override
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        // Start camera on resume
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }*/


    public void scanQR(View v) {

        //If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);

        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);

        } catch (Exception e) {
            //on catch, show the download dialog
            showDialog(ScanQrCodeActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
       }
    }

    //alert dialog for downloadDialog
    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {

        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int i) {

                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });

        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        return downloadDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                Toast.makeText(this, contents, Toast.LENGTH_LONG).show();

                // Handle successful scan
                sendPresenceConfirmation();
            }
            if(resultCode == RESULT_CANCELED){
                //handle cancel
            }
        }
    }


    public void sendPresenceConfirmation() {
        final String nusp = nuspView.getText().toString();
        //final int seminarId = seminarIdView.getText().toInt();
        final int seminarId = 1;

        Log.d("ScanQrCode", "nusp: " + nusp + " seminarId:" + seminarId);

        if (nusp.length() < 2) {
            nuspView.setError(getString(R.string.error_invalid_nusp));
            nuspView.requestFocus();
        } else if (seminarId  < 0) {
            seminarIdView.setError(getString(R.string.error_invalid_seminar_id));
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
