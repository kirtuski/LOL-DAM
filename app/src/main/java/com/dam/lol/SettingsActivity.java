package com.dam.lol;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;


//TODO acabar esta clase con lo que corresponda
//creo que el layout se genera con las propiedades que hay en xml/root_preferences.xml
//TODO hay que ver como coger info de esta clase, como la api key y lo que se nos ocurra
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Recuperamos el mensaje que pasamos antes
        Log.d("Ajustes", String.valueOf(getIntent().getIntExtra("prueba",1)));
    }

    //TODO metodo que lanza un intent abriendo el navegador y llendo a la pagina donde obtener la key
    //A lo mejor podemos usar la clase ClipboardManager para pegar automaticamente del portapapeles
    public void irRiot(View view) {
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}