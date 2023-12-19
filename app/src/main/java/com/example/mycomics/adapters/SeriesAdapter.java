package com.example.mycomics.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycomics.beans.SerieBean;
import com.example.mycomics.databinding.RecyclerviewAdapter2colBinding;

import java.util.ArrayList;

public class SeriesAdapter extends ListAdapter<SerieBean, SeriesAdapter.ViewHolder> {

    ArrayList<SerieBean> list;
    private OnClickListener onClickListener;


    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerviewAdapter2colBinding binding;
        public ViewHolder(RecyclerviewAdapter2colBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class Comparator extends DiffUtil.ItemCallback<SerieBean> {

        @Override
        public boolean areItemsTheSame(@NonNull SerieBean oldItem, @NonNull SerieBean newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull SerieBean oldItem, @NonNull SerieBean newItem) {
            return false;
        }
    }

    public SeriesAdapter(ArrayList<SerieBean> list) {
        super(new Comparator());
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(RecyclerviewAdapter2colBinding.inflate(layoutInflater));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SerieBean serieBean = getItem(position);
        holder.binding.tvRecyclerView2colCol1.setText(serieBean.getSerie_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(holder.getBindingAdapterPosition(), serieBean);
                }
            }
        });

    }
    public SerieBean getItem(int position) {
        return list.get(position);
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, SerieBean serieBean);
    }

}
