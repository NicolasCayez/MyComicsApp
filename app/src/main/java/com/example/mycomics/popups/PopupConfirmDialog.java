package com.example.mycomics.popups;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mycomics.R;

public class PopupConfirmDialog extends Dialog {
    /* -------------------------------------- */
    // Référence vers les éléments du popup
    /* -------------------------------------- */
    private String titre;
    private TextView tvPopupConfirmTitle;
    private Button btnPopupConfirmValider, btnPopupConfirmAnnuler;

    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */
    public PopupConfirmDialog(@NonNull Context context) {
        super(context, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.dialog_popup_confirm);
        this.titre = "Mon titre";
        this.tvPopupConfirmTitle = findViewById(R.id.tvPopupTitle);
        this.btnPopupConfirmValider = findViewById(R.id.btnPopupValider);
        this.btnPopupConfirmAnnuler = findViewById(R.id.btnPopupAnnuler);
    }

    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public Button getBtnPopupValider() {
        return btnPopupConfirmValider;
    }
    public Button getBtnPopupAnnuler() {
        return btnPopupConfirmAnnuler;
    }

    /* -------------------------------------- */
    // Méthode build
    /* -------------------------------------- */
    public void build(){
        show();
        tvPopupConfirmTitle.setText(titre);
    }
}
