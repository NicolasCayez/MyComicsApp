package com.example.mycomics.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.mycomics.R;
import com.example.mycomics.adapters.AuthorsAdapter;
import com.example.mycomics.adapters.BooksBookNbAdapter;
import com.example.mycomics.adapters.EditorsAdapter;
import com.example.mycomics.beans.AuthorBean;
import com.example.mycomics.beans.BookBean;
import com.example.mycomics.beans.EditorBean;
import com.example.mycomics.beans.SerieBean;
import com.example.mycomics.databinding.FragmentSerieDetailBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupTextDialog;

import java.util.ArrayList;

public class SerieDetailFragment extends Fragment {


    //* ----------------------------------------------------------------------------------------- */
    //* View binding declaration */
    //* ----------------------------------------------------------------------------------------- */
    FragmentSerieDetailBinding binding;


    //* ----------------------------------------------------------------------------------------- */
    //* Database handler initialization
    //* ----------------------------------------------------------------------------------------- */
    DataBaseHelper dataBaseHelper;


    //* ----------------------------------------------------------------------------------------- */
    //* Adapters handling RecycleViews data display
    //* ----------------------------------------------------------------------------------------- */
    BooksBookNbAdapter booksAdapter;
    AuthorsAdapter authorsAdapter;
    EditorsAdapter editorsAdapter;


    //* ----------------------------------------------------------------------------------------- */
    //* Empty constructor, required
    //* ----------------------------------------------------------------------------------------- */
    public SerieDetailFragment() {
        // Required empty public constructor
    }


