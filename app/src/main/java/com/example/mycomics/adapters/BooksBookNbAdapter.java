package com.example.mycomics.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycomics.beans.BookBean;
import com.example.mycomics.databinding.RecyclerviewAdapter2colAltBinding;

import java.util.ArrayList;

public class BooksBookNbAdapter extends ListAdapter<BookBean, BooksBookNbAdapter.ViewHolder> {

    ArrayList<BookBean> list;
    String bookNumber;
    private OnClickListener onClickListener;


    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerviewAdapter2colAltBinding binding;
        public ViewHolder(RecyclerviewAdapter2colAltBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

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

    public BooksBookNbAdapter(ArrayList<BookBean> list, String bookNumber) {
        super(new Comparator());
        this.list = list;
        this.bookNumber = bookNumber;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(RecyclerviewAdapter2colAltBinding.inflate(layoutInflater));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookBean bookBean = getItem(position);
        holder.binding.tvRecyclerViewAlt2colCol1.setText(bookBean.getBook_title());
        if (bookBean.getBook_number() == 0) {
            holder.binding.tvRecyclerViewAlt2colCol2.setText("");
        } else {
            holder.binding.tvRecyclerViewAlt2colCol2.setText(bookNumber + bookBean.getBook_number());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(holder.getBindingAdapterPosition(), bookBean);
                }
            }
        });

    }
    public BookBean getItem(int position) {
        return list.get(position);
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, BookBean bookBean);
    }

}
