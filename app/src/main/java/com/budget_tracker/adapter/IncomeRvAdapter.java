package com.budget_tracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.budget_tracker.R;
import com.budget_tracker.data_model.IncomeDm;
import com.budget_tracker.data_model.SpendingHistoryDm;
import com.budget_tracker.view_holder.IncomeRvHolder;
import com.budget_tracker.view_holder.SpendingHistoryRvHolder;

import java.util.List;

public class IncomeRvAdapter extends RecyclerView.Adapter<IncomeRvHolder> {

    private Context context;
    private List<IncomeDm> mValues;

    public IncomeRvAdapter(Context context, List<IncomeDm> mValues) {
        this.context = context;
        this.mValues = mValues;
    }

    public IncomeRvHolder newInstance(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.income_rv_item_layout, parent, false);
        return new IncomeRvHolder(view);
    }

    @NonNull
    @Override
    public IncomeRvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return newInstance(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeRvHolder holder, int position) {
        holder.onBind(mValues.get(position), position);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setmValues(List<IncomeDm> mValues) {
        this.mValues = mValues;
    }
}
