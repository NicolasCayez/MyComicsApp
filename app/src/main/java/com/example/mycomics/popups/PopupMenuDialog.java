package com.example.mycomics.popups;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.mycomics.R;

public class PopupMenuDialog extends Dialog {



    /* -------------------------------------- */
    // Constructor
    /* -------------------------------------- */

    public PopupMenuDialog(@NonNull Context context) {
        super(context, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.dialog_popup_menu);
    }
    /* -------------------------------------- */
    // Get/Set
    /* -------------------------------------- */


    /* -------------------------------------- */
    // Méthode build
    /* -------------------------------------- */
    public void build(){
        show();

    }
}