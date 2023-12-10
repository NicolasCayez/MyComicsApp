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
import com.example.mycomics.adapters.EditorsNbListAdapter;
import com.example.mycomics.beans.EditorBean;
import com.example.mycomics.databinding.FragmentEditorsBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupTextDialog;

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
    //* Adapters handling listViews data display
    //* ----------------------------------------------------------------------------------------- */
    ArrayAdapter editorsArrayAdapter;


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
        EditorsRefreshScreen();
        /* Search bar */
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
                EditorsRefreshScreen(); // To refresh display
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
                        EditorsRefreshScreen(); // To refresh display
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
                            EditorsRefreshScreen();
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
                EditorsRefreshScreen(); // To refresh display
            }
        });

        /* Editors list item click */
        // Click item
        binding.lvEditorsEditorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // EditorBean for the data to be send to destination
                EditorBean editorBean;
                try {
                    // EditorBean gets data from clicked item
                    editorBean = (EditorBean) binding.lvEditorsEditorsList.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    editorBean = new EditorBean(-1,"error");
                }
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("editor_id", editorBean.getEditor_id());
                bundle.putString("editor_name", editorBean.getEditor_name());
                // go to EditorDetailFragment with the data bundle
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
    //* Display intitialization and refresh method
    //* ----------------------------------------------------------------------------------------- */
    private void EditorsRefreshScreen(){
        if (binding.sbSearch.svSearch.getQuery().toString().length() > 0) {
            // If the search bar isn't empty
            // list filtered
            editorsArrayAdapter = new EditorsNbListAdapter(getActivity() , R.layout.listview_row_2col_reverse, dataBaseHelper.getEditorsListByFilter(binding.sbSearch.svSearch.getQuery().toString()));
        } else {
            // list without filter
            editorsArrayAdapter = new EditorsNbListAdapter(getActivity() , R.layout.listview_row_2col_reverse, dataBaseHelper.getEditorsList());
        }
        // list adapter charged with data
        binding.lvEditorsEditorsList.setAdapter(editorsArrayAdapter);
    }
}