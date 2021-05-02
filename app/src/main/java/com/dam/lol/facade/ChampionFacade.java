package com.dam.lol.facade;

import android.app.Activity;
import android.app.Application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ChampionFacade {
    private HashMap<Integer, String> championJson;
    private ArrayList<String> summonerSpellJson;
    private ArrayList<String> queueJson;

    public ChampionFacade(Application application) {
        //Initialise championJson
        championJson = new HashMap<>();
        try {
            JSONObject json = openJsonAsset("champion.json", application);
            JSONObject data = json.getJSONObject("data");
            Iterator<String> iterator = data.keys();
            while (iterator.hasNext()) {
                String mapData = iterator.next();
                JSONObject champ = data.getJSONObject(mapData);
                Integer mapKey = champ.getInt("key");
                championJson.put(mapKey, mapData);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    JSONObject openJsonAsset(String fileJSON, Application application) {
        JSONObject jsonObject;
        try {
            InputStream champions = application.getAssets().open(fileJSON);
            int size = champions.available();
            byte[] buffer = new byte[size];
            champions.read(buffer);
            champions.close();
            jsonObject = new JSONObject(new String(buffer, StandardCharsets.UTF_8));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }

    public String getChampionNameById(int id) {
        return championJson.get(id);
    }

    public String getSummonerSpellNameById(int id, Activity activity) {
        //TODO Este codigo obtiene el json como string, a lo mejor se puede encapsular como función?
        String jsonString;
        try {
            InputStream summoner = activity.getAssets().open("summoner.json");
            int size = summoner.available();
            byte[] buffer = new byte[size];
            summoner.read(buffer);
            summoner.close();
            jsonString = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String name = "";
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONObject data = json.getJSONObject("data");
            Iterator<String> keys = data.keys();
            //Hay que recorrer siempre el json completo, se podría simplificar si creamos un archivo xml que contenga
            // los datos que queremos, similar a server y server_url, un array de los campeones ordenados por id vamos
            //Y nos ahorramos tambien el codigo de arriba
            // TODO constructor crea diccionario que luego usa esta funcion
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject champ = data.getJSONObject(key);
                if (champ.getInt("key") == id)
                    name = key;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    public String getQueueNameById(int id, Activity activity) {
        //TODO Este codigo obtiene el json como string, a lo mejor se puede encapsular como función?
        String jsonString;
        try {
            InputStream queue = activity.getAssets().open("queues.json");
            int size = queue.available();
            byte[] buffer = new byte[size];
            queue.read(buffer);
            queue.close();
            jsonString = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String name = "";
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getInt("queueId") == id) {
                    name = jsonObject.getString("description");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return name;
    }
}


