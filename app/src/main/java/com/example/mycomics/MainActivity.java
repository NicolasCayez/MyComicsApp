package com.example.mycomics;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.mycomics.databinding.ActivityMainBinding;
import com.example.mycomics.fragments.AccueilFragment;
import com.example.mycomics.fragments.CollectionFragment;
import com.example.mycomics.helpers.DataBaseHelper;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Création de l'interface graphique
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.menuHamburger.setVisibility(View.GONE);

        /* -------------------------------------- */
        // Initialisation BDD au besoin
        /* -------------------------------------- */
        DataBaseHelper dataBaseHelper;
        dataBaseHelper = new DataBaseHelper(this);

        /* -------------------------------------- */
        // Clic sur le logo
        /* -------------------------------------- */
        binding.ivLogoMyComics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(binding.navHostFragment.getFragment()).popBackStack(R.id.accueilFragment, false);
//                        .navigate(R.id.action_accueilFragment_to_collectionFragment);
                binding.menuHamburger.setVisibility(View.INVISIBLE);
            }
        });

        /* -------------------------------------- */
        // Clic Menu Hamburger
        /* -------------------------------------- */
        binding.ivHamburgLines.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.menuHamburger.getVisibility() == View.GONE) {
                    binding.menuHamburger.setVisibility(View.VISIBLE);
                } else {
                    binding.menuHamburger.setVisibility(View.GONE);
                }
            }
        }));

        /* -------------------------------------- */
        // Clic Bouton menu Collection
        /* -------------------------------------- */
        binding.btnMenuCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                System.out.println();
//                NavHostFragment.findNavController(binding.navHostFragment.getFragment()).navigate(R.id.action_accueilFragment_to_collectionFragment);
                NavHostFragment.findNavController(binding.navHostFragment.getFragment()).navigate(R.id.collectionFragment);
                binding.menuHamburger.setVisibility(View.INVISIBLE);
            }
        });

        /* -------------------------------------- */
        // Clic Bouton menu Series
        /* -------------------------------------- */
        binding.btnMenuSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NavHostFragment.findNavController(binding.navHostFragment.getFragment()).navigate(R.id.action_accueilFragment_to_seriesFragment);
                NavHostFragment.findNavController(binding.navHostFragment.getFragment()).navigate(R.id.seriesFragment);
                binding.menuHamburger.setVisibility(View.INVISIBLE);
            }
        });

        /* -------------------------------------- */
        // Clic Bouton menu Tomes
        /* -------------------------------------- */
        binding.btnMenuTomes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NavHostFragment.findNavController(binding.navHostFragment.getFragment()).navigate(R.id.action_accueilFragment_to_tomesFragment);
                NavHostFragment.findNavController(binding.navHostFragment.getFragment()).navigate(R.id.tomesFragment);

                binding.menuHamburger.setVisibility(View.INVISIBLE);
            }
        });

        /* -------------------------------------- */
        // Clic Bouton menu Auteurs
        /* -------------------------------------- */
        binding.btnMenuAuteurs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NavHostFragment.findNavController(binding.navHostFragment.getFragment()).navigate(R.id.action_accueilFragment_to_auteursFragment);
                NavHostFragment.findNavController(binding.navHostFragment.getFragment()).navigate(R.id.auteursFragment);

                binding.menuHamburger.setVisibility(View.INVISIBLE);
            }
        });

        /* -------------------------------------- */
        // Clic Bouton menu Editeurs
        /* -------------------------------------- */
        binding.btnMenuEditeurs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NavHostFragment.findNavController(binding.navHostFragment.getFragment()).navigate(R.id.action_accueilFragment_to_editeursFragment);
                NavHostFragment.findNavController(binding.navHostFragment.getFragment()).navigate(R.id.editeursFragment);

                binding.menuHamburger.setVisibility(View.INVISIBLE);
            }
        });

        /* -------------------------------------- */
        // Clic Bouton menu Reglages
        /* -------------------------------------- */
        binding.btnMenuReglages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NavHostFragment.findNavController(binding.navHostFragment.getFragment()).navigate(R.id.action_accueilFragment_to_reglagesFragment);
                NavHostFragment.findNavController(binding.navHostFragment.getFragment()).navigate(R.id.reglagesFragment);

                binding.menuHamburger.setVisibility(View.INVISIBLE);
            }
        });
        


    }



    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }
}