package com.example.glumciprojekat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.glumciprojekat.R;
import com.example.glumciprojekat.db.model.Filmovi;

import java.util.List;

public class ListMovieAdapter extends BaseAdapter {

    private Context context;
    private List<Filmovi> filmoviList;

    public ListMovieAdapter(Context context, List<Filmovi> filmoviList) {
        this.context = context;
        this.filmoviList = filmoviList;
    }

    @Override
    public int getCount() {
        return filmoviList.size();
    }

    @Override
    public Object getItem(int position) {
        return filmoviList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.list_movie_adapter, null);

        TextView textNaziv = convertView.findViewById(R.id.list_movie_adapter_nazivFilma);
        textNaziv.setText("Naziv Filma: " + filmoviList.get(position).getNaziv());

        TextView textZanr = convertView.findViewById(R.id.list_movie_adapter_zanrFilma);
        textZanr.setText("Zanr Filma: " + filmoviList.get(position).getZanr());

        TextView textGodina = convertView.findViewById(R.id.list_movie_adapter_godinaIzlaska);
        textGodina.setText("Godina Izlaska Filma: " + filmoviList.get(position).getGodinaIzlazska()); //sad je full

        // ovde smo u sustini napravili nov layout kako zelimo da ti izgleda ono u listi, i ovde samo instancirali
        // sad cemo u detail activity da menjamo onaj ArrayAdapter i stavicemo ovaj da bi povezali ovaj adapter umesto arraya.
        // e da jer onaj array adapter ima one fukncije vec u sebi za ciscenje i dodavanje moramo ovde napraviti tu metodu
        // e sad s ovim adapterom i sa layoutom se mozes igrati do sutra sa ulepsavanjem i tako tim stvarima,
        // sad cu ti poslati na mess da vidis sta sam uradio
        // tipa da ovo sto pise "Naziv Filma: " je belo a ime glumca je npr onako boldovano crno
        // ali onda treba praviti u layoutu dupli text view
        // jer ne znam da li si skontao to da naprimer i ako u layoutu napises "Naziv Filma: " nece se prikazati jer se overriduje sa nazivom
        // pa trebalo bi dva puta i onda jedan napravis obican a drugi boldovan i tako
        // ali zahteva koncentraciju hahaha vazii posalji sliku ma mess davidim kako izgleda e zavvalujem ti dosta si pomogao ovde :D
         // nista nista kalu :) znaci ce :D
        // idem , pa ti cimaj ako treba nesto vazi vazii

        return convertView;
    }

    public void refreshAdapter(List<Filmovi> filmovi) {
        this.filmoviList.clear();
        this.filmoviList.addAll(filmovi);
        notifyDataSetChanged();

    }

}
