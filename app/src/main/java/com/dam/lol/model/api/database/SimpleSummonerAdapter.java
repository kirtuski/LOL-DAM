package com.dam.lol.model.api.database;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dam.lol.LolApplication;
import com.dam.lol.R;
import com.dam.lol.activities.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
// copiao del internete
public class SimpleSummonerAdapter extends ArrayAdapter<SimpleSummoner> {

    private int resourceLayout;
    private Context mContext;
    private Activity activity;

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

        SimpleSummoner p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.name);
            TextView tt2 = (TextView) v.findViewById(R.id.server);
            ImageButton imageButton = (ImageButton) v.findViewById(R.id.delete);
            if (tt1 != null) {
                tt1.setText(p.getName());
            }

            if (tt2 != null) {
                tt2.setText(p.getServer());
            }

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(LolApplication.getInstance().getDatabaseFacade().deleteSummoner(p.getName(), p.getServer()) != 0) {
                        Snackbar.make(v, "Invocador eliminado satisfactoriamente", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        SimpleSummonerAdapter.this.remove(p);
                    }
                    else
                        Snackbar.make(v, "Error eliminando el invocador", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                }
            });

            LinearLayout rowBody = (LinearLayout) v.findViewById(R.id.rowBody) ;
            rowBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SimpleSummoner simpleSummoner = (SimpleSummoner) p;
                    LolApplication.getInstance().getApiFacade().getIdFromSummoner(simpleSummoner.getName(), simpleSummoner.getServer(), activity);
                }
            });
        }

        return v;
    }

}