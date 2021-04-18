package com.dam.lol.facade;

import android.app.Activity;
import android.icu.text.Edits;
import android.util.Log;

import com.dam.lol.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class ChampionFacade {

    //TODO optimizar función
    public String getChampionNameById(int id, Activity activity) throws JSONException {

        //TODO Este codigo obtiene el json como string, a lo mejor se puede encapsular como función?
        String jsonString;
        try {
            InputStream champions = activity.getResources().openRawResource(R.raw.champion);
            //Si se abre con ese metodo se puede encapsular la función, contra, no se escribir la ruta
            //champions = activity.getAssets().open("champion.json");
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

        JSONObject json = new JSONObject(jsonString);
        Log.d("json", json.toString());
        Log.d("json", json.getJSONObject("data").toString());
        JSONObject data = json.getJSONObject("data");
        Iterator<String> keys = data.keys();
        String name = "";
        //Hay que recorrer siempre el json completo, se podría simplificar si creamos un archivo xml que contenga los datos que queremos, similar a server y server_url
        while(keys.hasNext()){
            String key = keys.next();
            JSONObject champ = data.getJSONObject(key);
            if (champ.getInt("key") == id)
                name = key;
        }

        return name;

    }


}


