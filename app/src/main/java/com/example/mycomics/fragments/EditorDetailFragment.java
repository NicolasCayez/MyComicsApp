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
import com.example.mycomics.adapters.SeriesNbListAdapter;
import com.example.mycomics.adapters.BooksNumberListAdapter;
import com.example.mycomics.beans.AuthorBean;
import com.example.mycomics.beans.BookBean;
import com.example.mycomics.beans.EditorBean;
import com.example.mycomics.beans.SerieBean;
import com.example.mycomics.databinding.FragmentEditorDetailBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupTextDialog;

public class EditorDetailFragment extends Fragment {


    //* ----------------------------------------------------------------------------------------- */
    //* View binding declaration */
    //* ----------------------------------------------------------------------------------------- */
    FragmentEditorDetailBinding binding;


    //* ----------------------------------------------------------------------------------------- */
    //* Database handler initialization
    //* ----------------------------------------------------------------------------------------- */
    DataBaseHelper dataBaseHelper;


    //* ----------------------------------------------------------------------------------------- */
    //* Adapters handling listViews data display
    //* ----------------------------------------------------------------------------------------- */
    ArrayAdapter booksArrayAdapter;
    ArrayAdapter seriesArrayAdapter;
    ArrayAdapter authorsArrayAdapter;


    //* ----------------------------------------------------------------------------------------- */
    //* Empty constructor, required
    //* ----------------------------------------------------------------------------------------- */
    public EditorDetailFragment() {
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
        binding = FragmentEditorDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();    }


    //* ----------------------------------------------------------------------------------------- */
    //* onViewCreated inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* Data sent from source fragments */
        int editor_id = getArguments().getInt("editor_id");

        /* Display initialization */
        EditorBean editorBean = dataBaseHelper.getEditorById(editor_id);
        editorDetailRefreshScreen(editorBean);

