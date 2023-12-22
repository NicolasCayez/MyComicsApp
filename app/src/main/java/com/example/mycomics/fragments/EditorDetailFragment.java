package com.example.mycomics.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.mycomics.R;
import com.example.mycomics.adapters.AuthorsAdapter;
import com.example.mycomics.adapters.BooksBookNbAdapter;
import com.example.mycomics.adapters.SeriesNbAdapter;
import com.example.mycomics.beans.AuthorBean;
import com.example.mycomics.beans.BookBean;
import com.example.mycomics.beans.EditorBean;
import com.example.mycomics.beans.SerieBean;
import com.example.mycomics.databinding.FragmentEditorDetailBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupTextDialog;

import java.util.ArrayList;

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
    //* Adapters handling RecycleViews data display
    //* ----------------------------------------------------------------------------------------- */
    SeriesNbAdapter seriesAdapter;
    BooksBookNbAdapter booksAdapter;
    AuthorsAdapter authorsAdapter;


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
        editorDetailInitialize(editorBean);

        /* Enter key after modifying the editor name */
        binding.etEditorDetailEditorName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // if "ENTER" key is pressed
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    binding.btnEditorDetailSaveEditor.performClick();
                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    return true; // inherited, necessary
                }
                return false; // inherited, necessary
            }
        });

        /* Click on Serie in the list (RecyclerView) */
        seriesAdapter.setOnClickListener(new SeriesNbAdapter.OnClickListener() {
            @Override
            public void onClick(int position, SerieBean serieBean) {
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("serie_id", serieBean.getSerie_id());;
                // go to AuthorDetailFragment with the data bundle
                findNavController(EditorDetailFragment.this).navigate(R.id.action_editorDetail_to_serieDetail, bundle);
            }
        });

        /* Click on Book in the list (RecyclerView) */
        booksAdapter.setOnClickListener(new BooksBookNbAdapter.OnClickListener() {
            @Override
            public void onClick(int position, BookBean bookBean) {
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", bookBean.getBook_id());;
                // go to AuthorDetailFragment with the data bundle
                findNavController(EditorDetailFragment.this).navigate(R.id.action_editorDetail_to_bookDetail, bundle);
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
                            popupTextDialog.getBtnPopupConfirm().performClick();
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
                editorDetailReload(editor_id); // To refresh display
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
    //* Display initialization and refresh methods
    //* ----------------------------------------------------------------------------------------- */
    private void editorDetailInitialize(EditorBean editorBean){

        // Editor Name
        binding.etEditorDetailEditorName.setText(editorBean.getEditor_name());

        /* Series list adapters charged with data */
        // Creating the list to display
        ArrayList<SerieBean> SeriesList = dataBaseHelper.getSeriesListByEditorId(editorBean.getEditor_id());
        // The adapter gets the list and the string value "books" needed for translations
        seriesAdapter = new SeriesNbAdapter(SeriesList, getString(R.string.Books));
        // the adapter and the layout are defined for the RecyclerView
        binding.rvEditorDetailSeriesList.setAdapter(seriesAdapter);
        binding.rvEditorDetailSeriesList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        seriesAdapter.submitList(SeriesList);

        /* Books list adapters charged with data */
        // Creating the list to display
        ArrayList<BookBean> BooksList = dataBaseHelper.getBooksListByEditorIdNoSerie(editorBean.getEditor_id());
        // The adapter gets the list and the string value "books" needed for translations
        booksAdapter = new BooksBookNbAdapter(BooksList, getString(R.string.BookNumber));
        // the adapter and the layout are defined for the RecyclerView
        binding.rvEditorDetailBooksList.setAdapter(booksAdapter);
        binding.rvEditorDetailBooksList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        booksAdapter.submitList(BooksList);

        /* Authors list adapters charged with data */
        // Creating the list to display
        ArrayList<AuthorBean> AuthorsList = dataBaseHelper.getAuthorsListByEditorId(editorBean.getEditor_id());
        // The adapter gets the list and the string value "books" needed for translations
        authorsAdapter = new AuthorsAdapter(AuthorsList);
        // the adapter and the layout are defined for the RecyclerView
        binding.rvEditorDetailAuthorsList.setAdapter(authorsAdapter);
        binding.rvEditorDetailAuthorsList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        authorsAdapter.submitList(AuthorsList);
    }
    private void editorDetailReload(Integer editor_id) {
        // Data bundle storing key-value pairs
        Bundle bundle = new Bundle();
        bundle.putInt("editor_id", editor_id);
        // go to AuthorDetailFragment with the data bundle
        findNavController(EditorDetailFragment.this).navigate(R.id.editorDetailFragment, bundle);
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