package com.dam.lol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

//TODO a lo mejor se puede crear una clase para implementar el listener de AdapterView para limpiar el main?
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Elementos del layout
    Spinner servidorSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        servidorSpinner = findViewById(R.id.servidorSpinner);
        servidorSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.servers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servidorSpinner.setAdapter(adapter);

    }

    //Metodo para cuando seleccionamos un elemento del selector
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String server = getResources().getStringArray(R.array.servers)[ (int) id ];
        Log.d("Spinner", "Se ha seleccionado: " + id + " " + server);
    }

    //Otro metodo que tiene que estar por implementar la interfaz
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    //Metodo que busca al invocador y si lo encuentra lanza un intent con la nueva actividad
    //Si no pues un toast de error?
    public void BuscaInvocador(View view) {
        final String api_key = "RGAPI-df09dc5e-4ba8-47d3-8286-85dd64d28ff6";
        //final String URL = "https://euw1.api.riotgames.com/lol/status/v3/shard-data?api_key=" + api_key;
        String URL = "https://euw1.api.riotgames.com/lol/status/v3/shard-data?api_key=RGAPI-df09dc5e-4ba8-47d3-8286-85dd64d28ff6";


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley", response.toString());
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

    }

    //Metodo que abre la nueva actividad con los ajustes
    public void AbrirAjustes(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        //Podemos pasar informacion entre actividades con el intent
        intent.putExtra("parametro", 2);
        startActivity(intent);
    }
}