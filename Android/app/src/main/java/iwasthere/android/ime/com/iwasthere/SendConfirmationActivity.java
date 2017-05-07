package iwasthere.android.ime.com.iwasthere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

/**
 * Created by nanda on 07/05/17.
 */

public class SendConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_confirmation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_sign_up);
    }


    public void readQrCode(View v) {
        Log.d("SendConfirmation", "After clicking readQrCode button");
        Intent i = new Intent(getApplicationContext(), ScanQrCodeActivity.class);
        startActivity(i);
    }

    public void sendPdf(View v) {
        //TODO

    }

}
