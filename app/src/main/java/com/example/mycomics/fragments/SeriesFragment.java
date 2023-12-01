package com.example.mycomics.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.mycomics.R;
import com.example.mycomics.adapters.SeriesNbListAdapter;
import com.example.mycomics.beans.SerieBean;
import com.example.mycomics.databinding.FragmentSeriesBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupTextDialog;

public class SeriesFragment extends Fragment {
    FragmentSeriesBinding binding;

    /* -------------------------------------- */
    // Variable BDD
    /* -------------------------------------- */
    DataBaseHelper dataBaseHelper;
    ArrayAdapter seriesArrayAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SeriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeriesFragment newInstance(String param1, String param2) {
        SeriesFragment fragment = new SeriesFragment();
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
        binding = FragmentSeriesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /* -------------------------------------- */
        // Initialisation affichage
        /* -------------------------------------- */
        afficherListeSeries();
        binding.sbSearch.svSearch.setQueryHint("Filtrer ou rechercher");
        /* -------------------------------------- */
        // Clic Bouton Chercher
        /* -------------------------------------- */
        binding.sbSearch.btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("filtre", binding.sbSearch.svSearch.getQuery().toString());
                findNavController(SeriesFragment.this).navigate(R.id.searchResultFragment, bundle);
            }
        });
        /* -------------------------------------- */
        // saisie searchBar
        /* -------------------------------------- */
        binding.sbSearch.svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                afficherListeSeries();
                return false;
            }
        });

        /* -------------------------------------- */
        // Clic Bouton ajout série
        /* -------------------------------------- */
        binding.btnSeriesAddSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Création Popup
                PopupTextDialog popupTextDialog = new PopupTextDialog(getActivity());
                popupTextDialog.setTitre("Entrez le nom de la série");
                popupTextDialog.setHint("Nom de la série");
                popupTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupTextDialog.getBtnPopupValider().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SerieBean serieBean;
                        try {
                            serieBean = new SerieBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                        } catch (Exception e) {
                            serieBean = new SerieBean(-1, "error" );
                        }
                        if(dataBaseHelper.verifDoublonSerie(serieBean.getSerie_nom())){
                            Toast.makeText(getActivity(), "Série déjà existante, enregistrement annulé", Toast.LENGTH_LONG).show();
                            popupTextDialog.dismiss(); // Fermeture Popup
                        } else {
                            //Appel DataBaseHelper
                            boolean success = dataBaseHelper.insertIntoSeries(serieBean);
                            Toast.makeText(getActivity(), "Série créée", Toast.LENGTH_SHORT).show();
                            popupTextDialog.dismiss(); // Fermeture Popup
                        }
                        afficherListeSeries();
                    }
                });

                popupTextDialog.getEtPopupText().setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            // Perform action on key press
                            SerieBean serieBean;
                            try {
                                serieBean = new SerieBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                            } catch (Exception e) {
                                serieBean = new SerieBean(-1, "error" );
                            }
                            if(dataBaseHelper.verifDoublonSerie(serieBean.getSerie_nom())){
                                Toast.makeText(getActivity(), "Série déjà existante, enregistrement annulé", Toast.LENGTH_LONG).show();
                                popupTextDialog.dismiss(); // Fermeture Popup
                            } else {
                                //Appel DataBaseHelper
                                boolean success = dataBaseHelper.insertIntoSeries(serieBean);
                                Toast.makeText(getActivity(), "Série créée", Toast.LENGTH_SHORT).show();
                                popupTextDialog.dismiss(); // Fermeture Popup
                            }
                            afficherListeSeries();
                            return true;
                        }
                        return false;
                    }
                });

                popupTextDialog.getBtnPopupAnnuler().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupTextDialog.dismiss(); // Fermeture Popup
                    }
                });

                popupTextDialog.build();
                afficherListeSeries();
            }
        });

        /* -------------------------------------- */
        // Clic Element liste Serie
        /* -------------------------------------- */
        binding.lvSeriesListeSeries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SerieBean serieBean;
                try {
                    serieBean = (SerieBean) binding.lvSeriesListeSeries.getItemAtPosition(position);
                } catch (Exception e) {
                    serieBean = new SerieBean(-1,"error");
                }
                Bundle bundle = new Bundle();
                bundle.putInt("serie_id", serieBean.getSerie_id());
                bundle.putString("serie_nom", serieBean.getSerie_nom());

                findNavController(SeriesFragment.this).navigate(R.id.action_series_to_serieDetail, bundle);

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void afficherListeSeries(){
        if (binding.sbSearch.svSearch.getQuery().toString().length() > 0) {
            seriesArrayAdapter = new SeriesNbListAdapter(getActivity() , R.layout.listview_row_2col_reverse, dataBaseHelper.listeSeriesFiltre(binding.sbSearch.svSearch.getQuery().toString()));
        } else {
            seriesArrayAdapter = new SeriesNbListAdapter(getActivity() , R.layout.listview_row_2col_reverse, dataBaseHelper.listeSeries());
        }
        binding.lvSeriesListeSeries.setAdapter(seriesArrayAdapter);
    }
}