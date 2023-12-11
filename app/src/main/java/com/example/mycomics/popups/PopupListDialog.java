package com.example.mycomics.popups;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mycomics.R;

public class PopupListDialog extends Dialog {
    /* -------------------------------------- */
    // Référence to popup elements
    /* -------------------------------------- */
    private String title;
    private TextView tvPopupTitle;
    private ListView lvPopupList;
    private Button btnPopupAbort;

    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */

    public PopupListDialog(@NonNull Context context) {
        super(context, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.dialog_popup_list);
        this.title = "Mon titre";
        this.tvPopupTitle = findViewById(R.id.tvPopupTitle);
        this.lvPopupList = findViewById(R.id.lvPopupList);
        this.btnPopupAbort = findViewById(R.id.btnPopupAnnuler);
    }

    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTvPopupTitle(TextView tvPopupTitle) {
        this.tvPopupTitle = tvPopupTitle;
    }

    public ListView getLvPopupListe() {
        return lvPopupList;
    }

    public void setLvPopupListe(ListView lvPopupListe) {
        this.lvPopupList = lvPopupListe;
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
