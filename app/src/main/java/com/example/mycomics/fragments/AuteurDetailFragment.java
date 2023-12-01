package com.example.mycomics.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.mycomics.R;
import com.example.mycomics.adapters.AuteursListAdapter;
import com.example.mycomics.adapters.EditeursListAdapter;
import com.example.mycomics.adapters.SeriesListAdapter;
import com.example.mycomics.adapters.SeriesNbListAdapter;
import com.example.mycomics.adapters.TomesListAdapter;
import com.example.mycomics.adapters.TomesSerieListAdapter;
import com.example.mycomics.beans.AuteurBean;
import com.example.mycomics.beans.EditeurBean;
import com.example.mycomics.beans.SerieBean;
import com.example.mycomics.beans.TomeBean;
import com.example.mycomics.databinding.FragmentAuteurDetailBinding;
import com.example.mycomics.databinding.FragmentAuteursBinding;
import com.example.mycomics.helpers.DataBaseHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AuteurDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuteurDetailFragment extends Fragment {
    FragmentAuteurDetailBinding binding;

    /* -------------------------------------- */
    // Variable BDD
    /* -------------------------------------- */
    DataBaseHelper dataBaseHelper;
    ArrayAdapter tomesArrayAdapter;
    ArrayAdapter seriesArrayAdapter;
    ArrayAdapter auteursArrayAdapter;
    ArrayAdapter editeursArrayAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AuteurDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AuteurDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AuteurDetailFragment newInstance(String param1, String param2) {
        AuteurDetailFragment fragment = new AuteurDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        /* -------------------------------------- */
        // Initialisation Base de données
        /* -------------------------------------- */
        dataBaseHelper = new DataBaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAuteurDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /* -------------------------------------- */
        // Récupération données
        /* -------------------------------------- */
        Integer auteur_id = getArguments().getInt("auteur_id");
        String auteur_pseudo = getArguments().getString("auteur_pseudo");
        String auteur_prenom = getArguments().getString("auteur_prenom");
        String auteur_nom = getArguments().getString("auteur_nom");
        /* -------------------------------------- */
        // Initialisation affichage
        /* -------------------------------------- */
        AuteurBean auteur = dataBaseHelper.selectAuteurSelonAuteurId(auteur_id);
        afficherDetailAuteur(auteur);

        /* -------------------------------------- */
        // Initialisation Nom fiche
        /* -------------------------------------- */
        binding.tvDetailAuteurPseudo.setText(auteur_pseudo);


        /* -------------------------------------- */
        // Clic Element liste Serie
        /* -------------------------------------- */
        binding.lvDetailAuteurListeSeries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SerieBean serieBean;
                try {
                    serieBean = (SerieBean) binding.lvDetailAuteurListeSeries.getItemAtPosition(position);
                } catch (Exception e) {
                    serieBean = new SerieBean(-1,"error");
                }
                Bundle bundle = new Bundle();
                bundle.putInt("serie_id", serieBean.getSerie_id());
                bundle.putString("serie_nom", serieBean.getSerie_nom());

                findNavController(AuteurDetailFragment.this).navigate(R.id.action_auteurDetail_to_serieDetail, bundle);

            }
        });

        /* -------------------------------------- */
        // Clic Element liste Tomes
        /* -------------------------------------- */
        binding.lvDetailAuteurListeTomes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TomeBean tome;
                TomeBean tomeBean;
                try {
                    tome = (TomeBean) binding.lvDetailAuteurListeTomes.getItemAtPosition(position);
                    tomeBean = dataBaseHelper.selectTomeSelonTomeId(tome.getTome_id());
                } catch (Exception e) {
                    tome = new TomeBean(-1,"error");
                    tomeBean = new TomeBean(-1,"error");
                }

                Bundle bundle = new Bundle();
                bundle.putInt("tome_id", tomeBean.getTome_id());
                bundle.putInt("tome_numero", tomeBean.getTome_id());
                bundle.putString("tome_titre", tomeBean.getTome_titre());
                bundle.putDouble("tome_prix_editeur", tomeBean.getTome_prix_editeur());
                bundle.putDouble("tome_valeur_connue", tomeBean.getTome_valeur_connue());
                bundle.putString("tome_date_edition", tomeBean.getTome_date_edition());
                bundle.putString("tome_isbn", tomeBean.getTome_isbn());
                bundle.putString("tome_image", tomeBean.getTome_image());
                bundle.putBoolean("tome_dedicace", tomeBean.isTome_dedicace());
                bundle.putBoolean("tome_edition_speciale", tomeBean.isTome_edition_speciale());
                bundle.putString("tome_edition_speciale_libelle", tomeBean.getTome_edition_speciale_libelle());
                bundle.putInt("serie_id", tomeBean.getSerie_id());
                bundle.putInt("editeur_id", tomeBean.getEditeur_id());

                findNavController(AuteurDetailFragment.this).navigate(R.id.action_auteurDetail_to_tomeDetail, bundle);

            }
        });

        /* -------------------------------------- */
        // Clic Element liste Editeur
        /* -------------------------------------- */
        binding.lvDetailAuteurListeEditeurs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditeurBean editeurBean;
                try {
                    editeurBean = (EditeurBean) binding.lvDetailAuteurListeEditeurs.getItemAtPosition(position);
                } catch (Exception e) {
                    editeurBean = new EditeurBean(-1,"error");
                }
                Bundle bundle = new Bundle();
                bundle.putInt("editeur_id", editeurBean.getEditeur_id());
                bundle.putString("editeur_nom", editeurBean.getEditeur_nom());
                findNavController(AuteurDetailFragment.this).navigate(R.id.action_auteurDetail_to_editeurDetail, bundle);

            }
        });

        /* -------------------------------------- */
        // Clic Element liste Auteur
        /* -------------------------------------- */
        binding.lvDetailAuteurListeAuteurs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AuteurBean auteurBean;
                try {
                    auteurBean = (AuteurBean) binding.lvDetailAuteurListeAuteurs.getItemAtPosition(position);
                } catch (Exception e) {
                    auteurBean = new AuteurBean(-1,"error","error","error");
                }
                Bundle bundle = new Bundle();
                bundle.putInt("auteur_id", auteurBean.getAuteur_id());
                bundle.putString("auteur_pseudo", auteurBean.getAuteur_pseudo());
                bundle.putString("auteur_nom", auteurBean.getAuteur_nom());
                bundle.putString("auteur_prenom", auteurBean.getAuteur_prenom());

                findNavController(AuteurDetailFragment.this).navigate(R.id.auteurDetailFragment, bundle);

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void afficherDetailAuteur(AuteurBean auteur){
        binding.tvDetailAuteurPseudo.setText(auteur.getAuteur_pseudo());

        seriesArrayAdapter = new SeriesNbListAdapter(getActivity(), R.layout.listview_row_2col_reverse, dataBaseHelper.listeSeriesSelonAuteurId(auteur.getAuteur_id()));
        binding.lvDetailAuteurListeSeries.setAdapter(seriesArrayAdapter);

        tomesArrayAdapter = new TomesListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.listeTomesSelonAuteurIdSansSerie(auteur.getAuteur_id()));
        binding.lvDetailAuteurListeTomes.setAdapter(tomesArrayAdapter);

        editeursArrayAdapter = new EditeursListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.listeEditeursSelonAuteurId(auteur.getAuteur_id()));
        binding.lvDetailAuteurListeEditeurs.setAdapter(editeursArrayAdapter);

        auteursArrayAdapter = new AuteursListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.listeAuteursPartenaires(auteur.getAuteur_id()));
        binding.lvDetailAuteurListeAuteurs.setAdapter(auteursArrayAdapter);
    }
}

