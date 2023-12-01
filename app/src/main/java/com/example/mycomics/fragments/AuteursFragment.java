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
import com.example.mycomics.adapters.AuteursNbListAdapter;
import com.example.mycomics.beans.AuteurBean;
import com.example.mycomics.databinding.FragmentAuteursBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupTextDialog;

public class AuteursFragment extends Fragment {
    FragmentAuteursBinding binding;

    /* -------------------------------------- */
    // Variable BDD
    /* -------------------------------------- */
    DataBaseHelper dataBaseHelper;
    ArrayAdapter auteursArrayAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AuteursFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AuteursFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AuteursFragment newInstance(String param1, String param2) {
        AuteursFragment fragment = new AuteursFragment();
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
        binding = FragmentAuteursBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /* -------------------------------------- */
        // Initialisation affichage
        /* -------------------------------------- */
        afficherListeAuteurs();
        binding.sbSearch.svSearch.setQueryHint("Filtrer ou rechercher");
        /* -------------------------------------- */
        // Clic Bouton Chercher
        /* -------------------------------------- */
        binding.sbSearch.btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("filtre", binding.sbSearch.svSearch.getQuery().toString());
                findNavController(AuteursFragment.this).navigate(R.id.searchResultFragment, bundle);
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
                afficherListeAuteurs();
                return false;
            }
        });
        /* -------------------------------------- */
        // Clic Bouton ajout auteur
        /* -------------------------------------- */
        binding.btnAuteursAddAuteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Création Popup
                PopupTextDialog popupTextDialog = new PopupTextDialog(getActivity());
                popupTextDialog.setTitre("Entrez le Pseudonyme (ou le Nom) de l'auteur");
                popupTextDialog.setHint("Pseudonyme de l'auteur");
                popupTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupTextDialog.getBtnPopupValider().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AuteurBean auteurBean;
                        try {
                            auteurBean = new AuteurBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                        } catch (Exception e) {
//                            Toast.makeText(ReglagesActivity.this, "Erreur création auteur", Toast.LENGTH_SHORT).show();
                            auteurBean = new AuteurBean(-1, "error" );
                        }
                        if(dataBaseHelper.verifDoublonAuteur(auteurBean.getAuteur_pseudo())){
                            Toast.makeText(getActivity(), "Auteur déjà existant, enregistrement annulé", Toast.LENGTH_LONG).show();
                            popupTextDialog.dismiss(); // Fermeture Popup
                        } else {
                            //Appel DataBaseHelper
                            boolean success = dataBaseHelper.insertIntoAuteurs(auteurBean);
                            Toast.makeText(getActivity(), "Auteur créé", Toast.LENGTH_SHORT).show();
                            popupTextDialog.dismiss(); // Fermeture Popup
                        }
                        afficherListeAuteurs();
                    }
                });

                popupTextDialog.getEtPopupText().setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            // Perform action on key press
                            AuteurBean auteurBean;
                            try {
                                auteurBean = new AuteurBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                            } catch (Exception e) {
//                            Toast.makeText(ReglagesActivity.this, "Erreur création auteur", Toast.LENGTH_SHORT).show();
                                auteurBean = new AuteurBean(-1, "error" );
                            }
                            if(dataBaseHelper.verifDoublonAuteur(auteurBean.getAuteur_pseudo())){
                                Toast.makeText(getActivity(), "Auteur déjà existant, enregistrement annulé", Toast.LENGTH_LONG).show();
                                popupTextDialog.dismiss(); // Fermeture Popup
                            } else {
                                //Appel DataBaseHelper
                                boolean success = dataBaseHelper.insertIntoAuteurs(auteurBean);
                                Toast.makeText(getActivity(), "Auteur créé", Toast.LENGTH_SHORT).show();
                                popupTextDialog.dismiss(); // Fermeture Popup
                            }
                            afficherListeAuteurs();
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
                afficherListeAuteurs();
            }
        });

        /* -------------------------------------- */
        // Clic Element liste Auteur
        /* -------------------------------------- */
        binding.lvAuteursListeAuteurs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AuteurBean auteurBean;
                try {
                    auteurBean = (AuteurBean) binding.lvAuteursListeAuteurs.getItemAtPosition(position);
                } catch (Exception e) {
                    auteurBean = new AuteurBean(-1,"error","error","error");
                }
                Bundle bundle = new Bundle();
                bundle.putInt("auteur_id", auteurBean.getAuteur_id());
                bundle.putString("auteur_pseudo", auteurBean.getAuteur_pseudo());
                bundle.putString("auteur_nom", auteurBean.getAuteur_nom());
                bundle.putString("auteur_prenom", auteurBean.getAuteur_prenom());
                findNavController(AuteursFragment.this).navigate(R.id.action_auteurs_to_auteurDetail, bundle);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
    private void afficherListeAuteurs(){
        if (binding.sbSearch.svSearch.getQuery().toString().length() > 0) {
            auteursArrayAdapter = new AuteursNbListAdapter(getActivity() , R.layout.listview_row_2col_reverse, dataBaseHelper.listeAuteursFiltre(binding.sbSearch.svSearch.getQuery().toString()));
        } else {
            auteursArrayAdapter = new AuteursNbListAdapter(getActivity() , R.layout.listview_row_2col_reverse, dataBaseHelper.listeAuteurs());
        }
        binding.lvAuteursListeAuteurs.setAdapter(auteursArrayAdapter);
    }
}