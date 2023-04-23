package com.budget_tracker.fragment.bottom_nav;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.budget_tracker.BottomNavigationActivity;
import com.budget_tracker.BudgetTrackerBaseFragment;
import com.budget_tracker.CommonFunction;
import com.budget_tracker.ExportDatabaseCSVTask;
import com.budget_tracker.ExportExpenseDatabaseCSVTask;
import com.budget_tracker.MySharedPreferences;
import com.budget_tracker.R;
import com.budget_tracker.adapter.SpendingHistoryRvAdapter;
import com.budget_tracker.data_model.CategoryDm;
import com.budget_tracker.data_model.SpendingHistoryDm;
import com.budget_tracker.database.DBHelper;
import com.budget_tracker.event.PopUpMenuEvent;
import com.budget_tracker.fragment.AddExpense;
import com.budget_tracker.views.SimpleTitleBar;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class SpendingHistory extends Fragment {

    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 200;

    View savedView;
    private RecyclerView recyclerView;
    private ImageView vAddExpense;

    SpendingHistoryRvAdapter adapter;

    List<SpendingHistoryDm> list = new ArrayList();

    public SpendingHistory() {
        // Required empty public constructor
    }

    public static SpendingHistory newInstance() {
        SpendingHistory fragment = new SpendingHistory();
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
        } else {
            view = inflater.inflate(R.layout.fragment_spending_history, container, false);
            SimpleTitleBar simpleTitleBar = new SimpleTitleBar(getContext());
            initViews(view);
            toolbar = simpleTitleBar;
            toolbarFrame = (ViewGroup) view.findViewById(R.id.fl_toolbar_frame);

            toolbarFrame.addView(toolbar);
            ((SimpleTitleBar) toolbar).setLeftButtonInvisible();
            ((SimpleTitleBar) toolbar).setTitleVisible("Spending History");
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

    private void getData() {
        DBHelper dbHelper = new DBHelper(getContext());
        list.clear();
        list = dbHelper.getSpendingHistory(MySharedPreferences.getLoggedInUserEmail(getContext()));
        adapter.setmValues(list);
    }

    private void bindData() {
        if(adapter == null){
            adapter = new SpendingHistoryRvAdapter(getContext(),list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }
        else{
            adapter.setmValues(list);
            adapter.notifyDataSetChanged();
        }
    }

    private void bindEvent() {
        vAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getContext() instanceof BottomNavigationActivity) {
                    ((BottomNavigationActivity) getContext()).replaceFragMain(new AddExpense(), true);
                }
            }
        });
        ((SimpleTitleBar) toolbar).setRightButton2OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonFunction.showPopUpMenu(getContext(),v,false);
            }
        });
    }

    public void onEventMainThread(PopUpMenuEvent event){
        if(event.getItem().getTitle().toString().equalsIgnoreCase("Logout")){
            MySharedPreferences.logout(getContext());
        }
        else{
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
        String[] columnsArray = {"Date", "Category", "Amount", "Transaction Type"};
        List<String> dateList = new ArrayList<>();
        List<String> amountList = new ArrayList<>();
        List<String> categoryList = new ArrayList<>();
        List<String> typeList = new ArrayList<>();
        for(int i=0; i<list.size(); i++){
            dateList.add(i,list.get(i).getDate());
            amountList.add(i,list.get(i).getAmount());
            categoryList.add(i,list.get(i).getCategory_name());
            typeList.add(i,list.get(i).getType());

        }
        ExportExpenseDatabaseCSVTask exportDatabaseCSVTask =
                new ExportExpenseDatabaseCSVTask(getContext(),columnsArray,dateList, categoryList, amountList, typeList);
        exportDatabaseCSVTask.execute();

    }


    private void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        vAddExpense = (ImageView) view.findViewById(R.id.v_add_expense);
    }


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