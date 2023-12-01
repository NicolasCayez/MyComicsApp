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
    // Référence vers les éléments du popup
    /* -------------------------------------- */
    private String titre;
    private TextView tvPopupTitle;
    private ListView lvPopupList;
    private Button btnPopupAnnuler;

    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */

    public PopupListDialog(@NonNull Context context) {
        super(context, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.dialog_popup_list);
        this.titre = "Mon titre";
        this.tvPopupTitle = findViewById(R.id.tvPopupTitle);
        this.lvPopupList = findViewById(R.id.lvPopupList);
        this.btnPopupAnnuler = findViewById(R.id.btnPopupAnnuler);
    }

    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */

    public void setTitre(String titre) {
        this.titre = titre;
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

    public Button getBtnPopupAnnuler() {
        return btnPopupAnnuler;
    }


    /* -------------------------------------- */
    // Méthode Build
    /* -------------------------------------- */
    public void Build(){
        show();
        tvPopupTitle.setText(titre);
    }
}
