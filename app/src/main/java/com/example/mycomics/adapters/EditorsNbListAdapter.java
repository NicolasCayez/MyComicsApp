package com.example.mycomics.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mycomics.R;
import com.example.mycomics.beans.EditorBean;
import com.example.mycomics.helpers.DataBaseHelper;

import java.util.List;

public class EditorsNbListAdapter extends ArrayAdapter<EditorBean> {

    private Context mContext;
    private int id;
    private List <EditorBean>items ;

    public EditorsNbListAdapter(Context context, int textViewResourceId , List<EditorBean> list )
    {
        super(context, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list ;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text1 = (TextView) mView.findViewById(R.id.tvListview_row_2col_reverse_col1);
        TextView text2 = (TextView) mView.findViewById(R.id.tvListview_row_2col_reverse_col2);
        int nbBooks = 0;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        if(items.get(position) != null )
        {
            text1.setText(items.get(position).getEditor_name());
            nbBooks = dataBaseHelper.getNbBooksByEditorId(items.get(position).getEditor_id());
            if (nbBooks > 0) {
                text2.setText(nbBooks + " books");
            }
        }

        return mView;
    }

}
