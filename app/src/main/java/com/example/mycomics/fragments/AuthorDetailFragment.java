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
import com.example.mycomics.adapters.SeriesNbListAdapter;
import com.example.mycomics.adapters.BooksListAdapter;
import com.example.mycomics.beans.AuthorBean;
import com.example.mycomics.beans.BookBean;
import com.example.mycomics.beans.EditorBean;
import com.example.mycomics.beans.SerieBean;
import com.example.mycomics.databinding.FragmentAuthorDetailBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupTextDialog;

public class AuthorDetailFragment extends Fragment {


    //* ----------------------------------------------------------------------------------------- */
    //* View binding declaration */
    //* ----------------------------------------------------------------------------------------- */
    FragmentAuthorDetailBinding binding;


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
    ArrayAdapter editorsArrayAdapter;


    //* ----------------------------------------------------------------------------------------- */
    //* Empty constructor, required
    //* ----------------------------------------------------------------------------------------- */
    public AuthorDetailFragment() {
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
        binding = FragmentAuthorDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();    }


    //* ----------------------------------------------------------------------------------------- */
    //* onViewCreated inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* Data sent from source fragments */
        int author_id = getArguments().getInt("author_id");

        /* Display initialization */
        AuthorBean authorBean = dataBaseHelper.getAuthorById(author_id);
        authorDetailRefreshScreen(authorBean);

