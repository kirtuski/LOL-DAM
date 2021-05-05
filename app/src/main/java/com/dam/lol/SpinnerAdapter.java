package com.dam.lol;


import android.view.View;
import android.widget.AdapterView;

import com.dam.lol.activities.MainActivity;

//TODO refactor a donde corresponda, a lo mejor paquete con el otro adapter?
public class SpinnerAdapter implements AdapterView.OnItemSelectedListener {
    private final MainActivity activity;

    public SpinnerAdapter(MainActivity activity) {
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
