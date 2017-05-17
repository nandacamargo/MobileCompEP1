package iwasthere.android.ime.com.iwasthere;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import android.support.test.espresso.IdlingResource;

import java.lang.reflect.Field;
import java.util.Set;

public class VolleyIdlingResource implements IdlingResource {

    private volatile ResourceCallback resourceCallback;

    private RequestQueue requestQueue;
    private Field mCurrentRequests;

    public VolleyIdlingResource() throws NoSuchFieldException {

        requestQueue = RequestQueueSingleton.getRequestQueue();

        mCurrentRequests = RequestQueue.class.getDeclaredField("mCurrentRequests");
        mCurrentRequests.setAccessible(true);
    }

    @Override
    public String getName() {
        return "Volley Idling Resource";
    }

    @Override
    public boolean isIdleNow() {
        try {
            Set<Request> set =  (Set<Request>) mCurrentRequests.get(requestQueue);
            if (set != null) {
                if (set.size() == 0) {
                    resourceCallback.onTransitionToIdle();
                    return true;
                }
                return false;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }
}
