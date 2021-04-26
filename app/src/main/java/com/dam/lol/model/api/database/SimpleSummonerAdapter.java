package com.dam.lol.model.api.database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.dam.lol.R;

import java.util.List;
// copiao del internete
public class SimpleSummonerAdapter extends ArrayAdapter<SimpleSummoner> {

    private int resourceLayout;
    private Context mContext;

    public SimpleSummonerAdapter(Context context, int resource, List<SimpleSummoner> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
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

            if (tt1 != null) {
                tt1.setText(p.getName());
            }

            if (tt2 != null) {
                tt2.setText(p.getServer());
            }

        }

        return v;
    }

}