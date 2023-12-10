package com.example.mycomics.popups;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mycomics.R;

public class PopupConfirmDialog extends Dialog {
    /* -------------------------------------- */
    // Référence to popup elements
    /* -------------------------------------- */
    private String title;
    private TextView tvPopupConfirmTitle;
    private Button btnPopupConfirmConfirm, btnPopupConfirmAbort;

    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */
    public PopupConfirmDialog(@NonNull Context context) {
        super(context, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.dialog_popup_confirm);
        this.title = "Title";
        this.tvPopupConfirmTitle = findViewById(R.id.tvPopupTitle);
        this.btnPopupConfirmConfirm = findViewById(R.id.btnPopupValider);
        this.btnPopupConfirmAbort = findViewById(R.id.btnPopupAnnuler);
    }

    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */
    public void setTitre(String title) {
        this.title = title;
    }
    public Button getBtnPopupValider() {
        return btnPopupConfirmConfirm;
    }
    public Button getBtnPopupAnnuler() {
        return btnPopupConfirmAbort;
    }

    /* -------------------------------------- */
    // Build Method
    /* -------------------------------------- */
    public void build(){
        show();
        tvPopupConfirmTitle.setText(title);
    }
}
