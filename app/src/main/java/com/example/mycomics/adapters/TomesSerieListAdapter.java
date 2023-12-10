package com.example.mycomics.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mycomics.R;
import com.example.mycomics.beans.BookSerieBean;

import java.util.List;

public class TomesSerieListAdapter extends ArrayAdapter<BookSerieBean> {

    private Context mContext;
    private int id;
    private List<BookSerieBean> items ;

    public TomesSerieListAdapter(Context context, int textViewResourceId , List<BookSerieBean> list )
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
            text1.setText(items.get(position).getSerie_name());
            if (items.get(position).getBook_number() == 0) {
                text2.setText("");
            }else{
                text1.setText(items.get(position).getSerie_name());
                text2.setText("T "+String.valueOf(items.get(position).getBook_number()));
            }
            text3.setText(items.get(position).getBook_title());
        }

        return mView;
    }

}
