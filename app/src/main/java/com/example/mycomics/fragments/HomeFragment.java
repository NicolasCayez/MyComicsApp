package com.example.mycomics.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mycomics.R;

import com.example.mycomics.databinding.FragmentHomeBinding;
import com.example.mycomics.helpers.DataBaseHelper;

public class HomeFragment extends Fragment{
    FragmentHomeBinding binding;

    /* -------------------------------------- */
    // Variable BDD
    /* -------------------------------------- */
    DataBaseHelper dataBaseHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* -------------------------------------- */
        // Initialisation Base de données
        /* -------------------------------------- */
        dataBaseHelper = new DataBaseHelper(getActivity());
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.sbSearch.svSearch.setQueryHint("Rechercher");
        /* -------------------------------------- */
        // Clic Bouton Chercher
        /* -------------------------------------- */
        binding.sbSearch.btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("filter", binding.sbSearch.svSearch.getQuery().toString());
                findNavController(HomeFragment.this).navigate(R.id.searchResultFragment, bundle);
            }
        });
        /* -------------------------------------- */
        // Clic Bouton Accueil Series
        /* -------------------------------------- */
        binding.btnAccueilSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(HomeFragment.this).navigate(R.id.action_home_to_series);
            }
        });

        /* -------------------------------------- */
        // Clic Bouton Accueil Tomes
        /* -------------------------------------- */
        binding.btnAccueilTomes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(HomeFragment.this).navigate(R.id.action_home_to_books);
            }
        });

        /* -------------------------------------- */
        // Clic Bouton Accueil Auteurs
        /* -------------------------------------- */
        binding.btnAccueilAuteurs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(HomeFragment.this).navigate(R.id.action_home_to_authors);
            }
        });

        /* -------------------------------------- */
        // Clic Bouton Accueil Editeurs
        /* -------------------------------------- */
        binding.btnAccueilEditeurs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNavController(HomeFragment.this).navigate(R.id.action_home_to_editors);
            }
        });




    }



}