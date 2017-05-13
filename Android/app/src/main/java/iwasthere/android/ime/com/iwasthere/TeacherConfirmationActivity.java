package iwasthere.android.ime.com.iwasthere;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class TeacherConfirmationActivity extends AppCompatActivity {

    private Seminar seminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_confirmation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button qrCodeButton = (Button) findViewById(R.id.generate_qr_code);

        qrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TeacherConfirmation", "Before generate QR Code");
                generateQR();
            }
        });

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
}
