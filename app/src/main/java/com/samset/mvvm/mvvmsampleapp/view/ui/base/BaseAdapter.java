package com.samset.mvvm.mvvmsampleapp.view.ui.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.test.mvvmsampleapp.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Copyright (C) Mvvm-android-master - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential.
 * <p>
 * Created by samset on 26/03/19 at 12:54 PM for Mvvm-android-master .
 */


public abstract class BaseAdapter<V> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private boolean isFooterEnabled = false;

    abstract protected ArrayList<V> getDataList();

    abstract protected RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent, int viewType);

    abstract protected void bindItemViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM) {
            viewHolder = createItemViewHolder(parent, viewType);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaseAdapter.ProgressViewHolder) {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        } else {
            bindItemViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (getDataList() == null) {
            return 0;
        }
        return isFooterEnabled ? getDataList().size() + 1 : getDataList().size();
    }


    @Override
    public int getItemViewType(int position) {
        return (isFooterEnabled && position == getDataList().size()) ? VIEW_PROG : VIEW_ITEM;
    }

    public void setFooterEnabled(boolean isEnabled) {
        this.isFooterEnabled = isEnabled;
        if (isEnabled) {
//            notifyDataSetChanged();
//            notifyItemInserted(getDataList().size());
        } else {
            notifyItemRemoved(getDataList().size());
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.pbItem);
        }
    }

}
