package com.example.mycomics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.View;

import com.example.mycomics.databinding.ActivityMainBinding;
import com.example.mycomics.helpers.DataBaseHelper;


public class MainActivity extends AppCompatActivity {


    /** ----------------------------------------------------------------------------------------- */
    /** View binding declaration
    /** ----------------------------------------------------------------------------------------- */
    private ActivityMainBinding binding = null;


    /** ----------------------------------------------------------------------------------------- */
    /** onCreate inherited Method override
    /** ----------------------------------------------------------------------------------------- */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Layout Inflater */
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /** Hamburger menu hidden by default */
        binding.menuHamburger.setVisibility(View.GONE);

        /** Database handler initialization */
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);

        /** Click event on logo */
        binding.ivLogoMyComics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to Home fragment and empty BackStack
                NavHostFragment.findNavController(binding.navHostFragment.getFragment()).popBackStack(R.id.homeFragment, false);
                // set Hamburger visibility to invisible
                binding.menuHamburger.setVisibility(View.INVISIBLE);
            }
        });

        /** Click event on Hamburger Menu lines */
        binding.ivHamburgLines.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle Hamburger menu visibility
                if (binding.menuHamburger.getVisibility() == View.GONE) {
                    binding.menuHamburger.setVisibility(View.VISIBLE);
                } else {
                    binding.menuHamburger.setVisibility(View.GONE);
                }
            }
        }));

        /** Click event on COLLEC (Hamburger menu) */
        binding.btnMenuCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to CollectionFragment
                NavHostFragment.findNavController(binding.navHostFragment.getFragment()).navigate(R.id.collectionFragment);
                // set Hamburger visibility to invisible
                binding.menuHamburger.setVisibility(View.INVISIBLE);
            }
        });

        /** Click event on SERIES (Hamburger menu) */
        binding.btnMenuSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to SeriesFragment
                NavHostFragment.findNavController(binding.navHostFragment.getFragment()).navigate(R.id.seriesFragment);
                // set Hamburger visibility to invisible
                binding.menuHamburger.setVisibility(View.INVISIBLE);
            }
        });

        /** Click event on BOOKS (Hamburger menu) */
        binding.btnMenuBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to BooksFragment
                NavHostFragment.findNavController(binding.navHostFragment.getFragment()).navigate(R.id.booksFragment);
                // set Hamburger visibility to invisible
                binding.menuHamburger.setVisibility(View.INVISIBLE);
            }
        });

        /** Click event on AUTHORS (Hamburger menu) */
        binding.btnMenuAuthors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to AuthorsFragment
                NavHostFragment.findNavController(binding.navHostFragment.getFragment()).navigate(R.id.authorsFragment);
                // set Hamburger visibility to invisible
                binding.menuHamburger.setVisibility(View.INVISIBLE);
            }
        });

        /** Click event on EDITORS (Hamburger menu) */
        binding.btnMenuEditors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to EditorsFragment
                NavHostFragment.findNavController(binding.navHostFragment.getFragment()).navigate(R.id.editorsFragment);
                // set Hamburger visibility to invisible
                binding.menuHamburger.setVisibility(View.INVISIBLE);
            }
        });

        /** Click event on SETTINGS (Hamburger menu) */
        binding.btnMenuSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to SettingsFragment
                NavHostFragment.findNavController(binding.navHostFragment.getFragment()).navigate(R.id.settingsFragment);
                // set Hamburger visibility to invisible
                binding.menuHamburger.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }
}