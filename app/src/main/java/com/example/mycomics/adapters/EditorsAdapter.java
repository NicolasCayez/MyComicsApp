package com.example.mycomics.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycomics.beans.EditorBean;
import com.example.mycomics.databinding.RecyclerviewAdapter1colBinding;

import java.util.ArrayList;

public class EditorsAdapter extends ListAdapter<EditorBean, EditorsAdapter.ViewHolder> {

    ArrayList<EditorBean> list;
    private OnClickListener onClickListener;


    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerviewAdapter1colBinding binding;
        public ViewHolder(RecyclerviewAdapter1colBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class Comparator extends DiffUtil.ItemCallback<EditorBean> {

        @Override
        public boolean areItemsTheSame(@NonNull EditorBean oldItem, @NonNull EditorBean newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull EditorBean oldItem, @NonNull EditorBean newItem) {
            return false;
        }
    }

    public EditorsAdapter(ArrayList<EditorBean> list) {
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
        EditorBean editorBean = getItem(position);
        holder.binding.tvRecyclerView1colCol1.setText(editorBean.getEditor_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(holder.getBindingAdapterPosition(), editorBean);
                }
            }
        });

    }
    public EditorBean getItem(int position) {
        return list.get(position);
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, EditorBean editorBean);
    }

}
