package com.dam.lol.facade;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.dam.lol.model.database.simplesummoner.SimpleSummoner;

import java.util.ArrayList;
import java.util.List;

public class DatabaseFacade extends SQLiteOpenHelper {
    //PARA CORONARSE LAS QUERYS DEBERIAN ESTAR EN UN XML Y RECUPERARLAS DE ALLI CUANDO SE VAYAN A USAR
    private static final String SQL_DROP_FAVORITE_SUMMONERS = "drop table if exists FAVORITE_SUMMONERS";
    private static final String SQL_CREATE_FAVORITE_SUMMONERS = "create table FAVORITE_SUMMONERS(" +
            "TABLE_KEY INTEGER primary key autoincrement," +
            "SUMMONER_NAME text," +
            "SERVER text)";

    public DatabaseFacade(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_FAVORITE_SUMMONERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(SQL_DROP_FAVORITE_SUMMONERS);
        database.execSQL(SQL_CREATE_FAVORITE_SUMMONERS);
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
        return database.delete("FAVORITE_SUMMONERS", "SUMMONER_NAME = ? AND SERVER = ?", new String[]{summonerName, server});
    }

    public boolean checkSummonerExists(String summonerName, String server) {
        SQLiteDatabase database = getReadableDatabase();
        String[] columns = {"SUMMONER_NAME", "SERVER"};
        String[] selectionArgs = {summonerName, server};
        Cursor findFavoriteSummonersCursor = database.query("FAVORITE_SUMMONERS", columns, "SUMMONER_NAME = ? AND SERVER = ?",
                selectionArgs, null, null, "SUMMONER_NAME", "1");
        boolean check = findFavoriteSummonersCursor.moveToFirst();
        findFavoriteSummonersCursor.close();
        return check;
    }

    public List<SimpleSummoner> findFavoriteSummoners() {
        SQLiteDatabase database = getReadableDatabase();
        String[] valores_recuperar = {"SUMMONER_NAME", "SERVER"};
        Cursor findFavoriteSummonersCursor = database.query("FAVORITE_SUMMONERS", valores_recuperar, null,
                null, null, null, "SUMMONER_NAME", null);
        List<SimpleSummoner> simpleSummoners = new ArrayList<>();

        if (findFavoriteSummonersCursor.moveToFirst()) {
            do {
                SimpleSummoner simpleSummoner = new SimpleSummoner(findFavoriteSummonersCursor.getString(0), findFavoriteSummonersCursor.getString(1));
                simpleSummoners.add(simpleSummoner);
            } while (findFavoriteSummonersCursor.moveToNext());
        }

        //db.close();
        findFavoriteSummonersCursor.close();
        return simpleSummoners;
    }
}
