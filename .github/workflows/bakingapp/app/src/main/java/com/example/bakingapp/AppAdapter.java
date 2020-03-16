package com.example.bakingapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppAdapter <D, VH extends com.example.bakingapp.viewholder.AppViewHolder<D>> extends RecyclerView.Adapter<VH> {
    private List<D> mData = null;
    private final AdapterOnClickListener LISTENER;
    private final int LAYOUT_ID;

    public interface AdapterOnClickListener {
        void onClick(int position);
    }

    public AppAdapter(int layout, AdapterOnClickListener listener) {
        this.LAYOUT_ID = layout;
        this.LISTENER = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Ugly type casting.
        return (VH) com.example.bakingapp.viewholder.ViewHolderFactory.createViewHolder(inflater, parent, this.LAYOUT_ID, LISTENER);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (mData != null && position < mData.size()) {
            holder.bind(mData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (this.mData == null) {
            return 0;
        }
        else {
            return this.mData.size();
        }
    }

    public void setData(List<D> data) {
        if (data != null) {
            this.mData = data;
            notifyDataSetChanged();
        }
    }

    public D get(int index) {
        if (index < 0 || index >= this.mData.size()) {
            return null;
        }
        else {
            return this.mData.get(index);
        }
    }
}
