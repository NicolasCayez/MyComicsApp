package com.example.mycomics.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mycomics.R;
import com.example.mycomics.beans.TomeBean;
import com.example.mycomics.beans.TomeSerieBean;

import java.util.List;

public class TomesSerieListAdapter extends ArrayAdapter<TomeSerieBean> {

    private Context mContext;
    private int id;
    private List<TomeSerieBean> items ;

    public TomesSerieListAdapter(Context context, int textViewResourceId , List<TomeSerieBean> list )
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

        TextView text1 = (TextView) mView.findViewById(R.id.tvListview_row_3col_col1);
        TextView text2 = (TextView) mView.findViewById(R.id.tvListview_row_3col_col2);
        TextView text3 = (TextView) mView.findViewById(R.id.tvListview_row_3col_col3);

        if(items.get(position) != null )
        {
            text1.setText(items.get(position).getSerie_nom());
            if (items.get(position).getTome_numero() == 0) {
                text2.setText("");
            }else{
                text1.setText(items.get(position).getSerie_nom());
                text2.setText("T "+String.valueOf(items.get(position).getTome_numero()));
            }
            text3.setText(items.get(position).getTome_titre());
        }

        return mView;
    }

}
