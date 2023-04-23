package com.budget_tracker;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class LoadFragment {
    FragmentManager fragmentManager;
    public LoadFragment(FragmentManager fragmentManager)
    {
        this.fragmentManager = fragmentManager;
    }

    protected void initializeFragment(Fragment fragment, boolean shouldAnimate) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        if (shouldAnimate) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
