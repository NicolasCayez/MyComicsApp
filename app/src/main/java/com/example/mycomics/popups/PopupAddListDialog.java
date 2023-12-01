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
    // Référence vers les éléments du popup
    /* -------------------------------------- */
    private String titre;
    private String hint;
    private TextView tvPopupTitle;
    private EditText etPopupText;
    private ListView lvPopupList;
    private Button btnPopupValider, btnPopupAnnuler;

    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */

    public PopupAddListDialog(@NonNull Context context) {
        super(context, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.dialog_popup_txt_list);
        this.titre = "Mon titre";
        this.tvPopupTitle = findViewById(R.id.tvPopupTitle);
        this.etPopupText = findViewById(R.id.etPopupText);
        this.lvPopupList = findViewById(R.id.lvPopupList);
        this.btnPopupValider = findViewById(R.id.btnPopupValider);
        this.btnPopupAnnuler = findViewById(R.id.btnPopupAnnuler);
    }

    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */
    public void setTitre(String titre) {
        this.titre = titre;
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
    public Button getBtnPopupValider() {
        return btnPopupValider;
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
