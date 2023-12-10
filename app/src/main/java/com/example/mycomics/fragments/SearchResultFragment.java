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
        SearchResultRefreshScreen(filter);

        /* hiding Search button, the search bar purpose here is only to filter */
        binding.sbSearch.btSearch.setVisibility(View.GONE);

        /* Search bar */
        // Search bar text submit listener, to filter all lists
        binding.sbSearch.svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String filter) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String filter) {
                SearchResultRefreshScreen(filter); // To refresh display, depending on filter
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
                bundle.putString("serie_name", serieBean.getSerie_name());
                // go to SerieDetailFragment with the data bundle
                findNavController(SearchResultFragment.this).navigate(R.id.action_searchResult_to_serieDetail, bundle);
            }
        });

        /* Books list item click */
        binding.lvsearchResultBooksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // BookBean for the data to be send to destination
                BookBean bookBean;
                try {
                    // BookBean gets data from clicked item
                    bookBean = dataBaseHelper.getBookById(((BookBean) binding.lvsearchResultBooksList.getItemAtPosition(position)).getBook_id());
                } catch (Exception e) {
                    // id set to -1 for error handling
                    bookBean = new BookBean(-1,"error");
                }
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", bookBean.getBook_id());
                bundle.putInt("book_number", bookBean.getBook_number());
                bundle.putString("book_title", bookBean.getBook_title());
                bundle.putDouble("book_editor_price", bookBean.getBook_editor_price());
                bundle.putDouble("book_value", bookBean.getBook_value());
                bundle.putString("book_edition_date", bookBean.getBook_edition_date());
                bundle.putString("book_isbn", bookBean.getBook_isbn());
                bundle.putString("book_picture", bookBean.getBook_picture());
                bundle.putBoolean("book_autograph", bookBean.isBook_autograph());
                bundle.putBoolean("book_special_edition", bookBean.isBook_special_edition());
                bundle.putString("book_special_edition_label", bookBean.getBook_special_edition_label());
                bundle.putInt("serie_id", bookBean.getSerie_id());
                bundle.putInt("editor_id", bookBean.getEditor_id());
                // go to BookDetailFragment with the data bundle
                findNavController(SearchResultFragment.this).navigate(R.id.action_searchResult_to_bookDetail, bundle);
            }
        });

        /* Author list item click */
        binding.lvsearchResultAuthorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // AuthorBean for the data to be send to destination
                AuthorBean authorBean;
                try {
                    // AuthorBean gets data from clicked item
                    authorBean = (AuthorBean) binding.lvsearchResultAuthorsList.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    authorBean = new AuthorBean(-1,"error","error","error");
                }
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("author_id", authorBean.getAuthor_id());
                bundle.putString("author_pseudonym", authorBean.getAuthor_pseudonym());
                bundle.putString("author_last_name", authorBean.getAuthor_last_name());
                bundle.putString("author_first_name", authorBean.getAuthor_first_name());
                // go to AuthorDetailFragment with the data bundle
                findNavController(SearchResultFragment.this).navigate(R.id.action_searchResult_to_authorDetail, bundle);
            }
        });

        /* Editors list item click */
        binding.lvsearchResultEditorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // EditorBean for the data to be send to destination
                EditorBean editorBean;
                try {
                    // EditorBean gets data from clicked item
                    editorBean = (EditorBean) binding.lvsearchResultEditorsList.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    editorBean = new EditorBean(-1,"error");
                }
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("editor_id", editorBean.getEditor_id());
                bundle.putString("editor_name", editorBean.getEditor_name());
                // go to EditorDetailFragment with the data bundle
                findNavController(SearchResultFragment.this).navigate(R.id.action_searchResult_to_editorDetail, bundle);
            }
        });

    }

    private void SearchResultRefreshScreen(String filter){
        // list adapters charged with data x4

        seriesArrayAdapter = new SeriesNbListAdapter(getActivity(), R.layout.listview_row_2col_reverse, dataBaseHelper.getSeriesListByFilter(filter));
        binding.lvSearchResultSeriesList.setAdapter(seriesArrayAdapter);

        booksArrayAdapter = new BooksNumberListAdapter(getActivity(), R.layout.listview_row_2col, dataBaseHelper.getBooksListByFilter(filter));
        binding.lvsearchResultBooksList.setAdapter(booksArrayAdapter);

        authorsArrayAdapter = new AuthorsListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.getAuthorsListByFilter(filter));
        binding.lvsearchResultAuthorsList.setAdapter(authorsArrayAdapter);

        editorsArrayAdapter = new EditorsListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.getEditorsListByFilter(filter));
        binding.lvsearchResultEditorsList.setAdapter(editorsArrayAdapter);
    }
}