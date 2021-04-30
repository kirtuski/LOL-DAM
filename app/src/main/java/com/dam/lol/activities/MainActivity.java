package com.dam.lol.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dam.lol.LolApplication;
import com.dam.lol.R;
import com.dam.lol.facade.ApiFacade;
import com.dam.lol.facade.DatabaseFacade;
import com.dam.lol.model.api.database.SimpleSummoner;
import com.dam.lol.model.api.database.SimpleSummonerAdapter;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;

//TODO hay que revisar bien como tratamos las exepciones

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    //Elementos del layout
    Spinner servidorSpinner;
    TextInputLayout nombreInvocadorInput;

    //Guardar el servidor elejido
    private String server_url = "";
    private ApiFacade apiFacade;
    private DatabaseFacade databaseFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeInput();
        initializeFacades();
        initializeSpinner();
        initializeFavoriteSummonerList();
    }

    protected void onResume() {
        super.onResume();
        initializeFavoriteSummonerList();
        initializeFacades();
    }

    private void initializeInput() {
        nombreInvocadorInput = findViewById(R.id.NombreInvocadorLayout);
    }

    private void initializeFacades() {
        apiFacade = LolApplication.getInstance().getApiFacade();
        this.databaseFacade = LolApplication.getInstance().getDatabaseFacade();
    }

    private void initializeSpinner() {
        servidorSpinner = findViewById(R.id.servidorSpinner);
        servidorSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.servers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servidorSpinner.setAdapter(adapter);
    }

    private void initializeFavoriteSummonerList() {
        List<SimpleSummoner> simpleSummoners = databaseFacade.findFavoriteSummoners();
        ListView favoriteChampionsList = findViewById(R.id.favoriteChampionsList);
        SimpleSummonerAdapter simpleSummonerAdapter = new SimpleSummonerAdapter(this, R.layout.favorite_summoner_layout, simpleSummoners, this);
        favoriteChampionsList.setAdapter(simpleSummonerAdapter);
    }

    //Metodo para cuando seleccionamos un elemento del selector
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        server_url = getResources().getStringArray(R.array.urlServers)[(int) id];
        Log.d("Main Selector Servidor", "Se ha seleccionado: " + server_url);

    }

    //Otro metodo que tiene que estar por implementar la interfaz
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    //Busca invocador y si lo encuentra lanza un intent con la nueva actividad
    public void BuscaInvocador(View view) {
        String nombre = nombreInvocadorInput.getEditText().getText().toString();
        if(nombre.length() == 0)
            Toast.makeText(this, "Introduce un nombre de invocador", Toast.LENGTH_SHORT).show();
        else
            apiFacade.getIdFromSummoner(nombre, server_url, this);
    }

    //Metodo que abre la nueva actividad con los ajustes
    public void AbrirAjustes(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult( intent, 1);
    }

    public void openChampionRotation(View view) {
        apiFacade.getChampionsRotation(server_url, this);
    }

    //Para recargar la api facade cuando regresemos de la actividad
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == 1)
            apiFacade = LolApplication.getInstance().getApiFacade();
    }
}