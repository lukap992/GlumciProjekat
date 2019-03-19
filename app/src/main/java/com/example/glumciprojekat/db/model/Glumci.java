package com.example.glumciprojekat.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

@DatabaseTable(tableName = Glumci.DATABASE_TABLE_NAME)
public class Glumci {

    public static final String DATABASE_TABLE_NAME = "glumci";
    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "ime";
    public static final String FIELD_PREZIME = "prezime";
    public static final String FIELD_BIOGRAFIJA = "biografija";
    public static final String FIELD_OCENA = "ocena";
    public static final String FIELD_DATUM = "datumRodjena";
    public static final String FIELD_FILMOVi = "filmovi";

    @DatabaseField(columnName = FIELD_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAME)
    private String ime;

    @DatabaseField(columnName = FIELD_PREZIME)
    private String prezime;

    @DatabaseField(columnName = FIELD_BIOGRAFIJA)
    private String biografija;

    @DatabaseField(columnName = FIELD_OCENA)
    private float ocena;

    @DatabaseField(columnName = FIELD_DATUM)
    private String datumRodjena;

    @ForeignCollectionField(columnName = FIELD_FILMOVi, eager = true)
    private ForeignCollection<Filmovi> filmovi;

    public Glumci() {
        //empty
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getBiografija() {
        return biografija;
    }

    public void setBiografija(String biografija) {
        this.biografija = biografija;
    }

    public float getOcena() {
        return ocena;
    }

    public void setOcena(float ocena) {
        this.ocena = ocena;
    }

    public String getDatumRodjena() {
        return datumRodjena;
    }

    public void setDatumRodjena(String datumRodjena) {
        this.datumRodjena = datumRodjena;
    }

    public ForeignCollection<Filmovi> getFilmovi() {
        return filmovi;
    }

    public void setFilmovi(ForeignCollection<Filmovi> filmovi) {
        this.filmovi = filmovi;
    }

    public String toString(){
        return ime;
    }
}
