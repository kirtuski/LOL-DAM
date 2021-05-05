package com.dam.lol.activities.mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
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
import com.dam.lol.activities.SettingsActivity;
import com.dam.lol.facade.ApiFacade;
import com.dam.lol.facade.DatabaseFacade;
import com.dam.lol.model.database.simplesummoner.SimpleSummoner;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    SimpleSummonerAdapter simpleSummonerAdapter;
    private TextInputLayout nombreInvocadorInput;
    private ActionMode mActionMode;
    private String server_url;
    private ApiFacade apiFacade;
    private DatabaseFacade databaseFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsActivity.ThemeSetup.applyTheme(this);
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

    private void initializeFavoriteSummonerList() {
        List<SimpleSummoner> simpleSummoners = databaseFacade.findFavoriteSummoners();
        ListView favoriteChampionsList = findViewById(R.id.favoriteChampionsList);
        simpleSummonerAdapter = new SimpleSummonerAdapter(this, R.layout.favorite_summoner_layout, simpleSummoners, this);
        favoriteChampionsList.setAdapter(simpleSummonerAdapter);
        favoriteChampionsList.setOnItemClickListener((AdapterView.OnItemClickListener) (parent, view, position, id) -> {
            SimpleSummoner simpleSummoner = (SimpleSummoner) simpleSummonerAdapter.getItem(position);
            if (simpleSummonerAdapter.getSelectedIds().size() == 0) {
                LolApplication.getInstance().getApiFacade().getIdFromSummoner(simpleSummoner.getName(), simpleSummoner.getServer(), MainActivity.this);
            } else {
                onListItemSelect(position);
            }
        });

        favoriteChampionsList.setOnItemLongClickListener((parent, view, position, id) -> {
            onListItemSelect(position);
            return true;
        });
    }

    private void onListItemSelect(int position) {
        simpleSummonerAdapter.toggleSelection(position);
        boolean hasCheckedItems = simpleSummonerAdapter.getSelectedCount() > 0;

        if (hasCheckedItems && mActionMode == null)
            // there are some selected items, start the actionMode
            mActionMode = startActionMode(new SimpleSummonerCallback());
        else if (!hasCheckedItems && mActionMode != null)
            // there no selected items, finish the actionMode
            mActionMode.finish();

        if (mActionMode != null)
            mActionMode.setTitle(simpleSummonerAdapter
                    .getSelectedCount() + " selected");
    }

    public void setServer_url(String server_url) {
        this.server_url = server_url;
    }

    private void initializeSpinner() {
        //Elementos del layout
        Spinner servidorSpinner = findViewById(R.id.servidorSpinner);
        servidorSpinner.setOnItemSelectedListener(new ServersSpinnerAdapter(this));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.servers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servidorSpinner.setAdapter(adapter);
    }

    //Busca invocador y si lo encuentra lanza un intent con la nueva actividad
    public void BuscaInvocador(View view) {
        String nombre = Objects.requireNonNull(nombreInvocadorInput.getEditText()).getText().toString();
        if (nombre.length() == 0)
            Toast.makeText(this, "Introduce un nombre de invocador", Toast.LENGTH_SHORT).show();
        else
            apiFacade.getIdFromSummoner(nombre, server_url, this);
    }

    //Metodo que abre la nueva actividad con los ajustes
    public void AbrirAjustes(MenuItem menuItem) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, 1);
    }

    public void openChampionRotation(View view) {
        apiFacade.getChampionsRotation(server_url, this);
    }

    //Para recargar la api facade cuando regresemos de la actividad
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            apiFacade = LolApplication.getInstance().getApiFacade();
    }

    public class SimpleSummonerCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // inflate contextual menu
            mode.getMenuInflater().inflate(R.menu.contextual_action_bar, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            if (item.getItemId() == R.id.menu_delete) {
                SparseBooleanArray selected = simpleSummonerAdapter
                        .getSelectedIds();
                for (int i = (selected.size() - 1); i >= 0; i--) {
                    if (selected.valueAt(i)) {
                        SimpleSummoner selectedItem = simpleSummonerAdapter
                                .getItem(selected.keyAt(i));
                        simpleSummonerAdapter.remove(selectedItem);
                    }
                }
                mode.finish();
                return true;
            }
            return false;

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // remove selection
            simpleSummonerAdapter.removeSelection();
            mActionMode = null;
        }
    }
}