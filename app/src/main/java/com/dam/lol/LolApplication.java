package com.dam.lol;

import android.app.Application;
import android.media.Image;

import androidx.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dam.lol.facade.ApiFacade;
import com.dam.lol.facade.ImageFacade;

//Clase application para implementar la cola de volley
public class LolApplication extends Application {
    private static LolApplication sInstance;
    private RequestQueue mRequestQueue;
    private ApiFacade apiFacade;
    private ImageFacade imageFacade;

    @Override
    public void onCreate() {
        super.onCreate();
        mRequestQueue = Volley.newRequestQueue(this);
        sInstance = this;
        this.apiFacade = new ApiFacade(PreferenceManager.getDefaultSharedPreferences(this).getString("key", ""));
        this.imageFacade = new ImageFacade();
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
    public ImageFacade getImageFacade() {
        return imageFacade;
    }

}
