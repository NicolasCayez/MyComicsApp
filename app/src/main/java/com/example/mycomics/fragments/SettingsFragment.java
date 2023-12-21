package com.example.mycomics.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycomics.R;
import com.example.mycomics.adapters.ProfilesAdapter;
import com.example.mycomics.beans.ProfileBean;
import com.example.mycomics.databinding.FragmentSettingsBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupListDialog;
import com.example.mycomics.popups.PopupTextDialog;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {


    //* ----------------------------------------------------------------------------------------- */
    //* View binding declaration */
    //* ----------------------------------------------------------------------------------------- */
    FragmentSettingsBinding binding;


    //* ----------------------------------------------------------------------------------------- */
    //* Database handler initialization
    //* ----------------------------------------------------------------------------------------- */
    DataBaseHelper dataBaseHelper;


    //* ----------------------------------------------------------------------------------------- */
    //* Adapters handling listViews data display
    //* ----------------------------------------------------------------------------------------- */
    ArrayAdapter profilsArrayAdapter;
    ProfilesAdapter profilesAdapter;


    //* ----------------------------------------------------------------------------------------- */
    //* Empty constructor, required
    //* ----------------------------------------------------------------------------------------- */
    public SettingsFragment() {
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
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    //* ----------------------------------------------------------------------------------------- */
    //* onViewCreated inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* Display initialization */
        activeProfileDisplay();

        /* Add Profile button handler and Profile creation popup */
        // Click event on add button
        binding.btnSettingsAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Popup for Profile creation with texts and see-through background
                PopupTextDialog popupTextDialog = new PopupTextDialog(getActivity());
                popupTextDialog.setTitle(getString(R.string.SettingsProfilePopupAddTitle));
                popupTextDialog.setHint(getString(R.string.SettingsProfilePopupAddHint));
                popupTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Click event on confirm button
                popupTextDialog.getBtnPopupConfirm().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ProfileBean for the data to be stored in the database
                        ProfileBean profileBean;
                        try {
                            // fetching text from the EditText
                            profileBean = new ProfileBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                        } catch (Exception e) {
                            // error toast message
                            Toast.makeText(getActivity(), getString(R.string.SettingsProfileCreationError), Toast.LENGTH_SHORT).show();
                            // id set to -1 for error handling
                            profileBean = new ProfileBean(-1, "error" );
                        }
                        // Checking if a duplicate with the same name already exists in the database
                        if (dataBaseHelper.checkProfileDuplicate(profileBean.getProfile_name())) {
                            // duplicate, error toast message
                            Toast.makeText(SettingsFragment.super.getContext(), getString(R.string.SettingsProfileDuplicateError), Toast.LENGTH_LONG).show();
                        } else {
                            // Database handler called with the insertion method
                            boolean success = dataBaseHelper.insertIntoProfiles(profileBean);
                            // Success toast message
                            Toast.makeText(getActivity(), getString(R.string.SettingsProfileCreationSuccess), Toast.LENGTH_SHORT).show();
                        }
                        popupTextDialog.dismiss(); // To close popup
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
                        popupTextDialog.dismiss(); // To close popup
                    }
                });
                popupTextDialog.build(); // To build the popup
                activeProfileDisplay(); // To refresh display
            }
        });

        /* Active Profile click */
        // Displays list of Profiles to choose from
        binding.tvSettingsActiveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Popup for Profile list with texts and see-through background
                PopupListDialog popupListDialog = new PopupListDialog(getActivity());
                popupListDialog.setTitle(getString(R.string.SettingsChooseProfile));
                popupListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Linking the RecyclerView
                RecyclerView recyclerView = popupListDialog.getRvPopupList();
                // Creating the list to display
                ArrayList<ProfileBean> profilesList = dataBaseHelper.getProfilesList();
                // The adapter gets the list and the string value "books" needed for translations
                profilesAdapter = new ProfilesAdapter(profilesList);
                // the adapter and the layout are defined for the RecyclerView
                recyclerView.setAdapter(profilesAdapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
                // The list is submitted to the adapter
                profilesAdapter.submitList(profilesList);

                /* Profile list item click */
                profilesAdapter.setOnClickListener(new ProfilesAdapter.OnClickListener() {
                    @Override
                    public void onClick(int position, ProfileBean profileBean) {
                        try {
                            // updating Active Profile in the Database
                            dataBaseHelper.updateActiveProfile(dataBaseHelper, profileBean.getProfile_id());
                            Toast.makeText(getActivity(), getString(R.string.SettingsProfileChangeSuccess), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            // error toast message
                            Toast.makeText(getActivity(), getString(R.string.SettingsProfileChangeError), Toast.LENGTH_SHORT).show();
                        }
                        popupListDialog.dismiss(); // To close popup
                    }
                });
                // Click event on abort button
                popupListDialog.getBtnPopupAbort().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupListDialog.dismiss(); // To close popup
                    }
                });
                popupListDialog.Build(); // To build the popup
                // action on popup closure -> refresh screen
                popupListDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        activeProfileDisplay();
                    }
                });
            }
        });





        /*TODO*************************************************************************************/
        /** Clic sur bouton DeleteProfil*/
        binding.btnSettingsDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //Création Popup
//                PopupMenuDialog popupMenuDialog = new PopupMenuDialog(ReglagesActivity.this);
//                Window window = popupMenuDialog.getWindow();
//                WindowManager.LayoutParams wlp = window.getAttributes();
//                wlp.gravity = Gravity.TOP;
//                window.setLayout(wlp.MATCH_PARENT, wlp.WRAP_CONTENT);
//                //clic listeners
//                popupMenuDialog.build();

//                Intent intent = new Intent(ReglagesActivity.this, MainActivity2.class);
//                startActivity(intent);
            }
        });

        /* Dark mode switch click click */
        binding.swSettingsDarkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (binding.swSettingsDarkMode.isChecked()){
                    // save mode
                    editor.putInt("darkMode", 1);
                    editor.apply();
                } else {
                    // save mode
                    editor.putInt("darkMode", 0);
                    editor.apply();
                }
                activeProfileDisplay();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
    private void activeProfileDisplay() {
        /* Dark mode */
        // Fetching config file
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int darkMode = sharedPref.getInt("darkMode", 0);
        // Applying light / dark mode
        if (darkMode == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            binding.swSettingsDarkMode.setChecked(true);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            binding.swSettingsDarkMode.setChecked(false);
        }
        /* Active profile display */
        try {
            binding.tvSettingsActiveProfile.setText(dataBaseHelper.getProfileByActiveProfile().getProfile_name());
        } catch (Exception e) {

        }
    }
}