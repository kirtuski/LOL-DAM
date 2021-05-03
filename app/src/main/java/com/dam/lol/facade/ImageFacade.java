package com.dam.lol.facade;

import android.graphics.drawable.Drawable;
import android.os.StrictMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageFacade {
    public Drawable getChampionImageByName(String name) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            InputStream is = (InputStream) new URL("http://ddragon.leagueoflegends.com/cdn/11.8.1/img/champion/" + name + ".png").getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public Drawable getSummonerSpellImageByName(String name) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            InputStream is = (InputStream) new URL("http://ddragon.leagueoflegends.com/cdn/11.8.1/img/spell/" + name + ".png").getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Drawable getProfileIconById(int id) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            InputStream is = (InputStream) new URL("http://ddragon.leagueoflegends.com/cdn/11.8.1/img/profileicon/" + id + ".png").getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Drawable getSplashByChampionName(String name) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            InputStream is = (InputStream) new URL("http://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + name + "_0.jpg").getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
