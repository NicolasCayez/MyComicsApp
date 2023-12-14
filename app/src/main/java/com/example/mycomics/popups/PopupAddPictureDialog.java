package com.example.mycomics.popups;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mycomics.R;


public class PopupAddPictureDialog extends Dialog {
    private String title;
    private View preview;
    private Button btnPopupConfirm, btnPopupAbort;
    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */
    public PopupAddPictureDialog(@NonNull Context context) {
        super(context, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.dialog_popup_add_picture);
        this.title = "Title";
        this.preview = findViewById(R.id.pvPopupPreview);
        this.btnPopupConfirm = findViewById(R.id.btnPopupConfirm);
        this.btnPopupAbort = findViewById(R.id.btnPopupAbort);
    }
    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public View getPreview() {
        return preview;
    }

    public void setPreview(View preview) {
        this.preview = preview;
    }

    public Button getBtnPopupConfirm() {
        return btnPopupConfirm;
    }

    public void setBtnPopupConfirm(Button btnPopupConfirm) {
        this.btnPopupConfirm = btnPopupConfirm;
    }

    public Button getBtnPopupAbort() {
        return btnPopupAbort;
    }

    public void setBtnPopupAbort(Button btnPopupAbort) {
        this.btnPopupAbort = btnPopupAbort;
    }

    /* -------------------------------------- */
    // Build Method
    /* -------------------------------------- */
    public void Build(){
        show();
    }
}
