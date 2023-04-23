package com.budget_tracker;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.budget_tracker.adapter.RecipeListAdapter;
import com.budget_tracker.event.SearchBarEvent;
import com.budget_tracker.views.SearchBarFilterView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private SearchBarFilterView vSearch;
    private RecyclerView recyclerView;


    private RecipeListAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        vSearch.removeFocus();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initView();
        bindData();
        bindEvent();
    }

    private void bindEvent() {
    }

    private void bindData() {
        vSearch.setUniqueID("event_listing");

    }

    private void initView() {
        vSearch = (SearchBarFilterView) findViewById(R.id.v_search);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }



    //MARK: - SearchBarEvent

    public void onEventMainThread(SearchBarEvent event) {
        if(event.getUniqueID().equals("event_listing")) {
            switch (event.getEventType()) {
                case Filter: {

                    break;
                }
                case Search_Submit: {
                    // Search submit
                    break;
                }
            }
        }
    }



}