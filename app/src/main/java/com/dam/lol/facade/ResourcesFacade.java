package com.dam.lol.facade;

import android.app.Application;

import com.dam.lol.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ResourcesFacade {
    private final HashMap<Integer, String> championMap;
    private final HashMap<Integer, String> summonerSpellMap;
    private final HashMap<Integer, String> queueMap;

    public ResourcesFacade(Application application) {
        //Initialise championMap
        championMap = new HashMap<>();
        try {
            JSONObject json = new JSONObject(openJsonAssetAsString("champion.json", application));
            JSONObject data = json.getJSONObject("data");
            Iterator<String> iterator = data.keys();
            while (iterator.hasNext()) {
                String mapData = iterator.next();
                JSONObject champ = data.getJSONObject(mapData);
                Integer mapKey = champ.getInt("key");
                championMap.put(mapKey, mapData);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Initialise summonerSpellMap
        summonerSpellMap = new HashMap<>();
        try {
            JSONObject json = new JSONObject(openJsonAssetAsString("summoner.json", application));
            JSONObject data = json.getJSONObject("data");
            Iterator<String> iterator = data.keys();
            while (iterator.hasNext()) {
                String mapData = iterator.next();
                JSONObject champ = data.getJSONObject(mapData);
                Integer mapKey = champ.getInt("key");
                summonerSpellMap.put(mapKey, mapData);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Initialise queueMap
        queueMap = new HashMap<>();
        try {
            JSONArray jsonArray = new JSONArray(openJsonAssetAsString("queues.json", application));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                queueMap.put(jsonObject.getInt("queueId"), jsonObject.getString("description"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    String openJsonAssetAsString(String fileJSON, Application application) {
        String jsonString;
        try {
            InputStream champions = application.getAssets().open(fileJSON);
            int size = champions.available();
            byte[] buffer = new byte[size];
            champions.read(buffer);
            champions.close();
            jsonString = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }

    public String getChampionNameById(int id) {
        return championMap.get(id);
    }

    public String getSummonerSpellNameById(int id) {
        return summonerSpellMap.get(id);
    }

    public String getQueueNameById(int id) {
        return queueMap.get(id);
    }

}


