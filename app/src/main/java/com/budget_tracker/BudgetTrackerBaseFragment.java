package com.budget_tracker;

import androidx.fragment.app.Fragment;

import de.greenrobot.event.EventBus;

public class BudgetTrackerBaseFragment extends Fragment {

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
