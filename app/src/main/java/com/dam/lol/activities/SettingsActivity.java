package com.dam.lol.activities;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.dam.lol.LolApplication;
import com.dam.lol.R;


//TODO acabar esta clase con lo que corresponda
//creo que el layout se genera con las propiedades que hay en xml/root_preferences.xml
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

        //TODO borrar
        Log.d("Ajustes", String.valueOf(getIntent().getIntExtra("prueba", 1)));
    }

    //A lo mejor podemos usar la clase ClipboardManager para pegar automaticamente del portapapeles
    public void irRiot(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.riotgames.com/"));
        startActivity(intent);
    }

    public void copyClipboard(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard.hasPrimaryClip()) {
            String contenidoPortapapeles = (String) clipboard.getPrimaryClip().getItemAt(0).getText();
            Log.d("Clipboard", contenidoPortapapeles);

            if (contenidoPortapapeles.startsWith("RGAPI") && contenidoPortapapeles.length() == 42) {
                Toast.makeText(this, "Contenido copiado", Toast.LENGTH_SHORT).show();
                SharedPreferences preferenceManager = PreferenceManager.getDefaultSharedPreferences(this);
                preferenceManager.edit().putString("key", contenidoPortapapeles).apply();
                LolApplication.getInstance().reloadApiKey();
                //Refresca la actividad para mostrar el nuevo contenido
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            } else
                Toast.makeText(this, "No se pudo copiar", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "No se pudo copiar", Toast.LENGTH_SHORT).show();
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