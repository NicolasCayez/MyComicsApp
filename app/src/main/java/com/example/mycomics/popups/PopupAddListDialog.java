package com.example.mycomics.popups;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mycomics.R;


public class PopupAddListDialog extends Dialog {
    /* -------------------------------------- */
    // Référence to popup elements
    /* -------------------------------------- */
    private String title;
    private String hint;
    private TextView tvPopupTitle;
    private EditText etPopupText;
    private ListView lvPopupList;
    private Button btnPopupConfirm, btnPopupAbort;

    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */

    public PopupAddListDialog(@NonNull Context context) {
        super(context, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.dialog_popup_txt_list);
        this.title = "Title";
        this.tvPopupTitle = findViewById(R.id.tvPopupTitle);
        this.etPopupText = findViewById(R.id.etPopupText);
        this.lvPopupList = findViewById(R.id.lvPopupList);
        this.btnPopupConfirm = findViewById(R.id.btnPopupValider);
        this.btnPopupAbort = findViewById(R.id.btnPopupAnnuler);
    }

    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */
    public void setTitre(String title) {
        this.title = title;
    }
    public void setHint(String hint) {
        this.hint = hint;
    }

    public EditText getEtPopupText() {
        return etPopupText;
    }


    public TextView getTvPopupTitle() {
        return tvPopupTitle;
    }
    public void setTvPopupTitle(TextView tvPopupTitle) {
        this.tvPopupTitle = tvPopupTitle;
    }

    public ListView getLvPopupList() {
        return lvPopupList;
    }
    public void setLvPopupList(ListView lvPopupList) {
        this.lvPopupList = lvPopupList;
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
    public void Build(){
        show();
        tvPopupTitle.setText(title);
    }
}
