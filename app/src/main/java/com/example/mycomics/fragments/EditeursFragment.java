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
import com.example.mycomics.adapters.EditeursNbListAdapter;
import com.example.mycomics.beans.EditeurBean;
import com.example.mycomics.databinding.FragmentEditeursBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupTextDialog;

public class EditeursFragment extends Fragment {
    FragmentEditeursBinding binding;

    /* -------------------------------------- */
    // Variable BDD
    /* -------------------------------------- */
    DataBaseHelper dataBaseHelper;
    ArrayAdapter editeursArrayAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditeursFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditeursFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditeursFragment newInstance(String param1, String param2) {
        EditeursFragment fragment = new EditeursFragment();
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
        binding = FragmentEditeursBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /* -------------------------------------- */
        // Initialisation affichage
        /* -------------------------------------- */
        afficherListeEditeurs();
        binding.sbSearch.svSearch.setQueryHint("Filtrer ou rechercher");
        /* -------------------------------------- */
        // Clic Bouton Chercher
        /* -------------------------------------- */
        binding.sbSearch.btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("filtre", binding.sbSearch.svSearch.getQuery().toString());
                findNavController(EditeursFragment.this).navigate(R.id.searchResultFragment, bundle);
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
                afficherListeEditeurs();
                return false;
            }
        });
        /* -------------------------------------- */
        // Clic Bouton ajout éditeur
        /* -------------------------------------- */
        binding.btnEditeursAddEditeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Création Popup
                PopupTextDialog popupTextDialog = new PopupTextDialog(getActivity());
                popupTextDialog.setTitre("Entrez le nom de l'éditeur");
                popupTextDialog.setHint("Nom de l'éditeur");
                popupTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupTextDialog.getBtnPopupValider().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditeurBean editeurBean;
                        try {
                            editeurBean = new EditeurBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                        } catch (Exception e) {
                            editeurBean = new EditeurBean(-1, "error" );
                        }
                        if(dataBaseHelper.verifDoublonEditeur(editeurBean.getEditeur_nom())){
                            Toast.makeText(getActivity(), "Editeur déjà existant, enregistrement annulé", Toast.LENGTH_LONG).show();
                            popupTextDialog.dismiss(); // Fermeture Popup
                        } else {
                            //Appel DataBaseHelper
                            boolean success = dataBaseHelper.insertIntoEditeurs(editeurBean);
                            Toast.makeText(getActivity(), "Editeur créé", Toast.LENGTH_SHORT).show();
                            popupTextDialog.dismiss(); // Fermeture Popup
                        }
                        afficherListeEditeurs();
                    }
                });

                popupTextDialog.getEtPopupText().setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            // Perform action on key press
                            EditeurBean editeurBean;
                            try {
                                editeurBean = new EditeurBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                            } catch (Exception e) {
                                editeurBean = new EditeurBean(-1, "error" );
                            }
                            if(dataBaseHelper.verifDoublonEditeur(editeurBean.getEditeur_nom())){
                                Toast.makeText(getActivity(), "Editeur déjà existant, enregistrement annulé", Toast.LENGTH_LONG).show();
                                popupTextDialog.dismiss(); // Fermeture Popup
                            } else {
                                //Appel DataBaseHelper
                                boolean success = dataBaseHelper.insertIntoEditeurs(editeurBean);
                                Toast.makeText(getActivity(), "Editeur créé", Toast.LENGTH_SHORT).show();
                                popupTextDialog.dismiss(); // Fermeture Popup
                            }
                            afficherListeEditeurs();
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
                afficherListeEditeurs();
            }
        });

        /* -------------------------------------- */
        // Clic Element liste Editeur
        /* -------------------------------------- */
        binding.lvEditeursListeEditeurs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditeurBean editeurBean;
                try {
                    editeurBean = (EditeurBean) binding.lvEditeursListeEditeurs.getItemAtPosition(position);
                } catch (Exception e) {
                    editeurBean = new EditeurBean(-1,"error");
                }
                Bundle bundle = new Bundle();
                bundle.putInt("editeur_id", editeurBean.getEditeur_id());
                bundle.putString("editeur_nom", editeurBean.getEditeur_nom());
                findNavController(EditeursFragment.this).navigate(R.id.action_editeurs_to_editeurDetail, bundle);

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
    private void afficherListeEditeurs(){
        if (binding.sbSearch.svSearch.getQuery().toString().length() > 0) {
            editeursArrayAdapter = new EditeursNbListAdapter(getActivity() , R.layout.listview_row_2col_reverse, dataBaseHelper.listeEditeursFiltre(binding.sbSearch.svSearch.getQuery().toString()));
        } else {
            editeursArrayAdapter = new EditeursNbListAdapter(getActivity() , R.layout.listview_row_2col_reverse, dataBaseHelper.listeEditeurs());
        }
        binding.lvEditeursListeEditeurs.setAdapter(editeursArrayAdapter);
    }
}