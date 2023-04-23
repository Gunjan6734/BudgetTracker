package com.budget_tracker.view_holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.budget_tracker.R;
import com.budget_tracker.data_model.IncomeDm;
import com.budget_tracker.data_model.SpendingHistoryDm;

public class IncomeRvHolder extends RecyclerView.ViewHolder {

    private ConstraintLayout rootLayout;
    private TextView amountTitle;
    private TextView amountValue;
    private TextView dateTitle;
    private TextView dateValue;
    private TextView typeTitle;
    private TextView typeValue;



    public IncomeRvHolder(@NonNull View itemView) {
        super(itemView);
        rootLayout = (ConstraintLayout) itemView.findViewById(R.id.root_layout);
        amountTitle = (TextView) itemView.findViewById(R.id.amount_title);
        amountValue = (TextView) itemView.findViewById(R.id.amount_value);
        dateTitle = (TextView) itemView.findViewById(R.id.date_title);
        dateValue = (TextView) itemView.findViewById(R.id.date_value);
        typeTitle = (TextView) itemView.findViewById(R.id.type_title);
        typeValue = (TextView) itemView.findViewById(R.id.type_value);
    }

    public void onBind(IncomeDm info, int position) {
        IncomeRvHolder holder = this;

        if(info.getIncome_amount() != null){
            holder.amountValue.setText(info.getIncome_amount());
        }

        if(info.getDate() != null){
            holder.dateValue.setText(info.getDate());
        }

        if(info.getIncome_source() != null){
            holder.typeValue.setText(info.getIncome_source());
        }

    }
}
