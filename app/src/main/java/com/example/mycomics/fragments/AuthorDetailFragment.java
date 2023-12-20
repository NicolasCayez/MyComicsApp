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
import com.example.mycomics.adapters.SeriesNbAdapter;
import com.example.mycomics.beans.AuthorBean;
import com.example.mycomics.beans.BookBean;
import com.example.mycomics.beans.EditorBean;
import com.example.mycomics.beans.SerieBean;
import com.example.mycomics.databinding.FragmentAuthorDetailBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupTextDialog;

import java.util.ArrayList;

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
    //* Adapters handling RecycleViews data display
    //* ----------------------------------------------------------------------------------------- */
    SeriesNbAdapter seriesAdapter;
    BooksBookNbAdapter booksAdapter;
    AuthorsAdapter authorsAdapter;
    EditorsAdapter editorsAdapter;


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

        /* Click on Serie in the list (RecyclerView) */
        seriesAdapter.setOnClickListener(new SeriesNbAdapter.OnClickListener() {
            @Override
            public void onClick(int position, SerieBean serieBean) {
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("serie_id", serieBean.getSerie_id());;
                // go to AuthorDetailFragment with the data bundle
                findNavController(AuthorDetailFragment.this).navigate(R.id.action_authorDetail_to_serieDetail, bundle);
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
                findNavController(AuthorDetailFragment.this).navigate(R.id.action_authorDetail_to_bookDetail, bundle);
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
                findNavController(AuthorDetailFragment.this).navigate(R.id.authorDetailFragment, bundle);
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

        /* Series list adapters charged with data */
        // Creating the list to display
        ArrayList<SerieBean> SeriesList = dataBaseHelper.getSeriesListByAuthorId(authorBean.getAuthor_id());
        // The adapter gets the list and the string value "books" needed for translations
        seriesAdapter = new SeriesNbAdapter(SeriesList, getString(R.string.Books));
        // the adapter and the layout are defined for the RecyclerView
        binding.rvAuthorDetailSeriesList.setAdapter(seriesAdapter);
        binding.rvAuthorDetailSeriesList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        seriesAdapter.submitList(SeriesList);

        /* Books list adapters charged with data */
        // Creating the list to display
        ArrayList<BookBean> BooksList = dataBaseHelper.getBooksListByAuthorIdNoSerie(authorBean.getAuthor_id());
        // The adapter gets the list and the string value "books" needed for translations
        booksAdapter = new BooksBookNbAdapter(BooksList, getString(R.string.BookNumber));
        // the adapter and the layout are defined for the RecyclerView
        binding.rvAuthorDetailBooksList.setAdapter(booksAdapter);
        binding.rvAuthorDetailBooksList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        booksAdapter.submitList(BooksList);

        /* Authors list adapters charged with data */
        // Creating the list to display
        ArrayList<AuthorBean> AuthorsList = dataBaseHelper.getAuthorsTeam(authorBean.getAuthor_id());
        // The adapter gets the list and the string value "books" needed for translations
        authorsAdapter = new AuthorsAdapter(AuthorsList);
        // the adapter and the layout are defined for the RecyclerView
        binding.rvAuthorDetailAuthorsList.setAdapter(authorsAdapter);
        binding.rvAuthorDetailAuthorsList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        authorsAdapter.submitList(AuthorsList);

        /* Editors list adapters charger with data */
        // Creating the list to display
        ArrayList<EditorBean> EditorsList = dataBaseHelper.getEditorsByAuthorId(authorBean.getAuthor_id());
        // The adapter gets the list and the string value "books" needed for translations
        editorsAdapter = new EditorsAdapter(EditorsList);
        // the adapter and the layout are defined for the RecyclerView
        binding.rvAuthorDetailEditorsList.setAdapter(editorsAdapter);
        binding.rvAuthorDetailEditorsList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        editorsAdapter.submitList(EditorsList);
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

