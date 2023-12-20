package com.example.mycomics.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.mycomics.R;
import com.example.mycomics.adapters.AuthorsAdapter;
import com.example.mycomics.adapters.BooksBookNbAdapter;
import com.example.mycomics.adapters.EditorsAdapter;
import com.example.mycomics.adapters.SeriesNbAdapter;
import com.example.mycomics.beans.AuthorBean;
import com.example.mycomics.beans.BookBean;
import com.example.mycomics.beans.EditorBean;
import com.example.mycomics.beans.SerieBean;
import com.example.mycomics.databinding.FragmentSearchResultBinding;
import com.example.mycomics.helpers.DataBaseHelper;

import java.util.ArrayList;

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
    //* Adapters handling RecycleViews data display
    //* ----------------------------------------------------------------------------------------- */
    SeriesNbAdapter seriesAdapter;
    BooksBookNbAdapter booksAdapter;
    AuthorsAdapter authorsAdapter;
    EditorsAdapter editorsAdapter;


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
                binding.sbSearch.btnSearch.performClick();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String filter) {
                searchResultRefreshScreen(filter); // To refresh display, depending on filter
                return false;
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
                findNavController(SearchResultFragment.this).navigate(R.id.action_searchResult_to_serieDetail, bundle);
            }
        });

        /* Click on Book in the list (RecyclerView) */
        booksAdapter.setOnClickListener(new BooksBookNbAdapter.OnClickListener() {
            @Override
            public void onClick(int position, BookBean bookBean) {
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", bookBean.getBook_id());
                // go to AuthorDetailFragment with the data bundle
                findNavController(SearchResultFragment.this).navigate(R.id.action_searchResult_to_bookDetail, bundle);
            }
        });

        /* Click on Author in the list (RecyclerView) */
        authorsAdapter.setOnClickListener(new AuthorsAdapter.OnClickListener() {
            @Override
            public void onClick(int position, AuthorBean authorBean) {
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("author_id", authorBean.getAuthor_id());
                // go to AuthorDetailFragment with the data bundle
                findNavController(SearchResultFragment.this).navigate(R.id.action_searchResult_to_authorDetail, bundle);
            }
        });

        /* Click on Editor in the list (RecyclerView) */
        editorsAdapter.setOnClickListener(new EditorsAdapter.OnClickListener() {
            @Override
            public void onClick(int position, EditorBean editorBean) {
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("editor_id", editorBean.getEditor_id());
                // go to AuthorDetailFragment with the data bundle
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
        /* Series list adapters charged with data */
        // Creating the list to display
        ArrayList<SerieBean> SeriesList = dataBaseHelper.getSeriesListByFilter(filter.replace("'",""));
        // The adapter gets the list and the string value "books" needed for translations
        seriesAdapter = new SeriesNbAdapter(SeriesList, getString(R.string.Books));
        // the adapter and the layout are defined for the RecyclerView
        binding.rvSearchResultSeriesList.setAdapter(seriesAdapter);
        binding.rvSearchResultSeriesList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        seriesAdapter.submitList(SeriesList);

        /* Books list adapters charged with data */
        // Creating the list to display
        ArrayList<BookBean> BooksList = dataBaseHelper.getBooksListByFilter(filter.replace("'",""));
        // The adapter gets the list and the string value "books" needed for translations
        booksAdapter = new BooksBookNbAdapter(BooksList, getString(R.string.BookNumber));
        // the adapter and the layout are defined for the RecyclerView
        binding.rvSearchResultBooksList.setAdapter(booksAdapter);
        binding.rvSearchResultBooksList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        booksAdapter.submitList(BooksList);

        /* Authors list adapters charged with data */
        // Creating the list to display
        ArrayList<AuthorBean> AuthorsList = dataBaseHelper.getAuthorsListByFilter(filter.replace("'",""));
        // The adapter gets the list and the string value "books" needed for translations
        authorsAdapter = new AuthorsAdapter(AuthorsList);
        // the adapter and the layout are defined for the RecyclerView
        binding.rvSearchResultAuthorsList.setAdapter(authorsAdapter);
        binding.rvSearchResultAuthorsList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        authorsAdapter.submitList(AuthorsList);

        /* Editors list adapters charger with data */
        // Creating the list to display
        ArrayList<EditorBean> EditorsList = dataBaseHelper.getEditorsListByFilter(filter.replace("'",""));
        // The adapter gets the list and the string value "books" needed for translations
        editorsAdapter = new EditorsAdapter(EditorsList);
        // the adapter and the layout are defined for the RecyclerView
        binding.rvSearchResultEditorsList.setAdapter(editorsAdapter);
        binding.rvSearchResultEditorsList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        editorsAdapter.submitList(EditorsList);
    }
}