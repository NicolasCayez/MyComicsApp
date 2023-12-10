package com.example.mycomics.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mycomics.R;
import com.example.mycomics.beans.BookBean;

import java.util.List;

public class TomesNumeroListAdapter extends ArrayAdapter<BookBean> {

    private Context mContext;
    private int id;
    private List<BookBean> items ;

    public TomesNumeroListAdapter(Context context, int textViewResourceId , List<BookBean> list )
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

        TextView text1 = (TextView) mView.findViewById(R.id.tvListview_row_2col_col1);
        TextView text2 = (TextView) mView.findViewById(R.id.tvListview_row_2col_col2);

        if(items.get(position) != null )
        {
            if (items.get(position).getBook_number() != null && items.get(position).getBook_number() > 0) {
                text1.setText(items.get(position).getBook_number().toString());
            }
            text2.setText(items.get(position).getBook_title().toString());
        }

        return mView;
    }

}
