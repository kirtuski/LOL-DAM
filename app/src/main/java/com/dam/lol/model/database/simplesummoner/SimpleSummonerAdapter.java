package com.dam.lol.model.database.simplesummoner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    List<SimpleSummoner> simpleSummoners;
    private View v;
    private SparseBooleanArray mSelectedItemsIds;

    public SimpleSummonerAdapter(Context context, int resource, List<SimpleSummoner> items, Activity activity) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
        this.activity = activity;
        mSelectedItemsIds = new SparseBooleanArray();
        this.simpleSummoners = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.v = convertView;

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
                tt2.setText(convertirServidor(p.getServer()));
            }

        }

        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(R.attr.colorPrimarySurface, typedValue, true);
        v.setBackgroundColor(mSelectedItemsIds.get(position) ? typedValue.data : Color.TRANSPARENT);
        return v;
    }

    public String convertirServidor(String servidor) {
        for (int i = 0; i < activity.getResources().getStringArray(R.array.urlServers).length; i++) {
            if (servidor.equals(activity.getResources().getStringArray(R.array.urlServers)[i])) {
                return activity.getResources().getStringArray(R.array.servers)[i];
            }
        }
        return null;
    }

    @Override
    public void remove(SimpleSummoner object) {
        // super.remove(object);
        simpleSummoners.remove(object);
        if (LolApplication.getInstance().getDatabaseFacade().deleteSummoner(object.getName(), object.getServer()) != 0) {
            Snackbar.make(v, "Invocador eliminado satisfactoriamente", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            simpleSummoners.remove(object);
            notifyDataSetChanged();
        } else {
            Snackbar.make(v, "Error eliminando el invocador", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public List<SimpleSummoner> getSimpleSummoners() {
        return simpleSummoners;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}