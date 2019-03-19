package com.example.glumciprojekat.activites;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.glumciprojekat.R;
import com.example.glumciprojekat.adapter.ListMovieAdapter;
import com.example.glumciprojekat.db.DataBaseHelper;
import com.example.glumciprojekat.db.model.Filmovi;
import com.example.glumciprojekat.db.model.Glumci;
import com.example.glumciprojekat.model.NavigationItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.ForeignCollection;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper;
    private Filmovi filmovi;

    private Glumci glumci = null;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private RelativeLayout drawerPane;
    private ArrayList<NavigationItem> drawerItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        try {
            detaljiGlumaca();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        navigationDrawer();
    }

    @SuppressLint("SetTextI18n")
    private void detaljiGlumaca() throws SQLException {
        Intent intent = getIntent();
        int id = intent.getExtras().getInt("position");

        glumci = getDatabaseHelper().getGlumciDao().queryForId(id);

        TextView nazivGlumca = findViewById(R.id.detail_naziv_glumca);
        nazivGlumca.setText("Naziv glumca: " + glumci.getIme());

        TextView prezimeGlumca = findViewById(R.id.detail_prezime_glumca);
        prezimeGlumca.setText("Prezime Glumca: " + glumci.getPrezime());

        TextView biografijaGlumca = findViewById(R.id.detail_biografija);
        biografijaGlumca.setText("Biografija glumca: " + glumci.getBiografija());

        TextView ocenaGlumca = findViewById(R.id.detail_ocena_glumca);
        ocenaGlumca.setText("Ocena glumca: " + String.valueOf(glumci.getOcena()));

        TextView datumRodjena = findViewById(R.id.detail_datum_rodjena);
        datumRodjena.setText("Datum Rodjena: " + String.valueOf(glumci.getDatumRodjena()));

        ListView list_filmovi = findViewById(R.id.detail_lista_filmova);
        ForeignCollection<Filmovi> filmoviForeignCollection = getDatabaseHelper().getGlumciDao().queryForId(id).getFilmovi();
        List<Filmovi> listaFilmova = new ArrayList<>(filmoviForeignCollection);
        ListMovieAdapter adapter = new ListMovieAdapter(this, listaFilmova);
        list_filmovi.setAdapter(adapter);

    }


    private void delete() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_obrisi_glumca);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Button btnYes = dialog.findViewById(R.id.obrisi_glumca_btnYes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = getIntent();
                    int id = intent.getExtras().getInt("position");

                    Glumci glumci = getDatabaseHelper().getGlumciDao().queryForId(id);
                    getDatabaseHelper().getGlumciDao().delete(glumci);

                    /** Provera da li je u settings ukljuceno za Prikazivanje Toast poruka. */
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DetailActivity.this);
                    boolean showMessage = sharedPreferences.getBoolean("toast_settings", true);
                    if (showMessage) {
                        Toast.makeText(DetailActivity.this, "Uspesno IZBRISANO", Toast.LENGTH_LONG).show();
                    }

                    /** Refreshuje applikaciju -- vodi nazad u MainApp s obrisanim objektom. */
                    Intent intent1 = new Intent(DetailActivity.this, MainActivity.class);
                    startActivity(intent1);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        Button btnNo = dialog.findViewById(R.id.obrisi_glumca_btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void addFilm() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_dodaj_film);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        final EditText editNazivFilma = dialog.findViewById(R.id.dialog_film_naziv_filma);
        final EditText editZanrFilma = dialog.findViewById(R.id.dialog_film_zanr_filma);
        final EditText editGodinaFilma = dialog.findViewById(R.id.dialog_film_godina_izlazska);


        Button btnOk = dialog.findViewById(R.id.dialog_film_btnYes);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nazivFilma = editNazivFilma.getText().toString();
                String zanrFilma = editZanrFilma.getText().toString();
                String godinaFilma = editGodinaFilma.getText().toString();

                if (editGodinaFilma.getText().toString().isEmpty() || !isValidDate(editGodinaFilma.getText().toString())) {
                    Toast.makeText(DetailActivity.this, "Date can't be EMPTY - Date Format: dd-MM-yyyy", Toast.LENGTH_LONG).show();
                    return;
                }

                // ono sto ovde vidim da fali je da nisi postavio kom glumcu dodajes film

                Intent intent = getIntent();
                int id = intent.getExtras().getInt("position");
                try {
                    glumci = getDatabaseHelper().getGlumciDao().queryForId(id);
                    filmovi = new Filmovi(); //
                    filmovi.setNaziv(nazivFilma);
                    filmovi.setZanr(zanrFilma);
                    filmovi.setGodinaIzlazska(godinaFilma);
                    filmovi.setGlumci(glumci);
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                try {
                    getDatabaseHelper().getFilmoviDao().create(filmovi);
                    refreshMovies(); //
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DetailActivity.this);
                    boolean showMassage = sharedPreferences.getBoolean("toast_settings", true);
                    if (showMassage) {
                        Toast.makeText(DetailActivity.this, "Film uspesno dodat", Toast.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
        Button btnNo = dialog.findViewById(R.id.dialog_film_btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void update() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_update);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        final EditText editNaziv = dialog.findViewById(R.id.dialog_update_ime_glumca);
        final EditText editPrezime = dialog.findViewById(R.id.dialog_update_prezime_glumca);
        final EditText editBiografija = dialog.findViewById(R.id.dialog_update_biografija_glumca);
        final EditText editTextOcena = dialog.findViewById(R.id.dialog_update_ocenu_glumca);
        final EditText editDatumRodjena = dialog.findViewById(R.id.dialog_update_datum_rodjena);

        Button btnYes = dialog.findViewById(R.id.dialog_update_btnYES);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editDatumRodjena.getText().toString().isEmpty() || !isValidDate(editDatumRodjena.getText().toString())) {
                    Toast.makeText(DetailActivity.this, "Date can't be EMPTY - Date Format: dd-MM-yyyy", Toast.LENGTH_LONG).show();
                    return;
                }

                glumci.setIme(editNaziv.getText().toString());
                glumci.setPrezime(editPrezime.getText().toString());
                glumci.setBiografija(editBiografija.getText().toString());
                glumci.setOcena(Float.parseFloat(editTextOcena.getText().toString()));
                glumci.setDatumRodjena(editDatumRodjena.getText().toString());

                try {
                    getDatabaseHelper().getGlumciDao().update(glumci);

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DetailActivity.this);
                    boolean showMassage = sharedPreferences.getBoolean("toast_settings", true);
                    if (showMassage) {
                        Toast.makeText(DetailActivity.this, "Izmena uspesno zavrsena", Toast.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void    refreshMovies() {
        ListView list_detail = findViewById(R.id.detail_lista_filmova);
        if (list_detail != null) {
            ListMovieAdapter filmoviArrayAdapter = (ListMovieAdapter) list_detail.getAdapter();
            if (filmoviArrayAdapter != null) {
                Intent intent = getIntent();
                int id = intent.getExtras().getInt("position");
                try {
                    ForeignCollection<Filmovi> filmoviForeignCollection = getDatabaseHelper().getGlumciDao().queryForId(id).getFilmovi();
                    List<Filmovi> listFilmovi = new ArrayList<>(filmoviForeignCollection);
                    filmoviArrayAdapter.refreshAdapter(listFilmovi);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            /*
            U sustini kad je obicna lista onda se ne koristi ovaj ForeignCollection. a ako je ovakva lista koja je vezava tim foreign ljucem
            Mora prvo da se kaze ForeignCollection i da trazis listu iz baze
            ono na sta treba da obratis paznju jeste to da trazis listu u kom si glumcu
            dole vidis da ima List<Filmovi> pa onda je nova lista sa foreignom
            u adapteru se prikazuje sve to, prvo ocistis ceo adapter, pa dodas u njega listu filmova ovu poslednju, i notify je da obavesti
            adapter za promenu da osvezi
            full vazii
            E sad fora je sto su ti zanr i godina izlaska zabetonirani ako ne napravis custom adapter.ako hoces mogu na brzinu da ti pokazem.
            moze
            da ne brisem ovo da ti ostavim? da da ostavi
            probaj, mislim da je to to eo samo udje u gta
            sve sam znao bez podsecanja osim ove poslednje metode ali dobro nisam dva dana nista radio smorilo me iskreno
            dobar si nema sta kalu ce tvojim stop
            hahahahah :D e znas sta smo zab samo
            jel prikazuje? da samo sto nema ono kao tipa  Naiv Filma i to gle na mess sliku
             */

        }
    }

    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public DataBaseHelper getDatabaseHelper() {
        if (dataBaseHelper == null) {
            dataBaseHelper = OpenHelperManager.getHelper(this, DataBaseHelper.class);
        }
        return dataBaseHelper;
    }

    public void navigationDrawer() {
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.izmena_filma, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.izmena_filma_action_update:
                update();
                break;
            case R.id.izmena_filma_action_delete:
                delete();
                break;
            case R.id.izmena_filma_action_add_film:
                addFilm();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