    //* ----------------------------------------------------------------------------------------- */
    //* onCreate inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Database handler initialization */
        dataBaseHelper = new DataBaseHelper(getActivity());
    }


    //* ----------------------------------------------------------------------------------------- */
    //* onCreateView inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSerieDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();    }


    //* ----------------------------------------------------------------------------------------- */
    //* onViewCreated inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* Data sent from source fragments */
        int serie_id = getArguments().getInt("serie_id");

        /* Display initialization */
        SerieBean serieBean = dataBaseHelper.getSerieById(serie_id);
        serieDetailRefreshScreen(serieBean);

        /* Click on Book in the list (RecyclerView) */
        booksAdapter.setOnClickListener(new BooksBookNbAdapter.OnClickListener() {
            @Override
            public void onClick(int position, BookBean bookBean) {
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", bookBean.getBook_id());;
                // go to AuthorDetailFragment with the data bundle
                findNavController(SerieDetailFragment.this).navigate(R.id.action_serieDetail_to_bookDetail, bundle);
            }
        });

        /* Click on Author in the list (RecyclerView) */
        authorsAdapter.setOnClickListener(new AuthorsAdapter.OnClickListener() {
            @Override
            public void onClick(int position, AuthorBean authorBean) {
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("author_id", authorBean.getAuthor_id());;
                // go to AuthorDetailFragment with the data bundle
                findNavController(SerieDetailFragment.this).navigate(R.id.action_serieDetail_to_authorDetail, bundle);
            }
        });

        /* Click on Editor in the list (RecyclerView) */
        editorsAdapter.setOnClickListener(new EditorsAdapter.OnClickListener() {
            @Override
            public void onClick(int position, EditorBean editorBean) {
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("editor_id", editorBean.getEditor_id());;
                // go to AuthorDetailFragment with the data bundle
                findNavController(SerieDetailFragment.this).navigate(R.id.action_serieDetail_to_editorDetail, bundle);
            }
        });

        /* Delete Serie click */
        binding.btnSerieDetailDeleteSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Popup for deleting a Serie
                PopupTextDialog popupTextDialog = new PopupTextDialog(getActivity());
                popupTextDialog.setTitle(getString(R.string.SerieDeleteConfirmName) + "\n\""
                        + dataBaseHelper.getSerieById(serie_id).getSerie_name() + "\"");
                popupTextDialog.setHint(getString(R.string.SerieDetailTitle));
                popupTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Click event on confirm button
                popupTextDialog.getBtnPopupConfirm().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String serieConfirmDeleteName = "";
                        try {
                            serieConfirmDeleteName = popupTextDialog.getEtPopupText().getText().toString();
                        } catch (Exception e) {
                            // do nothing
                        }
                        popupTextDialog.dismiss(); // To close Popup
                        if (!dataBaseHelper.getSerieById(serie_id).getSerie_name().equals(serieConfirmDeleteName)) {
                            // Name does not match
                            Toast.makeText(SerieDetailFragment.super.getActivity(), getString(R.string.SerieNameDoesntMatch), Toast.LENGTH_LONG).show();
                        } else {
                            // Name does match
                            // The serie is deleted from the Database (constraints too)
                            boolean successUpdate = dataBaseHelper.deleteSerie(dataBaseHelper, serie_id);
                            // go to BooksFragment since the BookDetail doesn't exist anymore
                            findNavController(SerieDetailFragment.this).navigate(R.id.seriesFragment);
                        }
                    }
                });
                // Key event, same behaviour as confirm button
                popupTextDialog.getEtPopupText().setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // if "ENTER" key is pressed
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            String serieConfirmDeleteName = "";
                            try {
                                serieConfirmDeleteName = popupTextDialog.getEtPopupText().getText().toString();
                            } catch (Exception e) {
                                // do nothing
                            }
                            popupTextDialog.dismiss(); // To close Popup
                            if (!dataBaseHelper.getSerieById(serie_id).getSerie_name().equals(serieConfirmDeleteName)) {
                                // Name does not match
                                Toast.makeText(SerieDetailFragment.super.getActivity(), getString(R.string.SerieNameDoesntMatch), Toast.LENGTH_LONG).show();
                            } else {
                                // Name does match
                                // The serie is deleted from the Database (constraints too)
                                boolean successUpdate = dataBaseHelper.deleteSerie(dataBaseHelper, serie_id);
                                // go to BooksFragment since the BookDetail doesn't exist anymore
                                findNavController(SerieDetailFragment.this).navigate(R.id.seriesFragment);
                            }
                            return true; // inherited, necessary
                        }
                        return false; // inherited, necessary
                    }
                });
                // Click event on abort button
                popupTextDialog.getBtnPopupAbort().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupTextDialog.dismiss(); // To close Popup
                    }
                });
                popupTextDialog.build(); // To build the popup
                serieDetailRefreshScreen(serieBean); // To refresh display
            }
        });

        /* Click event on Save Book button */
        binding.btnSerieDetailSaveSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSerie(serie_id);
            }
        });
    }


    //* ----------------------------------------------------------------------------------------- */
    //* onDestroy inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null; // to prevent memory leak
    }


    //* ----------------------------------------------------------------------------------------- */
    //* Display initialization and refresh method
    //* ----------------------------------------------------------------------------------------- */
    private void serieDetailRefreshScreen(SerieBean serieBean) {
        // Serie Name
        binding.etSerieDetailSerieName.setText(serieBean.getSerie_name());

        //SeriePicture
        /* TODO******************************************* */

        /* Books list adapters charged with data */
        // Creating the list to display
        ArrayList<BookBean> BooksList = dataBaseHelper.getBooksListBySerieId(serieBean.getSerie_id());
        // The adapter gets the list and the string value "books" needed for translations
        booksAdapter = new BooksBookNbAdapter(BooksList, getString(R.string.BookNumber));
        // the adapter and the layout are defined for the RecyclerView
        binding.rvSerieDetailBooksList.setAdapter(booksAdapter);
        binding.rvSerieDetailBooksList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        booksAdapter.submitList(BooksList);

        // Authors list adapters charger with data
        // Creating the list to display
        ArrayList<AuthorBean> AuthorsList = dataBaseHelper.getAuthorsList();
        // The adapter gets the list and the string value "books" needed for translations
        authorsAdapter = new AuthorsAdapter(AuthorsList);
        // the adapter and the layout are defined for the RecyclerView
        binding.rvSerieDetailAuthorsList.setAdapter(authorsAdapter);
        binding.rvSerieDetailAuthorsList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        authorsAdapter.submitList(AuthorsList);

        /* Editors list adapters charger with data */
        // Creating the list to display
        ArrayList<EditorBean> EditorsList = dataBaseHelper.getEditorsList();
        // The adapter gets the list and the string value "books" needed for translations
        editorsAdapter = new EditorsAdapter(EditorsList);
        // the adapter and the layout are defined for the RecyclerView
        binding.rvSerieDetailEditorsList.setAdapter(editorsAdapter);
        binding.rvSerieDetailEditorsList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        editorsAdapter.submitList(EditorsList);
    }

    //* ----------------------------------------------------------------------------------------- */
    //* Saving the Serie
    //* ----------------------------------------------------------------------------------------- */
    private void saveSerie(int serie_id){
        // SerieBean from fragment items data
        SerieBean serieBean = new SerieBean(
                serie_id,
                binding.etSerieDetailSerieName.getText().toString());
        // Database update of the Serie
        boolean updateOk = dataBaseHelper.updateSerie(dataBaseHelper, serieBean);
        if (updateOk) {
            Toast.makeText(getActivity(), getString(R.string.SerieUpdateSuccess), Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(getActivity(), getString(R.string.SerieUpdateError), Toast.LENGTH_SHORT);
        }
    }
}