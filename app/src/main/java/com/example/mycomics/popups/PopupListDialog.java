package com.example.mycomics.popups;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycomics.R;

public class PopupListDialog extends Dialog {

    //* ----------------------------------------------------------------------------------------- */
    // Référence to popup elements in XML
    //* ----------------------------------------------------------------------------------------- */
    private String title;
    private TextView tvPopupTitle;
    private RecyclerView rvPopupList;
    private Button btnPopupAbort;


    //* ----------------------------------------------------------------------------------------- */
    // Constructor
    //* ----------------------------------------------------------------------------------------- */
    public PopupListDialog(@NonNull Context context) {
        super(context, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.dialog_popup_list);
        this.title = "Mon titre";
        this.tvPopupTitle = findViewById(R.id.tvPopupTitle);
        this.rvPopupList = findViewById(R.id.rvPopupList);
        this.btnPopupAbort = findViewById(R.id.btnPopupAbort);
    }


    //* ----------------------------------------------------------------------------------------- */
    // Get/Set
    //* ----------------------------------------------------------------------------------------- */
    public void setTitle(String title) {
        this.title = title;
    }
    public void setTvPopupTitle(TextView tvPopupTitle) {
        this.tvPopupTitle = tvPopupTitle;
    }
    public Button getBtnPopupAbort() {
        return btnPopupAbort;
    }
    public RecyclerView getRvPopupList() {
        return rvPopupList;
    }
    public void setRvPopupList(RecyclerView rvPopupList) {
        this.rvPopupList = rvPopupList;
    }


    //* ----------------------------------------------------------------------------------------- */
    // Build Method
    //* ----------------------------------------------------------------------------------------- */
    public void Build(){
        show();
        tvPopupTitle.setText(title);
    }
}
