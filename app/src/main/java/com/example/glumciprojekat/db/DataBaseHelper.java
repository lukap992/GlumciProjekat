package com.example.glumciprojekat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.glumciprojekat.db.model.Filmovi;
import com.example.glumciprojekat.db.model.Glumci;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    public static final String DATABASE_NAME = "ormlite";
    private static final int DATABASE_VERSION = 1;

    private Dao<Glumci, Integer> getGlumci = null;
    private Dao<Filmovi, Integer> getFilmovi = null;

    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME,  null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Glumci.class);
            TableUtils.createTable(connectionSource, Filmovi.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        try {
            TableUtils.dropTable(connectionSource, Glumci.class, true);
            TableUtils.dropTable(connectionSource, Filmovi.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public Dao<Glumci, Integer> getGlumciDao() throws SQLException{
        if(getGlumci == null){
            getGlumci = getDao(Glumci.class);
        }
        return getGlumci;
    }
    public Dao<Filmovi, Integer> getFilmoviDao() throws SQLException{
        if(getFilmovi == null){
            getFilmovi = getDao(Filmovi.class);
        }
        return getFilmovi;
    }
}
