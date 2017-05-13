package iwasthere.android.ime.com.iwasthere;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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

/**
 * Created by nanda on 13/05/17.
 */

public class AcceptConfirmationActivity extends AppCompatActivity {

    private Seminar seminar;
    private int seminarId = 0;
    private CheckAdapter adapter;
    private CheckBox cb;

    private ArrayList<User> allAttendees = new ArrayList<>();
    private ArrayList<User> attendees = new ArrayList<>();
    private ArrayList<CheckboxModel> modelItems  = new ArrayList<>();;

    private ProgressBar progressBar;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_confirmation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emptyText = (TextView)findViewById(android.R.id.empty);
        emptyText.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.list_progress);
        progressBar.setVisibility(View.VISIBLE);

        seminar = SeminarSingleton.getInstance();
        seminarId = seminar.getId();

        acceptRequests();
    }


    public void acceptRequests() {

        Log.d("AcceptConfirmation", "On acceptRequest");

        String url = "http://207.38.82.139:8001/attendence/listStudents";

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response: ", response);
                        JSONObject resp = HttpUtil.getJSONObject(response, "AcceptConfirmationActivity");
                        if (HttpUtil.responseWasSuccess(resp)) {
                            Log.d("AcceptConfirmation", resp.toString());
                            JSONArray data = HttpUtil.getResponseDataArray(resp);
                            new AcceptConfirmationActivity.getUsersTask().execute(data);
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
                params.put("seminar_id", "" + seminarId);
                return params;
            }
        };
        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(strRequest);

    }

    public class CheckAdapter extends ArrayAdapter<CheckboxModel> implements Filterable {

        private ArrayList<User> userList;
        private ArrayList<User> filteredUserList;
        private ArrayList<CheckboxModel> checkboxList;

        //private CheckboxModel[] modelItems;
        private Context context;

        public CheckAdapter(Context context, ArrayList<CheckboxModel> checkboxList) {

            super(context, 0, checkboxList);

            this.context = context;
            this.checkboxList = checkboxList;

            this.userList = attendees;
            this.filteredUserList = attendees;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Log.d("AcceptConfirmation", "User: " + "On get view");
            User user = getUser(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);
            }
            TextView name = (TextView) convertView.findViewById(R.id.textView1);
            name.setText(user.getName());

            cb = (CheckBox) convertView.findViewById(R.id.checkBox1);

            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkboxClicked(view);
                }
            });

            return convertView;

        }

        public void checkboxClicked(View v) {

            if(cb.isChecked()) {
                Log.d("AcceptConfirmation", "Checkbox is checked. Id = " + cb.getId());

                //modelItems.get(i).setValue(1);
                // true,do the task
            }
            else {
            }
        }
        public User getItemAtPosition(int pos) {
            return filteredUserList.get(pos);
        }

        @Override
        public int getCount() {
            return userList.size();
        }

        /*@Override*/
        public User getUser(int i) {
            return checkboxList.get(i).getUser();
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

            Log.d("AcceptConfirmation", "On getUsersTask");

            if (data != null && data.length() > 0) {
                latch = new CountDownLatch(data.length());

                for (int i = 0; i < data.length(); i++) {
                    JSONObject user;
                    String url = null;
                    try {
                        user = data.getJSONObject(i);
                        url = "http://207.38.82.139:8001/student/get/" + user.getString("student_nusp");
                        Log.d("AcceptConfirmation", "url: " + url);
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

                                        CheckboxModel check = new CheckboxModel(user, 0);
                                        modelItems.add(check);
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

            Log.d("AcceptConfirmation", "onPostExecute");
            ListView requestList = (ListView) findViewById(R.id.list_requests);

            adapter = new AcceptConfirmationActivity.CheckAdapter(getApplicationContext(), modelItems);

            requestList.setAdapter(adapter);
            requestList.setTextFilterEnabled(false);

            requestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("AcceptConfirmation", "On confirmPresenceButton");

                }
            });

            progressBar.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
            requestList.setEmptyView(emptyText);
        }
    }


}
