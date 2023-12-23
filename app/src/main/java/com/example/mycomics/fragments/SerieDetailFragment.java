package com.example.mycomics.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
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
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.mycomics.R;
import com.example.mycomics.adapters.AuthorsAdapter;
import com.example.mycomics.adapters.BooksBookNbAdapter;
import com.example.mycomics.adapters.EditorsAdapter;
import com.example.mycomics.beans.AuthorBean;
import com.example.mycomics.beans.BookBean;
import com.example.mycomics.beans.EditorBean;
import com.example.mycomics.beans.SerieBean;
import com.example.mycomics.databinding.FragmentSerieDetailBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.example.mycomics.popups.PopupConfirmDialog;
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

public class SerieDetailFragment extends Fragment {


    //* ----------------------------------------------------------------------------------------- */
    //* View binding declaration */
    //* ----------------------------------------------------------------------------------------- */
    FragmentSerieDetailBinding binding;


    //* ----------------------------------------------------------------------------------------- */
    //* Pictures path and attribute initialization
    //* ----------------------------------------------------------------------------------------- */
    final String picturePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/MyComics/";
    private String imgName = "";


    //* ----------------------------------------------------------------------------------------- */
    //* Database handler initialization
    //* ----------------------------------------------------------------------------------------- */
    DataBaseHelper dataBaseHelper;


    //* ----------------------------------------------------------------------------------------- */
    //* Adapters handling RecycleViews data display
    //* ----------------------------------------------------------------------------------------- */
    BooksBookNbAdapter booksAdapter;
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
    public SerieDetailFragment() {
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
        binding = FragmentSerieDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();    }


    //* ----------------------------------------------------------------------------------------- */
    //* onViewCreated inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* Data sent from source fragments */
        int serie_id = getArguments().getInt("serie_id");

        /* Display initialization */
        SerieBean serieBean = dataBaseHelper.getSerieById(serie_id);
        serieDetailInitialize(serieBean);

