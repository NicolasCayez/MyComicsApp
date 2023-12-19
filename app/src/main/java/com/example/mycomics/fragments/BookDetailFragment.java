package com.example.mycomics.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycomics.R;
import com.example.mycomics.adapters.AuthorsAdapter;
import com.example.mycomics.adapters.EditorsAdapter;
import com.example.mycomics.adapters.SeriesAdapter;
import com.example.mycomics.beans.AuthorBean;
import com.example.mycomics.beans.BookBean;
import com.example.mycomics.beans.EditorBean;
import com.example.mycomics.beans.SerieBean;
import com.example.mycomics.databinding.FragmentBookDetailBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupAddListDialog;
import com.example.mycomics.popups.PopupConfirmDialog;
import com.example.mycomics.popups.PopupListDialog;
import com.example.mycomics.popups.PopupTextDialog;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class BookDetailFragment extends Fragment {


    //* ----------------------------------------------------------------------------------------- */
    //* View binding declaration */
    //* ----------------------------------------------------------------------------------------- */
    FragmentBookDetailBinding binding;

    private String picturePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/MyComics/";
    int SELECT_PICTURE = 200;
    private String imgName = "";

    //* ----------------------------------------------------------------------------------------- */
    //* Database handler initialization
    //* ----------------------------------------------------------------------------------------- */
    DataBaseHelper dataBaseHelper;


    //* ----------------------------------------------------------------------------------------- */
    //* Adapters handling RecycleViews data display
    //* ----------------------------------------------------------------------------------------- */
    SeriesAdapter seriesAdapter;
    AuthorsAdapter authorsAdapter;
    EditorsAdapter editorsAdapter;


    //* ----------------------------------------------------------------------------------------- */
    //* Camera Provider declarations
    //* ----------------------------------------------------------------------------------------- */
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;


    //* ----------------------------------------------------------------------------------------- */
    //* Empty constructor, required
    //* ----------------------------------------------------------------------------------------- */
    public BookDetailFragment() {
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
        /* Camera provider */
        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, getExecutor());

    }


    //* ----------------------------------------------------------------------------------------- */
    //* onCreateView inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();    }


    //* ----------------------------------------------------------------------------------------- */
    //* onViewCreated inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* Data sent from source fragments */
        Integer book_id = getArguments().getInt("book_id");

        /* Display initialization */
        BookBean bookBean = dataBaseHelper.getBookById(book_id);
        bookDetailRefreshScreen(book_id);

        /* Click event on Save Book button */
        binding.btnBookDetailSaveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBook(book_id);
            }
        });

        /* Click event on Add Author button */
        binding.btnBookDetailAddAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Saving Book in database before switching to popup
                saveBook(book_id);
                // Popup for Author : link existing or create new) with texts and see-through background
                PopupAddListDialog popupAddListDialog = new PopupAddListDialog(getActivity());
                popupAddListDialog.setTitle(getString(R.string.AuthorPopupAddListTitle));
                popupAddListDialog.setHint(getString(R.string.AuthorPopupAddListHint));
                popupAddListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupAddListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Linking the RecyclerView
                RecyclerView recyclerView = popupAddListDialog.getRvPopupList();
                // Creating the list to display
                ArrayList<AuthorBean> authorsList = dataBaseHelper.getAuthorsList();
                // The adapter gets the list and the string value "books" needed for translations
                authorsAdapter = new AuthorsAdapter(authorsList);
                // the adapter and the layout are defined for the RecyclerView
                recyclerView.setAdapter(authorsAdapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
                // The list is submitted to the adapter
                authorsAdapter.submitList(authorsList);
                /* Authors list item click */
                authorsAdapter.setOnClickListener(new AuthorsAdapter.OnClickListener() {
                    @Override
                    public void onClick(int position, AuthorBean authorBean) {
                        // Checking if a duplicate of the pair Book-Author already exists in the Database
                        if (dataBaseHelper.checkDetainingBookAuthorPairDuplicate(book_id, authorBean.getAuthor_id())) {
                            // duplicate, error toast message
                            Toast.makeText(BookDetailFragment.super.getContext(), authorBean.getAuthor_pseudonym() + " " + getString(R.string.BookAuthorPairDuplicate), Toast.LENGTH_SHORT).show();
                        } else {
                            // Database handler called with the insertion method
                            boolean successInsertWriting = dataBaseHelper.insertIntoWriting(book_id, authorBean.getAuthor_id());
                        }
                        popupAddListDialog.dismiss();  // To close popup
                        bookDetailRefreshScreen(book_id); // To refresh display
                    }
                });
                // Click event on confirm button (new Author)
                popupAddListDialog.getBtnPopupConfirm().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // AuthorBean for the data to be stored in the database
                        AuthorBean authorBean;
                        // fetching text length from the EditText not to save an empty pseudonym
                        if (popupAddListDialog.getEtPopupText().getText().length() > 0) {
                            try {
                                // fetching text from the EditText
                                authorBean = new AuthorBean(-1, popupAddListDialog.getEtPopupText().getText().toString());
                            } catch (Exception e) {
                                // error toast message
                                Toast.makeText(getActivity(), getString(R.string.AuthorCreationError), Toast.LENGTH_SHORT).show();
                                authorBean = new AuthorBean(-1, "error" );
                            }
                            popupAddListDialog.dismiss(); // To close Popup
                                                        // new instance of Database Handler
                            dataBaseHelper = new DataBaseHelper(getActivity());
                            // insertion in AUTHORS Table
                            boolean successInsertAuteur = dataBaseHelper.insertIntoAuthors(authorBean);
                            // insertion of the Book-Author pair in WRITING Table
                            boolean successInsertEcrire = dataBaseHelper.insertIntoWriting(book_id, dataBaseHelper.getAuthorLatest(authorBean).getAuthor_id());
                        }
                        bookDetailRefreshScreen(book_id); // To refresh display
                    }
                });
                // Click event on abort button
                popupAddListDialog.getBtnPopupAbort().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupAddListDialog.dismiss(); // To close popup
                    }
                });
                popupAddListDialog.Build(); // To build the popup
                // action on popup closure -> refresh screen
                popupAddListDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        bookDetailRefreshScreen(book_id);
                    }
                });
            }
        });

        /* Click event on Editor name */
        binding.tvBookDetailEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Saving Book in database before changing fragment
                saveBook(book_id);
                // EditorBean for the data to be send to destination
                EditorBean editorBean;
                try {
                    // EditorBean gets data from clicked item
                    editorBean = dataBaseHelper.getEditorByBookId(book_id);
                    // Data bundle storing key-value pairs
                    Bundle bundle = new Bundle();
                    bundle.putInt("editor_id", editorBean.getEditor_id());
                    // go to EditorDetailFragment with the data bundle
                    findNavController(BookDetailFragment.this).navigate(R.id.action_bookDetail_to_editorDetail, bundle);
                } catch (Exception e) {
                    // id set to -1 for error handling
                    editorBean = new EditorBean(-1,"error");
                }
            }
        });

        /* Click event on Change Editor button */
        binding.btnBookDetailChangeEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Saving Book in database before switching to popup
                saveBook(book_id);
                // Popup for Editor : to link an existing Editor with texts and see-through background
                PopupListDialog popupListDialog = new PopupListDialog(getActivity());
                popupListDialog.setTitle(getString(R.string.EditorPopupListTitle));
                popupListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Linking the RecyclerView
                RecyclerView recyclerView = popupListDialog.getRvPopupList();
                // Creating the list to display
                ArrayList<EditorBean> editorsList = dataBaseHelper.getEditorsList();
                // The adapter gets the list and the string value "books" needed for translations
                editorsAdapter = new EditorsAdapter(editorsList);
                // the adapter and the layout are defined for the RecyclerView
                recyclerView.setAdapter(editorsAdapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
                // The list is submitted to the adapter
                editorsAdapter.submitList(editorsList);
                /* Editors list item click */
                editorsAdapter.setOnClickListener(new EditorsAdapter.OnClickListener() {
                    @Override
                    public void onClick(int position, EditorBean editorBean) {
                        try {
                            // updating Book Editor in the Database
                            dataBaseHelper.updateBookEditor(dataBaseHelper, editorBean, book_id);
                        } catch (Exception e) {
                            // Do nothing
                        }
                        popupListDialog.dismiss(); // To close popup
                        bookDetailRefreshScreen(book_id); // To refresh display
                    }
                });
                // Click event on abort button
                popupListDialog.getBtnPopupAbort().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupListDialog.dismiss(); // To close popup
                    }
                });
                popupListDialog.Build(); // To build the popup
                bookDetailRefreshScreen(book_id); // To refresh display
            }
        });

        /* Click event on Add Editor button */
        binding.btnBookDetailAddEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Saving Book in database before switching to popup
                saveBook(book_id);
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
                        // success indicates if data got fetched from the EditText
                        boolean success = true;
                        try {
                            // fetching text from the EditText
                            editorBean = new EditorBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                        } catch (Exception e) {
                            // error toast message
                            Toast.makeText(getActivity(), getString(R.string.EditorCreationError), Toast.LENGTH_SHORT).show();
                            // id set to -1 for error handling
                            editorBean = new EditorBean(-1, "error");
                            success = false;
                        }
                        // if the EditText isn't empty and the new Editor isn't a duplicate
                        if (dataBaseHelper.checkEditorDuplicate(editorBean.getEditor_name()) && !success) {
                            // duplicate, error toast message
                            Toast.makeText(BookDetailFragment.super.getContext(), getString(R.string.EditorDuplicateError), Toast.LENGTH_SHORT).show();
                        } else {
                            // insertion in EDITORS Table
                            boolean successInsert = dataBaseHelper.insertIntoEditors(editorBean);
                            // Update Book Editor
                            boolean successUpdate = dataBaseHelper.updateBookEditor(dataBaseHelper, dataBaseHelper.getEditorLatest(editorBean), book_id);
                        }
                        popupTextDialog.dismiss(); // To close popup
                        bookDetailRefreshScreen(book_id); // To refresh display
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
                bookDetailRefreshScreen(book_id); // To refresh display
            }
        });

        /* Click event on Serie name */
        binding.tvBookDetailSerie.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 // Saving Book in database before changing fragment
                 saveBook(book_id);
                 // SerieBean for the data to be send to destination
                 SerieBean serieBean;
                 try {
                     // SerieBean gets data from clicked item
                     serieBean = dataBaseHelper.getSerieByBookId(book_id);
                     // Data bundle storing key-value pairs
                     Bundle bundle = new Bundle();
                     bundle.putInt("serie_id", serieBean.getSerie_id());
                     bundle.putString("serie_name", serieBean.getSerie_name());
                     // go to SerieDetailFragment with the data bundle
                     findNavController(BookDetailFragment.this).navigate(R.id.action_bookDetail_to_serieDetail, bundle);
                 } catch (Exception e) {
                     // id set to -1 for error handling
                     serieBean = new SerieBean(-1,"error");
                 }
             }
        });

        /* Click event on Change Serie button */
        binding.btnBookDetailChangeSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Saving Book in database before switching to popup
                saveBook(book_id);
                // Popup for Serie : to link an existing Serie with texts and see-through background
                PopupListDialog popupListDialog = new PopupListDialog(getActivity());
                popupListDialog.setTitle(getString(R.string.SeriePopupListTitle));
                popupListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Linking the RecyclerView
                RecyclerView recyclerView = popupListDialog.getRvPopupList();
                // Creating the list to display
                ArrayList<SerieBean> seriesList = dataBaseHelper.getSeriesList();
                // The adapter gets the list and the string value "books" needed for translations
                seriesAdapter = new SeriesAdapter(seriesList);
                // the adapter and the layout are defined for the RecyclerView
                recyclerView.setAdapter(seriesAdapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
                // The list is submitted to the adapter
                seriesAdapter.submitList(seriesList);
                /* Series list item click */
                seriesAdapter.setOnClickListener(new SeriesAdapter.OnClickListener() {
                    @Override
                    public void onClick(int position, SerieBean serieBean) {
                        try {
                            // updating Book Serie in the Database
                            dataBaseHelper.updateBookSerie(dataBaseHelper, serieBean, book_id);
                        } catch (Exception e) {
                            // Do nothing
                        }
                        popupListDialog.dismiss(); // To close popup
                        bookDetailRefreshScreen(book_id); // To refresh display
                    }
                });
                // Click event on abort button
                popupListDialog.getBtnPopupAbort().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupListDialog.dismiss(); // To close popup
                    }
                });
                popupListDialog.Build(); // To build the popup
                bookDetailRefreshScreen(book_id); // To refresh display
            }
        });

        /* Click event on Add Serie button */
        binding.btnBookDetailAddSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Saving Book in database before switching to popup
                saveBook(book_id);
                // Popup for Editor creation with texts and see-through background
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
                        // success indicates if data got fetched from the EditText
                        boolean success = true;
                        try {
                            // fetching text from the EditText
                            serieBean = new SerieBean(-1, popupTextDialog.getEtPopupText().getText().toString());
                        } catch (Exception e) {
                            // error toast message
                            Toast.makeText(getActivity(), getString(R.string.SerieCreationError), Toast.LENGTH_SHORT).show();
                            // id set to -1 for error handling
                            serieBean = new SerieBean(-1, "error");
                            success = false;
                        }
                        // if the EditText isn't empty and the new Serie isn't a duplicate
                        if (dataBaseHelper.checkSerieDuplicate(serieBean.getSerie_name()) && !success) {
                            // duplicate, error toast message
                            Toast.makeText(BookDetailFragment.super.getContext(), getString(R.string.SerieDuplicateError), Toast.LENGTH_SHORT).show();
                        } else {
                            // insertion in SERIES Table
                            boolean successInsert = dataBaseHelper.insertIntoSeries(serieBean);
                            // Update Book Serie
                            boolean successUpdate = dataBaseHelper.updateBookSerie(dataBaseHelper, dataBaseHelper.getSerieLatest(serieBean), book_id);
                        }
                        popupTextDialog.dismiss(); // To close popup
                        bookDetailRefreshScreen(book_id); // To refresh display
                    }
                });
                // Click event on abort button
                popupTextDialog.getBtnPopupAbort().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupTextDialog.dismiss(); // To close Popup
                    }
                });
                popupTextDialog.build(); // To build the popup
                bookDetailRefreshScreen(book_id); // To refresh display
            }
        });

        /* Click on Author in the list (RecyclerView) */
        authorsAdapter.setOnClickListener(new AuthorsAdapter.OnClickListener() {
            @Override
            public void onClick(int position, AuthorBean authorBean) {
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("author_id", authorBean.getAuthor_id());;
                // go to AuthorDetailFragment with the data bundle
                findNavController(BookDetailFragment.this).navigate(R.id.action_bookDetail_to_authorDetail, bundle);
            }
        });

        /* Delete (Remove) Serie from Book click */
        binding.btnBookDetailDeleteSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Saving Book in database before switching to popup
                saveBook(book_id);
                // Popup for deleting a Serie
                PopupConfirmDialog popupConfirmDialog = new PopupConfirmDialog(getActivity());
                popupConfirmDialog.setTitle(getString(R.string.Book) + "\n"
                        + dataBaseHelper.getBookById(book_id).getBook_title() + "\n"
                        + getString(R.string.BookConfirmSerieRemoval) + "\n"
                        + dataBaseHelper.getSerieByBookId(book_id).getSerie_name());
                popupConfirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Click event on confirm button
                popupConfirmDialog.getBtnPopupConfirm().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupConfirmDialog.dismiss(); // To close popup
                        // Database delete Serie_id from Book
                        boolean successUpdate = dataBaseHelper.deleteBookSerie(dataBaseHelper, book_id);
                        bookDetailRefreshScreen(book_id); // To refresh display
                    }
                });
                // Click event on abort button
                popupConfirmDialog.getBtnPopupABort().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupConfirmDialog.dismiss(); // To close Popup
                    }
                });
                popupConfirmDialog.build(); // To build the popup
                bookDetailRefreshScreen(book_id); // To refresh display
            }
        });

        /* Delete (Remove) Editor from Book click */
        binding.btnBookDetailDeleteEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Saving Book in database before switching to popup
                saveBook(book_id);
                // Popup for deleting an Editor
                PopupConfirmDialog popupConfirmDialog = new PopupConfirmDialog(getActivity());
                popupConfirmDialog.setTitle(getString(R.string.Book) + "\n"
                        + dataBaseHelper.getBookById(book_id).getBook_title() + "\n"
                        + getString(R.string.BookConfirmEditorRemoval) + "\n"
                        + dataBaseHelper.getEditorByBookId(book_id).getEditor_name());
                popupConfirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Click event on confirm button
                popupConfirmDialog.getBtnPopupConfirm().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupConfirmDialog.dismiss(); // To close popup
                        // Database delete Editor_id from Book
                        boolean successUpdate = dataBaseHelper.deleteBookEditor(dataBaseHelper, book_id);
                        bookDetailRefreshScreen(book_id); // To refresh display
                    }
                });
                // Click event on abort button
                popupConfirmDialog.getBtnPopupABort().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupConfirmDialog.dismiss(); // To close Popup
                    }
                });
                popupConfirmDialog.build(); // To build the popup
                bookDetailRefreshScreen(book_id); // To refresh display
            }
        });

        /* Delete (Remove) Author click */
        binding.btnBookDetailDeleteAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Saving Book in database before switching to popup
                saveBook(book_id);
                // Popup for deleting an Author from the list
                PopupListDialog popupListDialog = new PopupListDialog(getActivity());
                popupListDialog.setTitle(getString(R.string.Book) + "\n"
                        + dataBaseHelper.getBookById(book_id).getBook_title() + "\n"
                        + getString(R.string.BookChooseAuthorRemoval));
                popupListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Linking the RecyclerView
                RecyclerView recyclerView = popupListDialog.getRvPopupList();
                // Creating the list to display
                ArrayList<AuthorBean> authorsList = dataBaseHelper.getAuthorsListByBookId(book_id);
                // The adapter gets the list and the string value "books" needed for translations
                authorsAdapter = new AuthorsAdapter(authorsList);
                // the adapter and the layout are defined for the RecyclerView
                recyclerView.setAdapter(authorsAdapter);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
                // The list is submitted to the adapter
                authorsAdapter.submitList(authorsList);
                /* Authors list item click */
                authorsAdapter.setOnClickListener(new AuthorsAdapter.OnClickListener() {
                    @Override
                    public void onClick(int position, AuthorBean authorBean) {
                        try {
                            // Database delete Book-Author pair
                            boolean successUpdate = dataBaseHelper.deleteBookAuthor(dataBaseHelper, authorBean.getAuthor_id(), book_id);
                        } catch (Exception e) {
                            // error toast message
                            Toast.makeText(getActivity(), getString(R.string.BookRemoveAuthorError), Toast.LENGTH_SHORT).show();
                        }
                        popupListDialog.dismiss(); // To close popup
                        bookDetailRefreshScreen(book_id); // To refresh display
                    }
                });
                // Click event on abort button
                popupListDialog.getBtnPopupAbort().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupListDialog.dismiss(); // To close Popup
                    }
                });
                popupListDialog.Build(); // To build the popup
                bookDetailRefreshScreen(book_id); // To refresh display
            }
        });

        /* Delete Book click */
        binding.btnBookDetailDeleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Saving Book in database before switching to popup
                saveBook(book_id);
                // Popup for deleting a Book
                PopupTextDialog popupTextDialog = new PopupTextDialog(getActivity());
                popupTextDialog.setTitle(getString(R.string.Book) + "\n"
                        + dataBaseHelper.getBookById(book_id).getBook_title() + "\n"
                        + getString(R.string.BookWarningBookDelete));
                // Hint to ask user to write title of book to be deleted
                popupTextDialog.setHint(getString(R.string.BookDeleteConfirmTitle));
                popupTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Click event on confirm button
                popupTextDialog.getBtnPopupConfirm().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // String to fetch the title written in the EditText
                        String bookConfirmDeleteTitle = "";
                        try {
                            bookConfirmDeleteTitle = popupTextDialog.getEtPopupText().getText().toString();
                        } catch (Exception e) {
                            // do nothing
                        }
                        popupTextDialog.dismiss(); // To close Popup
                        if (!dataBaseHelper.getBookById(book_id).getBook_title().equals(bookConfirmDeleteTitle)) {
                            // Title does not match
                            Toast.makeText(BookDetailFragment.super.getActivity(), getString(R.string.BookTitleDoesntMatch), Toast.LENGTH_LONG).show();
                        } else {
                            // Title does match
                            // The book is deleted from the Database (constraints too)
                            boolean successUpdate = dataBaseHelper.deleteBook(dataBaseHelper, book_id);
                            // go to BooksFragment since the BookDetail doesn't exist anymore
                            findNavController(BookDetailFragment.this).navigate(R.id.booksFragment);
                        }
                    }
                });
                // Key event, same behaviour as confirm button
                popupTextDialog.getEtPopupText().setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // if "ENTER" key is pressed
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            String bookConfirmDeleteTitle = "";
                            try {
                                bookConfirmDeleteTitle = popupTextDialog.getEtPopupText().getText().toString();
                            } catch (Exception e) {
                                // do nothing
                            }
                            popupTextDialog.dismiss(); // To close Popup
                            if (!dataBaseHelper.getBookById(book_id).getBook_title().equals(bookConfirmDeleteTitle)) {
                                // Title does not match
                                Toast.makeText(BookDetailFragment.super.getActivity(), getString(R.string.BookTitleDoesntMatch), Toast.LENGTH_LONG).show();
                            } else {
                                // Title does match
                                // The book is deleted from the Database (constraints too)
                                boolean successUpdate = dataBaseHelper.deleteBook(dataBaseHelper, book_id);
                                // go to BooksFragment since the BookDetail doesn't exist anymore
                                findNavController(BookDetailFragment.this).navigate(R.id.booksFragment);
                            }
                            return true; // inherited, necessary
                        }
                        return false; // inherited, necessary
                    }
                });
                // Click event on abort button
                popupTextDialog.getBtnPopupAbort().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupTextDialog.dismiss(); // To close Popup
                    }
                });
                popupTextDialog.build(); // To build the popup
                bookDetailRefreshScreen(book_id); // To refresh display
            }
        });

        /* ChangePicture Click */
        registerResult(book_id);
        binding.btnBookDetailChangePicture.setOnClickListener(binding -> pickImage());

        /* AddPicture Click */
        binding.btnBookDetailAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Saving Book in database before switching to popup
                saveBook(book_id);
                // Data bundle to give book_id to the Picture Fragment
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", book_id);
                // Go to SearchResultFragment with the data bundle
                findNavController(BookDetailFragment.this).navigate(R.id.action_bookDetail_to_picture, bundle);
            }
        });

        /* DeletePicture Click */
        binding.btnBookDetailDeletePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Saving Book in database before switching to popup
                saveBook(book_id);
                // Popup for deleting an Editor
                PopupConfirmDialog popupConfirmDialog = new PopupConfirmDialog(getActivity());
                popupConfirmDialog.setTitle(getString(R.string.Book) + "\n"
                        + dataBaseHelper.getBookById(book_id).getBook_title() + "\n"
                        + getString(R.string.BookConfirmEditorRemoval));
                popupConfirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Click event on confirm button
                popupConfirmDialog.getBtnPopupConfirm().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Delete old picture
                        File imgFileOld = new File(picturePath + bookBean.getBook_picture());
                        if (imgFileOld.exists()) {
                            imgFileOld.delete();
                        }
                        // Removing picture from display because of physical delete delay
                        binding.ivBookDetailPicture.setImageResource(R.drawable.books_blue);
                        popupConfirmDialog.dismiss(); // To close popup
                    }
                });
                // Click event on abort button
                popupConfirmDialog.getBtnPopupABort().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupConfirmDialog.dismiss(); // To close Popup
                    }
                });
                popupConfirmDialog.build(); // To build the popup
                bookDetailRefreshScreen(book_id); // To refresh display
            }
        });
    }

    //* ----------------------------------------------------------------------------------------- */
    //* onDestroy inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }


    //* ----------------------------------------------------------------------------------------- */
    //* Display initialization and refresh method
    //* ----------------------------------------------------------------------------------------- */
    private void bookDetailRefreshScreen(Integer book_id){
        // Bookbean from the Database thanks to ID
        BookBean bookBean = dataBaseHelper.getBookById(book_id);
        // Book Title
        binding.etBookDetailBookTitle.setText(bookBean.getBook_title());
        // Book number if exists
        if (bookBean.getBook_number() == 0 || bookBean.getBook_number() == null) {
            binding.etBookDetailBookNumber.setText("");
        } else {
            binding.etBookDetailBookNumber.setText(String.valueOf(bookBean.getBook_number()));
        }
        // Serie name
        binding.tvBookDetailSerie.setText(dataBaseHelper.getSerieByBookId(book_id).getSerie_name());
        // Book ISBN
        binding.etBookDetailISBN.setText(bookBean.getBook_isbn() == null ? "" : String.valueOf(bookBean.getBook_isbn()));
        // Editor price
        binding.etBookDetailEditorPrice.setText(bookBean.getBook_editor_price() == 0 ? "" : String.valueOf(bookBean.getBook_editor_price()));
        // Value
        binding.etBookDetailValue.setText(bookBean.getBook_value() == 0 ? "" : String.valueOf(bookBean.getBook_value()));
        // Edition date
        binding.etBookDetailEditionDate.setText(bookBean.getBook_edition_date() == null ? "" : String.valueOf(bookBean.getBook_edition_date()));
        // Autographed ?
        binding.chkBookDetailAutograph.setChecked(Boolean.valueOf(bookBean.isBook_autograph()));
        // Speciel Edition ?
        binding.chkBookDetailSpecialEdition.setChecked(Boolean.valueOf(bookBean.isBook_special_edition()));
        // Special Edition Label
        binding.etBookDetailSpecialEditionLabel.setText(bookBean.getBook_special_edition_label() == null ? "" : String.valueOf( bookBean.getBook_special_edition_label()));
        // Picture from url
        /* TODO **************************       binding.ivBookDetailPicture; */
        File imgBook = new File(picturePath + bookBean.getBook_picture());
        System.out.println(picturePath + bookBean.getBook_picture());
        if (imgBook.exists()){
            // Show book picture
            Bitmap myBitmap = BitmapFactory.decodeFile(imgBook.getAbsolutePath());
            binding.ivBookDetailPicture.setImageBitmap(myBitmap);
        }

        /* Authors list adapters charged with data */
        // Creating the list to display
        ArrayList<AuthorBean> AuthorsList = dataBaseHelper.getAuthorsListByBookId(bookBean.getBook_id());
        // The adapter gets the list and the string value "books" needed for translations
        authorsAdapter = new AuthorsAdapter(AuthorsList);
        // the adapter and the layout are defined for the RecyclerView
        binding.rvBookDetailAuthorsList.setAdapter(authorsAdapter);
        binding.rvBookDetailAuthorsList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        authorsAdapter.submitList(AuthorsList);

        // Editor Name
        binding.tvBookDetailEditor.setText(dataBaseHelper.getEditorByBookId(book_id).getEditor_name());
    }


    //* ----------------------------------------------------------------------------------------- */
    //* Saving the book
    //* ----------------------------------------------------------------------------------------- */
    private void saveBook(int book_id){
        // Bookbean from fragment items data
        BookBean bookBean = new BookBean(
                book_id,
                binding.etBookDetailBookTitle.getText().toString(),
                binding.etBookDetailBookNumber.getText().length() == 0 ? null : Integer.parseInt(binding.etBookDetailBookNumber.getText().toString()),
                binding.etBookDetailISBN.getText().toString(),
                dataBaseHelper.getBookById(book_id).getBook_picture().replace("\"", ""),
                binding.etBookDetailEditorPrice.getText().length() == 0 ? 0.0 : Double.parseDouble(binding.etBookDetailEditorPrice.getText().toString()),
                binding.etBookDetailValue.getText().length() == 0 ? 0.0 : Double.parseDouble(binding.etBookDetailValue.getText().toString()),
                binding.etBookDetailEditionDate.getText().toString(),
                binding.chkBookDetailAutograph.isChecked(),
                binding.chkBookDetailSpecialEdition.isChecked(),
                binding.etBookDetailSpecialEditionLabel.getText().toString(),
                dataBaseHelper.getSerieByBookId(book_id).getSerie_id(),
                dataBaseHelper.getEditorByBookId(book_id).getEditor_id());
        // Database update of the Book
        boolean updateOk = dataBaseHelper.updateBook(dataBaseHelper, bookBean);
        if (updateOk) {
            Toast.makeText(getActivity(), getString(R.string.BookUpdateSuccess), Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(getActivity(), getString(R.string.BookUpdateError), Toast.LENGTH_SHORT);
        }
    }

    public static void refreshGallery(Context mContext, File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
    }
    /* Glide picture picker */
//    ActivityResultLauncher<PickVisualMediaRequest> launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
//        @SuppressLint("RestrictedApi")
//        @Override
//        public void onActivityResult(Uri o) {
//            if (o == null) {
//                Toast.makeText(getContext(), "No image Selected", Toast.LENGTH_SHORT).show();
//            } else {
//                Glide.with(getApplicationContext(getContext())).load(o).into(binding.ivBookDetailPicture);
//            }
//        }
//    });

    //* ----------------------------------------------------------------------------------------- */
    //* Camera : Executor
    //* ----------------------------------------------------------------------------------------- */
    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(getContext());
    }


    //* ----------------------------------------------------------------------------------------- */
    //* CameraX configuration
    //* ----------------------------------------------------------------------------------------- */
    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();

        // CameraSelector use case
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        // Preview use case
        Preview preview = new Preview.Builder().build();
