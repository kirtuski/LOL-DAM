package com.dam.lol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;

//TODO a lo mejor se puede crear una clase para implementar el listener de AdapterView para limpiar el main?
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Elementos del layout
    Spinner servidorSpinner;
    TextInputLayout nombreInvocadorInput;
    EditText trigger;

    private int server_id;

    //Necesitamos que se cree solo una vez? A lo mejor lo podemos instanciar como volley
    private llamaApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = new llamaApi(PreferenceManager.getDefaultSharedPreferences(this).getString("key", ""), this);

        servidorSpinner = findViewById(R.id.servidorSpinner);
        servidorSpinner.setOnItemSelectedListener(this);

        nombreInvocadorInput = findViewById(R.id.NombreInvocadorLayout);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.servers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servidorSpinner.setAdapter(adapter);

        trigger = findViewById(R.id.activadorSummoner);

        trigger.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(api.getInvocador() != null) {
                    //Ha tenido exito, creando intent y nueva actividad
                }

            }
        });
    }

    //Metodo para cuando seleccionamos un elemento del selector
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String server = getResources().getStringArray(R.array.servers)[ (int) id ];
        Log.d("Spinner", "Se ha seleccionado: " + id + " " + server);
        server_id = (int) id;
    }

    //Otro metodo que tiene que estar por implementar la interfaz
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    //Metodo que busca al invocador y si lo encuentra lanza un intent con la nueva actividad
    //Si no pues un toast de error?
    public void BuscaInvocador(View view) {
        final String api_key = PreferenceManager.getDefaultSharedPreferences(this).getString("key", "");
        String nombre = nombreInvocadorInput.getEditText().getText().toString();
        Log.d("Server id", String.valueOf(server_id));
        String server = getResources().getStringArray(R.array.urlServers)[server_id];
        api.getIdFromSummoner(nombre, server, api_key, trigger);
    }

    //Metodo que abre la nueva actividad con los ajustes
    public void AbrirAjustes(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        //Podemos pasar informacion entre actividades con el intent
        intent.putExtra("parametro", 2);
        startActivity(intent);
    }
}