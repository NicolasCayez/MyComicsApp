package com.example.mycomics.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mycomics.R;
import com.example.mycomics.adapters.AuteursListAdapter;
import com.example.mycomics.adapters.AuteursNbListAdapter;
import com.example.mycomics.adapters.EditeursListAdapter;
import com.example.mycomics.adapters.SeriesListAdapter;
import com.example.mycomics.beans.AuteurBean;
import com.example.mycomics.beans.EditeurBean;
import com.example.mycomics.beans.SerieBean;
import com.example.mycomics.beans.TomeBean;
import com.example.mycomics.beans.TomeSerieBean;
import com.example.mycomics.databinding.FragmentTomeDetailBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupConfirmDialog;
import com.example.mycomics.popups.PopupTextDialog;
import com.example.mycomics.popups.PopupAddListDialog;
import com.example.mycomics.popups.PopupListDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TomeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TomeDetailFragment extends Fragment {
    FragmentTomeDetailBinding binding;

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

    public TomeDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TomeDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TomeDetailFragment newInstance(String param1, String param2) {
        TomeDetailFragment fragment = new TomeDetailFragment();
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
        binding = FragmentTomeDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* -------------------------------------- */
        // Récupération données
        /* -------------------------------------- */
        Integer tome_id = getArguments().getInt("tome_id");
        Integer tome_numero = getArguments().getInt("tome_numero");
        String tome_titre = getArguments().getString("tome_titre");
        double tome_prix_editeur = getArguments().getDouble("tome_prix_editeur");
        double tome_valeur_connue = getArguments().getDouble("tome_valeur_connue");
        String tome_date_edition = getArguments().getString("tome_date_edition");
        String tome_isbn = getArguments().getString("tome_isbn");
        String tome_image = getArguments().getString("tome_image");
        boolean tome_dedicace = getArguments().getBoolean("tome_dedicace");
        boolean tome_edition_speciale = getArguments().getBoolean("tome_edition_speciale");
        String tome_edition_speciale_libelle = getArguments().getString("tome_edition_speciale_libelle");
        Integer serie_id = getArguments().getInt("serie_id");
        Integer editeur_id = getArguments().getInt("editeur-id");
        TomeBean tome = new TomeBean(tome_id, tome_titre, tome_numero, tome_isbn, tome_image, tome_prix_editeur, tome_valeur_connue, tome_date_edition, tome_dedicace, tome_edition_speciale, tome_edition_speciale_libelle, serie_id, editeur_id);
        /* -------------------------------------- */
        // Initialisation affichage
        /* -------------------------------------- */
        afficherDetailTome(tome_id);
        /* -------------------------------------- */
        // Initialisation Nom fiche
        /* -------------------------------------- */
        binding.etDetailTomeTitre.setText(tome_titre);
        /* -------------------------------------- */
        // Clic Enregistrer Tome
        /* -------------------------------------- */
        binding.btnDetailTomeSaveTome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTome(tome_id);
            }
        });

        /* -------------------------------------- */
        // Clic sur bouton AddAuteur
        /* -------------------------------------- */
        binding.btnDetailTomeAddAuteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sauvegarde modifications non enregistrées
                saveTome(tome_id);
                //Création Popup
                PopupAddListDialog popupAddListDialog = new PopupAddListDialog(getActivity());
                popupAddListDialog.setTitre("Choisissez un Auteur dans la liste ou créez-en un nouveau");
                popupAddListDialog.setHint("Pseudo nouvel auteur");
                popupAddListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ListView listView = popupAddListDialog.findViewById(R.id.lvPopupList);
                auteursArrayAdapter = new AuteursListAdapter(getActivity() , R.layout.listview_row_1col, dataBaseHelper.listeAuteurs());
                listView.setAdapter(auteursArrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AuteurBean auteurBean = null;
                        try {
                            auteurBean = (AuteurBean) popupAddListDialog.getLvPopupList().getItemAtPosition(position);
                        } catch (Exception e) {
                        }
                        popupAddListDialog.dismiss(); // Fermeture Popup
                        //Appel DataBaseHelper
                        dataBaseHelper = new DataBaseHelper(getActivity());
                        if (dataBaseHelper.verifDoublonTomeAuteur(tome_id, auteurBean.getAuteur_id())) {
                            // Editeur déjà existant
                            Toast.makeText(TomeDetailFragment.super.getContext(), "Auteur " + auteurBean.getAuteur_pseudo() + " déjà existant", Toast.LENGTH_SHORT).show();
                        } else {
                            // on enregiste
                            boolean successInsertDetenir = dataBaseHelper.insertIntoEcrire(tome_id, auteurBean.getAuteur_id());
                        }

                        afficherDetailTome(tome_id);
                    }
                });

                popupAddListDialog.getBtnPopupValider().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AuteurBean auteurBean;
                        if (popupAddListDialog.getEtPopupText().getText().length() > 0) {
                            try {
                                auteurBean = new AuteurBean(-1, popupAddListDialog.getEtPopupText().getText().toString());
                            } catch (Exception e) {
    //                            Toast.makeText(ReglagesActivity.this, "Erreur création profil", Toast.LENGTH_SHORT).show();
                                auteurBean = new AuteurBean(-1, "error" );
                            }
                            popupAddListDialog.dismiss(); // Fermeture Popup
                            //Appel DataBaseHelper
                            dataBaseHelper = new DataBaseHelper(getActivity());
                            boolean successInsertAuteur = dataBaseHelper.insertIntoAuteurs(auteurBean);
                            boolean successInsertEcrire = dataBaseHelper.insertIntoEcrire(tome_id, dataBaseHelper.selectDernierAuteurAjoute(auteurBean).getAuteur_id());
    //                        afficherDetailTome(tome_id)
                        }
                    }
                });
                popupAddListDialog.getBtnPopupAnnuler().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupAddListDialog.dismiss(); // Fermeture Popup
                    }
                });

                popupAddListDialog.Build();
                popupAddListDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        afficherDetailTome(tome_id);
                    }
                });
            }
        });

        /* -------------------------------------- */
        // Clic sur l'éditeur pour voir le détail
        /* -------------------------------------- */
        binding.tvDetailTomeEditeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sauvegarde modifications non enregistrées
                saveTome(tome_id);
                EditeurBean editeurBean;
                try {
                    editeurBean = dataBaseHelper.selectEditeurSelonTomeId(tome_id);
                } catch (Exception e) {
                    editeurBean = new EditeurBean  (-1,"error");
                }
                Bundle bundle = new Bundle();
                bundle.putInt("editeur_id", editeurBean.getEditeur_id());
                bundle.putString("editeur_nom", editeurBean.getEditeur_nom());

                findNavController(TomeDetailFragment.this).navigate(R.id.action_tomeDetail_to_editeurDetai, bundle);
            }
        });

        /* -------------------------------------- */
        // Clic sur changer l'éditeur pour avoir la liste
        /* -------------------------------------- */
        binding.btnDetailTomeChangeEditeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sauvegarde modifications non enregistrées
                saveTome(tome_id);
                //Création Popup
                PopupListDialog popupListDialog = new PopupListDialog(getActivity());
                popupListDialog.setTitre("Choisissez un Editeur dans la liste");
                popupListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ListView listView = (ListView) popupListDialog.findViewById(R.id.lvPopupList);
                editeursArrayAdapter = new EditeursListAdapter(getActivity() , R.layout.listview_row_1col, dataBaseHelper.listeEditeurs());
                listView.setAdapter(editeursArrayAdapter);
                //Clic Editeur choisi pour modification
                popupListDialog.getLvPopupListe().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        EditeurBean editeurBean;
                        try {
                            editeurBean = (EditeurBean) popupListDialog.getLvPopupListe().getItemAtPosition(position);
                            dataBaseHelper.updateEditeurDuTome(dataBaseHelper, editeurBean, tome_id);
                        } catch (Exception e) {
                            editeurBean = new EditeurBean(-1, "error" );
                        }
                        popupListDialog.dismiss(); // Fermeture Popup
                        afficherDetailTome(tome_id);

                        //Appel DataBaseHelper
