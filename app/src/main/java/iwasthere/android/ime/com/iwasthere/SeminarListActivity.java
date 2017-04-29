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
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SeminarListActivity extends AppCompatActivity {

    private ArrayList<Seminar> seminars;
    private ArrayList<Seminar> allSeminars;
    private ListView seminar_list;
    private SearchView mSearchView;
    private SeminarsAdapter adapter;
    private Boolean teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seminar_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        teacher = getIntent().getBooleanExtra("teacher", false);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (!teacher)
            fab.setVisibility(View.GONE);
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
            this.allSeminars = Seminar.getSeminars(seminarsJSON);
        } catch (JSONException e) {
            Log.e("ListActivity: ", "Invalid JSON returned from GET.");
        }
        this.seminar_list = (ListView) findViewById(R.id.seminar_list);
        adapter = new SeminarsAdapter(this, seminars);
        this.seminar_list.setAdapter(adapter);
        seminar_list.setTextFilterEnabled(true);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        if (!teacher) {
            MenuItem menuItem = menu.findItem(R.id.new_teacher);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_teacher:
                return true;
            case R.id.my_account:
                Intent i = new Intent(getApplicationContext(), RegisterUpdateActivity.class);
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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.seminar_item, parent, false);
            }
            TextView name = (TextView) convertView.findViewById(R.id.seminar_name);
            name.setText(seminar.getName());
            return convertView;
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
