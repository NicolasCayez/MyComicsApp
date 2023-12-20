package com.example.mycomics.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

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

        // Click event on SERIES button
        binding.btnHomeMenuSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(HomeFragment.this).navigate(R.id.action_home_to_series);
            }
        });
        // Click event on BOOKS button
        binding.btnHomeMenuBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(HomeFragment.this).navigate(R.id.action_home_to_books);
            }
        });
        // Click event on AUTHORS button
        binding.btnHomeMenuAuthors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(HomeFragment.this).navigate(R.id.action_home_to_authors);
            }
        });
        // Click event on EDITORS button
        binding.btnHomeMenuEditors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(HomeFragment.this).navigate(R.id.action_home_to_editors);
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
