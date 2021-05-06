package com.dam.lol.activities.mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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
    private TextInputLayout summonerNameInput;
    private ActionMode mActionMode;
    private String server_url;
    private ApiFacade apiFacade;
    private DatabaseFacade databaseFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        SettingsActivity.ThemeSetup.applyTheme(this);

        initializeInput();
        initializeFacades();
        initializeSpinner();
        initializeFavoriteSummonerList();
    }


    private void initializeInput() {
        summonerNameInput = findViewById(R.id.name_input_layout);
    }

    private void initializeFacades() {
        this.apiFacade = LolApplication.getInstance().getApiFacade();
        this.databaseFacade = LolApplication.getInstance().getDatabaseFacade();
    }

    private void initializeFavoriteSummonerList() {
        List<SimpleSummoner> simpleSummoners = databaseFacade.findFavoriteSummoners();
        ListView favoriteChampionsList = findViewById(R.id.favorite_champions_list);
        simpleSummonerAdapter = new SimpleSummonerAdapter(this, R.layout.favorites_container, simpleSummoners, this);
        favoriteChampionsList.setAdapter(simpleSummonerAdapter);
        favoriteChampionsList.setOnItemClickListener((parent, view, position, id) -> {
            SimpleSummoner simpleSummoner = simpleSummonerAdapter.getItem(position);
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
            mActionMode.setTitle(getResources().getQuantityString(R.plurals.summoners_selected, simpleSummonerAdapter.getSelectedCount(), simpleSummonerAdapter.getSelectedCount()));
    }

    public void setServer_url(String server_url) {
        this.server_url = server_url;
    }

    private void initializeSpinner() {
        //Elementos del layout
        Spinner servidorSpinner = findViewById(R.id.servers_spinner);
        servidorSpinner.setOnItemSelectedListener(new ServersSpinnerAdapter(this));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.servers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servidorSpinner.setAdapter(adapter);
    }

    //Search summoner and launch activity if successfully
    public void searchSummoner(View view) {
        String name = Objects.requireNonNull(summonerNameInput.getEditText()).getText().toString();
        if (name.length() == 0)
            Toast.makeText(this, getString(R.string.no_input), Toast.LENGTH_SHORT).show();
        else
            apiFacade.getIdFromSummoner(name, server_url, this);
    }

    //Metodo que abre la nueva actividad con los ajustes
    public void AbrirAjustes(MenuItem menuItem) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, 1);
    }

    public void openChampionRotation(View view) {
        apiFacade.getChampionsRotation(server_url, this);
    }

    //TODO refactor a su propio archivo?
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