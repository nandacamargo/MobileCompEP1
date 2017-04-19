package iwasthere.android.ime.com.iwasthere;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    Button register,login;
//    ProgressBar progressBar;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void clickConfirm(View v) {
        email =(EditText) findViewById(R.id.edMail);
        password =(EditText) findViewById(R.id.edPassword);

//        progressBar.setVisibility(View.VISIBLE);

        String s1 = email.getText().toString();
        String s2 = password.getText().toString();

        setContentView(R.layout.activity_list);
        Log.d("login", "Click Confirm");
    }

    public void signUp(View v) {
        setContentView(R.layout.activity_signup);
    }


}
