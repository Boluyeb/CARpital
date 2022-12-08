package uk.ac.tees.a0174604.carpital;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;

    private VolleySingleton(Context context){
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

//    ensure only one thread is created
    public static  synchronized  VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getmRequestQueue() {
        return mRequestQueue;
    }
}