//        preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());

        // Image capture use case
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);
    }


    //* ----------------------------------------------------------------------------------------- */
    //* CameraX use
    //* ----------------------------------------------------------------------------------------- */
    private void capturePicture(int book_id) {

        ContentValues cv = new ContentValues();
        cv.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyComics");
        cv.put(MediaStore.MediaColumns.DISPLAY_NAME, "bookID_" + book_id);
        cv.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(
                        getContext().getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        cv
                ).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Toast.makeText(getActivity(), "Photo sauvegardée", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(getActivity(), "Photo erreur bordel de merde : " + exception.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
        );
    }


    ActivityResultLauncher<Intent> resultLauncher;
    private void registerResult(int book_id) {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {
                            Uri imageUri = result.getData().getData();
                            /************************************************/
                            // Put result image in the ImageView
                            binding.ivBookDetailPicture.setImageURI(imageUri);
                            //TODO Save Image
                            // Fetch last temporary picture
                            File imgToSave = new File(picturePath + "temp.jpg");
                            // Get old book picture
                            String imgToDelete = null;
                            try {
                                imgToDelete = dataBaseHelper.getBookById(book_id).getBook_picture();
                            } catch (Exception e) {
                                // no picture in database for this book
                            }
                            File imgFileOld = new File(picturePath + imgToDelete);
                            if (imgFileOld.exists()) { // The picture physically exists
                                // delete old physical image file
                                imgFileOld.delete();
                            }


                            String[] filePathColumn = {MediaStore.Images.Media.DATA};
                            Cursor cursor = getContext().getContentResolver().query(
                                    imageUri, filePathColumn, null, null, null);
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String filePath = cursor.getString(columnIndex);
                            cursor.close();
                            Bitmap imgSelected = BitmapFactory.decodeFile(filePath);
// original sizes
                            int originWidth = imgSelected.getWidth();
                            int originHeight = imgSelected.getHeight();
                            // size wanted
                            final int newWidth = 600;
                            Bitmap imgNew;
                            if(originWidth > newWidth) {
                                // reduce size if needed
                                int newHeight = originHeight / (originWidth / newWidth);
                                imgNew = Bitmap.createScaledBitmap(imgSelected, newWidth, newHeight, false);
                            } else { // size stays the same
                                imgNew = Bitmap.createScaledBitmap(imgSelected, originWidth, originHeight, false);
                            }
                            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                            imgNew.compress(Bitmap.CompressFormat.JPEG,70 , outStream);
                            String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            imgName = "BookId_" + book_id + "_" + timeStamp + ".jpg";
                            File imgResized = new File(picturePath + imgName);
                            try {
                                imgResized.createNewFile();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            //write the bytes in file
                            FileOutputStream fo = null;
                            try {
                                fo = new FileOutputStream(imgResized);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                fo.write(outStream.toByteArray());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            // Close de FileOutput
                            try {
                                fo.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            // Delete origin file
                            imgToSave.delete();
                            // update book picture in database
                            dataBaseHelper.updateBookPicture(dataBaseHelper,book_id, imgName);
                            // update gallery
                            //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(myNewFile)));
                            refreshGallery(getContext(), imgResized);



                            /************************************************/



                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "No Image selected", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );
    }
    /** Gallery pick image */
    private void pickImage() {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    public File getBitmapFile(Intent data) {
        Uri selectedImage = data.getData();
        Cursor cursor = getContext().getContentResolver().query(selectedImage, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
        cursor.moveToFirst();

        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String selectedImagePath = cursor.getString(idx);
        cursor.close();

        return new File(selectedImagePath);
    }
    private boolean writeBitmapToFile(Bitmap bitmap, File destination) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(destination);
            return bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception ex) {
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            }catch (IOException ex) {}
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }
}