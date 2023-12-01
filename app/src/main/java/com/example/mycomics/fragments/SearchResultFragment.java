package com.example.mycomics.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import com.example.mycomics.adapters.TomesNumeroListAdapter;
import com.example.mycomics.beans.AuteurBean;
import com.example.mycomics.beans.EditeurBean;
import com.example.mycomics.beans.SerieBean;
import com.example.mycomics.beans.TomeBean;
import com.example.mycomics.beans.TomeSerieBean;
import com.example.mycomics.databinding.FragmentSearchResultBinding;
import com.example.mycomics.databinding.FragmentSerieDetailBinding;
import com.example.mycomics.helpers.DataBaseHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultFragment extends Fragment {
    FragmentSearchResultBinding binding;

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

    public SearchResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResultFragment newInstance(String param1, String param2) {
        SearchResultFragment fragment = new SearchResultFragment();
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
        binding = FragmentSearchResultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /* -------------------------------------- */
        // Récupération données
        /* -------------------------------------- */
        String filtre = getArguments().getString("filtre");
        /* -------------------------------------- */
        // Initialisation affichage
        /* -------------------------------------- */
        afficherResultatRecherche(filtre);
        /* -------------------------------------- */
        // Bouton chercher inutile cat filtre auto
        /* -------------------------------------- */
        binding.sbSearch.btSearch.setVisibility(View.GONE);
        /* -------------------------------------- */
        // saisie searchBar
        /* -------------------------------------- */
        binding.sbSearch.svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String filtre) {
                afficherResultatRecherche(filtre);
                return false;
            }
        });

        /* -------------------------------------- */
        // Clic Element liste Serie
        /* -------------------------------------- */
        binding.lvRechercheListeSeries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SerieBean serieBean;
                try {
                    serieBean = (SerieBean) binding.lvRechercheListeSeries.getItemAtPosition(position);
                } catch (Exception e) {
                    serieBean = new SerieBean(-1,"error");
                }
                Bundle bundle = new Bundle();
                bundle.putInt("serie_id", serieBean.getSerie_id());
                bundle.putString("serie_nom", serieBean.getSerie_nom());

                findNavController(SearchResultFragment.this).navigate(R.id.action_searchResult_to_serieDetail, bundle);

            }
        });

        /* -------------------------------------- */
        // Clic Element liste Tomes
        /* -------------------------------------- */
        binding.lvRechercheListeTomes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TomeSerieBean tomeSerieBean;
                try {
                    tomeSerieBean = (TomeSerieBean) binding.lvRechercheListeTomes.getItemAtPosition(position);
                } catch (Exception e) {
                    tomeSerieBean = new TomeSerieBean(-1,"error");
                }
                Bundle bundle = new Bundle();
                bundle.putInt("tome_id", tomeSerieBean.getTome_id());
                bundle.putInt("tome_numero", tomeSerieBean.getTome_id());
                bundle.putString("tome_titre", tomeSerieBean.getTome_titre());
                bundle.putDouble("tome_prix_editeur", tomeSerieBean.getTome_prix_editeur());
                bundle.putDouble("tome_valeur_connue", tomeSerieBean.getTome_valeur_connue());
                bundle.putString("tome_date_edition", tomeSerieBean.getTome_date_edition());
                bundle.putString("tome_isbn", tomeSerieBean.getTome_isbn());
                bundle.putString("tome_image", tomeSerieBean.getTome_image());
                bundle.putBoolean("tome_dedicace", tomeSerieBean.isTome_dedicace());
                bundle.putBoolean("tome_edition_speciale", tomeSerieBean.isTome_edition_speciale());
                bundle.putString("tome_edition_speciale_libelle", tomeSerieBean.getTome_edition_speciale_libelle());
                bundle.putInt("serie_id", tomeSerieBean.getSerie_id());
                bundle.putString("serie_nom", tomeSerieBean.getSerie_nom());
                bundle.putInt("editeur_id", tomeSerieBean.getEditeur_id());

                findNavController(SearchResultFragment.this).navigate(R.id.action_searchResult_to_tomeDetail, bundle);

            }
        });

        /* -------------------------------------- */
        // Clic Element liste Auteur
        /* -------------------------------------- */
        binding.lvRechercheListeAuteurs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AuteurBean auteurBean;
                try {
                    auteurBean = (AuteurBean) binding.lvRechercheListeAuteurs.getItemAtPosition(position);
                } catch (Exception e) {
                    auteurBean = new AuteurBean(-1,"error","error","error");
                }
                Bundle bundle = new Bundle();
                bundle.putInt("auteur_id", auteurBean.getAuteur_id());
                bundle.putString("auteur_pseudo", auteurBean.getAuteur_pseudo());
                bundle.putString("auteur_nom", auteurBean.getAuteur_nom());
                bundle.putString("auteur_prenom", auteurBean.getAuteur_prenom());
                findNavController(SearchResultFragment.this).navigate(R.id.action_searchResult_to_auteurDetail, bundle);
            }
        });

        /* -------------------------------------- */
        // Clic Element liste Editeur
        /* -------------------------------------- */
        binding.lvRechercheListeEditeurs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditeurBean editeurBean;
                try {
                    editeurBean = (EditeurBean) binding.lvRechercheListeEditeurs.getItemAtPosition(position);
                } catch (Exception e) {
                    editeurBean = new EditeurBean(-1,"error");
                }
                Bundle bundle = new Bundle();
                bundle.putInt("editeur_id", editeurBean.getEditeur_id());
                bundle.putString("editeur_nom", editeurBean.getEditeur_nom());
                findNavController(SearchResultFragment.this).navigate(R.id.action_searchResult_to_editeurDetail, bundle);
            }
        });

    }

    private void afficherResultatRecherche(String filtre){
        seriesArrayAdapter = new SeriesNbListAdapter(getActivity(), R.layout.listview_row_2col_reverse, dataBaseHelper.listeSeriesFiltre(filtre));
        binding.lvRechercheListeSeries.setAdapter(seriesArrayAdapter);

        tomesArrayAdapter = new TomesNumeroListAdapter(getActivity(), R.layout.listview_row_2col, dataBaseHelper.listeTomesFiltre(filtre));
        binding.lvRechercheListeTomes.setAdapter(tomesArrayAdapter);

        auteursArrayAdapter = new AuteursListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.listeAuteursFiltre(filtre));
        binding.lvRechercheListeAuteurs.setAdapter(auteursArrayAdapter);

        editeursArrayAdapter = new EditeursListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.listeEditeursFiltre(filtre));
        binding.lvRechercheListeEditeurs.setAdapter(editeursArrayAdapter);
    }
}