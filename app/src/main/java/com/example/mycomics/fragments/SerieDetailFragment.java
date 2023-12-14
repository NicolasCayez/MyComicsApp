package com.example.mycomics.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

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
import android.widget.Toast;

import com.example.mycomics.R;
import com.example.mycomics.adapters.AuthorsListAdapter;
import com.example.mycomics.adapters.EditorsListAdapter;
import com.example.mycomics.adapters.BooksNumberListAdapter;
import com.example.mycomics.beans.AuthorBean;
import com.example.mycomics.beans.EditorBean;
import com.example.mycomics.beans.SerieBean;
import com.example.mycomics.beans.BookBean;
import com.example.mycomics.databinding.FragmentSerieDetailBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupTextDialog;

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
    //* Adapters handling listViews data display
    //* ----------------------------------------------------------------------------------------- */
    ArrayAdapter booksArrayAdapter;
    ArrayAdapter authorsArrayAdapter;
    ArrayAdapter editorsArrayAdapter;


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

        /* Books list item click */
        binding.lvSerieDetailBooksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // BookBean for the data to be send to destination
                BookBean bookBean;
                try {
                    // BookBean gets data from clicked item
                    bookBean = dataBaseHelper.getBookById(((BookBean) binding.lvSerieDetailBooksList.getItemAtPosition(position)).getBook_id());
                } catch (Exception e) {
                    // id set to -1 for error handling
                    bookBean = new BookBean(-1,"error");
                }
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", bookBean.getBook_id());
                // go to BookDetailFragment with the data bundle
                findNavController(SerieDetailFragment.this).navigate(R.id.action_serieDetail_to_bookDetail, bundle);
            }
        });

        /* Author list item click */
        binding.lvSerieDetailAuthorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // AuthorBean for the data to be send to destination
                AuthorBean authorBean;
                try {
                    // AuthorBean gets data from clicked item
                    authorBean = (AuthorBean) binding.lvSerieDetailAuthorsList.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    authorBean = new AuthorBean(-1,"error","error","error");
                }
                Bundle bundle = new Bundle();
                // Data bundle storing key-value pairs
                bundle.putInt("author_id", authorBean.getAuthor_id());
                bundle.putString("author_pseudonym", authorBean.getAuthor_pseudonym());
                // go to AuthorDetailFragment with the data bundle
                findNavController(SerieDetailFragment.this).navigate(R.id.action_serieDetail_to_authorDetail, bundle);
            }
        });

        /* Editors list item click */
        binding.lvSerieDetailEditorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // EditorBean for the data to be send to destination
                EditorBean editorBean;
                try {
                    // EditorBean gets data from clicked item
                    editorBean = (EditorBean) binding.lvSerieDetailEditorsList.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    editorBean = new EditorBean(-1,"error");
                }
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("editor_id", editorBean.getEditor_id());
                bundle.putString("editor_name", editorBean.getEditor_name());
                // go to EditorDetailFragment with the data bundle
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
        // Books list adapters charger with data
        booksArrayAdapter = new BooksNumberListAdapter(getActivity(), R.layout.listview_row_2col, dataBaseHelper.getBooksListBySerieId(serieBean.getSerie_id()));
        binding.lvSerieDetailBooksList.setAdapter(booksArrayAdapter);
        // Authors list adapters charger with data
        authorsArrayAdapter = new AuthorsListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.getAuthorsListBySerieId(serieBean.getSerie_id()));
        binding.lvSerieDetailAuthorsList.setAdapter(authorsArrayAdapter);
        // Editors list adapters charger with data
        editorsArrayAdapter = new EditorsListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.getEditorsBySerieId(serieBean.getSerie_id()));
        binding.lvSerieDetailEditorsList.setAdapter(editorsArrayAdapter);
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