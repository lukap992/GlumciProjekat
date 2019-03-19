package com.example.glumciprojekat.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Filmovi.DATABASE_TABLE_NAME)
public class Filmovi {

    public static final String DATABASE_TABLE_NAME = "filmovi";
    public static final String FIELD_ID = "id";
    public static final String FIELD_NAZIV_FILMA = "naziv";
    public static final String FIELD_ZANR = "zanr";
    public static final String FIELD_godinaIZLAZSKA = "godinaIzlaska";
    public static final String FIELD_GLUMCI = "glumci";

    @DatabaseField(columnName = FIELD_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAZIV_FILMA)
    private String naziv;

    @DatabaseField(columnName = FIELD_ZANR)
    private String zanr;

    @DatabaseField(columnName = FIELD_godinaIZLAZSKA)
    private String godinaIzlazska;

    @DatabaseField( columnName = FIELD_GLUMCI, foreign = true, foreignAutoRefresh = true)
    private Glumci glumci;

    public Filmovi() {
        //empty
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getZanr() {
        return zanr;
    }

    public void setZanr(String zanr) {
        this.zanr = zanr;
    }

    public String getGodinaIzlazska() {
        return godinaIzlazska;
    }

    public void setGodinaIzlazska(String godinaIzlazska) {
        this.godinaIzlazska = godinaIzlazska;
    }

    public Glumci getGlumci() {
        return glumci;
    }

    public void setGlumci(Glumci glumci) {
        this.glumci = glumci;
    }
    public String toString(){
        return naziv;
    }
}
