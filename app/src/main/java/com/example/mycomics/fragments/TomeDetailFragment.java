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
import com.example.mycomics.adapters.EditeursListAdapter;
import com.example.mycomics.adapters.SeriesListAdapter;
import com.example.mycomics.beans.AuthorBean;
import com.example.mycomics.beans.BookBean;
import com.example.mycomics.beans.EditorBean;
import com.example.mycomics.beans.SerieBean;
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
        Integer book_id = getArguments().getInt("book_id");
        Integer book_number = getArguments().getInt("book_number");
        String book_title = getArguments().getString("book_title");
        double book_editor_price = getArguments().getDouble("book_editor_price");
        double book_value = getArguments().getDouble("book_value");
        String book_edition_date = getArguments().getString("book_edition_date");
        String book_isbn = getArguments().getString("book_isbn");
        String book_picture = getArguments().getString("book_picture");
        boolean book_autograph = getArguments().getBoolean("book_autograph");
        boolean book_special_edition = getArguments().getBoolean("book_special_edition");
        String book_special_edition_label = getArguments().getString("book_special_edition_label");
        Integer serie_id = getArguments().getInt("serie_id");
        Integer editor_id = getArguments().getInt("editor_id");
        BookBean bookBean = new BookBean(book_id, book_title, book_number, book_isbn, book_picture,
                book_editor_price, book_value, book_edition_date, book_autograph,
                book_special_edition, book_special_edition_label, serie_id, editor_id);
        /* -------------------------------------- */
        // Initialisation affichage
        /* -------------------------------------- */
        afficherDetailTome(book_id);
        /* -------------------------------------- */
        // Initialisation Nom fiche
        /* -------------------------------------- */
        binding.etDetailTomeTitre.setText(book_title);
        /* -------------------------------------- */
        // Clic Enregistrer Tome
        /* -------------------------------------- */
        binding.btnDetailTomeSaveTome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTome(book_id);
            }
        });

        /* -------------------------------------- */
        // Clic sur bouton AddAuteur
        /* -------------------------------------- */
        binding.btnDetailTomeAddAuteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sauvegarde modifications non enregistrées
                saveTome(book_id);
                //Création Popup
                PopupAddListDialog popupAddListDialog = new PopupAddListDialog(getActivity());
                popupAddListDialog.setTitre("Choisissez un Auteur dans la liste ou créez-en un nouveau");
                popupAddListDialog.setHint("Pseudo nouvel auteur");
                popupAddListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ListView listView = popupAddListDialog.findViewById(R.id.lvPopupList);
                auteursArrayAdapter = new AuteursListAdapter(getActivity() , R.layout.listview_row_1col, dataBaseHelper.getAuthorsList());
                listView.setAdapter(auteursArrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AuthorBean authorBean = null;
                        try {
                            authorBean = (AuthorBean) popupAddListDialog.getLvPopupList().getItemAtPosition(position);
                        } catch (Exception e) {
                        }
                        popupAddListDialog.dismiss(); // Fermeture Popup
                        //Appel DataBaseHelper
                        dataBaseHelper = new DataBaseHelper(getActivity());
                        if (dataBaseHelper.checkDetainingBookAuthorPairDuplicate(book_id, authorBean.getAuthor_id())) {
                            // Editeur déjà existant
                            Toast.makeText(TomeDetailFragment.super.getContext(), "Auteur " + authorBean.getAuthor_pseudonym() + " déjà existant", Toast.LENGTH_SHORT).show();
                        } else {
                            // on enregiste
                            boolean successInsertDetenir = dataBaseHelper.insertIntoWriting(book_id, authorBean.getAuthor_id());
                        }

                        afficherDetailTome(book_id);
                    }
                });

                popupAddListDialog.getBtnPopupValider().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AuthorBean authorBean;
                        if (popupAddListDialog.getEtPopupText().getText().length() > 0) {
                            try {
                                authorBean = new AuthorBean(-1, popupAddListDialog.getEtPopupText().getText().toString());
                            } catch (Exception e) {
    //                            Toast.makeText(ReglagesActivity.this, "Erreur création profil", Toast.LENGTH_SHORT).show();
                                authorBean = new AuthorBean(-1, "error" );
                            }
                            popupAddListDialog.dismiss(); // Fermeture Popup
                            //Appel DataBaseHelper
                            dataBaseHelper = new DataBaseHelper(getActivity());
                            boolean successInsertAuteur = dataBaseHelper.insertIntoAuthors(authorBean);
                            boolean successInsertEcrire = dataBaseHelper.insertIntoWriting(book_id, dataBaseHelper.getAuthorLatest(authorBean).getAuthor_id());
    //                        afficherDetailTome(book_id)
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
                        afficherDetailTome(book_id);
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
                saveTome(book_id);
                EditorBean editorBean;
                try {
                    editorBean = dataBaseHelper.getEditorByBookId(book_id);
                } catch (Exception e) {
                    editorBean = new EditorBean(-1,"error");
                }
                Bundle bundle = new Bundle();
                bundle.putInt("editor_id", editorBean.getEditor_id());
                bundle.putString("editeur_nom", editorBean.getEditor_name());

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
                saveTome(book_id);
                //Création Popup
                PopupListDialog popupListDialog = new PopupListDialog(getActivity());
                popupListDialog.setTitre("Choisissez un Editeur dans la liste");
                popupListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ListView listView = (ListView) popupListDialog.findViewById(R.id.lvPopupList);
                editeursArrayAdapter = new EditeursListAdapter(getActivity() , R.layout.listview_row_1col, dataBaseHelper.getEditorsList());
                listView.setAdapter(editeursArrayAdapter);
                //Clic Editeur choisi pour modification
                popupListDialog.getLvPopupListe().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        EditorBean editorBean;
                        try {
                            editorBean = (EditorBean) popupListDialog.getLvPopupListe().getItemAtPosition(position);
                            dataBaseHelper.updateBookEditor(dataBaseHelper, editorBean, book_id);
                        } catch (Exception e) {
                            editorBean = new EditorBean(-1, "error" );
                        }
                        popupListDialog.dismiss(); // Fermeture Popup
                        afficherDetailTome(book_id);

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
                afficherDetailTome(book_id);

            }
        });

        /* -------------------------------------- */
        // Clic sur bouton AddEditeur
        /* -------------------------------------- */
        binding.btnDetailTomeAddEditeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sauvegarde modifications non enregistrées
                saveTome(book_id);
                //Création Popup
                PopupTextDialog popupTextDialog = new PopupTextDialog(getActivity());
                popupTextDialog.setTitre("Entrez un nouvel éditeur");
                popupTextDialog.setHint("Nom de l'éditeur");
                popupTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                popupTextDialog.getBtnPopupValider().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditorBean editorBean;
                        boolean success = true;
                        try {
                            editorBean = new EditorBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                        } catch (Exception e) {
//                            Toast.makeText(ReglagesActivity.this, "Erreur création Editeur", Toast.LENGTH_SHORT).show();
                            editorBean = new EditorBean(-1, "error");
                            success = false;
                        }
                        popupTextDialog.dismiss(); // Fermeture Popup
                        //Appel DataBaseHelper
                        dataBaseHelper = new DataBaseHelper(getActivity());
                        if (dataBaseHelper.checkEditorDuplicate(editorBean.getEditor_name()) && !success) {
                            // Editeur déjà existant
                            Toast.makeText(TomeDetailFragment.super.getContext(), "Editeur " + binding.tvDetailTomeEditeur.getText().toString() + " déjà existant", Toast.LENGTH_SHORT).show();
                        } else {
                            // on enregiste
                            boolean successInsert = dataBaseHelper.insertIntoEditors(editorBean);
                            boolean successUpdate = dataBaseHelper.updateBookEditor(dataBaseHelper, dataBaseHelper.getEditorLatest(editorBean), book_id);

                        }
                        afficherDetailTome(book_id);
                    }
                });
                popupTextDialog.getBtnPopupAnnuler().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupTextDialog.dismiss(); // Fermeture Popup
                    }
                });
                popupTextDialog.build();
                afficherDetailTome(book_id);
            }
        });

        /* -------------------------------------- */
        // Clic sur la série pour voir le détail
        /* -------------------------------------- */
        binding.tvDetailTomeSerie.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //sauvegarde modifications non enregistrées
                 saveTome(book_id);
                 SerieBean serieBean;
                 try {
                     serieBean = dataBaseHelper.getSerieByBookId(book_id);
                 } catch (Exception e) {
                     serieBean = new SerieBean(-1,"error");
                 }
                 Bundle bundle = new Bundle();
                 bundle.putInt("serie_id", serieBean.getSerie_id());
                 bundle.putString("serie_name", serieBean.getSerie_name());

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
                saveTome(book_id);
                //Création Popup
                PopupListDialog popupListDialog = new PopupListDialog(getActivity());
                popupListDialog.setTitre("Choisissez une série dans la liste");
                popupListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ListView listView = (ListView) popupListDialog.findViewById(R.id.lvPopupList);
                seriesArrayAdapter = new SeriesListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.getSeriesList());
                listView.setAdapter(seriesArrayAdapter);
                //Clic Editeur choisi pour modification
                popupListDialog.getLvPopupListe().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SerieBean serieBean;
                        try {
                            serieBean = (SerieBean) popupListDialog.getLvPopupListe().getItemAtPosition(position);
                            dataBaseHelper.updateBookSerie(dataBaseHelper, serieBean, book_id);
                        } catch (Exception e) {
                            serieBean = new SerieBean(-1, "error");
                        }
                        popupListDialog.dismiss(); // Fermeture Popup
                        afficherDetailTome(book_id);

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
                afficherDetailTome(book_id);

            }
        });

        /* -------------------------------------- */
        // Clic sur bouton AddSerie
        /* -------------------------------------- */
        binding.btnDetailTomeAddSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sauvegarde modifications non enregistrées
                saveTome(book_id);
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
                        if (dataBaseHelper.checkSerieDuplicate(serieBean.getSerie_name()) && !success) {
                            // Editeur déjà existant
                            Toast.makeText(TomeDetailFragment.super.getContext(), "Série " + binding.tvDetailTomeTitreSerie.getText().toString() + " déjà existante", Toast.LENGTH_SHORT).show();
                        } else {
                            // on enregiste
                            boolean successInsert = dataBaseHelper.insertIntoSeries(serieBean);
                            boolean successUpdate = dataBaseHelper.updateBookSerie(dataBaseHelper, dataBaseHelper.getSerieLatest(serieBean), book_id);

                        }
                        afficherDetailTome(book_id);
                    }
                });
                popupTextDialog.getBtnPopupAnnuler().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupTextDialog.dismiss(); // Fermeture Popup
                    }
                });
                popupTextDialog.build();
                afficherDetailTome(book_id);
            }
        });

        /* -------------------------------------- */
        // Clic Element liste Auteur
        /* -------------------------------------- */
        binding.lvDetailTomeAuteurs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AuthorBean authorBean;
                try {
                    authorBean = (AuthorBean) binding.lvDetailTomeAuteurs.getItemAtPosition(position);
                } catch (Exception e) {
                    authorBean = new AuthorBean(-1,"error","error","error");
                }
                Bundle bundle = new Bundle();
                bundle.putInt("author_id", authorBean.getAuthor_id());
                bundle.putString("author_pseudonym", authorBean.getAuthor_pseudonym());
                bundle.putString("author_last_name", authorBean.getAuthor_last_name());
                bundle.putString("author_first_name", authorBean.getAuthor_first_name());

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
                saveTome(book_id);
                //Création Popup
                PopupConfirmDialog popupConfirmDialog = new PopupConfirmDialog(getActivity());
                popupConfirmDialog.setTitre( "Tome\n\" " + book_title + " \"\nConfirmer le retrait de la série\n\" " + dataBaseHelper.getSerieByBookId(book_id).getSerie_name() + " \"");
                popupConfirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupConfirmDialog.getBtnPopupValider().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupConfirmDialog.dismiss();
                        boolean successUpdate = dataBaseHelper.deleteBookSerie(dataBaseHelper, book_id);
                        afficherDetailTome(book_id);
                    }
                });
                popupConfirmDialog.getBtnPopupAnnuler().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupConfirmDialog.dismiss(); // Fermeture Popup
                    }
                });
                popupConfirmDialog.build();
                afficherDetailTome(book_id);
            }
        });

        /* -------------------------------------- */
        // Clic bouton delete Editeur
        /* -------------------------------------- */
        binding.btnDetailTomeDeleteEditeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sauvegarde modifications non enregistrées
                saveTome(book_id);
                //Création Popup
                PopupConfirmDialog popupConfirmDialog = new PopupConfirmDialog(getActivity());
                popupConfirmDialog.setTitre( "Tome\n\" " + book_title + " \"\nConfirmer le retrait de l'édieur\n\" " + dataBaseHelper.getEditorByBookId(book_id).getEditor_name() + " \"");
                popupConfirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupConfirmDialog.getBtnPopupValider().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupConfirmDialog.dismiss();
                        boolean successUpdate = dataBaseHelper.deleteBookEditor(dataBaseHelper, book_id);
                        afficherDetailTome(book_id);
                    }
                });
                popupConfirmDialog.getBtnPopupAnnuler().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupConfirmDialog.dismiss(); // Fermeture Popup
                    }
                });
                popupConfirmDialog.build();
                afficherDetailTome(book_id);
            }
        });

        /* -------------------------------------- */
        // Clic bouton delete Auteur
        /* -------------------------------------- */
        binding.btnDetailTomeDeleteAuteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sauvegarde modifications non enregistrées
                saveTome(book_id);
                //Création Popup
                PopupListDialog popupListDialog = new PopupListDialog(getActivity());
                popupListDialog.setTitre( "Tome\n\" " + book_title + " \"\nChoisissez l'auteur à retirer");
                popupListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ListView listView = (ListView) popupListDialog.findViewById(R.id.lvPopupList);
                auteursArrayAdapter = new AuteursListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.getAuthorsListByBookId(book_id));
                listView.setAdapter(auteursArrayAdapter);
                //Clic Editeur choisi pour modification
                popupListDialog.getLvPopupListe().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AuthorBean authorBean;
                        try {
                            authorBean = (AuthorBean) popupListDialog.getLvPopupListe().getItemAtPosition(position);
                            boolean successUpdate = dataBaseHelper.deleteBookAuthor(dataBaseHelper, authorBean.getAuthor_id(), book_id);
                        } catch (Exception e) {
                            authorBean = new AuthorBean(-1, "error");
                        }
                        popupListDialog.dismiss(); // Fermeture Popup
                        afficherDetailTome(book_id);
                    }
                });
                popupListDialog.getBtnPopupAnnuler().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupListDialog.dismiss(); // Fermeture Popup
                    }
                });
                popupListDialog.Build();
                afficherDetailTome(book_id);
            }
        });


        /* -------------------------------------- */
        // Clic bouton delete Tome
        /* -------------------------------------- */
        binding.btnDetailTomeDeleteTome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sauvegarde modifications non enregistrées
                saveTome(book_id);
                //Création Popup
                PopupTextDialog popupTextDialog = new PopupTextDialog(getActivity());
                popupTextDialog.setTitre( "Tome\n\" " + book_title + " \"\nOpération irréversible, confirmer la suppression");
                popupTextDialog.setHint("Nom du tome à supprimer");
                popupTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupTextDialog.getBtnPopupValider().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BookBean bookBean;
                        boolean success = true;
                        try {
                            bookBean = new BookBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                        } catch (Exception e) {
                            bookBean = new BookBean(-1, "error");
                            success = false;
                        }
                        popupTextDialog.dismiss(); // Fermeture Popup
                        //Appel DataBaseHelper
                        dataBaseHelper = new DataBaseHelper(getActivity());
                        if (!dataBaseHelper.getBookById(book_id).getBook_title().equals(popupTextDialog.getEtPopupText().getText().toString())) {
                            // Titre saisi différent
                            Toast.makeText(TomeDetailFragment.super.getActivity(), "Le nom ne correspond pas", Toast.LENGTH_LONG).show();
                        } else {
                            // on enlève le tome et ses contraintes dans ECRIRE et DETENIR
                            boolean successUpdate = dataBaseHelper.deleteBook(dataBaseHelper, book_id);
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
                            BookBean bookBean;
                            boolean success = true;
                            try {
                                bookBean = new BookBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                            } catch (Exception e) {
                                bookBean = new BookBean(-1, "error");
                                success = false;
                            }
                            popupTextDialog.dismiss(); // Fermeture Popup
                            //Appel DataBaseHelper
                            dataBaseHelper = new DataBaseHelper(getActivity());
                            if (!dataBaseHelper.getBookById(book_id).getBook_title().equals(popupTextDialog.getEtPopupText().getText().toString())) {
                                // Titre saisi différent
                                Toast.makeText(TomeDetailFragment.super.getActivity(), "Le nom ne correspond pas", Toast.LENGTH_LONG).show();
                            } else {
                                // on enlève le tome et ses contraintes dans ECRIRE et DETENIR
                                boolean successUpdate = dataBaseHelper.deleteBook(dataBaseHelper, book_id);
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
                afficherDetailTome(book_id);
            }
        });

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void afficherDetailTome(Integer book_id){
        BookBean bookBean = dataBaseHelper.getBookById(book_id);
        binding.etDetailTomeTitre.setText(bookBean.getBook_title());
        if (bookBean.getBook_number() == 0 || bookBean.getBook_number() == null) {
            binding.etDetailTomeNumero.setText("");
        } else {
            binding.etDetailTomeNumero.setText(String.valueOf(bookBean.getBook_number()));
        }
        binding.tvDetailTomeSerie.setText(dataBaseHelper.getSerieByBookId(book_id).getSerie_name());
        binding.etDetailTomeISBN.setText(bookBean.getBook_isbn() == null ? "" : String.valueOf(bookBean.getBook_isbn()));//---------------
        binding.etDetailTomePrixEditeur.setText(bookBean.getBook_editor_price() == 0 ? "" : String.valueOf(bookBean.getBook_editor_price()));
        binding.etDetailTomeValeurActuelle.setText(bookBean.getBook_value() == 0 ? "" : String.valueOf(bookBean.getBook_value()));
        binding.etDetailTomeDateEdition.setText(bookBean.getBook_edition_date() == null ? "" : String.valueOf(bookBean.getBook_edition_date()));//---------------
        binding.chkDetailTomeDedicace.setChecked(Boolean.valueOf(bookBean.isBook_autograph()));
        binding.chkDetailTomeEditionSpeciale.setChecked(Boolean.valueOf(bookBean.isBook_special_edition()));
        binding.etDetailTomeEditionSpecialeLibelle.setText(bookBean.getBook_special_edition_label() == null ? "" : String.valueOf( bookBean.getBook_special_edition_label()));//---------------
        //image ici
        auteursArrayAdapter = new AuteursListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.getAuthorsListByBookId(book_id));
        binding.lvDetailTomeAuteurs.setAdapter(auteursArrayAdapter);
        binding.tvDetailTomeEditeur.setText(dataBaseHelper.getEditorByBookId(book_id).getEditor_name());
    }

    private void saveTome(int book_id){
        BookBean tomeModif = new BookBean(
                book_id,
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
                dataBaseHelper.getSerieByBookId(book_id).getSerie_id(),
                dataBaseHelper.getEditorByBookId(book_id).getEditor_id());
        boolean updateOk = dataBaseHelper.updateBook(dataBaseHelper, tomeModif);
        if (updateOk) {
            Toast.makeText(getActivity(),"Tome modifié avec succès" , Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(getActivity(),"Erreur modification tome" , Toast.LENGTH_SHORT);
        }
    }



}