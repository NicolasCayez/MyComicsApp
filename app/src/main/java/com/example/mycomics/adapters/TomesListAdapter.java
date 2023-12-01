package com.example.mycomics.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mycomics.R;
import com.example.mycomics.beans.TomeBean;

import java.util.List;

public class TomesListAdapter extends ArrayAdapter<TomeBean> {

    private Context mContext;
    private int id;
    private List<TomeBean> items ;

    public TomesListAdapter(Context context, int textViewResourceId , List<TomeBean> list )
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

        TextView text1 = (TextView) mView.findViewById(R.id.tvListview_row_1col_col1);

        if(items.get(position) != null )
        {
            text1.setText(items.get(position).getTome_titre().toString());
        }

        return mView;
    }

}
