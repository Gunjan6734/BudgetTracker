package com.budget_tracker.view_holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.budget_tracker.R;
import com.budget_tracker.data_model.SpendingHistoryDm;

public class SpendingHistoryRvHolder extends RecyclerView.ViewHolder {

    private ConstraintLayout rootLayout;
    private TextView categoryTitle;
    private TextView categoryValue;
    private TextView dateTitle;
    private TextView dateValue;
    private TextView typeTitle;
    private TextView typeValue;
    private TextView amountTitle;
    private TextView amountValue;
    public SpendingHistoryRvHolder(@NonNull View itemView) {
        super(itemView);
        rootLayout = (ConstraintLayout) itemView.findViewById(R.id.root_layout);
        categoryTitle = (TextView) itemView.findViewById(R.id.category_title);
        categoryValue = (TextView) itemView.findViewById(R.id.category_value);
        dateTitle = (TextView) itemView.findViewById(R.id.date_title);
        dateValue = (TextView) itemView.findViewById(R.id.date_value);
        typeTitle = (TextView) itemView.findViewById(R.id.type_title);
        typeValue = (TextView) itemView.findViewById(R.id.type_value);
        amountTitle = (TextView) itemView.findViewById(R.id.amount_title);
        amountValue = (TextView) itemView.findViewById(R.id.amount_value);

    }

    public void onBind(SpendingHistoryDm info, int position) {
        SpendingHistoryRvHolder holder = this;
        if(info.getCategory_name() != null){
            holder.categoryValue.setText(info.getCategory_name());
        }
        if(info.getAmount() != null){
            holder.amountValue.setText(info.getAmount());
        }

        if(info.getDate() != null){
            holder.dateValue.setText(info.getDate());
        }

        if(info.getType() != null){
            holder.typeValue.setText(info.getType());
        }

    }
}
