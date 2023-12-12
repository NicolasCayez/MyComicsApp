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
import com.example.mycomics.adapters.BooksSerieListAdapter;
import com.example.mycomics.beans.BookBean;
import com.example.mycomics.beans.BookSerieBean;
import com.example.mycomics.databinding.FragmentBooksBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupTextDialog;

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
    //* Adapters handling listViews data display
    //* ----------------------------------------------------------------------------------------- */
    ArrayAdapter tomesArrayAdapter;
    ArrayAdapter bookSerieArrayAdapter;


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

        /* Display initialization */
        BooksRefreshScreen();
        /* Search bar */
        // Search Hint initialization
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
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                BooksRefreshScreen(); // To refresh display
                return false;
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
                        BooksRefreshScreen(); // To refresh display
                    }
                });
                // Key event, same behaviour as confirm button
                popupTextDialog.getEtPopupText().setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // if "ENTER" key is pressed
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            BookBean bookBean;
                            try {
                                bookBean = new BookBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), getString(R.string.BookCreationError), Toast.LENGTH_SHORT).show();
                                bookBean = new BookBean(-1, "error" );
                            }
                            if (dataBaseHelper.checkBookDuplicate(bookBean.getBook_title())) {
                                Toast.makeText(BooksFragment.super.getContext(), getString(R.string.BookDuplicateError), Toast.LENGTH_LONG).show();
                            } else {
                                boolean successInsertBooks = dataBaseHelper.insertIntoBooks(bookBean);
                                boolean successInsertDetaining = dataBaseHelper.insertIntoDetaining(dataBaseHelper.getBookLatest(bookBean));
                                Toast.makeText(getActivity(), getString(R.string.BookCreationSuccess), Toast.LENGTH_SHORT).show();
                            }
                            popupTextDialog.dismiss();
                            BooksRefreshScreen();
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
                    }
                });
                popupTextDialog.build(); // To build the popup
                BooksRefreshScreen(); // To refresh display
            }
        });

        /* Books list item click */
        // Click item
        binding.lvBooksBooksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // BookBean for the data to be send to destination
                BookBean bookBean;
                try {
                    // BookBean gets data from clicked item
                    bookBean = (BookSerieBean) binding.lvBooksBooksList.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    bookBean = new BookSerieBean(-1,"error");
                }
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", bookBean.getBook_id());
                bundle.putString("book_title", bookBean.getBook_title());
                // go to BookDetailFragment with the data bundle
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
    private void BooksRefreshScreen(){
        if (binding.sbSearch.svSearch.getQuery().toString().length() > 0) {
            // If the search bar isn't empty
            // list filtered
            bookSerieArrayAdapter = new BooksSerieListAdapter(getActivity() , R.layout.listview_row_3col, dataBaseHelper.getBooksAndSeriesListByFilter(binding.sbSearch.svSearch.getQuery().toString()));
        } else {
            // list without filter
            bookSerieArrayAdapter = new BooksSerieListAdapter(getActivity() , R.layout.listview_row_3col, dataBaseHelper.getBooksAndBooksSeriesList());
        }
        // list adapter charged with data
        binding.lvBooksBooksList.setAdapter(bookSerieArrayAdapter);
    }
}