        /* Enter key after modifying the serie name */
        binding.etSerieDetailSerieName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // if "ENTER" key is pressed
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    binding.btnSerieDetailSaveSerie.performClick();
                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    return true; // inherited, necessary
                }
                return false; // inherited, necessary
            }
        });

        /* Click on Book in the list (RecyclerView) */
        booksAdapter.setOnClickListener(new BooksBookNbAdapter.OnClickListener() {
            @Override
            public void onClick(int position, BookBean bookBean) {
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", bookBean.getBook_id());;
                // go to AuthorDetailFragment with the data bundle
                findNavController(SerieDetailFragment.this).navigate(R.id.action_serieDetail_to_bookDetail, bundle);
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
                findNavController(SerieDetailFragment.this).navigate(R.id.action_serieDetail_to_authorDetail, bundle);
            }
        });

        /* Click on Editor in the list (RecyclerView) */
        editorsAdapter.setOnClickListener(new EditorsAdapter.OnClickListener() {
            @Override
            public void onClick(int position, EditorBean editorBean) {
                // Data bundle storing key-value pairs
                Bundle bundle = new Bundle();
                bundle.putInt("editor_id", editorBean.getEditor_id());;
                // go to AuthorDetailFragment with the data bundle
                findNavController(SerieDetailFragment.this).navigate(R.id.action_serieDetail_to_editorDetail, bundle);
            }
        });

        /* Delete Serie click */
        binding.btnSerieDetailDeleteSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Popup for deleting a Serie
                PopupTextDialog popupTextDialog = new PopupTextDialog(getActivity());
                popupTextDialog.setTitle(getString(R.string.SerieDeleteConfirmName) + "\n\""
                        + dataBaseHelper.getSerieById(serie_id).getSerie_name() + "\"");
                popupTextDialog.setHint(getString(R.string.SerieDetailTitle));
                popupTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Click event on confirm button
                popupTextDialog.getBtnPopupConfirm().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String serieConfirmDeleteName = "";
                        try {
                            serieConfirmDeleteName = popupTextDialog.getEtPopupText().getText().toString();
                        } catch (Exception e) {
                            // do nothing
                        }
                        popupTextDialog.dismiss(); // To close Popup
                        if (!dataBaseHelper.getSerieById(serie_id).getSerie_name().equals(serieConfirmDeleteName)) {
                            // Name does not match
                            Toast.makeText(SerieDetailFragment.super.getActivity(), getString(R.string.SerieNameDoesntMatch), Toast.LENGTH_LONG).show();
                        } else {
                            // Name does match
                            // The serie is deleted from the Database (constraints too)
                            boolean successUpdate = dataBaseHelper.deleteSerie(dataBaseHelper, serie_id);
                            // go to BooksFragment since the BookDetail doesn't exist anymore
                            findNavController(SerieDetailFragment.this).navigate(R.id.seriesFragment);
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
                            popupTextDialog.getBtnPopupConfirm().performClick();
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
                serieDetailReload(serie_id); // To refresh display
            }
        });

        /* Click event on Save Book button */
        binding.btnSerieDetailSaveSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSerie(serie_id);
            }
        });

        /* ChangePicture Click */
        registerResult(serie_id);
        binding.btnSerieDetailChangePicture.setOnClickListener(binding -> pickImage());

        /* AddPicture Click */
        binding.btnSerieDetailAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Saving Serie in database before switching to popup
                saveSerie(serie_id);
                // Data bundle to give serie_id to the Picture Fragment
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", -1);
                bundle.putInt("serie_id", serie_id);
                // Go to SearchResultFragment with the data bundle
                findNavController(SerieDetailFragment.this).navigate(R.id.action_serieDetail_to_picture, bundle);
            }
        });

        /* DeletePicture Click */
        binding.btnSerieDetailDeletePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Saving Book in database before switching to popup
                saveSerie(serie_id);
                // Popup for deleting a Picture
                PopupConfirmDialog popupConfirmDialog = new PopupConfirmDialog(getActivity());
                popupConfirmDialog.setTitle(getString(R.string.Serie) + "\n"
                        + dataBaseHelper.getSerieById(serie_id).getSerie_name() + "\n"
                        + getString(R.string.SerieConfirmPictureRemoval));
                popupConfirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // Click event on confirm button
                popupConfirmDialog.getBtnPopupConfirm().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Delete old picture
                        File imgFileOld = new File(picturePath + "SerieId_" + serie_id + ".jpg");
                        if (imgFileOld.exists()) {
                            imgFileOld.delete();
                        }
                        // Removing picture from display because of physical delete delay
                        popupConfirmDialog.dismiss(); // To close popup
                        serieDetailReload(serie_id); // To refresh display
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
                serieDetailReload(serie_id); // To refresh display
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
    private void serieDetailInitialize(SerieBean serieBean) {
        // Serie Name
        binding.etSerieDetailSerieName.setText(serieBean.getSerie_name());

        //SeriePicture
        binding.ivSerieDetailPicture.setImageResource(R.drawable.series);
        File imgSerie = new File(picturePath + "SerieId_" + serieBean.getSerie_id() + ".jpg");
        System.out.println(picturePath + "SerieId_" + serieBean.getSerie_id() + ".jpg");
        if (imgSerie.exists()){
            // Show book picture
            Bitmap myBitmap = BitmapFactory.decodeFile(imgSerie.getAbsolutePath());
            binding.ivSerieDetailPicture.setImageBitmap(myBitmap);
        }

        /* Books list adapters charged with data */
        // Creating the list to display
        ArrayList<BookBean> BooksList = dataBaseHelper.getBooksListBySerieId(serieBean.getSerie_id());
        // The adapter gets the list and the string value "books" needed for translations
        booksAdapter = new BooksBookNbAdapter(BooksList, getString(R.string.BookNumber));
        // the adapter and the layout are defined for the RecyclerView
        binding.rvSerieDetailBooksList.setAdapter(booksAdapter);
        binding.rvSerieDetailBooksList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        booksAdapter.submitList(BooksList);

        // Authors list adapters charger with data
        // Creating the list to display
        ArrayList<AuthorBean> AuthorsList = dataBaseHelper.getAuthorsListBySerieId(serieBean.getSerie_id());
        // The adapter gets the list and the string value "books" needed for translations
        authorsAdapter = new AuthorsAdapter(AuthorsList);
        // the adapter and the layout are defined for the RecyclerView
        binding.rvSerieDetailAuthorsList.setAdapter(authorsAdapter);
        binding.rvSerieDetailAuthorsList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        authorsAdapter.submitList(AuthorsList);

        /* Editors list adapters charger with data */
        // Creating the list to display
        ArrayList<EditorBean> EditorsList = dataBaseHelper.getEditorsListBySerieId(serieBean.getSerie_id());
        // The adapter gets the list and the string value "books" needed for translations
        editorsAdapter = new EditorsAdapter(EditorsList);
        // the adapter and the layout are defined for the RecyclerView
        binding.rvSerieDetailEditorsList.setAdapter(editorsAdapter);
        binding.rvSerieDetailEditorsList.setLayoutManager(new GridLayoutManager(getContext(),1));
        // The list is submitted to the adapter
        editorsAdapter.submitList(EditorsList);
    }
    private void serieDetailReload(Integer serie_id) {
        // Data bundle storing key-value pairs
        Bundle bundle = new Bundle();
        bundle.putInt("serie_id", serie_id);
        // go to AuthorDetailFragment with the data bundle
        findNavController(SerieDetailFragment.this).navigate(R.id.serieDetailFragment, bundle);
    }

    //* ----------------------------------------------------------------------------------------- */
    //* Saving the Serie
    //* ----------------------------------------------------------------------------------------- */
    private void saveSerie(int serie_id){
        // SerieBean from fragment items data
        SerieBean serieBean = new SerieBean(
                serie_id,
                binding.etSerieDetailSerieName.getText().toString());
        // Database update of the Serie
        boolean updateOk = dataBaseHelper.updateSerie(dataBaseHelper, serieBean);
        if (updateOk) {
            Toast.makeText(getActivity(), getString(R.string.SerieUpdateSuccess), Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(getActivity(), getString(R.string.SerieUpdateError), Toast.LENGTH_SHORT);
        }
    }


    //* ----------------------------------------------------------------------------------------- */
    //* Refresh gallery to keep phone gallery up to date
    //* ----------------------------------------------------------------------------------------- */
    public static void refreshGallery(Context mContext, File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
    }


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

        cameraProvider.bindToLifecycle( this, cameraSelector, preview, imageCapture);
    }


    //* ----------------------------------------------------------------------------------------- */
    //* CameraX use
    //* ----------------------------------------------------------------------------------------- */
    private void capturePicture(int book_id) {

        ContentValues cv = new ContentValues();
        cv.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyComics");
        cv.put(MediaStore.MediaColumns.DISPLAY_NAME, "SerieId_" + book_id);
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
    private void registerResult(int serie_id) {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {
                            Uri imageUri = result.getData().getData();
                            // Put result image in the ImageView
                            binding.ivSerieDetailPicture.setImageURI(imageUri);
                            //TODO Save Image
                            // Fetch last temporary picture
                            File imgToSave = new File(picturePath + "temp.jpg");
                            // Get old serie picture
                            String imgToDelete = null;
                            try {
                                imgToDelete = "SerieId_" + serie_id;
                            } catch (Exception e) {
                                // no picture for this serie
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
                            imgName = "SerieId_" + serie_id + ".jpg";
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
                            // update gallery
                            refreshGallery(getContext(), imgResized);
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
            }catch (IOException ex) {
                // Do nothing
            }
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