package com.example.mycomics.popups;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mycomics.R;


public class PopupTextDialog extends Dialog {
    /* -------------------------------------- */
    // Référence to popup elements
    /* -------------------------------------- */
    private String title;
    private String hint;
    private TextView tvPopupTitle;
    private EditText etPopupText;
    private Button btnPopupConfirm, btnPopupAbort;

    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */
    public PopupTextDialog(@NonNull Context context) {
        super(context, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.dialog_popup_txt);
        this.title = "Title";
        this.hint = "Hint";
        this.tvPopupTitle = findViewById(R.id.tvPopupTitle);
        this.etPopupText = findViewById(R.id.etPopupText);
        this.btnPopupConfirm = findViewById(R.id.btnPopupConfirm);
        this.btnPopupAbort = findViewById(R.id.btnPopupAbort);
    }

    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */
    public void setTitle(String title) {
        this.title = title;
    }
    public void setHint(String hint) {
        this.hint = hint;
    }
    public EditText getEtPopupText() {
        return etPopupText;
    }
    public Button getBtnPopupConfirm() {
        return btnPopupConfirm;
    }
    public Button getBtnPopupAbort() {
        return btnPopupAbort;
    }

    /* -------------------------------------- */
    // Build Method
    /* -------------------------------------- */
    public void build(){
        show();
        tvPopupTitle.setText(title);
        etPopupText.setHint(hint);
    }
}
