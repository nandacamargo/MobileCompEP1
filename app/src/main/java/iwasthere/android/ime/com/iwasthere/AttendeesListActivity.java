package iwasthere.android.ime.com.iwasthere;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AttendeesListActivity extends AppCompatActivity {

    private static ArrayList<User> attendees;
    private UsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendees_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int id = getIntent().getIntExtra("id", -1);
        if (id < 0) finish();
        Log.d("ID obtido:","" + id);
        try {
            new GetAttendeesTask().execute("" + id).get();
        } catch (Exception e) {

        }
        Log.d("Attendees", "JA TERMINEI");
        if (attendees != null) Log.d("Attendees:", attendees.toString());
        else Log.d("Afferson", "vsf");

//        ListView attendeesList = (ListView) findViewById(R.id.list);
//        adapter = new UsersAdapter(this, attendees);
//        attendeesList.setAdapter(adapter);
//        attendeesList.setTextFilterEnabled(true);
    }

    private class GetAttendeesTask extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... params) {
            String postParams = "seminar_id=" + params[0];
            String StringURL = "http://207.38.82.139:8001/attendence/listStudents";

            return HttpUtil.doPost(StringURL, postParams);
        }

        @Override
        protected void onPostExecute(JSONObject jObj) {
            if (jObj != null) {
                try {
                    attendees = User.getStudents(jObj.getJSONArray("data"));
                    Log.d("GetAttendeesTask", attendees.toString());
                } catch (JSONException e) {
                    attendees = new ArrayList<>();
                }
            }
        }
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
                    for (int i = 0; i < attendees.size(); i++) {
                        if (attendees.get(i).getName().toLowerCase().contains(constraint)) {
                            filteredUserList.add(attendees.get(i));
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

}