        /* Series list item click */
        binding.lvEditorDetailSeriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // SerieBean for the data to be send to destination
                SerieBean serieBean;
                try {
                    // SerieBean gets data from clicked item
                    serieBean = (SerieBean) binding.lvEditorDetailSeriesList.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    serieBean = new SerieBean(-1,"error");
                }
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("serie_id", serieBean.getSerie_id());
                // go to SerieDetailFragment with the data bundle
                findNavController(EditorDetailFragment.this).navigate(R.id.action_editorDetail_to_serieDetail, bundle);
            }
        });

        /* Books list item click */
        binding.lvEditorDetailBooksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // BookBean for the data to be send to destination
                BookBean bookBean;
                try {
                    // BookBean gets data from clicked item
                    bookBean = (BookBean) binding.lvEditorDetailBooksList.getItemAtPosition(position);
//                    bookBean = dataBaseHelper.getBookById(((BookBean) binding.lvEditorDetailBooksList.getItemAtPosition(position)).getBook_id());
                } catch (Exception e) {
                    // id set to -1 for error handling
                    bookBean = new BookBean(-1,"error");
                }
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", bookBean.getBook_id());
                // go to BookDetailFragment with the data bundle
                findNavController(EditorDetailFragment.this).navigate(R.id.action_editorDetail_to_bookDetail, bundle);
            }
        });

        /* Authors list item click */
        binding.lvEditorDetailAuthorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // AuthorBean for the data to be send to destination
                AuthorBean authorBean;
                try {
                    // AuthorBean gets data from clicked item
                    authorBean = (AuthorBean) binding.lvEditorDetailAuthorsList.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    authorBean = new AuthorBean(-1,"error","error","error");
                }
                Bundle bundle = new Bundle();
                // Data bundle storing key-value pairs
                bundle.putInt("author_id", authorBean.getAuthor_id());
                // go to AuthorDetailFragment with the data bundle
                findNavController(EditorDetailFragment.this).navigate(R.id.action_editorDetail_to_authorDetail, bundle);
            }
        });

        /* Delete Serie click */
        binding.btnEditorDetailDeleteEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Popup for deleting an Editor
                PopupTextDialog popupTextDialog = new PopupTextDialog(getActivity());
                popupTextDialog.setTitle(getString(R.string.EditorDeleteConfirmName) + "\n\""
                        + dataBaseHelper.getEditorById(editor_id).getEditor_name() + "\"");
                popupTextDialog.setHint(getString(R.string.EditorDetailName));
                popupTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Click event on confirm button
                popupTextDialog.getBtnPopupConfirm().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String editorConfirmDeleteName = "";
                        try {
                            editorConfirmDeleteName = popupTextDialog.getEtPopupText().getText().toString();
                        } catch (Exception e) {
                            // do nothing
                        }
                        popupTextDialog.dismiss(); // To close Popup
                        if (!dataBaseHelper.getEditorById(editor_id).getEditor_name().equals(editorConfirmDeleteName)) {
                            // Name does not match
                            Toast.makeText(EditorDetailFragment.super.getActivity(), getString(R.string.EditorNameDoesntMatch), Toast.LENGTH_LONG).show();
                        } else {
                            // Name does match
                            // The Editor is deleted from the Database (constraints too)
                            boolean successUpdate = dataBaseHelper.deleteEditor(dataBaseHelper, editor_id);
                            // go to BooksFragment since the BookDetail doesn't exist anymore
                            findNavController(EditorDetailFragment.this).navigate(R.id.editorsFragment);
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
                            String editorConfirmDeleteName = "";
                            try {
                                editorConfirmDeleteName = popupTextDialog.getEtPopupText().getText().toString();
                            } catch (Exception e) {
                                // do nothing
                            }
                            popupTextDialog.dismiss(); // To close Popup
                            if (!dataBaseHelper.getEditorById(editor_id).getEditor_name().equals(editorConfirmDeleteName)) {
                                // Name does not match
                                Toast.makeText(EditorDetailFragment.super.getActivity(), getString(R.string.EditorNameDoesntMatch), Toast.LENGTH_LONG).show();
                            } else {
                                // Name does match
                                // The Editor is deleted from the Database (constraints too)
                                boolean successUpdate = dataBaseHelper.deleteEditor(dataBaseHelper, editor_id);
                                // go to BooksFragment since the BookDetail doesn't exist anymore
                                findNavController(EditorDetailFragment.this).navigate(R.id.editorsFragment);
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
                editorDetailRefreshScreen(editorBean); // To refresh display
            }
        });

        /* Click event on Save Book button */
        binding.btnEditorDetailSaveEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEditor(editor_id);
            }
        });
    }


    //* ----------------------------------------------------------------------------------------- */
    //* onDestroy inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }


    //* ----------------------------------------------------------------------------------------- */
    //* Display initialization and refresh method
    //* ----------------------------------------------------------------------------------------- */
    private void editorDetailRefreshScreen(EditorBean editorBean){
        // Editor Name
        binding.etEditorDetailEditorName.setText(editorBean.getEditor_name());
        // Series list adapters charger with data
        seriesArrayAdapter = new SeriesNbListAdapter(getActivity(), R.layout.listview_row_2col_reverse, dataBaseHelper.getSeriesListByEditorId(editorBean.getEditor_id()));
        binding.lvEditorDetailSeriesList.setAdapter(seriesArrayAdapter);
        // Books list adapters charger with data
        booksArrayAdapter = new BooksNumberListAdapter(getActivity(), R.layout.listview_row_2col, dataBaseHelper.getBooksListByEditorIdNoSerie(editorBean.getEditor_id()));
        binding.lvEditorDetailBooksList.setAdapter(booksArrayAdapter);
        // Authors list adapters charger with data
        authorsArrayAdapter = new AuthorsListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.getAuthorsListByEditorId(editorBean.getEditor_id()));
        binding.lvEditorDetailAuthorsList.setAdapter(authorsArrayAdapter);
    }

    //* ----------------------------------------------------------------------------------------- */
    //* Saving the Editor
    //* ----------------------------------------------------------------------------------------- */
    private void saveEditor(int editor_id){
        // EditorBean from fragment items data
        EditorBean editorBean = new EditorBean(
                editor_id,
                binding.etEditorDetailEditorName.getText().toString());
        // Database update of the Editor
        boolean updateOk = dataBaseHelper.updateEditor(dataBaseHelper, editorBean);
        if (updateOk) {
            Toast.makeText(getActivity(), getString(R.string.EditorUpdateSuccess), Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(getActivity(), getString(R.string.EditorUpdateError), Toast.LENGTH_SHORT);
        }
    }
}