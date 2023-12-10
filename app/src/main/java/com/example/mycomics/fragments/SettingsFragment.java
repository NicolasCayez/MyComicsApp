package com.example.mycomics.fragments;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mycomics.R;
import com.example.mycomics.adapters.ProfilesListAdapter;
import com.example.mycomics.beans.ProfileBean;
import com.example.mycomics.databinding.FragmentSettingsBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupTextDialog;
import com.example.mycomics.popups.PopupListDialog;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    FragmentSettingsBinding binding;
    /* -------------------------------------- */
    // Variable BDD
    /* -------------------------------------- */
    DataBaseHelper dataBaseHelper;
    ArrayAdapter profilsArrayAdapter;





    /**-------------------------------------- */
    /** TEST
     /**-------------------------------------- */
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;






    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        /* -------------------------------------- */
        // Initialisation Base de données
        /* -------------------------------------- */
        dataBaseHelper = new DataBaseHelper(getActivity());

        /**-------------------------------------- */
        /** TEST
         /**-------------------------------------- */
        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());

    }
    private void afficherProfilActif() {
        try {
            System.out.println("test: " + dataBaseHelper.getProfileByActiveProfile().getProfile_name());
            binding.tvProfilActif.setText(dataBaseHelper.getProfileByActiveProfile().getProfile_name());
        } catch (Exception e) {

        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /* -------------------------------------- */
        // Initialisation affichage
        /* -------------------------------------- */
        afficherProfilActif();

        /* -------------------------------------- */
        // Clic sur bouton AddProfil
        /* -------------------------------------- */
        binding.btnAddProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Création Popup
                PopupTextDialog popupTextDialog = new PopupTextDialog(getActivity());
                popupTextDialog.setTitre("Entrez un nom de profil");
                popupTextDialog.setHint("Nom de profil");
                popupTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupTextDialog.getBtnPopupValider().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProfileBean profileBean;
                        try {
                            profileBean = new ProfileBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                        } catch (Exception e) {
//                            Toast.makeText(ReglagesActivity.this, "Erreur création profil", Toast.LENGTH_SHORT).show();
                            profileBean = new ProfileBean(-1, "error" );
                        }
                        popupTextDialog.dismiss(); // Fermeture Popup
                        //Appel DataBaseHelper
                        dataBaseHelper = new DataBaseHelper(getActivity());
                        boolean success = dataBaseHelper.insertIntoProfiles(profileBean);
//                        afficherListeProfils();
                    }
                });
                popupTextDialog.getBtnPopupAnnuler().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupTextDialog.dismiss(); // Fermeture Popup
                    }
                });
                popupTextDialog.build();
                afficherProfilActif();
            }
        });
        /************************************************************ TEST MENU POPUP
         /* -------------------------------------- */
        // Clic sur bouton DeleteProfil
        /* -------------------------------------- */
        binding.btnDeleteProfil.setOnClickListener(new View.OnClickListener() {
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



        /* -------------------------------------- */
        // Clic sur profil actif pour avoir la liste
        /* -------------------------------------- */
        binding.tvProfilActif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Création Popup
                PopupListDialog popupListDialog = new PopupListDialog(getActivity());
                popupListDialog.setTitre("Choisissez un profil dans la liste");
                popupListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ListView listView = (ListView) popupListDialog.findViewById(R.id.lvPopupList);
                profilsArrayAdapter = new ProfilesListAdapter(getActivity() , R.layout.listview_row_1col, dataBaseHelper.getProfilesList());
                listView.setAdapter(profilsArrayAdapter);
                //Clic Profil choisi pour modification
                popupListDialog.getLvPopupListe().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ProfileBean profileBean;
                        try {
                            profileBean = (ProfileBean) popupListDialog.getLvPopupListe().getItemAtPosition(position);
                            dataBaseHelper.updateActiveProfile(dataBaseHelper, ((ProfileBean) popupListDialog.getLvPopupListe().getItemAtPosition(position)).getProfile_id());
                        } catch (Exception e) {
                            profileBean = new ProfileBean(-1, "error" );
                        }
                        popupListDialog.dismiss(); // Fermeture Popup
                        //Appel DataBaseHelper
//                        dataBaseHelper = new DataBaseHelper(getActivity());
                    }
                });
                popupListDialog.getBtnPopupAnnuler().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupListDialog.dismiss(); // Fermeture Popup
                    }
                });
                popupListDialog.Build();
                popupListDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        afficherProfilActif();
                    }
                });
            }
        });





        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(getContext()));
        binding.buttonphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();

            }
        });





    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }





    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }








}