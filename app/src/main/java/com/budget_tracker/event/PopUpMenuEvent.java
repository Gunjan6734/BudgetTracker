package com.budget_tracker.event;

import android.view.MenuItem;

public class PopUpMenuEvent {

    private MenuItem item;

    public MenuItem getItem() {
        return item;
    }

    public void setItem(MenuItem item) {
        this.item = item;
    }

    public PopUpMenuEvent(MenuItem item) {
        this.item = item;
    }


}
