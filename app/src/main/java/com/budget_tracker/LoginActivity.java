package com.budget_tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.budget_tracker.fragment.LoginFragment;

import de.greenrobot.event.EventBus;

public class LoginActivity extends AppCompatActivity {
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intiViews();
        bindData();

    }

    private void bindData() {
        boolean isLoggedIn = MySharedPreferences.getUserLoggedInStatus(this);
        if(isLoggedIn){
            Intent intent = new Intent(this, BottomNavigationActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            changeFragments(new LoginFragment(),true);
        }

    }

    private void intiViews() {
        frameLayout = findViewById(R.id.frame_layout);
    }

    public void changeFragments(Fragment fragment, boolean shouldAnimate) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (shouldAnimate) {
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        }
        ft.replace(R.id.frame_layout, fragment);
        ft.commit();
    }


}