//                        dataBaseHelper = new DataBaseHelper(getActivity());
                    }
                });
                popupListDialog.getBtnPopupAnnuler().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupListDialog.dismiss(); // Fermeture Popup
                    }
                });
                popupListDialog.Build();
                afficherDetailTome(tome_id);

            }
        });

        /* -------------------------------------- */
        // Clic sur bouton AddEditeur
        /* -------------------------------------- */
        binding.btnDetailTomeAddEditeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sauvegarde modifications non enregistrées
                saveTome(tome_id);
                //Création Popup
                PopupTextDialog popupTextDialog = new PopupTextDialog(getActivity());
                popupTextDialog.setTitre("Entrez un nouvel éditeur");
                popupTextDialog.setHint("Nom de l'éditeur");
                popupTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                popupTextDialog.getBtnPopupValider().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditeurBean editeurBean;
                        boolean success = true;
                        try {
                            editeurBean = new EditeurBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                        } catch (Exception e) {
//                            Toast.makeText(ReglagesActivity.this, "Erreur création Editeur", Toast.LENGTH_SHORT).show();
                            editeurBean = new EditeurBean(-1, "error");
                            success = false;
                        }
                        popupTextDialog.dismiss(); // Fermeture Popup
                        //Appel DataBaseHelper
                        dataBaseHelper = new DataBaseHelper(getActivity());
                        if (dataBaseHelper.verifDoublonEditeur(editeurBean.getEditeur_nom()) && !success) {
                            // Editeur déjà existant
                            Toast.makeText(TomeDetailFragment.super.getContext(), "Editeur " + binding.tvDetailTomeEditeur.getText().toString() + " déjà existant", Toast.LENGTH_SHORT).show();
                        } else {
                            // on enregiste
                            boolean successInsert = dataBaseHelper.insertIntoEditeurs(editeurBean);
                            boolean successUpdate = dataBaseHelper.updateEditeurDuTome(dataBaseHelper, dataBaseHelper.selectDernierEditeurAjoute(editeurBean), tome_id);

                        }
                        afficherDetailTome(tome_id);
                    }
                });
                popupTextDialog.getBtnPopupAnnuler().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupTextDialog.dismiss(); // Fermeture Popup
                    }
                });
                popupTextDialog.build();
                afficherDetailTome(tome_id);
            }
        });

        /* -------------------------------------- */
        // Clic sur la série pour voir le détail
        /* -------------------------------------- */
        binding.tvDetailTomeSerie.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //sauvegarde modifications non enregistrées
                 saveTome(tome_id);
                 SerieBean serieBean;
                 try {
                     serieBean = dataBaseHelper.selectSerieSelonTomeId(tome_id);
                 } catch (Exception e) {
                     serieBean = new SerieBean(-1,"error");
                 }
                 Bundle bundle = new Bundle();
                 bundle.putInt("serie_id", serieBean.getSerie_id());
                 bundle.putString("serie_nom", serieBean.getSerie_nom());

                 findNavController(TomeDetailFragment.this).navigate(R.id.action_tomeDetail_to_serieDetail, bundle);
             }
        });

        /* -------------------------------------- */
        // Clic sur changer la série pour avoir la liste
        /* -------------------------------------- */
        binding.btnDetailTomeChangeSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sauvegarde modifications non enregistrées
                saveTome(tome_id);
                //Création Popup
                PopupListDialog popupListDialog = new PopupListDialog(getActivity());
                popupListDialog.setTitre("Choisissez une série dans la liste");
                popupListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ListView listView = (ListView) popupListDialog.findViewById(R.id.lvPopupList);
                seriesArrayAdapter = new SeriesListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.listeSeries());
                listView.setAdapter(seriesArrayAdapter);
                //Clic Editeur choisi pour modification
                popupListDialog.getLvPopupListe().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SerieBean serieBean;
                        try {
                            serieBean = (SerieBean) popupListDialog.getLvPopupListe().getItemAtPosition(position);
                            dataBaseHelper.updateSerieDuTome(dataBaseHelper, serieBean, tome_id);
                        } catch (Exception e) {
                            serieBean = new SerieBean(-1, "error");
                        }
                        popupListDialog.dismiss(); // Fermeture Popup
                        afficherDetailTome(tome_id);

                        //Appel DataBaseHelper
