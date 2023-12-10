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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultFragment extends Fragment {
    FragmentSearchResultBinding binding;

    /* -------------------------------------- */
    // Variable BDD
    /* -------------------------------------- */
    DataBaseHelper dataBaseHelper;
    ArrayAdapter tomesArrayAdapter;
    ArrayAdapter seriesArrayAdapter;
    ArrayAdapter auteursArrayAdapter;
    ArrayAdapter editeursArrayAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResultFragment newInstance(String param1, String param2) {
        SearchResultFragment fragment = new SearchResultFragment();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchResultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /* -------------------------------------- */
        // Récupération données
        /* -------------------------------------- */
        String filter = getArguments().getString("filter");
        /* -------------------------------------- */
        // Initialisation affichage
        /* -------------------------------------- */
        afficherResultatRecherche(filter);
        /* -------------------------------------- */
        // Bouton chercher inutile cat filter auto
        /* -------------------------------------- */
        binding.sbSearch.btSearch.setVisibility(View.GONE);
        /* -------------------------------------- */
        // saisie searchBar
        /* -------------------------------------- */
        binding.sbSearch.svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String filter) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String filter) {
                afficherResultatRecherche(filter);
                return false;
            }
        });

        /* -------------------------------------- */
        // Clic Element liste Serie
        /* -------------------------------------- */
        binding.lvRechercheListeSeries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SerieBean serieBean;
                try {
                    serieBean = (SerieBean) binding.lvRechercheListeSeries.getItemAtPosition(position);
                } catch (Exception e) {
                    serieBean = new SerieBean(-1,"error");
                }
                Bundle bundle = new Bundle();
                bundle.putInt("serie_id", serieBean.getSerie_id());
                bundle.putString("serie_name", serieBean.getSerie_name());

                findNavController(SearchResultFragment.this).navigate(R.id.action_searchResult_to_serieDetail, bundle);

            }
        });

        /* -------------------------------------- */
        // Clic Element liste Tomes
        /* -------------------------------------- */
        binding.lvRechercheListeTomes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookBean bookBean;
                try {
                    bookBean = dataBaseHelper.getBookById(((BookBean) binding.lvRechercheListeTomes.getItemAtPosition(position)).getBook_id());
                } catch (Exception e) {
                    bookBean = new BookBean(-1,"error");
                }
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

                findNavController(SearchResultFragment.this).navigate(R.id.action_searchResult_to_bookDetail, bundle);

            }
        });

        /* -------------------------------------- */
        // Clic Element liste Auteur
        /* -------------------------------------- */
        binding.lvRechercheListeAuteurs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AuthorBean authorBean;
                try {
                    authorBean = (AuthorBean) binding.lvRechercheListeAuteurs.getItemAtPosition(position);
                } catch (Exception e) {
                    authorBean = new AuthorBean(-1,"error","error","error");
                }
                Bundle bundle = new Bundle();
                bundle.putInt("author_id", authorBean.getAuthor_id());
                bundle.putString("author_pseudonym", authorBean.getAuthor_pseudonym());
                bundle.putString("author_last_name", authorBean.getAuthor_last_name());
                bundle.putString("author_first_name", authorBean.getAuthor_first_name());
                findNavController(SearchResultFragment.this).navigate(R.id.action_searchResult_to_authorDetail, bundle);
            }
        });

        /* -------------------------------------- */
        // Clic Element liste Editeur
        /* -------------------------------------- */
        binding.lvRechercheListeEditeurs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditorBean editorBean;
                try {
                    editorBean = (EditorBean) binding.lvRechercheListeEditeurs.getItemAtPosition(position);
                } catch (Exception e) {
                    editorBean = new EditorBean(-1,"error");
                }
                Bundle bundle = new Bundle();
                bundle.putInt("editor_id", editorBean.getEditor_id());
                bundle.putString("editor_name", editorBean.getEditor_name());
                findNavController(SearchResultFragment.this).navigate(R.id.action_searchResult_to_editorDetail, bundle);
            }
        });

    }

    private void afficherResultatRecherche(String filter){
        seriesArrayAdapter = new SeriesNbListAdapter(getActivity(), R.layout.listview_row_2col_reverse, dataBaseHelper.getSeriesListByFilter(filter));
        binding.lvRechercheListeSeries.setAdapter(seriesArrayAdapter);

        tomesArrayAdapter = new BooksNumberListAdapter(getActivity(), R.layout.listview_row_2col, dataBaseHelper.getBooksListByFilter(filter));
        binding.lvRechercheListeTomes.setAdapter(tomesArrayAdapter);

        auteursArrayAdapter = new AuthorsListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.getAuthorsListByFilter(filter));
        binding.lvRechercheListeAuteurs.setAdapter(auteursArrayAdapter);

        editeursArrayAdapter = new EditorsListAdapter(getActivity(), R.layout.listview_row_1col, dataBaseHelper.getEditorsListByFilter(filter));
        binding.lvRechercheListeEditeurs.setAdapter(editeursArrayAdapter);
    }
}