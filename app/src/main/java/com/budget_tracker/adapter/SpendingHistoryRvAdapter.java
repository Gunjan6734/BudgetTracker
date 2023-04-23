package com.budget_tracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.budget_tracker.R;
import com.budget_tracker.data_model.SpendingHistoryDm;
import com.budget_tracker.view_holder.RecipeViewHolder;
import com.budget_tracker.view_holder.SpendingHistoryRvHolder;

import java.util.List;

public class SpendingHistoryRvAdapter extends RecyclerView.Adapter<SpendingHistoryRvHolder> {

    private Context context;
    private List<SpendingHistoryDm> mValues;

    public SpendingHistoryRvAdapter(Context context, List<SpendingHistoryDm> mValues) {
        this.context = context;
        this.mValues = mValues;
    }

    public SpendingHistoryRvHolder newInstance(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_spending_history, parent, false);
        return new SpendingHistoryRvHolder(view);
    }

    @NonNull
    @Override
    public SpendingHistoryRvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return newInstance(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SpendingHistoryRvHolder holder, int position) {
        holder.onBind(mValues.get(position), position);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setmValues(List<SpendingHistoryDm> mValues) {
        this.mValues = mValues;
    }
}
