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
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.mycomics.R;
import com.example.mycomics.adapters.AuthorsNbAdapter;
import com.example.mycomics.beans.AuthorBean;
import com.example.mycomics.databinding.FragmentAuthorsBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupTextDialog;

import java.util.ArrayList;

public class AuthorsFragment extends Fragment {


    //* ----------------------------------------------------------------------------------------- */
    //* View binding declaration */
    //* ----------------------------------------------------------------------------------------- */
    FragmentAuthorsBinding binding;


    //* ----------------------------------------------------------------------------------------- */
    //* Database handler initialization
    //* ----------------------------------------------------------------------------------------- */
    DataBaseHelper dataBaseHelper;


    //* ----------------------------------------------------------------------------------------- */
    //* Adapters handling RecycleViews data display
    //* ----------------------------------------------------------------------------------------- */
    AuthorsNbAdapter authorsAdapter;


    //* ----------------------------------------------------------------------------------------- */
    //* Empty constructor, required
    //* ----------------------------------------------------------------------------------------- */
    public AuthorsFragment() {
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
        binding = FragmentAuthorsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    //* ----------------------------------------------------------------------------------------- */
    //* onViewCreated inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* Display initialization */
        authorsRefreshScreen();
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
                findNavController(AuthorsFragment.this).navigate(R.id.searchResultFragment, bundle);
            }
        });
        // Search bar text submit listener, to filter Authors list
        binding.sbSearch.svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                authorsRefreshScreen(); // To refresh display
                return false;
            }
        });

        /* Add Author button handler and Author creation popup */
        // Click event on add button
        binding.btnAuthorsAddAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Popup for Author creation with texts and see-through background
                PopupTextDialog popupTextDialog = new PopupTextDialog(getActivity());
                popupTextDialog.setTitle(getString(R.string.AuthorPopupAddTitle));
                popupTextDialog.setHint(getString(R.string.AuthorPopupAddHint));
                popupTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Click event on confirm button
                popupTextDialog.getBtnPopupConfirm().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // AuthorBean for the data to be stored in the database
                        AuthorBean authorBean;
                        try {
                            // fetching text from the EditText
                            authorBean = new AuthorBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                        } catch (Exception e) {
                            // error toast message
                            Toast.makeText(getActivity(), getString(R.string.AuthorCreationError), Toast.LENGTH_SHORT).show();
                            // id set to -1 for error handling
                            authorBean = new AuthorBean(-1, "error" );
                        }
                        // Checking if a duplicate with the same pseudonym already exists in the database
                        if(dataBaseHelper.checkAuthorDuplicate(authorBean.getAuthor_pseudonym())){
                            // duplicate, error toast message
                            Toast.makeText(getActivity(), getString(R.string.AuthorDuplicateError), Toast.LENGTH_LONG).show();
                        } else {
                            // Database handler called with the insertion method
                            boolean success = dataBaseHelper.insertIntoAuthors(authorBean);
                            // Success toast message
                            Toast.makeText(getActivity(), getString(R.string.AuthorCreationSuccess), Toast.LENGTH_SHORT).show();
                        }
                        popupTextDialog.dismiss(); // To close popup
                        authorsRefreshScreen(); // To refresh display
                    }
                });
                // Key event, same behaviour as confirm button
                popupTextDialog.getEtPopupText().setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // if "ENTER" key is pressed
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            AuthorBean authorBean;
                            try {
                                authorBean = new AuthorBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), getString(R.string.AuthorCreationError), Toast.LENGTH_SHORT).show();
                                authorBean = new AuthorBean(-1, "error" );
                            }
                            if(dataBaseHelper.checkAuthorDuplicate(authorBean.getAuthor_pseudonym())){
                                Toast.makeText(getActivity(), getString(R.string.AuthorDuplicateError), Toast.LENGTH_LONG).show();
                            } else {
                                boolean success = dataBaseHelper.insertIntoAuthors(authorBean);
                                Toast.makeText(getActivity(), getString(R.string.AuthorCreationSuccess), Toast.LENGTH_SHORT).show();
                            }
                            popupTextDialog.dismiss();
                            authorsRefreshScreen();
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
                authorsRefreshScreen(); // To refresh display
            }
        });

        /* Click on Author in the list (RecyclerView) */
        authorsAdapter.setOnClickListener(new AuthorsNbAdapter.OnClickListener() {
            @Override
            public void onClick(int position, AuthorBean authorBean) {
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("author_id", authorBean.getAuthor_id());;
                // go to AuthorDetailFragment with the data bundle
                findNavController(AuthorsFragment.this).navigate(R.id.action_authors_to_authorDetail, bundle);
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
    private void authorsRefreshScreen(){
        // Creating the list to display
        ArrayList<AuthorBean> AuthorsList = dataBaseHelper.getAuthorsList();
        // If the search bar contains a filter
        if (binding.sbSearch.svSearch.getQuery().toString().length() > 0) {
            AuthorsList = dataBaseHelper.getAuthorsListByFilter(binding.sbSearch.svSearch.getQuery().toString());
        }
        // The adapter gets the list and the string value "books" needed for translations
        authorsAdapter = new AuthorsNbAdapter(AuthorsList, getString(R.string.Books));
        // the adapter and the layout are defined for the RecyclerView
        binding.rvAuthorsAuthorsList.setAdapter(authorsAdapter);
        binding.rvAuthorsAuthorsList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        authorsAdapter.submitList(AuthorsList);
    }
}