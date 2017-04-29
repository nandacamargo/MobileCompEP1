package iwasthere.android.ime.com.iwasthere;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SeminarListActivity extends AppCompatActivity {

    private ArrayList<Seminar> seminars;
    private ListView seminar_list;

    public class SeminarsAdapter extends ArrayAdapter<Seminar> {
        public SeminarsAdapter(Context context, ArrayList<Seminar> seminars) {
            super(context, 0, seminars);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Seminar seminar = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.seminar_item, parent, false);
            }
            TextView name = (TextView) convertView.findViewById(R.id.seminar_name);
            name.setText(seminar.getName());
            return convertView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seminar_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddSeminarActivity.class);
                startActivity(i);
            }
        });
        String result = getIntent().getStringExtra("result");
        try {
            JSONObject jObj = new JSONObject(result);
            JSONArray seminarsJSON = jObj.getJSONArray("data");
            this.seminars = Seminar.getSeminars(seminarsJSON);
        } catch (JSONException e) {
            Log.e("ListActivity: ", "Invalid JSON returned from GET.");
        }
        this.seminar_list = (ListView) findViewById(R.id.seminar_list);
        SeminarsAdapter adapter = new SeminarsAdapter(this, this.seminars);
        this.seminar_list.setAdapter(adapter);
    }



}
