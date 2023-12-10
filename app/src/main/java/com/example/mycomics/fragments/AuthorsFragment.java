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
import com.example.mycomics.adapters.AuthorsNbListAdapter;
import com.example.mycomics.beans.AuthorBean;
import com.example.mycomics.databinding.FragmentAuthorsBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupTextDialog;

public class AuthorsFragment extends Fragment {


    /** ----------------------------------------------------------------------------------------- */
    /** View binding declaration */
    /** ----------------------------------------------------------------------------------------- */
    FragmentAuthorsBinding binding;


    /** ----------------------------------------------------------------------------------------- */
    /** Database handler initialization
    /** ----------------------------------------------------------------------------------------- */
    DataBaseHelper dataBaseHelper;


    /** ----------------------------------------------------------------------------------------- */
    /** Adapters handling listViews data display
    /** ----------------------------------------------------------------------------------------- */
    ArrayAdapter authorsArrayAdapter;


    /** ----------------------------------------------------------------------------------------- */
    /** Empty constructor, required
    /** ----------------------------------------------------------------------------------------- */
    public AuthorsFragment() {
        // Required empty public constructor
    }


    /** ----------------------------------------------------------------------------------------- */
    /** onCreate inherited Method override
    /** ----------------------------------------------------------------------------------------- */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Database handler initialization */
        dataBaseHelper = new DataBaseHelper(getActivity());
    }


    /** ----------------------------------------------------------------------------------------- */
    /** onCreateView inherited Method override
    /** ----------------------------------------------------------------------------------------- */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAuthorsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    /** ----------------------------------------------------------------------------------------- */
    /** onViewCreated inherited Method override
    /** ----------------------------------------------------------------------------------------- */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /** Display initialization */
        AuthorsDisplayRefresh();

        /** Search bar */
        // Search Hint initialization
        binding.sbSearch.svSearch.setQueryHint(getString(R.string.SearchHintFilterOrSearch));
        // Click event on Search button
        binding.sbSearch.btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Data bundle storing search string
                Bundle bundle = new Bundle();
                bundle.putString("filter", binding.sbSearch.svSearch.getQuery().toString());
                // Go to SearchResultFragment with the data bundle
                findNavController(AuthorsFragment.this).navigate(R.id.searchResultFragment, bundle);
            }
        });
        // Text submit listener, filters authors list
        binding.sbSearch.svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                AuthorsDisplayRefresh();
                return false;
            }
        });

        /** Add Author button handler and Author creation popup */
        // Click event on add button
        binding.btnAuteursAddAuteur.setOnClickListener(new View.OnClickListener() {
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
                        // Initialization AuthorBean for the data to be stored in the database
                        AuthorBean authorBean;
                        try {
                            // fetching text from the EditText
                            authorBean = new AuthorBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                        } catch (Exception e) {
                            // error toast message
                            Toast.makeText(getActivity(), "Erreur création auteur", Toast.LENGTH_SHORT).show(); /*TODO strings et traduction */
                            // id set to -1 for error handling
                            authorBean = new AuthorBean(-1, "error" );
                        }
                        // Checking if a duplicate with the same pseudonym already exists in the database
                        if(dataBaseHelper.checkAuthorDuplicate(authorBean.getAuthor_pseudonym())){
                            // duplicate, error toast message
                            Toast.makeText(getActivity(), "Auteur déjà existant, enregistrement annulé", Toast.LENGTH_LONG).show(); /*TODO strings et traduction */
                        } else {
                            // Database handler called with the insertion method
                            boolean success = dataBaseHelper.insertIntoAuthors(authorBean);
                            // Success toast message
                            Toast.makeText(getActivity(), "Auteur créé", Toast.LENGTH_SHORT).show(); /*TODO strings et traduction */
                        }
                        popupTextDialog.dismiss(); // To lose popup
                        AuthorsDisplayRefresh(); // To refresh display
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
                            Toast.makeText(getActivity(), "Erreur création auteur", Toast.LENGTH_SHORT).show(); /*TODO strings et traduction */
                                authorBean = new AuthorBean(-1, "error" );
                            }
                            if(dataBaseHelper.checkAuthorDuplicate(authorBean.getAuthor_pseudonym())){
                                Toast.makeText(getActivity(), "Auteur déjà existant, enregistrement annulé", Toast.LENGTH_LONG).show(); /*TODO strings et traduction */
                            } else {
                                boolean success = dataBaseHelper.insertIntoAuthors(authorBean);
                                Toast.makeText(getActivity(), "Auteur créé", Toast.LENGTH_SHORT).show(); /*TODO strings et traduction */
                            }
                            popupTextDialog.dismiss();
                            AuthorsDisplayRefresh();
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
                AuthorsDisplayRefresh(); // To refresh display
            }
        });

        /** Author list item click */
        // Click item
        binding.lvAuteursListeAuteurs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Initialization AuthorBean
                AuthorBean authorBean;
                try {
                    // AuthorBean gets data from clicked item
                    authorBean = (AuthorBean) binding.lvAuteursListeAuteurs.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    authorBean = new AuthorBean(-1,"error","error","error");
                }
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("author_id", authorBean.getAuthor_id());
                bundle.putString("author_pseudonym", authorBean.getAuthor_pseudonym());
                bundle.putString("author_last_name", authorBean.getAuthor_last_name());
                bundle.putString("author_first_name", authorBean.getAuthor_first_name());
                // go to AuthorDetailFragment with the data bundle
                findNavController(AuthorsFragment.this).navigate(R.id.action_authors_to_authorDetail, bundle);
            }
        });
    }

    /** ----------------------------------------------------------------------------------------- */
    /** onDestroy inherited Method override
    /** ----------------------------------------------------------------------------------------- */
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null; // freeing memory
    }

    /** ----------------------------------------------------------------------------------------- */
    /** Display intitialization and refresh method
    /** ----------------------------------------------------------------------------------------- */
    private void AuthorsDisplayRefresh(){
        if (binding.sbSearch.svSearch.getQuery().toString().length() > 0) {
            // If the search bar isn't empty
            // list filtered
            authorsArrayAdapter = new AuthorsNbListAdapter(getActivity() , R.layout.listview_row_2col_reverse, dataBaseHelper.getAuthorsListByFilter(binding.sbSearch.svSearch.getQuery().toString()));
        } else {
            // list without filter
            authorsArrayAdapter = new AuthorsNbListAdapter(getActivity() , R.layout.listview_row_2col_reverse, dataBaseHelper.getAuthorsList());
        }
        // list adapter charged with data
        binding.lvAuteursListeAuteurs.setAdapter(authorsArrayAdapter);
    }
}