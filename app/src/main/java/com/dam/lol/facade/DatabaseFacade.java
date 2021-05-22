package com.dam.lol.facade;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.dam.lol.R;
import com.dam.lol.model.database.simplesummoner.SimpleSummoner;

import java.util.ArrayList;
import java.util.List;

public class DatabaseFacade extends SQLiteOpenHelper {
    private final Context context;

    public DatabaseFacade(Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(context.getString(R.string.sql_create_favorites));
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(context.getString(R.string.sql_drop_favorites));
        database.execSQL(context.getString(R.string.sql_create_favorites));
    }

    public long insertSummoner(String summonerName, String server) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues insertSummonerValues = new ContentValues();
        insertSummonerValues.put("SUMMONER_NAME", summonerName);
        insertSummonerValues.put("SERVER", server);
        return database.insert("FAVORITE_SUMMONERS", null, insertSummonerValues);
    }

    public int deleteSummoner(String summonerName, String server) {
        SQLiteDatabase database = getWritableDatabase();
        return database.delete("FAVORITE_SUMMONERS", "SUMMONER_NAME = ? AND SERVER = ?",
                new String[]{summonerName, server});
    }

    public boolean checkSummonerExists(String summonerName, String server) {
        SQLiteDatabase database = getReadableDatabase();
        String[] columns = {"SUMMONER_NAME", "SERVER"};
        String[] selectionArgs = {summonerName, server};
        Cursor findFavoriteSummonersCursor = database.query("FAVORITE_SUMMONERS", columns,
                "SUMMONER_NAME = ? AND SERVER = ?",
                selectionArgs, null, null, "SUMMONER_NAME", "1");
        boolean check = findFavoriteSummonersCursor.moveToFirst();
        findFavoriteSummonersCursor.close();
        return check;
    }

    public List<SimpleSummoner> findFavoriteSummoners() {
        SQLiteDatabase database = getReadableDatabase();
        String[] valuesToGet = {"SUMMONER_NAME", "SERVER"};
        Cursor findFavoriteSummonersCursor = database.query("FAVORITE_SUMMONERS", valuesToGet, null,
                null, null, null, "SUMMONER_NAME", null);
        List<SimpleSummoner> simpleSummoners = new ArrayList<>();

        if (findFavoriteSummonersCursor.moveToFirst()) {
            do {
                SimpleSummoner simpleSummoner = new SimpleSummoner(findFavoriteSummonersCursor.getString(0), findFavoriteSummonersCursor.getString(1));
                simpleSummoners.add(simpleSummoner);
            } while (findFavoriteSummonersCursor.moveToNext());
        }
        findFavoriteSummonersCursor.close();
        return simpleSummoners;
    }
}
