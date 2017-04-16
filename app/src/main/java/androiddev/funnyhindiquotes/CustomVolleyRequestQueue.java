package androiddev.funnyhindiquotes;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

/**
 * Created by Gurpreet on 30-03-2017.
 */

public class CustomVolleyRequestQueue {
    private static CustomVolleyRequestQueue mInstance;
    private static Context context;
    private RequestQueue requestQueue;

    private CustomVolleyRequestQueue(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized CustomVolleyRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CustomVolleyRequestQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
           requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        requestQueue.add(request);
    }
}
