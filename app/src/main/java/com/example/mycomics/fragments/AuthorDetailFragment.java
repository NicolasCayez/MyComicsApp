package com.example.mycomics.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.mycomics.adapters.BooksListAdapter;
import com.example.mycomics.beans.AuthorBean;
import com.example.mycomics.beans.BookBean;
import com.example.mycomics.beans.EditorBean;
import com.example.mycomics.beans.SerieBean;
import com.example.mycomics.databinding.FragmentAuthorDetailBinding;
import com.example.mycomics.helpers.DataBaseHelper;

public class AuthorDetailFragment extends Fragment {


    //* ----------------------------------------------------------------------------------------- */
    //* View binding declaration */
    //* ----------------------------------------------------------------------------------------- */
    FragmentAuthorDetailBinding binding;


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
    public AuthorDetailFragment() {
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
        binding = FragmentAuthorDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();    }


    //* ----------------------------------------------------------------------------------------- */
    //* onViewCreated inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* Data sent from source fragments */
        int author_id = getArguments().getInt("author_id");

        /* Display initialization */
        AuthorBean authorBean = dataBaseHelper.getAuthorById(author_id);
        AuthorDetailRefreshScreen(authorBean);

        /* Series list item click */
        binding.lvAuthorDetailSeriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // SerieBean for the data to be send to destination
                SerieBean serieBean;
                try {
                    // SerieBean gets data from clicked item
                    serieBean = (SerieBean) binding.lvAuthorDetailSeriesList.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    serieBean = new SerieBean(-1,"error");
                }
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("serie_id", serieBean.getSerie_id());
                // go to SerieDetailFragment with the data bundle
                findNavController(AuthorDetailFragment.this).navigate(R.id.action_authorDetail_to_serieDetail, bundle);
            }
        });

        /* Books list item click */
        binding.lvAuthorDetailBooksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // BookBean for the data to be send to destination
                BookBean bookBean;
                try {
                    // BookBean gets data from clicked item
                    bookBean = (BookBean) binding.lvAuthorDetailBooksList.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    bookBean = new BookBean(-1,"error");
                }
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", bookBean.getBook_id());
                // go to BookDetailFragment with the data bundle
                findNavController(AuthorDetailFragment.this).navigate(R.id.action_authorDetail_to_bookDetail, bundle);
            }
        });

        /* Authors list item click */
        binding.lvAuthorDetailAuthorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // AuthorBean for the data to be send to destination
                AuthorBean authorBean;
                try {
                    // AuthorBean gets data from clicked item
                    authorBean = (AuthorBean) binding.lvAuthorDetailAuthorsList.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    authorBean = new AuthorBean(-1,"error","error","error");
                }
                Bundle bundle = new Bundle();
                // Data bundle storing key-value pairs
                bundle.putInt("author_id", authorBean.getAuthor_id());
                // go to AuthorDetailFragment with the data bundle
                findNavController(AuthorDetailFragment.this).navigate(R.id.authorDetailFragment, bundle);
            }
        });

        /* Editors list item click */
        binding.lvAuthorDetailEditorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // EditorBean for the data to be send to destination
                EditorBean editorBean;
                try {
                    // EditorBean gets data from clicked item
                    editorBean = (EditorBean) binding.lvAuthorDetailEditorsList.getItemAtPosition(position);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    editorBean = new EditorBean(-1,"error");
                }
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("editor_id", editorBean.getEditor_id());
                bundle.putString("editor_name", editorBean.getEditor_name());
                // go to EditorDetailFragment with the data bundle
                findNavController(AuthorDetailFragment.this).navigate(R.id.action_authorDetail_to_editorDetail, bundle);
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
    private void AuthorDetailRefreshScreen(AuthorBean authorBean){
        // Serie Name
        binding.tvAuthorDetailAuthorPseudonym.setText(authorBean.getAuthor_pseudonym());
        // Series list adapters charger with data
        seriesArrayAdapter = new SeriesNbListAdapter(getActivity(), R.layout.listview_row_2col_reverse, dataBaseHelper.getSeriesListByAuthorId(authorBean.getAuthor_id()));
        binding.lvAuthorDetailSeriesList.setAdapter(seriesArrayAdapter);
        // Books list adapters charger with data
        booksArrayAdapter = new BooksListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.getBooksListByAuthorIdNoSerie(authorBean.getAuthor_id()));
        binding.lvAuthorDetailBooksList.setAdapter(booksArrayAdapter);
        // Authors list adapters charger with data
        authorsArrayAdapter = new AuthorsListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.getAuthorsTeam(authorBean.getAuthor_id()));
        binding.lvAuthorDetailAuthorsList.setAdapter(authorsArrayAdapter);
        // Editors list adapters charger with data
        editorsArrayAdapter = new EditorsListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.getEditorsByAuthorId(authorBean.getAuthor_id()));
        binding.lvAuthorDetailEditorsList.setAdapter(editorsArrayAdapter);
    }
}

