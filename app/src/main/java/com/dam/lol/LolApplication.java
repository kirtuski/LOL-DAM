package com.dam.lol;

import android.app.Application;

import androidx.preference.PreferenceManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.dam.lol.facade.ApiFacade;
import com.dam.lol.facade.ChampionFacade;
import com.dam.lol.facade.DatabaseFacade;
import com.dam.lol.facade.ImageFacade;
import com.dam.lol.cache.LruBitmapCache;

//Clase application para implementar la cola de volley
public class LolApplication extends Application {
    private static LolApplication sInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader imageLoader;

    //Facades
    private ApiFacade apiFacade;
    private ImageFacade imageFacade;
    private ChampionFacade championFacade;
    private DatabaseFacade databaseFacade;

    public synchronized static LolApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mRequestQueue = Volley.newRequestQueue(this);
        imageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        this.apiFacade = new ApiFacade(PreferenceManager.getDefaultSharedPreferences(this).getString("key", ""));
        this.imageFacade = new ImageFacade();
        this.championFacade = new ChampionFacade(this);
        this.databaseFacade = new DatabaseFacade(this.getApplicationContext(), "DatabaseFacade", null, 1);
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return this.imageLoader;
    }

    public ApiFacade getApiFacade() {
        return apiFacade;
    }

    public ImageFacade getImageFacade() {
        return imageFacade;
    }

    public ChampionFacade getChampionFacade() {
        return championFacade;
    }

    public DatabaseFacade getDatabaseFacade() {
        return databaseFacade;
    }

    public void reloadApiKey() {
        this.apiFacade = new ApiFacade(PreferenceManager.getDefaultSharedPreferences(this).getString("key", ""));
    }
}
