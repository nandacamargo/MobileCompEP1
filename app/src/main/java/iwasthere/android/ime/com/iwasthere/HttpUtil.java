package iwasthere.android.ime.com.iwasthere;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dududcbier on 30/04/17.
 */

public class HttpUtil {

    public static JSONObject getJSONObject(String response, String origin) {
        JSONObject resp = null;
        try {
            resp = new JSONObject(response);
        }
        catch (JSONException e) {
            resp = null;
            Log.e(origin, "unable to parse JSON");
        }
        return resp;
    }

    public static Boolean responseWasSuccess(JSONObject resp) {
        if (resp == null) return false;
        try {
            if (resp.has("success"))
                return resp.getBoolean("success");
        } catch (JSONException e) {
            Log.e("responseWasSuccess", "unable to parse JSON");
        }
        return false;
    }

    public static JSONArray getResponseDataArray(JSONObject resp) {
        if (resp == null) return null;
        try {
            if (resp.has("data"))
                return resp.getJSONArray("data");
        } catch (JSONException e) {
            Log.e("responseWasSuccess", "unable to parse JSON");
        }
        return null;
    }

    public static String getResponseDataString(JSONObject resp) {
        if (resp == null) return null;
        try {
            if (resp.has("data"))
                return resp.getString("data");
        } catch (JSONException e) {
            Log.e("responseWasSuccess", "unable to parse JSON");
        }
        return null;
    }
}
