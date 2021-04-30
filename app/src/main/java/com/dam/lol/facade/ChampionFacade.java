package com.dam.lol.facade;

import android.app.Activity;
import android.icu.text.Edits;
import android.util.Log;

import com.dam.lol.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;

public class ChampionFacade {

    //TODO optimizar función
    public String getChampionNameById(int id, Activity activity) {

        //TODO Este codigo obtiene el json como string, a lo mejor se puede encapsular como función?
        String jsonString;
        try {
            InputStream champions = activity.getAssets().open("champion.json");
            int size = champions.available();
            byte[] buffer = new byte[size];
            champions.read(buffer);
            champions.close();
            //Ese warning se puede arreglar y si subimos un poco la api minima
            jsonString = new String(buffer, "UTF-8");
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
        }catch (JSONException e){
            e.printStackTrace();
        }
        return name;

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
            //Ese warning se puede arreglar y si subimos un poco la api minima
            jsonString = new String(buffer, "UTF-8");
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
        }catch (JSONException e){
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
            //Ese warning se puede arreglar y si subimos un poco la api minima
            jsonString = new String(buffer, "UTF-8");
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
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return name;
    }
}


