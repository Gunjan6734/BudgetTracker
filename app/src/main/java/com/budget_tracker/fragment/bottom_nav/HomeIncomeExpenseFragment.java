package com.budget_tracker.fragment.bottom_nav;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.budget_tracker.BottomNavigationActivity;
import com.budget_tracker.BudgetTrackerBaseFragment;
import com.budget_tracker.CommonFunction;
import com.budget_tracker.ExportDatabaseCSVTask;
import com.budget_tracker.MySharedPreferences;
import com.budget_tracker.R;
import com.budget_tracker.adapter.IncomeRvAdapter;
import com.budget_tracker.adapter.SpendingHistoryRvAdapter;
import com.budget_tracker.data_model.IncomeDm;
import com.budget_tracker.data_model.SpendingHistoryDm;
import com.budget_tracker.database.DBHelper;
import com.budget_tracker.event.PopUpMenuEvent;
import com.budget_tracker.fragment.AddExpense;
import com.budget_tracker.fragment.AddIncomeFragment;
import com.budget_tracker.views.SimpleTitleBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeIncomeExpenseFragment extends BudgetTrackerBaseFragment {

    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 200;
    View savedView;
    private RecyclerView incomeRv;
    private ImageView incomeIv;

    IncomeRvAdapter adapter;

    List<IncomeDm> list = new ArrayList();


    public HomeIncomeExpenseFragment() {
        // Required empty public constructor
    }

    public static HomeIncomeExpenseFragment newInstance() {
        HomeIncomeExpenseFragment fragment = new HomeIncomeExpenseFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }
    View toolbar;
    ViewGroup toolbarFrame;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if (savedView != null) {
            view = savedView;
        }
        else {
            view = inflater.inflate(R.layout.fragment_home_income_expense, container, false);
            SimpleTitleBar simpleTitleBar = new SimpleTitleBar(getContext());
            initViews(view);
            toolbar = simpleTitleBar;
            toolbarFrame = (ViewGroup) view.findViewById(R.id.fl_toolbar_frame);

            toolbarFrame.addView(toolbar);
            ((SimpleTitleBar) toolbar).setLeftButtonInvisible();
            ((SimpleTitleBar) toolbar).setTitleVisible("Income");
            ((SimpleTitleBar) toolbar).setRightButton2Visible();
            bindData();

            bindEvent();
            savedView = view;
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void bindEvent() {
        incomeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BottomNavigationActivity) getContext()).replaceFragMain(new AddIncomeFragment(), true);
            }
        });
        ((SimpleTitleBar) toolbar).setRightButton2OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonFunction.showPopUpMenu(getContext(),v,true);
            }
        });
    }

    private void getData() {
        DBHelper dbHelper = new DBHelper(getContext());
        list.clear();
        list = dbHelper.getIncomeData(MySharedPreferences.getLoggedInUserEmail(getContext()));
        adapter.setmValues(list);
    }


    public void onEventMainThread(PopUpMenuEvent event){
        if(event.getItem().getTitle().toString().equalsIgnoreCase("Logout")){
            MySharedPreferences.logout(getContext());
        }
        else{
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

                }
            }
            else {
                generateReport();
                // Permission has already been granted
            }

        }

    }

    private void generateReport() {
        String[] columnsArray = {"Date", "Source", "Amount"};
        List<String> dateList = new ArrayList<>();
        List<String> amountList = new ArrayList<>();
        List<String> sourceList = new ArrayList<>();
        for(int i=0; i<list.size(); i++){
            dateList.add(i,list.get(i).getDate());
            amountList.add(i,list.get(i).getIncome_amount());
            sourceList.add(i,list.get(i).getIncome_source());

        }
        ExportDatabaseCSVTask exportDatabaseCSVTask =
                new ExportDatabaseCSVTask(getContext(),columnsArray,dateList, sourceList, amountList);
        exportDatabaseCSVTask.execute();

        }

    private void bindData() {
        if(adapter == null){
            adapter = new IncomeRvAdapter(getContext(),list);
            incomeRv.setLayoutManager(new LinearLayoutManager(getContext()));
            incomeRv.setAdapter(adapter);
        }
        else{
            adapter.setmValues(list);
            adapter.notifyDataSetChanged();
        }
    }

    private void initViews(View view) {
        incomeRv = (RecyclerView) view.findViewById(R.id.income_rv);
        incomeIv = (ImageView) view.findViewById(R.id.income_iv);
    }



}