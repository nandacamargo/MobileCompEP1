package com.example.nanda.register;

/**
 * Created by nanda on 17/04/17.
 */


import java.util.ArrayList;
import java.util.List;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


public class RegisterUser extends Activity {
    EditText mail,password;
    Button register,login;
    ProgressBar progressBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_teacher);
        mail =(EditText) findViewById(R.id.edMail);
        password =(EditText) findViewById(R.id.edPassword);
        register =(Button) findViewById(R.id.btConfirm);

        progressBar=(ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);

        register.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                String s1 = mail.getText().toString();
                String s2 = password.getText().toString();
                new ExecuteTask().execute(s1,s2);
            }
        });

    }

    class ExecuteTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
          /*  PostData(params);*/
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
        }

    }

/*
    public void PostData(String[] values) {
        try {
            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://10.0.0.8:7777/HttpPostServlet/servlet/httpPostServlet");
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", valuse[0]));
            list.add(new BasicNameValuePair("pass",valuse[1]));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            httpClient.execute(httpPost);
        }
        catch(Exception e) {
            System.out.println(e);
        }

    }*/

}
