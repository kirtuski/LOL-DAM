package com.dam.lol;

import android.app.Application;

import androidx.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dam.lol.facade.ApiFacade;

//Clase application para implementar la cola de volley
public class LolApplication extends Application {
    private static LolApplication sInstance;
    private RequestQueue mRequestQueue;
    private ApiFacade apiFacade;

    @Override
    public void onCreate() {
        super.onCreate();
        mRequestQueue = Volley.newRequestQueue(this);
        sInstance = this;
        this.apiFacade = new ApiFacade(PreferenceManager.getDefaultSharedPreferences(this).getString("key", ""));
    }
    public synchronized static LolApplication
    getInstance() {
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ApiFacade getApiFacade() {
        return apiFacade;
    }
}