//                        dataBaseHelper = new DataBaseHelper(getActivity());
                    }
                });
                popupListDialog.getBtnPopupAnnuler().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupListDialog.dismiss(); // Fermeture Popup
                    }
                });
                popupListDialog.Build();
                afficherDetailTome(tome_id);

            }
        });

        /* -------------------------------------- */
        // Clic sur bouton AddSerie
        /* -------------------------------------- */
        binding.btnDetailTomeAddSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sauvegarde modifications non enregistrées
                saveTome(tome_id);
                //Création Popup
                PopupTextDialog popupTextDialog = new PopupTextDialog(getActivity());
                popupTextDialog.setTitre("Entrez une nouvelle série");
                popupTextDialog.setHint("Nom de la série");
                popupTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupTextDialog.getBtnPopupValider().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SerieBean serieBean;
                        boolean success = true;
                        try {
                            serieBean = new SerieBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                        } catch (Exception e) {
//                            Toast.makeText(ReglagesActivity.this, "Erreur création Editeur", Toast.LENGTH_SHORT).show();
                            serieBean = new SerieBean(-1, "error");
                            success = false;
                        }
                        popupTextDialog.dismiss(); // Fermeture Popup
                        //Appel DataBaseHelper
                        dataBaseHelper = new DataBaseHelper(getActivity());
                        if (dataBaseHelper.verifDoublonSerie(serieBean.getSerie_nom()) && !success) {
                            // Editeur déjà existant
                            Toast.makeText(TomeDetailFragment.super.getContext(), "Série " + binding.tvDetailTomeTitreSerie.getText().toString() + " déjà existante", Toast.LENGTH_SHORT).show();
                        } else {
                            // on enregiste
                            boolean successInsert = dataBaseHelper.insertIntoSeries(serieBean);
                            boolean successUpdate = dataBaseHelper.updateSerieDuTome(dataBaseHelper, dataBaseHelper.DerniereSerieAjoutee(serieBean), tome_id);

                        }
                        afficherDetailTome(tome_id);
                    }
                });
                popupTextDialog.getBtnPopupAnnuler().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupTextDialog.dismiss(); // Fermeture Popup
                    }
                });
                popupTextDialog.build();
                afficherDetailTome(tome_id);
            }
        });

        /* -------------------------------------- */
        // Clic Element liste Auteur
        /* -------------------------------------- */
        binding.lvDetailTomeAuteurs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AuteurBean auteurBean;
                try {
                    auteurBean = (AuteurBean) binding.lvDetailTomeAuteurs.getItemAtPosition(position);
                } catch (Exception e) {
                    auteurBean = new AuteurBean(-1,"error","error","error");
                }
                Bundle bundle = new Bundle();
                bundle.putInt("auteur_id", auteurBean.getAuteur_id());
                bundle.putString("auteur_pseudo", auteurBean.getAuteur_pseudo());
                bundle.putString("auteur_nom", auteurBean.getAuteur_nom());
                bundle.putString("auteur_prenom", auteurBean.getAuteur_prenom());

                findNavController(TomeDetailFragment.this).navigate(R.id.action_tomeDetail_to_auteurDetail, bundle);

            }
        });

        /* -------------------------------------- */
        // Clic bouton delete Serie
        /* -------------------------------------- */
        binding.btnDetailTomeDeleteSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sauvegarde modifications non enregistrées
                saveTome(tome_id);
                //Création Popup
                PopupConfirmDialog popupConfirmDialog = new PopupConfirmDialog(getActivity());
                popupConfirmDialog.setTitre( "Tome\n\" " + tome_titre + " \"\nConfirmer le retrait de la série\n\" " + dataBaseHelper.selectSerieSelonTomeId(tome_id).getSerie_nom() + " \"");
                popupConfirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupConfirmDialog.getBtnPopupValider().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupConfirmDialog.dismiss();
                        boolean successUpdate = dataBaseHelper.deleteSerieDuTome(dataBaseHelper, tome_id);
                        afficherDetailTome(tome_id);
                    }
                });
                popupConfirmDialog.getBtnPopupAnnuler().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupConfirmDialog.dismiss(); // Fermeture Popup
                    }
                });
                popupConfirmDialog.build();
                afficherDetailTome(tome_id);
            }
        });

        /* -------------------------------------- */
        // Clic bouton delete Editeur
        /* -------------------------------------- */
        binding.btnDetailTomeDeleteEditeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sauvegarde modifications non enregistrées
                saveTome(tome_id);
                //Création Popup
                PopupConfirmDialog popupConfirmDialog = new PopupConfirmDialog(getActivity());
                popupConfirmDialog.setTitre( "Tome\n\" " + tome_titre + " \"\nConfirmer le retrait de l'édieur\n\" " + dataBaseHelper.selectEditeurSelonTomeId(tome_id).getEditeur_nom() + " \"");
                popupConfirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupConfirmDialog.getBtnPopupValider().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupConfirmDialog.dismiss();
                        boolean successUpdate = dataBaseHelper.deleteEditeurDuTome(dataBaseHelper, tome_id);
                        afficherDetailTome(tome_id);
                    }
                });
                popupConfirmDialog.getBtnPopupAnnuler().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupConfirmDialog.dismiss(); // Fermeture Popup
                    }
                });
                popupConfirmDialog.build();
                afficherDetailTome(tome_id);
            }
        });

        /* -------------------------------------- */
        // Clic bouton delete Auteur
        /* -------------------------------------- */
        binding.btnDetailTomeDeleteAuteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sauvegarde modifications non enregistrées
                saveTome(tome_id);
                //Création Popup
                PopupListDialog popupListDialog = new PopupListDialog(getActivity());
                popupListDialog.setTitre( "Tome\n\" " + tome_titre + " \"\nChoisissez l'auteur à retirer");
                popupListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ListView listView = (ListView) popupListDialog.findViewById(R.id.lvPopupList);
                auteursArrayAdapter = new AuteursListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.listeAuteursSelonTomeId(tome_id));
                listView.setAdapter(auteursArrayAdapter);
                //Clic Editeur choisi pour modification
                popupListDialog.getLvPopupListe().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AuteurBean auteurBean;
                        try {
                            auteurBean = (AuteurBean) popupListDialog.getLvPopupListe().getItemAtPosition(position);
                            boolean successUpdate = dataBaseHelper.deleteAuteurDuTome(dataBaseHelper, auteurBean.getAuteur_id(), tome_id);
                        } catch (Exception e) {
                            auteurBean = new AuteurBean(-1, "error");
                        }
                        popupListDialog.dismiss(); // Fermeture Popup
                        afficherDetailTome(tome_id);
                    }
                });
                popupListDialog.getBtnPopupAnnuler().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupListDialog.dismiss(); // Fermeture Popup
                    }
                });
                popupListDialog.Build();
                afficherDetailTome(tome_id);
            }
        });


        /* -------------------------------------- */
        // Clic bouton delete Tome
        /* -------------------------------------- */
        binding.btnDetailTomeDeleteTome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sauvegarde modifications non enregistrées
                saveTome(tome_id);
                //Création Popup
                PopupTextDialog popupTextDialog = new PopupTextDialog(getActivity());
                popupTextDialog.setTitre( "Tome\n\" " + tome_titre + " \"\nOpération irréversible, confirmer la suppression");
                popupTextDialog.setHint("Nom du tome à supprimer");
                popupTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupTextDialog.getBtnPopupValider().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TomeBean tomeBean;
                        boolean success = true;
                        try {
                            tomeBean = new TomeBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                        } catch (Exception e) {
                            tomeBean = new TomeBean(-1, "error");
                            success = false;
                        }
                        popupTextDialog.dismiss(); // Fermeture Popup
                        //Appel DataBaseHelper
                        dataBaseHelper = new DataBaseHelper(getActivity());
                        if (!dataBaseHelper.selectTomeSelonTomeId(tome_id).getTome_titre().equals(popupTextDialog.getEtPopupText().getText().toString())) {
                            // Titre saisi différent
                            Toast.makeText(TomeDetailFragment.super.getActivity(), "Le nom ne correspond pas", Toast.LENGTH_LONG).show();
                        } else {
                            // on enlève le tome et ses contraintes dans ECRIRE et DETENIR
                            boolean successUpdate = dataBaseHelper.deleteTome(dataBaseHelper, tome_id);
                            findNavController(TomeDetailFragment.this).navigate(R.id.tomesFragment);
                        }
                    }
                });
                popupTextDialog.getEtPopupText().setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            // Perform action on key press
                            TomeBean tomeBean;
                            boolean success = true;
                            try {
                                tomeBean = new TomeBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                            } catch (Exception e) {
                                tomeBean = new TomeBean(-1, "error");
                                success = false;
                            }
                            popupTextDialog.dismiss(); // Fermeture Popup
                            //Appel DataBaseHelper
                            dataBaseHelper = new DataBaseHelper(getActivity());
                            if (!dataBaseHelper.selectTomeSelonTomeId(tome_id).getTome_titre().equals(popupTextDialog.getEtPopupText().getText().toString())) {
                                // Titre saisi différent
                                Toast.makeText(TomeDetailFragment.super.getActivity(), "Le nom ne correspond pas", Toast.LENGTH_LONG).show();
                            } else {
                                // on enlève le tome et ses contraintes dans ECRIRE et DETENIR
                                boolean successUpdate = dataBaseHelper.deleteTome(dataBaseHelper, tome_id);
                                findNavController(TomeDetailFragment.this).navigate(R.id.tomesFragment);
                            }
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
                afficherDetailTome(tome_id);
            }
        });

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void afficherDetailTome(Integer tome_id){
        TomeBean tome = dataBaseHelper.selectTomeSelonTomeId(tome_id);
        binding.etDetailTomeTitre.setText(tome.getTome_titre());
        if (tome.getTome_numero() == 0 || tome.getTome_numero() == null) {
            binding.etDetailTomeNumero.setText("");
        } else {
            binding.etDetailTomeNumero.setText(String.valueOf(tome.getTome_numero()));
        }
        binding.tvDetailTomeSerie.setText(dataBaseHelper.selectSerieSelonTomeId(tome_id).getSerie_nom());
        binding.etDetailTomeISBN.setText(tome.getTome_isbn() == null ? "" : String.valueOf(tome.getTome_isbn()));//---------------
        binding.etDetailTomePrixEditeur.setText(tome.getTome_prix_editeur() == 0 ? "" : String.valueOf(tome.getTome_prix_editeur()));
        binding.etDetailTomeValeurActuelle.setText(tome.getTome_valeur_connue() == 0 ? "" : String.valueOf(tome.getTome_valeur_connue()));
        binding.etDetailTomeDateEdition.setText(tome.getTome_date_edition() == null ? "" : String.valueOf(tome.getTome_date_edition()));//---------------
        binding.chkDetailTomeDedicace.setChecked(Boolean.valueOf(tome.isTome_dedicace()));
        binding.chkDetailTomeEditionSpeciale.setChecked(Boolean.valueOf(tome.isTome_edition_speciale()));
        binding.etDetailTomeEditionSpecialeLibelle.setText(tome.getTome_edition_speciale_libelle() == null ? "" : String.valueOf( tome.getTome_edition_speciale_libelle()));//---------------
        //image ici
        auteursArrayAdapter = new AuteursListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.listeAuteursSelonTomeId(tome_id));
        binding.lvDetailTomeAuteurs.setAdapter(auteursArrayAdapter);
        binding.tvDetailTomeEditeur.setText(dataBaseHelper.selectEditeurSelonTomeId(tome_id).getEditeur_nom());
    }

    private void saveTome(int tome_id){
        TomeBean tomeModif = new TomeBean(
                tome_id,
                binding.etDetailTomeTitre.getText().toString(),
                binding.etDetailTomeNumero.getText().length() == 0 ? null : Integer.parseInt(binding.etDetailTomeNumero.getText().toString()),
                binding.etDetailTomeISBN.getText().toString(),
                "pas d'image",
                binding.etDetailTomePrixEditeur.getText().length() == 0 ? 0.0 : Double.parseDouble(binding.etDetailTomePrixEditeur.getText().toString()),
                binding.etDetailTomeValeurActuelle.getText().length() == 0 ? 0.0 : Double.parseDouble(binding.etDetailTomeValeurActuelle.getText().toString()),
                binding.etDetailTomeDateEdition.getText().toString(),
                binding.chkDetailTomeDedicace.isChecked(),
                binding.chkDetailTomeEditionSpeciale.isChecked(),
                binding.etDetailTomeEditionSpecialeLibelle.getText().toString(),
                dataBaseHelper.selectSerieSelonTomeId(tome_id).getSerie_id(),
                dataBaseHelper.selectEditeurSelonTomeId(tome_id).getEditeur_id());
        boolean updateOk = dataBaseHelper.updateTome(dataBaseHelper, tomeModif);
        if (updateOk) {
            Toast.makeText(getActivity(),"Tome modifié avec succès" , Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(getActivity(),"Erreur modification tome" , Toast.LENGTH_SHORT);
        }
    }



}