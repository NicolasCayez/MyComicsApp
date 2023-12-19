package com.example.mycomics.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycomics.beans.AuthorBean;
import com.example.mycomics.databinding.RecyclerviewAdapter1colBinding;

import java.util.ArrayList;

public class AuthorsAdapter extends ListAdapter<AuthorBean, AuthorsAdapter.ViewHolder> {

    ArrayList<AuthorBean> list;
    private OnClickListener onClickListener;


    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerviewAdapter1colBinding binding;
        public ViewHolder(RecyclerviewAdapter1colBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class Comparator extends DiffUtil.ItemCallback<AuthorBean> {

        @Override
        public boolean areItemsTheSame(@NonNull AuthorBean oldItem, @NonNull AuthorBean newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull AuthorBean oldItem, @NonNull AuthorBean newItem) {
            return false;
        }
    }

    public AuthorsAdapter(ArrayList<AuthorBean> list) {
        super(new Comparator());
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(RecyclerviewAdapter1colBinding.inflate(layoutInflater));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AuthorBean authorBean = getItem(position);
        holder.binding.tvRecyclerView1colCol1.setText(authorBean.getAuthor_pseudonym());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(holder.getBindingAdapterPosition(), authorBean);
                }
            }
        });

    }
    public AuthorBean getItem(int position) {
        return list.get(position);
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, AuthorBean authorBean);
    }

}
