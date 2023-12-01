package com.example.mycomics.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mycomics.R;
import com.example.mycomics.beans.AuteurBean;
import com.example.mycomics.helpers.DataBaseHelper;

import java.util.List;

public class AuteursNbListAdapter extends ArrayAdapter<AuteurBean> {

    private Context mContext;
    private int id;
    private List<AuteurBean> items ;

    public AuteursNbListAdapter(Context context, int textViewResourceId , List<AuteurBean> list )
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
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        int nbTomes = 0;
        if(items.get(position) != null )
        {
            text1.setText(items.get(position).getAuteur_pseudo());
            nbTomes = dataBaseHelper.nbTomesSelonAuteurId(items.get(position).getAuteur_id());
            if (nbTomes > 0) {
                text2.setText(nbTomes + " tomes");
            }
        }

        return mView;
    }

}
