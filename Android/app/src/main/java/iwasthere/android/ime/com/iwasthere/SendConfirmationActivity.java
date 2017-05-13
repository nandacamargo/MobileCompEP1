package iwasthere.android.ime.com.iwasthere;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nanda on 07/05/17.
 */

public class SendConfirmationActivity extends AppCompatActivity {

    private Seminar seminar;
    private int seminarId = 0;

    private User user;
    private String nusp;

    private TextView tvScanFormat, tvScanContent;
    private LinearLayout llSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_confirmation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        seminar = SeminarSingleton.getInstance();
        seminarId = seminar.getId();
        user = UserSingleton.getInstance();
        nusp = user.getNusp();

        //getSupportActionBar().setTitle(R.string.title_send_confirmation);
        Log.d("SendConfirmation", "On function create of SendConfirmationActivity");

        tvScanFormat = (TextView) findViewById(R.id.tvScanFormat);
        tvScanContent = (TextView) findViewById(R.id.tvScanContent);
        llSearch = (LinearLayout) findViewById(R.id.llSearch);

        final Button qrCodeButton = (Button) findViewById(R.id.qr_code_button);
        if (user.isTeacher())  qrCodeButton.setText(R.string.generate_qr_code);

        qrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.isTeacher())
                    generateQR();
                 else
                    scanQR();
            }
        });

        final Button confirmButton = (Button) findViewById(R.id.second_confirmation);
        if (user.isTeacher())  confirmButton.setText(R.string.other_confirmation);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.isTeacher())
                    confirmPresence(1);
                else {
                    //getStudentsRequests();
                    confirmPresence(0);
                }
            }
        });

    }

    /*******************************************************/
    /*QR Code functions*/

    public void scanQR() {

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
        Log.d("SendConfirmation", "Results: " + result);

        if (result != null) {
            String contents = result.getContents();
            if (result.getContents() == null) {
                llSearch.setVisibility(View.GONE);
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                showDialog("Your confirmation failed",  getApplicationContext().getString(R.string.error_null_qr_code));

            } else {
                /*showDialog(0, result.toString());*/
                llSearch.setVisibility(View.VISIBLE);
                tvScanContent.setText(result.getContents());
                tvScanFormat.setText(result.getFormatName());
                Log.d("Contents", contents);

                int value = Integer.parseInt(contents);

                /*seminar = SeminarSingleton.deleteInstance();*/
                seminar = SeminarSingleton.updateInstance(seminar.getName(), seminar.getId());
                /*seminarId = seminar.getId();*/
                Log.e("SendConfirmation", "Seminar ID: " + seminarId);

                if (seminarId != value) {
                    Log.e("Student Confirmation", "This QRCode doesn't correspond to the seminar");
                    showDialog("Your confirmation failed", getApplicationContext().getString(R.string.error_wrong_seminar_id));

                }

                else
                    confirmPresence(1);

                Log.d("SendConfirmation", "After scanning");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void generateQR() {
        Log.d("SendConfirmation", "On generateQR");

        seminar = SeminarSingleton.getInstance();
        String text2Qr = "" + seminar.getId();

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            Intent intent = new Intent(getApplicationContext(), QrActivity.class);
            intent.putExtra("pic",bitmap);
            startActivity(intent);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    private void showDialog(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(SendConfirmationActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("Ok", null);
        builder.setCancelable(true);
        builder.create().show();
    }

    /*******************************************************/

    public void confirmPresence(final int confirmed) {


        Log.d("sendScanResults", "On method confirmPresence");
        Log.d("sendScanResults", "Confirmed is: " + confirmed);
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
                    params.put("seminar_id", "" + seminarId);
                    params.put("data", "teste");
                    params.put("confirmed", "" + confirmed);
                    return params;
                }
            };
            RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(strRequest);
        Log.d("SendConfirmation", "Leaving confirmPresence");
    }

    private void postPresenceConfirmation() {
        Intent i = new Intent(getApplicationContext(), AttendeesListActivity.class);
        startActivity(i);
        /*showDialog("Success",  getApplicationContext().getString(R.string.confirmation_successed));*/
        showDialog("Success",  getApplicationContext().getString(R.string.pending_request));
    }
}
