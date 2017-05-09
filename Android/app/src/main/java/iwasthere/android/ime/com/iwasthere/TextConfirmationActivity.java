package iwasthere.android.ime.com.iwasthere;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nanda on 08/05/17.
 */

public class TextConfirmationActivity extends AppCompatActivity{

    private User user;
    private EditText nameView;

    private String name;
    private int seminarId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_confirmation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_text_confirmation);

        nameView = (EditText) findViewById(R.id.yourName);

        user = UserSingleton.getInstance();

    }

    public void confirmPresence(View v) {

        String url = "http://207.38.82.139:8001/attendence/submit";
        final int confirmed;

        if (user.isTeacher()) confirmed = 1;
        else confirmed = 0;

        Log.d("Confirmed = ", "" + confirmed);

        name = nameView.getText().toString();
        Log.d("TextConfirmation", "name: " + name);

        if (name.length() < 1) {
            nameView.setError(getString(R.string.error_field_required));
            nameView.requestFocus();
        }

        //TODO: Fix the seminarId, it's getting the wrong id
        seminarId = getIntent().getIntExtra("id", -1);

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Response: ", response);
                    JSONObject resp = HttpUtil.getJSONObject(response, "confirmPresence");
                    if (HttpUtil.responseWasSuccess(resp)) {
                        postConfirmPresence();
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
                    params.put("nusp", user.getNusp());
                    params.put("seminar_id", "" + seminarId);
                    params.put("data", name);
                    params.put("confirmed", "" + confirmed);
                    return params;
                }
            };
            RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(strRequest);

    }

    private void postConfirmPresence() {
        Intent i = new Intent(this, AttendeesListActivity.class);
        startActivity(i);
    }
}
