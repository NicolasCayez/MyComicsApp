package com.example.mycomics.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

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

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mycomics.R;
import com.example.mycomics.databinding.FragmentPictureBinding;
import com.example.mycomics.helpers.DataBaseHelper;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;


public class PictureFragment extends Fragment {


    //* ----------------------------------------------------------------------------------------- */
    //* View binding declaration */
    //* ----------------------------------------------------------------------------------------- */
    FragmentPictureBinding binding;


    //* ----------------------------------------------------------------------------------------- */
    //* Database handler initialization
    //* ----------------------------------------------------------------------------------------- */
    DataBaseHelper dataBaseHelper;


    //* ----------------------------------------------------------------------------------------- */
    //* Camera Provider declarations
    //* ----------------------------------------------------------------------------------------- */
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    private String picturePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/MyComics/";
    private String imgName = "";
//    private File imgFile = null;


    //* ----------------------------------------------------------------------------------------- */
    //* Empty constructor, required
    //* ----------------------------------------------------------------------------------------- */
    public PictureFragment() {
        // Required empty public constructor
    }


    //* ----------------------------------------------------------------------------------------- */
    //* onCreate inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        File imgFileOld = new File(picturePath + "temp.jpg");
        if (imgFileOld.exists()) {
            imgFileOld.delete();
        }
    }


    //* ----------------------------------------------------------------------------------------- */
    //* onCreateView inherited Method override
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPictureBinding.inflate(inflater, container, false);

        pictureRefreshScreen();

        return binding.getRoot();
    }


    //* ----------------------------------------------------------------------------------------- */
    //* onViewCreated inherited Method override
    //* ----------------------------------------------------------------------------------------- */


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataBaseHelper = new DataBaseHelper(getContext());

        /* Data sent from source fragments */
        Integer book_id = getArguments().getInt("book_id");

        /* click  take picture */
        binding.btnPictureAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete last temporary taken picture if multiple shots
                File imgFileOld = new File(picturePath + "temp.jpg");
                if (imgFileOld.exists()) {
                    imgFileOld.delete();
                }
                // Capture new picture
                capturePicture();
            }
        });

        binding.btnPictureSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch last temporary picture
                File imgToSave = new File(picturePath + "temp.jpg");
                // If there is a taken picture
                if (imgToSave.exists()){
                    // Get old book picture
                    String imgToDelete = null;
                    try {
                        imgToDelete = dataBaseHelper.getBookById(book_id).getBook_picture();
                    } catch (Exception e) {
                        // no picture in database for this book
                    }
                    if (imgToSave.length() > 0) { // the database has a picture referenced
                        File imgFileOld = new File(picturePath + imgToDelete);
                        if (imgFileOld.exists()) { // The picture physically exists
                            // delete old physical image file
                            imgFileOld.delete();
                        }
                    }
                }
                    // we'll start with the temporary picture already open to a file
                    Bitmap imgOrigin = BitmapFactory.decodeFile(imgToSave.getAbsolutePath());
                    // original sizes
                    int originWidth = imgOrigin.getWidth();
                    int originHeight = imgOrigin.getHeight();
                    // size wanted
                    final int newWidth = 600;
                    Bitmap imgNew;
                    if(originWidth > newWidth) {
                        // reduce size if needed
                        int newHeight = originHeight / (originWidth / newWidth);
                        imgNew = Bitmap.createScaledBitmap(imgOrigin, newWidth, newHeight, false);
                    } else { // size stays the same
                        imgNew = Bitmap.createScaledBitmap(imgOrigin, originWidth, originHeight, false);
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
                // Data bundle storing search string
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", book_id);
                // Go to SearchResultFragment with the data bundle
                findNavController(PictureFragment.this).navigate(R.id.action_picture_to_bookDetail, bundle);
            }
        });

        binding.btnPictureRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File imgToSave = new File(picturePath + "temp.jpg");
                if (imgToSave.exists()){
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.fromFile(imgToSave));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    //learn content provider for more info
                    OutputStream os= null;
                    try {
                        os = getContext().getContentResolver().openOutputStream(Uri.fromFile(imgToSave));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    bmp.compress(Bitmap.CompressFormat.JPEG,100,os);
                }
                pictureRefreshScreen();
            }
        });

        binding.btnPictureAbort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Data bundle storing search string
                Bundle bundle = new Bundle();
                bundle.putInt("book_id", book_id);
                // Go to SearchResultFragment with the data bundle
                findNavController(PictureFragment.this).navigate(R.id.action_picture_to_bookDetail, bundle);
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
    //* refreshScreen
    //* ----------------------------------------------------------------------------------------- */
    public void pictureRefreshScreen() {
        File img = new File(picturePath + "temp.jpg");
        if (img.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(img.getAbsolutePath());
            binding.ivPicturePreview.setImageBitmap(myBitmap);
        }
    }


    //* ----------------------------------------------------------------------------------------- */
    //* Executor
    //* ----------------------------------------------------------------------------------------- */
    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(getContext());
    }


    //* ----------------------------------------------------------------------------------------- */
    //* startCameraX
    //* ----------------------------------------------------------------------------------------- */
    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();

        // CameraSelector use case
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        // Preview use case
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(binding.pvPicture.getSurfaceProvider());

        // Image capture use case
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        cameraProvider.bindToLifecycle((LifecycleOwner) getActivity(), cameraSelector, preview, imageCapture);
    }


    //* ----------------------------------------------------------------------------------------- */
    //* CapturePicture
    //* ----------------------------------------------------------------------------------------- */
    private void capturePicture() {
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyComics");
        cv.put(MediaStore.MediaColumns.DISPLAY_NAME, "temp");
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
                        pictureRefreshScreen();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(getActivity(), "Erreur prise photo : " + exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
    public static void refreshGallery(Context mContext, File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
    }
}