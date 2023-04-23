package com.budget_tracker.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.budget_tracker.R;
import com.budget_tracker.view_holder.RecipeViewHolder;

import java.util.List;


    public class RecipeListAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
        public Activity activity;

        private boolean isAddPadding;

        //MARK: - constructor

        public RecipeListAdapter(Activity activity, boolean isAddPadding) {
            this.activity = activity;

            this.isAddPadding = isAddPadding;
        }

        //MARK: - init

        public static RecipeViewHolder newInstance(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
            return new RecipeViewHolder(view);
        }

        @NonNull
        @Override
        public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return newInstance(parent);
        }

        @Override
        public void onBindViewHolder(final RecipeViewHolder holder, final int position) {
//            holder.onBind(activity, mValues.get(position));
        }

        @Override
        public int getItemCount() {
            return 0;
        }

    }
