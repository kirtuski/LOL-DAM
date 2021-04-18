package com.dam.lol.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dam.lol.LolApplication;
import com.dam.lol.R;
import com.dam.lol.facade.ApiFacade;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

//TODO Lee esto antes de borrarme el implement y que luego tenga que buscar porque no se ejecuta
//Si se quita ese implement no se puede ver cuando se cambia el selector, o se hace en otra clase o se queda aqui
//Si se hace en otra clase los metodos que hay que llevarse son onItemSelected y onNothingSelected
//Si se quita no funciona la app
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    //Elementos del layout
    Spinner servidorSpinner;
    TextInputLayout nombreInvocadorInput;

    //Guardar el servidor elejido
    private String server_url = "";
    private ApiFacade apiFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiFacade = LolApplication.getInstance().getApiFacade();

        servidorSpinner = findViewById(R.id.servidorSpinner);
        servidorSpinner.setOnItemSelectedListener(this);

        nombreInvocadorInput = findViewById(R.id.NombreInvocadorLayout);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.servers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servidorSpinner.setAdapter(adapter);
    }

    protected void onResume() {
        super.onResume();
        apiFacade = LolApplication.getInstance().getApiFacade();
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

        //TODO podemos usar esto para pasar el objeto invocador
        //Podemos pasar informacion entre actividades con el intent
        //En ajustes se ve como obtener la info
        intent.putExtra("parametro", 2);

        startActivityForResult( intent, 1);
    }

    //TODO Leeme: Lo he cambiado para que funcione de la misma forma que busca invocador, si tiene exito el metodo, cambia de actividad, borra despues de leer si estas de acuerdo
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