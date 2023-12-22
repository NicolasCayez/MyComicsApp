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
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.mycomics.R;
import com.example.mycomics.adapters.BookSerieAdapter;
import com.example.mycomics.beans.BookBean;
import com.example.mycomics.beans.BookSerieBean;
import com.example.mycomics.databinding.FragmentBooksBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupTextDialog;

import java.util.ArrayList;

public class BooksFragment extends Fragment {


    //* ----------------------------------------------------------------------------------------- */
    //* View binding declaration */
    //* ----------------------------------------------------------------------------------------- */
    FragmentBooksBinding binding;


    //* ----------------------------------------------------------------------------------------- */
    //* Database handler initialization
    //* ----------------------------------------------------------------------------------------- */
    DataBaseHelper dataBaseHelper;


    //* ----------------------------------------------------------------------------------------- */
    //* Filter needed for the list
    //* ----------------------------------------------------------------------------------------- */
    String filter = "";


    //* ----------------------------------------------------------------------------------------- */
    //* Adapters handling RecycleViews data display
    //* ----------------------------------------------------------------------------------------- */
    BookSerieAdapter bookSerieAdapter;


    //* ----------------------------------------------------------------------------------------- */
    //* Empty constructor, required
    //* ----------------------------------------------------------------------------------------- */
    public BooksFragment() {
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
        binding = FragmentBooksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    //* ----------------------------------------------------------------------------------------- */
    //* onViewCreated inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* Data sent from this fragment when filtering list */
        try {
            filter = getArguments().getString("filter");
        } catch (Exception e) {
            filter = "";
        }

        /* Display initialization */
        booksInitialize(filter);

        /* Search bar */
        // Search Hint initialization
//        binding.sbSearch.svSearch.setQueryHint(getString(R.string.SearchHintFilterOrSearch));
        binding.sbSearch.svSearch.setQueryHint(getString(R.string.SearchHintFilterOrSearch));
        // Click event on Search button
        binding.sbSearch.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Data bundle storing search string
                Bundle bundle = new Bundle();
                bundle.putString("filter", binding.sbSearch.svSearch.getQuery().toString());
                // Go to SearchResultFragment with the data bundle
                findNavController(BooksFragment.this).navigate(R.id.searchResultFragment, bundle);
            }
        });
        // Search bar text submit listener, to filter Books list
        binding.sbSearch.svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                binding.sbSearch.btnSearch.performClick();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        binding.sbSearch.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Data bundle storing search string
                Bundle bundle = new Bundle();
                bundle.putString("filter", binding.sbSearch.svSearch.getQuery().toString());
                // Go to SearchResultFragment with the data bundle
                findNavController(BooksFragment.this).navigate(R.id.booksFragment, bundle);
            }
        });
        /* Add Book button handler and Book creation popup */
        // Click event on add button
        binding.btnBooksAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Popup for Book creation with texts and see-through background
                PopupTextDialog popupTextDialog = new PopupTextDialog(getActivity());
                popupTextDialog.setTitle(getString(R.string.BookPopupAddTitle));
                popupTextDialog.setHint(getString(R.string.BookPopupAddTitle));
                popupTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Click event on confirm button
                popupTextDialog.getBtnPopupConfirm().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // BookBean for the data to be stored in the database
                        BookBean bookBean;
                        try {
                            // fetching text from the EditText
                            bookBean = new BookBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                        } catch (Exception e) {
                            // error toast message
                            Toast.makeText(getActivity(), getString(R.string.BookCreationError), Toast.LENGTH_SHORT).show();
                            // id set to -1 for error handling
                            bookBean = new BookBean(-1, "error" );
                        }
                        // Checking if a duplicate with the same name already exists in the database
                        if (dataBaseHelper.checkBookDuplicate(bookBean.getBook_title())) {
                            // duplicate, error toast message
                            Toast.makeText(BooksFragment.super.getContext(), getString(R.string.BookDuplicateError), Toast.LENGTH_LONG).show();
                        } else {
                            // Database handler called with the insertion methods (Tables BOOKS and DETAINING)
                            boolean successInsertBooks = dataBaseHelper.insertIntoBooks(bookBean);
                            boolean successInsertDetaining = dataBaseHelper.insertIntoDetaining(dataBaseHelper.getBookLatest(bookBean));
                            // Success toast message
                            Toast.makeText(getActivity(), getString(R.string.BookCreationSuccess), Toast.LENGTH_SHORT).show();
                        }
                        popupTextDialog.dismiss(); // To close popup
                        booksReload(filter); // To reload display
                    }
                });
                // Key event, same behaviour as confirm button
                popupTextDialog.getEtPopupText().setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // if "ENTER" key is pressed
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            // Hide the keyboard
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                        popupTextDialog.dismiss(); // To close popup
                        booksReload(filter); // To reload display
                    }
                });
                popupTextDialog.build(); // To build the popup

            }
        });

        /* Click on Book in the list (RecyclerView) */
        bookSerieAdapter.setOnClickListener(new BookSerieAdapter.OnClickListener() {
            @Override
            public void onClick(int position, BookSerieBean bookSerieBean) {
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", bookSerieBean.getBook_id());
                // go to AuthorDetailFragment with the data bundle
                findNavController(BooksFragment.this).navigate(R.id.action_books_to_bookDetail, bundle);
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
    private void booksInitialize(String filter){
        // Creating the list to display
        ArrayList<BookSerieBean> booksList = dataBaseHelper.getBooksAndBooksSeriesList();
        // If the search bar contains a filter
        if (filter.length() > 0) {
            booksList = dataBaseHelper.getBooksAndSeriesListByFilter(filter.replace("'",""));
        }
        // The adapter gets the list and the string value "books" needed for translations
        bookSerieAdapter = new BookSerieAdapter(booksList, getString(R.string.BookNumber));
        // the adapter and the layout are defined for the RecyclerView
        binding.rvBooksBooksList.setAdapter(bookSerieAdapter);
        binding.rvBooksBooksList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        bookSerieAdapter.submitList(booksList);
    }
    private void booksReload(String filter) {
        // Data bundle storing key-value pairs
        Bundle bundle = new Bundle();
        bundle.putString("filter", filter);
        // go to AuthorDetailFragment with the data bundle
        findNavController(BooksFragment.this).navigate(R.id.booksFragment, bundle);
    }
}