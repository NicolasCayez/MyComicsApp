package com.example.mycomics.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.mycomics.R;
import com.example.mycomics.adapters.AuthorsListAdapter;
import com.example.mycomics.adapters.EditorsListAdapter;
import com.example.mycomics.adapters.SeriesNbListAdapter;
import com.example.mycomics.adapters.BooksNumberListAdapter;
import com.example.mycomics.beans.AuthorBean;
import com.example.mycomics.beans.BookBean;
import com.example.mycomics.beans.EditorBean;
import com.example.mycomics.beans.SerieBean;
import com.example.mycomics.databinding.FragmentSearchResultBinding;
import com.example.mycomics.helpers.DataBaseHelper;

public class SearchResultFragment extends Fragment {


    //* ----------------------------------------------------------------------------------------- */
    //* View binding declaration */
    //* ----------------------------------------------------------------------------------------- */
    FragmentSearchResultBinding binding;


    //* ----------------------------------------------------------------------------------------- */
    //* Database handler initialization
    //* ----------------------------------------------------------------------------------------- */
    DataBaseHelper dataBaseHelper;


    //* ----------------------------------------------------------------------------------------- */
    //* Adapters handling listViews data display
    //* ----------------------------------------------------------------------------------------- */
    ArrayAdapter booksArrayAdapter;
    ArrayAdapter seriesArrayAdapter;
    ArrayAdapter authorsArrayAdapter;
    ArrayAdapter editorsArrayAdapter;


    //* ----------------------------------------------------------------------------------------- */
    //* Empty constructor, required
    //* ----------------------------------------------------------------------------------------- */
    public SearchResultFragment() {
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
        binding = FragmentSearchResultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    //* ----------------------------------------------------------------------------------------- */
    //* onViewCreated inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* Data sent from source fragments */
        String filter = getArguments().getString("filter");

        /* Display initialization */
        searchResultRefreshScreen(filter);

        /* hiding Search button, the search bar purpose here is only to filter */
        binding.sbSearch.btnSearch.setVisibility(View.GONE);

        /* Search bar */
        // Search bar text submit listener, to filter all lists
        binding.sbSearch.svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String filter) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String filter) {
                searchResultRefreshScreen(filter); // To refresh display, depending on filter
                return false;
            }
        });

        /* Series list item click */
        binding.lvSearchResultSeriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // SerieBean for the data to be send to destination
                SerieBean serieBean;
                try {
                    // SerieBean gets data from clicked item
                    serieBean = (SerieBean) binding.lvSearchResultSeriesList.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    serieBean = new SerieBean(-1,"error");
                }
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("serie_id", serieBean.getSerie_id());
                // go to SerieDetailFragment with the data bundle
                findNavController(SearchResultFragment.this).navigate(R.id.action_searchResult_to_serieDetail, bundle);
            }
        });

        /* Books list item click */
        binding.lvSearchResultBooksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // BookBean for the data to be send to destination
                BookBean bookBean;
                try {
                    // BookBean gets data from clicked item
                    bookBean = (BookBean) binding.lvSearchResultBooksList.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    bookBean = new BookBean(-1,"error");
                }
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", bookBean.getBook_id());
                findNavController(SearchResultFragment.this).navigate(R.id.action_searchResult_to_bookDetail, bundle);
            }
        });

        /* Author list item click */
        binding.lvSearchResultAuthorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // AuthorBean for the data to be send to destination
                AuthorBean authorBean;
                try {
                    // AuthorBean gets data from clicked item
                    authorBean = (AuthorBean) binding.lvSearchResultAuthorsList.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    authorBean = new AuthorBean(-1,"error","error","error");
                }
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("author_id", authorBean.getAuthor_id());
//                bundle.putString("author_pseudonym", authorBean.getAuthor_pseudonym());
                // go to AuthorDetailFragment with the data bundle
                findNavController(SearchResultFragment.this).navigate(R.id.action_searchResult_to_authorDetail, bundle);
            }
        });

        /* Editors list item click */
        binding.lvSearchResultEditorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // EditorBean for the data to be send to destination
                EditorBean editorBean;
                try {
                    // EditorBean gets data from clicked item
                    editorBean = (EditorBean) binding.lvSearchResultEditorsList.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    editorBean = new EditorBean(-1,"error");
                }
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("editor_id", editorBean.getEditor_id());
//                bundle.putString("editor_name", editorBean.getEditor_name());
                // go to EditorDetailFragment with the data bundle
                findNavController(SearchResultFragment.this).navigate(R.id.action_searchResult_to_editorDetail, bundle);
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
    private void searchResultRefreshScreen(String filter){
        // Series list adapters charger with data
        seriesArrayAdapter = new SeriesNbListAdapter(getActivity(), R.layout.listview_row_2col_reverse, dataBaseHelper.getSeriesListByFilter(filter));
        binding.lvSearchResultSeriesList.setAdapter(seriesArrayAdapter);
        // Books list adapters charger with data
        booksArrayAdapter = new BooksNumberListAdapter(getActivity(), R.layout.listview_row_2col, dataBaseHelper.getBooksListByFilter(filter));
        binding.lvSearchResultBooksList.setAdapter(booksArrayAdapter);
        // Authors list adapters charger with data
        authorsArrayAdapter = new AuthorsListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.getAuthorsListByFilter(filter));
        binding.lvSearchResultAuthorsList.setAdapter(authorsArrayAdapter);
        // Editors list adapters charger with data
        editorsArrayAdapter = new EditorsListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.getEditorsListByFilter(filter));
        binding.lvSearchResultEditorsList.setAdapter(editorsArrayAdapter);
    }
}