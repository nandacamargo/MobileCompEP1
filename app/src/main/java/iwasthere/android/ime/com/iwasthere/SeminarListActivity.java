package iwasthere.android.ime.com.iwasthere;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dududcbier on 4/19/17.
 */

public class SeminarListActivity extends Activity {
    private ArrayList<Seminar> seminars;
    private ListView seminar_list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String result = getIntent().getStringExtra("result");
        try {
            JSONObject jObj = new JSONObject(result);
            JSONArray seminarsJSON = jObj.getJSONArray("data");
            this.seminars = Seminar.getSeminars(seminarsJSON);
        } catch (JSONException e) {
            Log.e("ListActivity: ", "Invalid JSON returned from GET.");
        }
        setContentView(R.layout.activity_seminar_list);
        this.seminar_list = (ListView) findViewById(R.id.seminar_list);
        SeminarsAdapter adapter = new SeminarsAdapter(this, this.seminars);
        this.seminar_list.setAdapter(adapter);
    }

}
