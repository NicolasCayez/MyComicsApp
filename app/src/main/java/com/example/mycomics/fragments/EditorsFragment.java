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
import com.example.mycomics.adapters.EditorsNbAdapter;
import com.example.mycomics.beans.EditorBean;
import com.example.mycomics.databinding.FragmentEditorsBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupTextDialog;

import java.util.ArrayList;

public class EditorsFragment extends Fragment {


    //* ----------------------------------------------------------------------------------------- */
    //* View binding declaration */
    //* ----------------------------------------------------------------------------------------- */
    FragmentEditorsBinding binding;


    //* ----------------------------------------------------------------------------------------- */
    //* Database handler initialization
    //* ----------------------------------------------------------------------------------------- */
    DataBaseHelper dataBaseHelper;


    //* ----------------------------------------------------------------------------------------- */
    //* Adapters handling RecycleViews data display
    //* ----------------------------------------------------------------------------------------- */
    EditorsNbAdapter editorsAdapter;


    //* ----------------------------------------------------------------------------------------- */
    //* Empty constructor, required
    //* ----------------------------------------------------------------------------------------- */
    public EditorsFragment() {
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
        binding = FragmentEditorsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    //* ----------------------------------------------------------------------------------------- */
    //* onViewCreated inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* Display initialization */
        editorsRefreshScreen();
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
                findNavController(EditorsFragment.this).navigate(R.id.searchResultFragment, bundle);
            }
        });
        // Search bar text submit listener, to filter Editors list
        binding.sbSearch.svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                editorsRefreshScreen(); // To refresh display
                return false;
            }
        });

        /* Add Editor button handler and Editor creation popup */
        // Click event on add button
        binding.btnEditorsAddEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Popup for Editor creation with texts and see-through background
                PopupTextDialog popupTextDialog = new PopupTextDialog(getActivity());
                popupTextDialog.setTitle(getString(R.string.EditorPopupAddTitle));
                popupTextDialog.setHint(getString(R.string.EditorPopupAddHint));
                popupTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Click event on confirm button
                popupTextDialog.getBtnPopupConfirm().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // EditorBean for the data to be stored in the database
                        EditorBean editorBean;
                        try {
                            // fetching text from the EditText
                            editorBean = new EditorBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                        } catch (Exception e) {
                            // error toast message
                            Toast.makeText(getActivity(), getString(R.string.EditorCreationError), Toast.LENGTH_SHORT).show();
                            // id set to -1 for error handling
                            editorBean = new EditorBean(-1, "error" );
                        }
                        // Checking if a duplicate with the same name already exists in the database
                        if(dataBaseHelper.checkEditorDuplicate(editorBean.getEditor_name())){
                            // duplicate, error toast message
                            Toast.makeText(getActivity(), getString(R.string.EditorDuplicateError), Toast.LENGTH_LONG).show();
                        } else {
                            // Database handler called with the insertion method
                            boolean success = dataBaseHelper.insertIntoEditors(editorBean);
                            // Success toast message
                            Toast.makeText(getActivity(), getString(R.string.EditorCreationSuccess), Toast.LENGTH_SHORT).show();
                        }
                        popupTextDialog.dismiss(); // To close popup
                        editorsRefreshScreen(); // To refresh display
                    }
                });
                // Key event, same behaviour as confirm button
                popupTextDialog.getEtPopupText().setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // if "ENTER" key is pressed
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            EditorBean editorBean;
                            try {
                                editorBean = new EditorBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), getString(R.string.EditorCreationError), Toast.LENGTH_SHORT).show();
                                editorBean = new EditorBean(-1, "error" );
                            }
                            if(dataBaseHelper.checkEditorDuplicate(editorBean.getEditor_name())){
                                Toast.makeText(getActivity(), getString(R.string.EditorDuplicateError), Toast.LENGTH_LONG).show();
                            } else {
                                boolean success = dataBaseHelper.insertIntoEditors(editorBean);
                                Toast.makeText(getActivity(), getString(R.string.EditorCreationSuccess), Toast.LENGTH_SHORT).show();
                            }
                            popupTextDialog.dismiss();
                            editorsRefreshScreen();
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
                editorsRefreshScreen(); // To refresh display
            }
        });

        /* Click on Editor in the list (RecyclerView) */
       editorsAdapter.setOnClickListener(new EditorsNbAdapter.OnClickListener() {
            @Override
            public void onClick(int position, EditorBean editorBean) {
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("editor_id", editorBean.getEditor_id());;
                // go to AuthorDetailFragment with the data bundle
                findNavController(EditorsFragment.this).navigate(R.id.action_editors_to_editorDetail, bundle);
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
    private void editorsRefreshScreen(){
        // Creating the list to display
        ArrayList<EditorBean> EditorsList = dataBaseHelper.getEditorsList();
        // If the search bar contains a filter
        if (binding.sbSearch.svSearch.getQuery().toString().length() > 0) {
            EditorsList = dataBaseHelper.getEditorsListByFilter(binding.sbSearch.svSearch.getQuery().toString());
        }
        // The adapter gets the list and the string value "books" needed for translations
        editorsAdapter = new EditorsNbAdapter(EditorsList, getString(R.string.Books));
        // the adapter and the layout are defined for the RecyclerView
        binding.rvEditorsEditorsList.setAdapter(editorsAdapter);
        binding.rvEditorsEditorsList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        editorsAdapter.submitList(EditorsList);
    }
}