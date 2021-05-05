package com.dam.lol.activities;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.dam.lol.LolApplication;
import com.dam.lol.R;


//TODO añadir idioma? Hacer temas?
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
    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.app_settings, rootKey);
            ListPreference themePreference = getPreferenceManager().findPreference(getString(R.string.settings_theme_key));
            if (themePreference.getValue() == null) {
                themePreference.setValue(ThemeSetup.Mode.DEFAULT.name());
            }
            themePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                ThemeSetup.applyTheme(ThemeSetup.Mode.valueOf((String) newValue));
                return true;
            });
        }
    }

    //TODO Refactor https://github.com/danielme-com/android-light-dark/blob/main/app/src/main/java/com/danielme/android/dark/settings/ThemeSetup.java
    public static class ThemeSetup {
        private ThemeSetup() {
        }
        public enum Mode {
            DEFAULT, DARK, LIGHT
        }
        public static void applyTheme(Mode mode) {
            switch (mode) {
                case DARK:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                case LIGHT:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                default:
                    if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    }
            }
        }
        public static void applyTheme(Context context) {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String value = defaultSharedPreferences.getString(context.getString(R.string.settings_theme_key), Mode.DEFAULT.name());
            applyTheme(Mode.valueOf(value));
        }

    }

}