        /* Series list item click */
        binding.lvAuthorDetailSeriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // SerieBean for the data to be send to destination
                SerieBean serieBean;
                try {
                    // SerieBean gets data from clicked item
                    serieBean = (SerieBean) binding.lvAuthorDetailSeriesList.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    serieBean = new SerieBean(-1,"error");
                }
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("serie_id", serieBean.getSerie_id());
                // go to SerieDetailFragment with the data bundle
                findNavController(AuthorDetailFragment.this).navigate(R.id.action_authorDetail_to_serieDetail, bundle);
            }
        });

        /* Books list item click */
        binding.lvAuthorDetailBooksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // BookBean for the data to be send to destination
                BookBean bookBean;
                try {
                    // BookBean gets data from clicked item
                    bookBean = (BookBean) binding.lvAuthorDetailBooksList.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    bookBean = new BookBean(-1,"error");
                }
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", bookBean.getBook_id());
                // go to BookDetailFragment with the data bundle
                findNavController(AuthorDetailFragment.this).navigate(R.id.action_authorDetail_to_bookDetail, bundle);
            }
        });

        /* Authors list item click */
        binding.lvAuthorDetailAuthorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // AuthorBean for the data to be send to destination
                AuthorBean authorBean;
                try {
                    // AuthorBean gets data from clicked item
                    authorBean = (AuthorBean) binding.lvAuthorDetailAuthorsList.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    authorBean = new AuthorBean(-1,"error","error","error");
                }
                Bundle bundle = new Bundle();
                // Data bundle storing key-value pairs
                bundle.putInt("author_id", authorBean.getAuthor_id());
                // go to AuthorDetailFragment with the data bundle
                findNavController(AuthorDetailFragment.this).navigate(R.id.authorDetailFragment, bundle);
            }
        });

        /* Editors list item click */
        binding.lvAuthorDetailEditorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // EditorBean for the data to be send to destination
                EditorBean editorBean;
                try {
                    // EditorBean gets data from clicked item
                    editorBean = (EditorBean) binding.lvAuthorDetailEditorsList.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    editorBean = new EditorBean(-1,"error");
                }
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("editor_id", editorBean.getEditor_id());
                bundle.putString("editor_name", editorBean.getEditor_name());
                // go to EditorDetailFragment with the data bundle
                findNavController(AuthorDetailFragment.this).navigate(R.id.action_authorDetail_to_editorDetail, bundle);
            }
        });

        /* Delete Author click */
        binding.btnAuthorDetailDeleteAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Popup for deleting an Author
                PopupTextDialog popupTextDialog = new PopupTextDialog(getActivity());
                popupTextDialog.setTitle(getString(R.string.AuthorDeleteConfirmPseudonym) + "\n\""
                        + dataBaseHelper.getAuthorById(author_id).getAuthor_pseudonym() + "\"");
                popupTextDialog.setHint(getString(R.string.AuthorDetailPseudonym));
                popupTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Click event on confirm button
                popupTextDialog.getBtnPopupConfirm().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String authorConfirmDeletePseudonym = "";
                        try {
                            authorConfirmDeletePseudonym = popupTextDialog.getEtPopupText().getText().toString();
                        } catch (Exception e) {
                            // do nothing
                        }
                        popupTextDialog.dismiss(); // To close Popup
                        if (!dataBaseHelper.getAuthorById(author_id).getAuthor_pseudonym().equals(authorConfirmDeletePseudonym)) {
                            // Name does not match
                            Toast.makeText(AuthorDetailFragment.super.getActivity(), getString(R.string.AuthorPseudonymDoesntMatch), Toast.LENGTH_LONG).show();
                        } else {
                            // Name does match
                            // The serie is deleted from the Database (constraints too)
                            boolean successUpdate = dataBaseHelper.deleteAuthor(dataBaseHelper, author_id);
                            // go to BooksFragment since the BookDetail doesn't exist anymore
                            findNavController(AuthorDetailFragment.this).navigate(R.id.authorsFragment);
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
                            String authorConfirmDeletePseudonym = "";
                            try {
                                authorConfirmDeletePseudonym = popupTextDialog.getEtPopupText().getText().toString();
                            } catch (Exception e) {
                                // do nothing
                            }
                            popupTextDialog.dismiss(); // To close Popup
                            if (!dataBaseHelper.getAuthorById(author_id).getAuthor_pseudonym().equals(authorConfirmDeletePseudonym)) {
                                // Name does not match
                                Toast.makeText(AuthorDetailFragment.super.getActivity(), getString(R.string.AuthorPseudonymDoesntMatch), Toast.LENGTH_LONG).show();
                            } else {
                                // Name does match
                                // The serie is deleted from the Database (constraints too)
                                boolean successUpdate = dataBaseHelper.deleteAuthor(dataBaseHelper, author_id);
                                // go to BooksFragment since the BookDetail doesn't exist anymore
                                findNavController(AuthorDetailFragment.this).navigate(R.id.authorsFragment);
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
                authorDetailRefreshScreen(authorBean); // To refresh display
            }
        });

        /* Click event on Save Book button */
        binding.btnAuthorDetailSaveAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAuthor(author_id);
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
    private void authorDetailRefreshScreen(AuthorBean authorBean){
        // Author Pseudonym
        binding.etAuthorDetailAuthorPseudonym.setText(authorBean.getAuthor_pseudonym());
        // Author Last Name
        binding.etAuthorDetailAuthorLastName.setText(authorBean.getAuthor_last_name());
        // Author First Name
        binding.etAuthorDetailAuthorFirstName.setText(authorBean.getAuthor_first_name());
        // Series list adapters charger with data
        seriesArrayAdapter = new SeriesNbListAdapter(getActivity(), R.layout.listview_row_2col_reverse, dataBaseHelper.getSeriesListByAuthorId(authorBean.getAuthor_id()));
        binding.lvAuthorDetailSeriesList.setAdapter(seriesArrayAdapter);
        // Books list adapters charger with data
        booksArrayAdapter = new BooksListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.getBooksListByAuthorIdNoSerie(authorBean.getAuthor_id()));
        binding.lvAuthorDetailBooksList.setAdapter(booksArrayAdapter);
        // Authors list adapters charger with data
        authorsArrayAdapter = new AuthorsListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.getAuthorsTeam(authorBean.getAuthor_id()));
        binding.lvAuthorDetailAuthorsList.setAdapter(authorsArrayAdapter);
        // Editors list adapters charger with data
        editorsArrayAdapter = new EditorsListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.getEditorsByAuthorId(authorBean.getAuthor_id()));
        binding.lvAuthorDetailEditorsList.setAdapter(editorsArrayAdapter);
    }

    //* ----------------------------------------------------------------------------------------- */
    //* Saving the Author
    //* ----------------------------------------------------------------------------------------- */
    private void saveAuthor(int author_id){
        // AuthorBean from fragment items data
        AuthorBean authorBean = new AuthorBean(
                author_id,
                binding.etAuthorDetailAuthorPseudonym.getText().toString(),
                binding.etAuthorDetailAuthorLastName.getText().toString(),
                binding.etAuthorDetailAuthorFirstName.getText().toString());
        // Database update of the Author
        boolean updateOk = dataBaseHelper.updateAuthor(dataBaseHelper, authorBean);
        if (updateOk) {
            Toast.makeText(getActivity(), getString(R.string.SerieUpdateSuccess), Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(getActivity(), getString(R.string.SerieUpdateError), Toast.LENGTH_SHORT);
        }
    }
}

