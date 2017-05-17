package iwasthere.android.ime.com.iwasthere;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    private User user;
    private String nusp;
    private Seminar seminar;
    private int seminarId = 0;
    private CheckAdapter adapter;

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

        user = null;
        user = UserSingleton.getInstance();
        nusp = user.getNusp();

        emptyText = (TextView)findViewById(android.R.id.empty);
        emptyText.setVisibility(View.GONE);

        seminar = SeminarSingleton.getInstance();
        seminarId = seminar.getId();

        Button studentsWereThere = (Button) findViewById(R.id.students_were_there);

        if (user.isTeacher()) {
            acceptRequests();
        }
        else {
            studentsWereThere.setVisibility(View.GONE);
            confirmPresence(user, 0);
        }
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
        private Context context;

        public CheckAdapter(Context context, ArrayList<CheckboxModel> checkboxList) {

            super(context, 0, checkboxList);

            this.context = context;
            this.userList = attendees;
            this.filteredUserList = attendees;
        }


        private class ViewHolder {
            TextView nusp;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Log.d("AcceptConfirmation", "User: " + "On get view");

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.row, null);

                holder = new ViewHolder();
                holder.nusp = (TextView) convertView.findViewById(R.id.textView1);
                holder.nusp.setText(" (nusp: " + user.getNusp() + ")");

                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        User user = (User) cb.getTag();
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        user.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            User user = userList.get(position);

            holder.name.setText(user.getName());
            holder.name.setChecked(user.isSelected());
            holder.name.setTag(user);


            return convertView;
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

    public void studentsWereThere(View view) {

        StringBuffer responseText = new StringBuffer();
        String url = "http://207.38.82.139:8001/attendence/submit";
        responseText.append("The following were selected...\n");

        ArrayList<User> userList = adapter.userList;

        for (int i = 0; i < userList.size(); i++) {
            final User curr_user = userList.get(i);
            if (curr_user.isSelected()){
                responseText.append("\n" + curr_user.getName());

                confirmPresence(curr_user, 1);
            }
        }
        Log.d("AcceptConfirmation", "Leaving studentsWereThere");
        Intent i = new Intent(getApplicationContext(), AttendeesListActivity.class);
        startActivity(i);

        Toast.makeText(getApplicationContext(),
                R.string.students_confirmation_successed, Toast.LENGTH_LONG).show();
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
                    JSONObject info;
                    String url = null;
                    String confirmed = "1";
                    String data_field = "";

                    Log.d("AcceptConfirmation", "data: " + data);

                    try {
                        info = data.getJSONObject(i);
                        confirmed = info.getString("confirmed");
                        data_field = info.getString("data");
                        url = "http://207.38.82.139:8001/student/get/" + info.getString("student_nusp");
                        Log.d("AcceptConfirmation", "url: " + url);
                    } catch (JSONException e) {
                        Log.e("getUsersTask", e.getMessage());
                    }

                    final int finalconfirmed = Integer.parseInt(confirmed);
                    final String field = data_field;
                        StringRequest strRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        JSONObject resp = HttpUtil.getJSONObject(response, "getUsersTask");
                                        if (HttpUtil.responseWasSuccess(resp)) {
                                            String data = HttpUtil.getResponseDataString(resp);
                                            User user = new User(data, false);
                                            if (finalconfirmed == 0 || field == "pending") {
                                                attendees.add(user);
                                                allAttendees.add(user);
                                            }
                                            latch.countDown();
                                        } else {
                                            Snackbar.make(findViewById(android.R.id.content), R.string.error_connection, Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
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

                    // When clicked, show a toast with the TextView text
                    User user_to_confirm = (User) parent.getItemAtPosition(position);
                    Toast.makeText(getApplicationContext(),
                    "Clicked on Row: " + user_to_confirm.getName(),
                    Toast.LENGTH_LONG).show();
                }
            });

            emptyText.setVisibility(View.VISIBLE);
            requestList.setEmptyView(emptyText);
        }
    }

    /*******************************************************/

    private void showDialog(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(AcceptConfirmationActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("Ok", null);
        builder.setCancelable(true);
        builder.create().show();
    }


    public void confirmPresence(final User curr_user, final int confirmed) {

        String url;
        final String data;
        url = "http://207.38.82.139:8001/attendence/submit";

        if (confirmed == 1)
            data = "confirmed";
        else
            data = "pending";

        Log.d("AcceptConfirmation", "User name: " + curr_user.getName());

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response: ", response);
                        JSONObject resp = HttpUtil.getJSONObject(response, "sendPresenceConfirmation");
                        if (HttpUtil.responseWasSuccess(resp)) {
                            Log.d("sendConfirmation", "SUCCESS");
                            postPresenceConfirmation(confirmed);
                        }
                        else {
                            Snackbar.make(findViewById(android.R.id.content), "An error occurred. Please try again later.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nusp", curr_user.getNusp());
                params.put("seminar_id", "" + seminarId);
                params.put("data", data);
                params.put("confirmed", "" + confirmed);
                return params;
            }
        };
        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(strRequest);
        Log.d("SendConfirmation", "Leaving confirmPresence");
    }

    private void postPresenceConfirmation(int confirmed) {
        /*Intent i = new Intent(getApplicationContext(), SeminarListActivity.class);
        startActivity(i);*/

        if (confirmed == 0)
            showDialog("Success",  getApplicationContext().getString(R.string.pending_request));
        else
            showDialog("Success",  getApplicationContext().getString(R.string.confirmation_successed));
    }

}
