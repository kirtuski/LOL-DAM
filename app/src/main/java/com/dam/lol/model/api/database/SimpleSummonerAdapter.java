package com.dam.lol.model.api.database;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dam.lol.LolApplication;
import com.dam.lol.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

// copiao del internete
public class SimpleSummonerAdapter extends ArrayAdapter<SimpleSummoner> {

    private final int resourceLayout;
    private final Context mContext;
    private final Activity activity;

    public SimpleSummonerAdapter(Context context, int resource, List<SimpleSummoner> items, Activity activity) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        SimpleSummoner simpleSummoner = getItem(position);

        if (simpleSummoner != null) {
            TextView tt1 = v.findViewById(R.id.name);
            TextView tt2 = v.findViewById(R.id.server);
            ImageButton imageButton = v.findViewById(R.id.delete);
            if (tt1 != null) {
                tt1.setText(simpleSummoner.getName());
            }

            if (tt2 != null) {
                tt2.setText(simpleSummoner.getServer());
            }

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LolApplication.getInstance().getDatabaseFacade().deleteSummoner(simpleSummoner.getName(), simpleSummoner.getServer()) != 0) {
                        Snackbar.make(v, "Invocador eliminado satisfactoriamente", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        SimpleSummonerAdapter.this.remove(simpleSummoner);
                    } else
                        Snackbar.make(v, "Error eliminando el invocador", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                }
            });

            LinearLayout rowBody = v.findViewById(R.id.rowBody);
            rowBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LolApplication.getInstance().getApiFacade().getIdFromSummoner(simpleSummoner.getName(), simpleSummoner.getServer(), activity);
                }
            });
        }

        return v;
    }

}