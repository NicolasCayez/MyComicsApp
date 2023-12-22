package com.example.mycomics.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.mycomics.R;
import com.example.mycomics.adapters.SeriesNbAdapter;
import com.example.mycomics.beans.SerieBean;
import com.example.mycomics.databinding.FragmentSeriesBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupTextDialog;

import java.util.ArrayList;

public class SeriesFragment extends Fragment {


    //* ----------------------------------------------------------------------------------------- */
    //* View binding declaration */
    //* ----------------------------------------------------------------------------------------- */
    FragmentSeriesBinding binding;


    //* ----------------------------------------------------------------------------------------- */
    //* Database handler initialization
    //* ----------------------------------------------------------------------------------------- */
    DataBaseHelper dataBaseHelper;


    //* ----------------------------------------------------------------------------------------- */
    //* Filter needed for the list
    //* ----------------------------------------------------------------------------------------- */
    String filter = "";


    //* ----------------------------------------------------------------------------------------- */
    //* Adapters handling RecycleViews data display
    //* ----------------------------------------------------------------------------------------- */
    SeriesNbAdapter seriesAdapter;


    //* ----------------------------------------------------------------------------------------- */
    //* Empty constructor, required
    //* ----------------------------------------------------------------------------------------- */
    public SeriesFragment() {
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
        binding = FragmentSeriesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    //* ----------------------------------------------------------------------------------------- */
    //* onViewCreated inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* Data sent from this fragment when filtering list */
        try {
            filter = getArguments().getString("filter");
        } catch (Exception e) {
            filter = "";
        }

        /* Display initialization */
        seriesInitialize();

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
                findNavController(SeriesFragment.this).navigate(R.id.searchResultFragment, bundle);
            }
        });
        // Search bar text submit listener, to filter Series list
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

        /* Add Serie button handler and Serie creation popup */
        // Click event on add button
        binding.btnSeriesAddSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Popup for Serie creation with texts and see-through background
                PopupTextDialog popupTextDialog = new PopupTextDialog(getActivity());
                popupTextDialog.setTitle(getString(R.string.SeriePopupAddTitle));
                popupTextDialog.setHint(getString(R.string.SeriePopupAddHint));
                popupTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Click event on confirm button
                popupTextDialog.getBtnPopupConfirm().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // SerieBean for the data to be stored in the database
                        SerieBean serieBean;
                        try {
                            // fetching text from the EditText
                            serieBean = new SerieBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                        } catch (Exception e) {
                            // error toast message
                            Toast.makeText(getActivity(), getString(R.string.SerieCreationError), Toast.LENGTH_SHORT).show();
                            // id set to -1 for error handling
                            serieBean = new SerieBean(-1, "error" );
                        }
                        // Checking if a duplicate with the same name already exists in the database
                        if(dataBaseHelper.checkSerieDuplicate(serieBean.getSerie_name())){
                            // duplicate, error toast message
                            Toast.makeText(getActivity(), getString(R.string.SerieDuplicateError), Toast.LENGTH_LONG).show();
                        } else {
                            // Database handler called with the insertion method
                            boolean success = dataBaseHelper.insertIntoSeries(serieBean);
                            // Success toast message
                            Toast.makeText(getActivity(), getString(R.string.SerieCreationSuccess), Toast.LENGTH_SHORT).show();
                        }
                        popupTextDialog.dismiss(); // To close popup
                        seriesReload(filter); // To reload display
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
                            // Hide the keyboard
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                        seriesReload(filter); // To reload display
                    }
                });
                popupTextDialog.build(); // To build the popup
            }
        });

        /* Click on Serie in the list (RecyclerView) */
        seriesAdapter.setOnClickListener(new SeriesNbAdapter.OnClickListener() {
            @Override
            public void onClick(int position, SerieBean serieBean) {
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("serie_id", serieBean.getSerie_id());
                // go to AuthorDetailFragment with the data bundle
                findNavController(SeriesFragment.this).navigate(R.id.action_series_to_serieDetail, bundle);
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
    private void seriesInitialize(){
        // Creating the list to display
        ArrayList<SerieBean> SeriesList = dataBaseHelper.getSeriesList();
        // If the search bar contains a filter
        if (filter.length() > 0) {
            SeriesList = dataBaseHelper.getSeriesListByFilter(filter.replace("'",""));
        }
        // The adapter gets the list and the string value "books" needed for translations
        seriesAdapter = new SeriesNbAdapter(SeriesList, getString(R.string.Books));
        // the adapter and the layout are defined for the RecyclerView
        binding.rvSeriesSeriesList.setAdapter(seriesAdapter);
        binding.rvSeriesSeriesList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        seriesAdapter.submitList(SeriesList);
    }
    private void seriesReload(String filter) {
        // Data bundle storing key-value pairs
        Bundle bundle = new Bundle();
        bundle.putString("filter", filter);
        // go to AuthorDetailFragment with the data bundle
        findNavController(SeriesFragment.this).navigate(R.id.seriesFragment, bundle);
    }
}