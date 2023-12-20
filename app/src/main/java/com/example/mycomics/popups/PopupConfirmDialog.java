package com.example.mycomics.popups;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mycomics.R;

public class PopupConfirmDialog extends Dialog {

    //* ----------------------------------------------------------------------------------------- */
    // Référence to popup elements in XML
    //* ----------------------------------------------------------------------------------------- */
    private String title;
    private TextView tvPopupConfirmTitle;
    private Button btnPopupConfirmConfirm, btnPopupConfirmAbort;


    //* ----------------------------------------------------------------------------------------- */
    // Constructor
    //* ----------------------------------------------------------------------------------------- */
    public PopupConfirmDialog(@NonNull Context context) {
        super(context, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.dialog_popup_confirm);
        this.title = "Title";
        this.tvPopupConfirmTitle = findViewById(R.id.tvPopupTitle);
        this.btnPopupConfirmConfirm = findViewById(R.id.btnPopupConfirm);
        this.btnPopupConfirmAbort = findViewById(R.id.btnPopupAbort);
    }


    //* ----------------------------------------------------------------------------------------- */
    // Get/Set
    //* ----------------------------------------------------------------------------------------- */
    public void setTitle(String title) {
        this.title = title;
    }
    public Button getBtnPopupConfirm() {
        return btnPopupConfirmConfirm;
    }
    public Button getBtnPopupABort() {
        return btnPopupConfirmAbort;
    }


    //* ----------------------------------------------------------------------------------------- */
    // Build Method
    //* ----------------------------------------------------------------------------------------- */
    public void build(){
        show();
        tvPopupConfirmTitle.setText(title);
    }
}
