package iwasthere.android.ime.com.iwasthere;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class AttendeesListActivity extends AppCompatActivity {

    private ArrayList<User> allAttendees = new ArrayList<>();
    private ArrayList<User> attendees = new ArrayList<>();
    private UsersAdapter adapter;
    private SearchView mSearchView;
    private ProgressBar progressBar;
    private TextView emptyText;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendees_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emptyText = (TextView)findViewById(android.R.id.empty);
        emptyText.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.list_progress);
        progressBar.setVisibility(View.VISIBLE);

        final int id = getIntent().getIntExtra("id", -1);
        if (id < 0) finish();

        user = UserSingleton.getInstance();

        String url = "http://207.38.82.139:8001/attendence/listStudents";

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response: ", response);
                        JSONObject resp = HttpUtil.getJSONObject(response, "AttendeesListActivity");
                        if (HttpUtil.responseWasSuccess(resp)) {
                            Log.d("AttendeesListActivity", resp.toString());
                            JSONArray data = HttpUtil.getResponseDataArray(resp);
                            new getUsersTask().execute(data);
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), R.string.error_connection, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_connection, Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("seminar_id", "" + id);
                return params;
            }
        };
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

    public class UsersAdapter extends ArrayAdapter<User> implements Filterable {

        private ArrayList<User> userList;
        private ArrayList<User> filteredUserList;

        public UsersAdapter(Context context, ArrayList<User> users) {
            super(context, 0, users);
            this.userList = users;
            filteredUserList = users;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            User user = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            }
            TextView name = (TextView) convertView.findViewById(R.id.item_name);
            name.setText(user.getName());
            return convertView;
        }

        public User getItemAtPosition(int pos) {
            return filteredUserList.get(pos);
        }

        @Override
        public int getCount() {
            return userList.size();
        }

        @Override
        public User getItem(int i) {
            return userList.get(i);
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

                    userList = (ArrayList<User>) results.values;
                    notifyDataSetChanged();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults results = new FilterResults();
                    filteredUserList = new ArrayList<>();

                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < allAttendees.size(); i++) {
                        if (allAttendees.get(i).getName().toLowerCase().contains(constraint)) {
                            filteredUserList.add(allAttendees.get(i));
                        }
                    }
                    results.count = filteredUserList.size();
                    results.values = filteredUserList;

                    return results;
                }
            };

            return filter;
        }
    }

    // The getUserTask is responsible for synchronizing the threads that get the attendees infor-
    // mation. This is important because otherwise the ListView ends up empty since it ends up
    // being initialized before any of the http get requests are done.
    public class getUsersTask extends AsyncTask<JSONArray, Void, Void> {

        private CountDownLatch latch;

        @Override
        protected Void doInBackground(JSONArray... params) {
            JSONArray data = params[0];

            if (data != null && data.length() > 0) {
                latch = new CountDownLatch(data.length());

                for (int i = 0; i < data.length(); i++) {
                    JSONObject user;
                    String url = null;
                    try {
                        user = data.getJSONObject(i);
                        url = "http://207.38.82.139:8001/student/get/" + user.getString("student_nusp");
                    } catch (JSONException e) {
                        Log.e("getUsersTask", e.getMessage());
                    }

                    StringRequest strRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONObject resp = HttpUtil.getJSONObject(response, "getUsersTask");
                                    if (HttpUtil.responseWasSuccess(resp)) {
                                        String data = HttpUtil.getResponseDataString(resp);
                                        User user = new User(data, false);
                                        attendees.add(user);
                                        allAttendees.add(user);
                                        latch.countDown();
                                    } else {
                                        Snackbar.make(findViewById(android.R.id.content), R.string.error_connection, Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), R.string.error_connection, Toast.LENGTH_SHORT).show();
                                }
                            });
                    RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(strRequest);
                }
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    Log.e("Thread", "interrupted");
                }
            }
            else {
                Log.e("getUsersTask", "data is null");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ListView attendeesList = (ListView) findViewById(R.id.list);
            adapter = new UsersAdapter(getApplicationContext(), attendees);
            attendeesList.setAdapter(adapter);
            attendeesList.setTextFilterEnabled(false);
            attendeesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("AttendeesList", "On iWasThereButton");

                }
            });
            progressBar.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
            attendeesList.setEmptyView(emptyText);
        }
    }

    public void iWasThereButton(View v) {

        Intent i = new Intent(getApplicationContext(), SendConfirmationActivity.class);
        startActivity(i);
    }
}
