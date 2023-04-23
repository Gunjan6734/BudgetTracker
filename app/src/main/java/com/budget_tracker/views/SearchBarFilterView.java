package com.budget_tracker.views;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.budget_tracker.R;
import com.budget_tracker.event.SearchBarEvent;

import de.greenrobot.event.EventBus;


public class SearchBarFilterView extends ConstraintLayout {
    Context context;

    private SearchView searchView;
    private ImageView ivFilter;

    String uniqueID = "";
    Drawable filterDrawable;
    boolean isShowFilter = true;

    // MARK: - constructor

    public SearchBarFilterView(@NonNull Context context) {
        super(context);
    }

    public SearchBarFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        initView();
        bindData();
        bindEvent();
    }

    public SearchBarFilterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //MARK: - setters

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public void setFilterDrawable(Drawable filterDrawable) {
        this.filterDrawable = filterDrawable;

        if(filterDrawable != null) {
            ivFilter.setImageDrawable(filterDrawable);
        } else {
            ivFilter.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.search_bar_filter));
        }
    }

    public void setShowFilter(boolean showFilter) {
        isShowFilter = showFilter;
        ivFilter.setVisibility(isShowFilter ? View.VISIBLE : View.GONE);
    }

    public void removeFocus() {
        searchView.clearFocus();
    }

    // MARK: - init

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.search_bar_filter, this);
        searchView = (SearchView) view.findViewById(R.id.searchView);
        ivFilter = (ImageView) findViewById(R.id.iv_filter);
    }

    private void bindData() {
        int searchCloseButtonId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView searchViewCloseButton = (ImageView) this.searchView.findViewById(searchCloseButtonId);
        searchViewCloseButton.setBackground(null);
    }

    private void bindEvent() {
        searchView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                EventBus.getDefault().post(new SearchBarEvent(SearchBarEvent.EventType.Search_Submit, query.trim(), uniqueID));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")) {
                    EventBus.getDefault().post(new SearchBarEvent(SearchBarEvent.EventType.Search_Submit, "", uniqueID));
                }
                return false;
            }
        });

        ivFilter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SearchBarEvent(SearchBarEvent.EventType.Filter, null, uniqueID));
            }
        });
    }
}
