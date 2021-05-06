package com.dam.lol.activities.main;


import android.view.View;
import android.widget.AdapterView;

import com.dam.lol.R;

public class ServersSpinnerAdapter implements AdapterView.OnItemSelectedListener {
    private final MainActivity activity;

    public ServersSpinnerAdapter(MainActivity activity) {
        this.activity = activity;

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        activity.setServer_url(activity.getResources().getStringArray(R.array.urlServers)[(int) id]);

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
