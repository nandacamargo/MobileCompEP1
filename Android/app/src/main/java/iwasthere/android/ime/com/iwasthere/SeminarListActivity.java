package iwasthere.android.ime.com.iwasthere;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class SeminarListActivity extends AppCompatActivity {

    private ArrayList<Seminar> seminars;
    private ArrayList<Seminar> allSeminars;
    private SearchView mSearchView;
    private SeminarsAdapter adapter;
    private User user;
    private SeminarSingleton seminar;

    private String name;
    private int seminarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seminar_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = UserSingleton.getInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (!user.isTeacher())
            fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddSeminarActivity.class);
                startActivity(i);
            }
        });

        String url = "http://207.38.82.139:8001/seminar";
        StringRequest strRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.d("Response: ", response);
                        JSONObject resp = HttpUtil.getJSONObject(response, "SeminarListActivity-onCreate");
                        if (HttpUtil.responseWasSuccess(resp)) getSeminars(resp);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), R.string.error_connection, Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(strRequest);

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
        mSearchView.setSubmitButtonEnabled(false);
        mSearchView.setQueryHint(getString(R.string.search_hint));
    }

    public void getSeminars(JSONObject resp){
        JSONArray seminarsJSON =  HttpUtil.getResponseDataArray(resp);
        this.seminars = Seminar.getSeminars(seminarsJSON);
        this.allSeminars = Seminar.getSeminars(seminarsJSON);
        Collections.sort(this.seminars);
        Collections.sort(this.allSeminars);

        final ListView seminarList = (ListView) findViewById(R.id.list);
        adapter = new SeminarsAdapter(this, seminars);
        seminarList.setAdapter(adapter);
        seminarList.setTextFilterEnabled(true);
        seminarList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), AttendeesListActivity.class);
                i.putExtra("id", adapter.getItemAtPosition(position).getId());
                name = adapter.getItemAtPosition(position).getName();
                seminarId = adapter.getItemAtPosition(position).getId();
                SeminarSingleton.deleteInstance();
                SeminarSingleton.getInstance(name, seminarId);
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        if (!user.isTeacher()) {
            MenuItem menuItem = menu.findItem(R.id.new_teacher);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent i;
        switch (item.getItemId()) {
            case R.id.new_teacher:
                i = new Intent(getApplicationContext(), SignUpActivity.class);
                i.putExtra("teacher", true);
                startActivity(i);
                return true;
            case R.id.my_account:
                i = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class SeminarsAdapter extends ArrayAdapter<Seminar> implements Filterable {

        private ArrayList<Seminar> seminarList;
        private ArrayList<Seminar> filteredSeminarList;

        public SeminarsAdapter(Context context, ArrayList<Seminar> seminars) {
            super(context, 0, seminars);
            this.seminarList = seminars;
            filteredSeminarList = seminars;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Seminar seminar = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            }
            TextView name = (TextView) convertView.findViewById(R.id.item_name);
            name.setText(seminar.getName());
            return convertView;
        }

        public Seminar getItemAtPosition(int pos) {
            return filteredSeminarList.get(pos);
        }

        @Override
        public int getCount() {
            return seminarList.size();
        }

        @Override
        public Seminar getItem(int i) {
            return seminarList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public Filter getFilter() {

            final Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    seminarList = (ArrayList<Seminar>) results.values;
                    notifyDataSetChanged();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults results = new FilterResults();
                    filteredSeminarList = new ArrayList<Seminar>();

                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < allSeminars.size(); i++) {
                        if (allSeminars.get(i).getName().toLowerCase().contains(constraint)) {
                            filteredSeminarList.add(allSeminars.get(i));
                        }
                    }
                    results.count = filteredSeminarList.size();
                    results.values = filteredSeminarList;

                    return results;
                }
            };

            return filter;
        }
    }

}
