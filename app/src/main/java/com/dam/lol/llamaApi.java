package com.dam.lol;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

//Funciona al extender Activity?
public class llamaApi extends Activity {
    //final private String api_key = PreferenceManager.getDefaultSharedPreferences(this).getString("key","");

    private Invocador invocador;

    //static para no crear la clase?
    public Invocador getIdFromSummoner(String nombre, String servidor, String api_key){
        //TODO el servidor va al principio de la url, EUW corresponde a euw1, hay que hacer una tabla con las equivalencias

        final String URL = "https://" + servidor + ".api.riotgames.com/lol/summoner/v4/summoners/by-name/" + nombre + "?api_key=" + api_key;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley", response.toString());
                        try {

                        invocador = new Invocador();
                        invocador.setId( response.getString("id") );
                        invocador.setPuuid( response.getString("puuid"));
                        invocador.setName( response.getString("name"));
                        invocador.setProfileIconId( response.getInt("profileIconId"));
                        invocador.setSummonerLevel( response.getInt("summonerLevel"));

                        EditText text= findViewById(R.id.activadorSummoner);
                        text.setText("ok");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        VolleyLog.e("Error", error.getMessage());
                        Toast.makeText(getApplicationContext(), "Ha petao", Toast.LENGTH_SHORT).show();

                    }
                });

        lolApplication.getInstance().getRequestQueue().add(jsonObjectRequest);
        return invocador;
    }

}
