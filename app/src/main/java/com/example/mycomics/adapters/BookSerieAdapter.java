package com.example.mycomics.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycomics.beans.BookSerieBean;
import com.example.mycomics.databinding.RecyclerviewAdapter2lines2colBinding;

import java.util.ArrayList;

public class BookSerieAdapter extends ListAdapter<BookSerieBean, BookSerieAdapter.ViewHolder> {

    ArrayList<BookSerieBean> list;
    String bookNumber;
    private OnClickListener onClickListener;


    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerviewAdapter2lines2colBinding binding;
        public ViewHolder(RecyclerviewAdapter2lines2colBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class Comparator extends DiffUtil.ItemCallback<BookSerieBean> {

        @Override
        public boolean areItemsTheSame(@NonNull BookSerieBean oldItem, @NonNull BookSerieBean newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull BookSerieBean oldItem, @NonNull BookSerieBean newItem) {
            return false;
        }
    }

    public BookSerieAdapter(ArrayList<BookSerieBean> list, String bookNumber) {
        super(new Comparator());
        this.list = list;
        this.bookNumber = bookNumber;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(RecyclerviewAdapter2lines2colBinding.inflate(layoutInflater));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookSerieBean bookSerieBean = getItem(position);
        holder.binding.tvRecyclerView2lines2colLine1col1.setText(bookSerieBean.getSerie_name());
        if (bookSerieBean.getBook_number() == 0) {
            holder.binding.tvRecyclerView2lines2colLine1col2.setText("");
        } else {
            holder.binding.tvRecyclerView2lines2colLine1col2.setText(bookNumber + bookSerieBean.getBook_number());
        }
        holder.binding.tvRecyclerView2lines2colLine2.setText(bookSerieBean.getBook_title());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(holder.getBindingAdapterPosition(), bookSerieBean);
                }
            }
        });

    }
    public BookSerieBean getItem(int position) {
        return list.get(position);
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, BookSerieBean bookSerieBean);
    }

}
