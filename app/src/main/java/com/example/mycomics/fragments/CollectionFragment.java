package com.example.mycomics.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.mycomics.R;
import com.example.mycomics.adapters.BooksAdapter;
import com.example.mycomics.adapters.NoLinkRecordAdapter;
import com.example.mycomics.beans.BookBean;
import com.example.mycomics.beans.NoLinkRecordBean;
import com.example.mycomics.databinding.FragmentCollectionBinding;
import com.example.mycomics.helpers.DataBaseHelper;

import java.util.ArrayList;

public class CollectionFragment extends Fragment {


    //* ----------------------------------------------------------------------------------------- */
    //* View binding declaration */
    //* ----------------------------------------------------------------------------------------- */
    FragmentCollectionBinding binding;


    //* ----------------------------------------------------------------------------------------- */
    //* Database handler initialization
    //* ----------------------------------------------------------------------------------------- */
    DataBaseHelper dataBaseHelper;


    //* ----------------------------------------------------------------------------------------- */
    //* Adapters handling RecycleViews data display
    //* ----------------------------------------------------------------------------------------- */
    BooksAdapter booksAdapter;
    NoLinkRecordAdapter noLinkRecordAdapter;


    //* ----------------------------------------------------------------------------------------- */
    //* Empty constructor, required
    //* ----------------------------------------------------------------------------------------- */
    public CollectionFragment() {
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
        binding = FragmentCollectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    //* ----------------------------------------------------------------------------------------- */
    //* onViewCreated inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* Display initialization */
        afficherPageCollection();

        /* Click on Book in the Books with only the title list (RecyclerView) */
        booksAdapter.setOnClickListener(new BooksAdapter.OnClickListener() {
            @Override
            public void onClick(int position, BookBean bookBean) {
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", bookBean.getBook_id());
                // go to AuthorDetailFragment with the data bundle
                findNavController(CollectionFragment.this).navigate(R.id.action_collection_to_bookDetail, bundle);
            }
        });

        /* Click on one record in the Not Linked list (RecyclerView) */
        noLinkRecordAdapter.setOnClickListener(new NoLinkRecordAdapter.OnClickListener() {
            @Override
            public void onClick(int position, NoLinkRecordBean record) {
                Bundle bundle = new Bundle();
                // Switch depending on the record type
                switch (record.getRecord_type()) {
                    case "book":
                        // Data bundle storing key-value pairs

                        bundle.putInt("book_id", record.getRecord_id());
                        // go to AuthorDetailFragment with the data bundle
                        findNavController(CollectionFragment.this).navigate(R.id.action_collection_to_bookDetail, bundle);
                        break;
                    case "serie":
                        // Data bundle storing key-value pairs
                        bundle = new Bundle();
                        bundle.putInt("serie_id", record.getRecord_id());
                        // go to AuthorDetailFragment with the data bundle
                        findNavController(CollectionFragment.this).navigate(R.id.action_collection_to_serieDetail, bundle);
                        break;
                    case "author":
                        // Data bundle storing key-value pairs
                        bundle = new Bundle();
                        bundle.putInt("author_id", record.getRecord_id());
                        // go to AuthorDetailFragment with the data bundle
                        findNavController(CollectionFragment.this).navigate(R.id.action_collection_to_authorDetail, bundle);
                        break;
                    case "editor":
                        // Data bundle storing key-value pairs
                        bundle = new Bundle();
                        bundle.putInt("editor_id", record.getRecord_id());
                        // go to AuthorDetailFragment with the data bundle
                        findNavController(CollectionFragment.this).navigate(R.id.action_collection_to_editorDetail, bundle);
                        break;
                    default:
                        // Do nothing
                }
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
    private void afficherPageCollection(){
        /* Active profile */
        binding.tvCollectionActiveProfile.setText(dataBaseHelper.getProfileByActiveProfile().getProfile_name());

        /* Number of books */
        binding.tvCollectionNbBooks.setText(String.valueOf(dataBaseHelper.getNbBooksCollection()));
        /* Number of series */
        binding.tvCollectionNbSeries.setText(String.valueOf(dataBaseHelper.getNbSeriesCollection()));
        /* Number of books not in a serie */
        binding.tvCollectionNbBooksNoSerie.setText(String.valueOf(dataBaseHelper.getNBBooksNoSerie()));


        /* Books with name only list */
        // Creating the list to display
        ArrayList<BookBean> BooksList = dataBaseHelper.getBooksWithNameOnly();
        // The adapter gets the list and the string value "books" needed for translations
        booksAdapter = new BooksAdapter(BooksList);
        // the adapter and the layout are defined for the RecyclerView
        binding.rvCollectionBooksNameOnly.setAdapter(booksAdapter);
        binding.rvCollectionBooksNameOnly.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        booksAdapter.submitList(BooksList);

        /* No-link records */
        // Creating the list to display
        ArrayList<NoLinkRecordBean> noLinkRecordList = dataBaseHelper.getNoLinkRecordsList();
        // The adapter gets the list and the string value "books" needed for translations
        noLinkRecordAdapter = new NoLinkRecordAdapter(noLinkRecordList);
        // the adapter and the layout are defined for the RecyclerView
        binding.rvCollectionAloneItems.setAdapter(noLinkRecordAdapter);
        binding.rvCollectionAloneItems.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        noLinkRecordAdapter.submitList(noLinkRecordList);

    }
}