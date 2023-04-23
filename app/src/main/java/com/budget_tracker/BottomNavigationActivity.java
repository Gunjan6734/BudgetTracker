package com.budget_tracker;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.budget_tracker.data_model.CategoryDm;
import com.budget_tracker.database.DBHelper;
import com.budget_tracker.fragment.bottom_nav.BudgetFragment;
import com.budget_tracker.fragment.bottom_nav.HomeIncomeExpenseFragment;
import com.budget_tracker.fragment.bottom_nav.SpendingHistory;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import java.util.ArrayList;
import java.util.List;

public class BottomNavigationActivity extends AppCompatActivity
        implements BottomNavigationView
        .OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    FrameLayout nav_host_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        isFirstTime();
        initViews();
        bindData();
        bindEvent();
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.nav_view);
        nav_host_fragment = findViewById(R.id.bottom_nav_host_fragment);
        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
    }

    private void bindEvent() {

    }
    private void bindData() {
        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_budget, R.id.navigation_dashboard, R.id.navigation_history)
                .build();

        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.navigation_budget:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.bottom_nav_host_fragment, new BudgetFragment())
                        .commit();
                return true;

            case R.id.navigation_dashboard:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.bottom_nav_host_fragment, new HomeIncomeExpenseFragment())
                        .commit();
                return true;

            case R.id.navigation_history:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.bottom_nav_host_fragment, new SpendingHistory())
                        .commit();
                return true;
        }
        return false;
    }


    public void bindCategoryData(){
        ArrayList<CategoryDm> list = new ArrayList<>();
        list.add(new CategoryDm("Electricity"));
        list.add(new CategoryDm("Education"));
        list.add(new CategoryDm("Travelling"));
        list.add(new CategoryDm("Household Items"));
        list.add(new CategoryDm("Food"));
        list.add(new CategoryDm("Entertainment"));
        list.add(new CategoryDm("Insurance"));
        list.add(new CategoryDm("Shopping"));
        list.add(new CategoryDm("Tax"));
        list.add(new CategoryDm("Rent"));
        list.add(new CategoryDm("Transportation"));
        list.add(new CategoryDm("Health"));
        list.add(new CategoryDm("Shopping"));

        DBHelper databaseHelper;
        databaseHelper = new DBHelper(this);
        for(int i=0; i<list.size(); i++){
            String res = databaseHelper.addCategory(list.get(i));
            Toast.makeText(this, res, Toast.LENGTH_LONG).show();
        }
    }

    private void isFirstTime()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("firstTime", false)) {
            // run your one time code
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();
            bindCategoryData();
        }
    }

    public void replaceFragMain( Fragment fragment, boolean addTobackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.bottom_nav_host_fragment, fragment).commit();
        if (addTobackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
    }



}