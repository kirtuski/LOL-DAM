package com.dam.lol;

import android.app.Application;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

//Clase application para implementar la cola de volley
public class lolApplication extends Application {
    private static lolApplication sInstance;
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        mRequestQueue = Volley.newRequestQueue(this);
        sInstance = this;
    }
    public synchronized static lolApplication
    getInstance() {
        return sInstance;
    }
    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
}
