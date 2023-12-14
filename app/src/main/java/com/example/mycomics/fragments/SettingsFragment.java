package com.example.mycomics.fragments;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCaseGroup;
import androidx.camera.core.ViewPort;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import android.os.Environment;
import android.util.Rational;
import android.util.Size;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mycomics.MainActivity;
import com.example.mycomics.R;
import com.example.mycomics.adapters.ProfilesListAdapter;
import com.example.mycomics.beans.ProfileBean;
import com.example.mycomics.databinding.FragmentSettingsBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupTextDialog;
import com.example.mycomics.popups.PopupListDialog;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

        /*TODO*************************************************************************************/
        /**-------------------------------------- */
        /** TEST
        /**-------------------------------------- */

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
        ActiveProfileDisplay();

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
                            ProfileBean profileBean;
                            try {
                                profileBean = new ProfileBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), getString(R.string.SettingsProfileCreationError), Toast.LENGTH_SHORT).show();
                                profileBean = new ProfileBean(-1, "error" );
                            }
                            if (dataBaseHelper.checkProfileDuplicate(profileBean.getProfile_name())) {
                                Toast.makeText(SettingsFragment.super.getContext(), getString(R.string.SettingsProfileDuplicateError), Toast.LENGTH_LONG).show();
                            } else {
                                boolean success = dataBaseHelper.insertIntoProfiles(profileBean);
                                Toast.makeText(getActivity(), getString(R.string.SettingsProfileCreationSuccess), Toast.LENGTH_SHORT).show();
                            }
                            popupTextDialog.dismiss();
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
                ActiveProfileDisplay(); // To refresh display
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
                // getting access to the listView
                ListView listView = (ListView) popupListDialog.findViewById(R.id.lvPopupList);
                // setting up the Adapter for the list
                profilsArrayAdapter = new ProfilesListAdapter(getActivity() , R.layout.listview_row_1col, dataBaseHelper.getProfilesList());
                listView.setAdapter(profilsArrayAdapter);
                /* Profile list item click */
                popupListDialog.getLvPopupListe().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // ProfileBean for the data from the chosen item
                        ProfileBean profileBean;
                        try {
                            // fetching data from the list
                            profileBean = (ProfileBean) popupListDialog.getLvPopupListe().getItemAtPosition(position);
                            // updating Active Profile in the Database
                            dataBaseHelper.updateActiveProfile(dataBaseHelper, ((ProfileBean) popupListDialog.getLvPopupListe().getItemAtPosition(position)).getProfile_id());
                            Toast.makeText(getActivity(), getString(R.string.SettingsProfileChangeSuccess), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            // error toast message
                            Toast.makeText(getActivity(), getString(R.string.SettingsProfileChangeError), Toast.LENGTH_SHORT).show();
                            // id set to -1 for error handling
                            profileBean = new ProfileBean(-1, "error" );
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
                        ActiveProfileDisplay();
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

/***************** TEST CAMERA ********************************************************************/
        binding.buttonphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
    private void ActiveProfileDisplay() {
        try {
            System.out.println("test: " + dataBaseHelper.getProfileByActiveProfile().getProfile_name());
            binding.tvSettingsActiveProfile.setText(dataBaseHelper.getProfileByActiveProfile().getProfile_name());
        } catch (Exception e) {

        }
    }
}