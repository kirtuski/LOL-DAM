package com.dam.lol.activities.main;

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
import com.dam.lol.model.database.simplesummoner.SimpleSummoner;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class SimpleSummonerAdapter extends ArrayAdapter<SimpleSummoner> {

    private final int resourceLayout;
    private final Context mContext;
    private final Activity activity;
    final List<SimpleSummoner> simpleSummoners;
    private View view;
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
        this.view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(resourceLayout, null);
        }

        SimpleSummoner simpleSummoner = getItem(position);

        if (simpleSummoner != null) {
            TextView nameTextView = view.findViewById(R.id.name);
            TextView serverTextView = view.findViewById(R.id.server);

            if (nameTextView != null) {
                nameTextView.setText(simpleSummoner.getName());
            }

            if (serverTextView != null) {
                serverTextView.setText(getServerNameByServerUrl(simpleSummoner.getServer()));
            }

        }

        TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(R.attr.colorPrimarySurface, typedValue, true);
        view.setBackgroundColor(mSelectedItemsIds.get(position) ? typedValue.data : Color.TRANSPARENT);
        return view;
    }

    public String getServerNameByServerUrl(String serverUrl) {
        for (int i = 0; i < activity.getResources().getStringArray(R.array.urlServers).length; i++) {
            if (serverUrl.equals(activity.getResources().getStringArray(R.array.urlServers)[i])) {
                return activity.getResources().getStringArray(R.array.servers)[i];
            }
        }
        return null;
    }

    @Override
    public void remove(SimpleSummoner object) {
        simpleSummoners.remove(object);
        if (LolApplication.getInstance().getDatabaseFacade().deleteSummoner(object.getName(), object.getServer()) != 0) {
            Snackbar.make(view, getContext().getString(R.string.deleting_summoner_success), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            simpleSummoners.remove(object);
            notifyDataSetChanged();
        } else {
            Snackbar.make(view, getContext().getString(R.string.deleting_summoner_error), Snackbar.LENGTH_LONG)
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