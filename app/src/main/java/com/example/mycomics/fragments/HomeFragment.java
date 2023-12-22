package com.example.mycomics.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.app.Activity;
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

import com.example.mycomics.R;
import com.example.mycomics.databinding.FragmentHomeBinding;
import com.example.mycomics.helpers.DataBaseHelper;

public class HomeFragment extends Fragment{


    //* ----------------------------------------------------------------------------------------- */
    //* View binding declaration
    //* ----------------------------------------------------------------------------------------- */
    FragmentHomeBinding binding;


    //* ----------------------------------------------------------------------------------------- */
    //* Database handler initialization
    //* ----------------------------------------------------------------------------------------- */
    DataBaseHelper dataBaseHelper;

    //* ----------------------------------------------------------------------------------------- */
    //* Empty constructor, required
    //* ----------------------------------------------------------------------------------------- */
    public HomeFragment() {
        // Required empty public constructor
    }


    //* ----------------------------------------------------------------------------------------- */
    //* onCreate inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Database handler initialization */
        dataBaseHelper = new DataBaseHelper(getActivity());
    }


    //* ------------------------------------------------------------------------------------------ */
    //* onCreateView inherited Method override
    //* ------------------------------------------------------------------------------------------ */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    //* ----------------------------------------------------------------------------------------- */
    //* onViewCreated inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* Search bar */
        // Search Hint initialization
        binding.sbSearch.svSearch.setQueryHint(getString(R.string.SearchHintSearch));

        /* hiding Filter button, the search bar purpose here is only to filter */
        binding.sbSearch.btnFilter.setVisibility(View.GONE);

        // Click event on Search button
        binding.sbSearch.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Data bundle storing search string
                Bundle bundle = new Bundle();
                bundle.putString("filter", binding.sbSearch.svSearch.getQuery().toString());
                // Go to SearchResultFragment with the data bundle
                findNavController(HomeFragment.this).navigate(R.id.searchResultFragment, bundle);
                binding.sbSearch.svSearch.setQuery("", false);

            }
        });
        // Key event, calling click on search button
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
        // Key event, hiding the keyboard when enter is clicked
        binding.sbSearch.svSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // if "ENTER" key is pressed
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    return true; // inherited, necessary
                }
                return false; // inherited, necessary
            }
        });

        // Click event on SERIES button
        binding.btnHomeMenuSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Data bundle storing search string
                Bundle bundle = new Bundle();
                bundle.putString("filter", "");
                // Go to SeriesFragment with the data bundle
                findNavController(HomeFragment.this).navigate(R.id.action_home_to_series, bundle);
            }
        });
        // Click event on BOOKS button
        binding.btnHomeMenuBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Data bundle storing search string
                Bundle bundle = new Bundle();
                bundle.putString("filter", "");
                // Go to BooksFragment with the data bundle
                findNavController(HomeFragment.this).navigate(R.id.action_home_to_books, bundle);
            }
        });
        // Click event on AUTHORS button
        binding.btnHomeMenuAuthors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Data bundle storing search string
                Bundle bundle = new Bundle();
                bundle.putString("filter", "");
                // Go to AuthorsFragment with the data bundle
                findNavController(HomeFragment.this).navigate(R.id.action_home_to_authors, bundle);
            }
        });
        // Click event on EDITORS button
        binding.btnHomeMenuEditors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Data bundle storing search string
                Bundle bundle = new Bundle();
                bundle.putString("filter", "");
                // Go to EditorsFragment with the data bundle
                findNavController(HomeFragment.this).navigate(R.id.action_home_to_editors, bundle);
            }
        });
    }


    //* ----------------------------------------------------------------------------------------- */
    //* onDestroyView inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // to prevent memory leak
    }



}
