package com.example.mycomics.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycomics.beans.BookBean;
import com.example.mycomics.databinding.RecyclerviewAdapter2colBinding;

import java.util.ArrayList;

public class BooksAdapter extends ListAdapter<BookBean, BooksAdapter.ViewHolder> {

    // List in the RecyclerView
    final ArrayList<BookBean> list;
    // Click listener to use like OnItemClickListener in ListViews
    private OnClickListener onClickListener;


    //* ----------------------------------------------------------------------------------------- */
    //* Constructor
    //* ----------------------------------------------------------------------------------------- */
    public BooksAdapter(ArrayList<BookBean> list) {
        super(new Comparator());
        this.list = list;
    }


    //* ----------------------------------------------------------------------------------------- */
    //* Comparator - To generate a list sized for the RecyclerView and save memory
    //* ----------------------------------------------------------------------------------------- */
    static class Comparator extends DiffUtil.ItemCallback<BookBean> {
        @Override
        public boolean areItemsTheSame(@NonNull BookBean oldItem, @NonNull BookBean newItem) {
            return false;
        }
        @Override
        public boolean areContentsTheSame(@NonNull BookBean oldItem, @NonNull BookBean newItem) {
            return false;
        }
    }


    //* ----------------------------------------------------------------------------------------- */
    //* ViewHolder - Displays the list given by the Comparator
    //* ----------------------------------------------------------------------------------------- */
    static class ViewHolder extends RecyclerView.ViewHolder {
        // Binding the template to be inflated in the layout
        RecyclerviewAdapter2colBinding binding;
        public ViewHolder(RecyclerviewAdapter2colBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    //* ----------------------------------------------------------------------------------------- */
    //* Method onCreateViewHolder - To inflate the Layout with the chosen XML template
    //* ----------------------------------------------------------------------------------------- */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(RecyclerviewAdapter2colBinding.inflate(layoutInflater));
    }


    //* ----------------------------------------------------------------------------------------- */
    //* Method onBindViewHolder - The RecyclerView row gets the data and the OnClickListener
    //* ----------------------------------------------------------------------------------------- */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // The item at this position is saved as a bean
        BookBean bookBean = getItem(position);
        // The data is given to the layout items
        holder.binding.tvRecyclerView2colCol1.setText(bookBean.getBook_title());
        // The OnclickListener for this position is linked to the data (bean)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(holder.getBindingAdapterPosition(), bookBean);
                }
            }
        });
    }


    //* ----------------------------------------------------------------------------------------- */
    //* Method getItem - returns the current item at the position for the OnClickListener
    //* ----------------------------------------------------------------------------------------- */
    public BookBean getItem(int position) {
        return list.get(position);
    }


    //* ----------------------------------------------------------------------------------------- */
    //* Method setOnClickListener Constructor
    //* ----------------------------------------------------------------------------------------- */
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    //* ----------------------------------------------------------------------------------------- */
    //* Interface OncLickListener, returns the clicked Item
    //* ----------------------------------------------------------------------------------------- */
    public interface OnClickListener {
        void onClick(int position, BookBean bookBean);
    }
}
