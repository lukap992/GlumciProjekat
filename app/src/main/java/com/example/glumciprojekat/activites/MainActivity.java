package com.example.glumciprojekat.activites;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.glumciprojekat.R;
import com.example.glumciprojekat.adapter.DrawerAdapter;
import com.example.glumciprojekat.db.DataBaseHelper;
import com.example.glumciprojekat.db.model.Glumci;
import com.example.glumciprojekat.model.NavigationItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper;

    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private RelativeLayout drawerPane;
    private ArrayList<NavigationItem> drawerItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            prikaziSveGlumce();
        } catch (SQLException e) {
        }
        navigationDrawer();

    }

    private void prikaziSveGlumce() throws SQLException {
        final ListView listView = findViewById(R.id.lista_glumaca);
        final List<Glumci> listaGlumaca = getDataBaseHelper().getGlumciDao().queryForAll();
        ArrayAdapter<Glumci> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, listaGlumaca);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Glumci glumci = (Glumci)listView.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("position", glumci.getId()); // zahvaljujem... nista tu sam :)
                startActivity(intent);
            }
        });

    }
    private void refresh() throws  SQLException{
        ListView listView = findViewById(R.id.lista_glumaca);
        if(listView != null){
            ArrayAdapter<Glumci> arrayAdapter = (ArrayAdapter<Glumci>) listView.getAdapter();
            if(arrayAdapter != null){
                arrayAdapter.clear();
                List<Glumci> listaGlumaca = getDataBaseHelper().getGlumciDao().queryForAll();
                arrayAdapter.addAll(listaGlumaca);
                arrayAdapter.notifyDataSetChanged();
            }
        }
    }
    private void addGlumac(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_dodaj_glumca);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final EditText nazivG = dialog.findViewById(R.id.dialog_dodaj_ime_glumca);
        final EditText prezimeG = dialog.findViewById(R.id.dialog_dodaj_prezime_glumca);
        final EditText biografijaG = dialog.findViewById(R.id.dialog_dodaj_biografija_glumca);
        final EditText ocenaG = dialog.findViewById(R.id.dialog_dodaj_ocenu_glumca);
        final EditText datumRodjenaG =dialog.findViewById(R.id.dialog_dodaj_datum_rodjena);
        Button btnYES = dialog.findViewById(R.id.dialog_dodaj_btnYES);
        btnYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (datumRodjenaG.getText().toString().isEmpty() || !isValidDate(datumRodjenaG.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Date can't be EMPTY - Date Format: dd-MM-yyyy", Toast.LENGTH_LONG).show();
                    return;
                }

                // ovde bi radio ostale provere tipa ....

                if (nazivG.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Naziv ne sme biti prazan", Toast.LENGTH_LONG).show();
                    return; // return obavezno , itd tako za dalje aham znaci treba za sve?
                }
                if(prezimeG.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Prezime ne sme biti prazno", Toast.LENGTH_LONG).show();
                    return;
                }
                if(biografijaG.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Biografija ne sme biti prazna", Toast.LENGTH_LONG).show();
                    return;
                }
                if(ocenaG.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Ocena ne sme biti prazna", Toast.LENGTH_LONG).show();
                    return;
                }

                String nazivGlumca = nazivG.getText().toString();
                String prezimeGlumca = prezimeG.getText().toString();
                String biografijaGlumca = biografijaG.getText().toString();
                float ocenaGlumca = Float.parseFloat(ocenaG.getText().toString());
                String datum = datumRodjenaG.getText().toString(); // toEto


                Glumci glumci = new Glumci();
                glumci.setIme(nazivGlumca);
                glumci.setPrezime(prezimeGlumca);
                glumci.setBiografija(biografijaGlumca);
                glumci.setOcena(ocenaGlumca);
                glumci.setDatumRodjena(datum);





                try {
                    getDataBaseHelper().getGlumciDao().create(glumci);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    boolean showMassage = sharedPreferences.getBoolean("toast_settings", true);
                    if(showMassage){
                        Toast.makeText(MainActivity.this, "Glumac uspesno dodat", Toast.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                    refresh();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public static boolean isValidDate(String inDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        try{
            dateFormat.parse(inDate.trim());
        }catch (ParseException pe){
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dodaj_glumca, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_addGlumac:
                addGlumac();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void navigationDrawer(){
        Toolbar toolbar =findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_format_list);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.show();
        }
        drawerItems.add(new NavigationItem("Prikaz glumaca", "Prikazuje sve glumce", R.drawable.ic_group_black_24dp));
        drawerItems.add(new NavigationItem("Podesavanja", "Podesavanja aplikacije", R.drawable.ic_settings_black_24dp));

        DrawerAdapter drawerAdapter = new DrawerAdapter(this, drawerItems);
        drawerListView = findViewById(R.id.nav_list);
        drawerListView.setAdapter(drawerAdapter);
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        drawerTitle = getTitle();
        drawerLayout = findViewById(R.id.main_drawer_layout);
        drawerPane = findViewById(R.id.main_dawer_pane);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(drawerTitle);
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setTitle(drawerTitle);
                super.onDrawerClosed(drawerView);
            }
        };

    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position == 0){
                getIntent();
            }else if(position == 1){
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
            drawerLayout.closeDrawer(drawerPane);
        }

    }

    public DataBaseHelper getDataBaseHelper(){
        if(dataBaseHelper == null){
            dataBaseHelper = OpenHelperManager.getHelper(this, DataBaseHelper.class);
        }
        return dataBaseHelper;
    }

    @Override
    protected void onDestroy() {
        if(dataBaseHelper != null){
            OpenHelperManager.releaseHelper();
            dataBaseHelper = null;
        }
        super.onDestroy();
    }


}
