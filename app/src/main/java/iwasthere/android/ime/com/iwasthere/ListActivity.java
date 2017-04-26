package iwasthere.android.ime.com.iwasthere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.ExecutionException;

/**
 * Created by dududcbier on 4/19/17.
 */

public class ListActivity extends Activity {
    HttpGetRequest http;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }

    public void addTeacher(View v){
        Log.d("list", "Click Add");
    }

    public void listSeminars(View v) throws InterruptedException, ExecutionException {
        String result = new HttpGetRequest().execute("http://207.38.82.139:8001/seminar").get();
        Intent intent = new Intent(this, SeminarListActivity.class);
        intent.putExtra("result", result);
        startActivity(intent);
    }
}
