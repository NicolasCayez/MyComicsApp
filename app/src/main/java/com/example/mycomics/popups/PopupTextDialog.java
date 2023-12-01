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
    // Référence vers les éléments du popup
    /* -------------------------------------- */
    private String titre;
    private String hint;
    private TextView tvPopupTitle;
    private EditText etPopupText;
    private Button btnPopupValider, btnPopupAnnuler;

    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */
    public PopupTextDialog(@NonNull Context context) {
        super(context, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.dialog_popup_txt);
        this.titre = "Mon titre";
        this.hint = "texte";
        this.tvPopupTitle = findViewById(R.id.tvPopupTitle);
        this.etPopupText = findViewById(R.id.etPopupText);
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
    public Button getBtnPopupValider() {
        return btnPopupValider;
    }
    public Button getBtnPopupAnnuler() {
        return btnPopupAnnuler;
    }

    /* -------------------------------------- */
    // Méthode build
    /* -------------------------------------- */
    public void build(){
        show();
        tvPopupTitle.setText(titre);
        etPopupText.setHint(hint);
    }
}
