package com.dam.lol;

import android.util.Log;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

//Funciona al extender Activity?
public class llamaApi {
    final private String api_key;

    private Invocador invocador;

    public llamaApi(String api_key) {
        this.api_key = api_key;
    }

    //static para no crear la clase?
    public void getIdFromSummoner(String nombre, String servidor, EditText text) {

        final String URL = "https://" + servidor + ".api.riotgames.com/lol/summoner/v4/summoners/by-name/" + nombre + "?api_key=" + api_key;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley", response.toString());
                        try {

                            invocador = new Invocador();
                            invocador.setId(response.getString("id"));
                            invocador.setPuuid(response.getString("puuid"));
                            invocador.setName(response.getString("name"));
                            invocador.setProfileIconId(response.getInt("profileIconId"));
                            invocador.setSummonerLevel(response.getInt("summonerLevel"));

                            //Trigger para avisar a la clase principal
                            text.setText("ok");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        text.setText("nok");
                        VolleyLog.e("Error", error.getMessage());
                    }
                });

        lolApplication.getInstance().getRequestQueue().add(jsonObjectRequest);
    }

    public Invocador getInvocador() {
        return invocador;
    }
}