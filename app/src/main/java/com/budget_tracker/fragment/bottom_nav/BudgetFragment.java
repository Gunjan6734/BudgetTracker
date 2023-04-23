package com.budget_tracker.fragment.bottom_nav;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.budget_tracker.CommonFunction;
import com.budget_tracker.MySharedPreferences;
import com.budget_tracker.R;
import com.budget_tracker.data_model.IncomeDm;
import com.budget_tracker.data_model.MonthlyBudgetDm;
import com.budget_tracker.data_model.SpendingHistoryDm;
import com.budget_tracker.database.DBHelper;
import com.budget_tracker.views.SimpleTitleBar;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;


import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BudgetFragment extends Fragment {

    protected View savedView;
    private CardView cardViewGraph;
    private BarChart barChart;
    private CardView otherDetailsCv;
    private TextView totalSpendingHeadTv;
    private TextView totalSpendingValueTv;
    private TextView totalIncomeHeadTv;
    private TextView totalIncomeValueTv;

    private CardView monthlyBudgetCv;
    private TextView monthSpinnerHeadTv;
    private CardView spinnerCv;
    private AppCompatSpinner monthSpinner;
    private TextView monthlyBudgetHeadTv;
    private EditText monthlyBudgetValueEt;
    private TextView yearSpinnerHeadTv;
    private CardView yearSpinnerCv;
    private AppCompatSpinner yearSpinner;
    private Button submitMonthlyBudgetTv;
    private TextView selectedMonthTv;
    private TextView selectedYearTv;
    private TextView monthlyBudgetValueTv;


    List<SpendingHistoryDm> spendingList;
    List<SpendingHistoryDm> monthList;
    List<IncomeDm> incomeList;
    List<IncomeDm> monthIncomeList;

    ArrayAdapter yearAdapter;
    ArrayAdapter monthAdapter;

    View toolbar;
    ViewGroup toolbarFrame;

    String selectedMonth = "";
    String selectedYear="";

    DBHelper dbHelper ;
    String[] monthsArray;
    String[] yearsArray;
    float monthlyBudget = 0f;
    int totalIncome = 0;
    int totalSpending = 0;

    public BudgetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if (savedView != null) {
            view = savedView;
        } else {
            view = inflater.inflate(R.layout.fragment_budget, container, false);
            SimpleTitleBar simpleTitleBar = new SimpleTitleBar(getContext());
            initView(view);
            toolbar = simpleTitleBar;
            toolbarFrame = (ViewGroup) view.findViewById(R.id.fl_toolbar_frame);

            toolbarFrame.addView(toolbar);
            ((SimpleTitleBar) toolbar).setLeftButtonInvisible();
            ((SimpleTitleBar) toolbar).setTitleVisible("Budget");
            ((SimpleTitleBar) toolbar).setRightButton2Visible();
            bindData();
            bindEvent();
            savedView = view;
        }
        return view;
    }

    private void bindEvent() {
        ((SimpleTitleBar) toolbar).setRightButton2OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonFunction.showPopUpMenu(getContext(),v,false);
            }
        });


        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                selectedMonth = monthsArray[position];
                updateViews();
                Log.e("selected", "value --" + position + "  ,  " + selectedMonth);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                selectedYear = yearsArray[position];
                updateViews();
                Log.e("selected", "value --" + position + "  ,  " + selectedYear);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        submitMonthlyBudgetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthlyBudgetDm dm = new MonthlyBudgetDm();
                if(monthlyBudgetValueEt.getText().toString().isEmpty()){
                    monthlyBudgetValueEt.requestFocus();
                    monthlyBudgetValueEt.setError("Enter Value");
                }else {
                    dm.setBudget_amount(monthlyBudgetValueEt.getText().toString());
                    dm.setBudget_month(selectedMonth);
                    dm.setBudget_year(selectedYear);
                    try {
                        dbHelper.addMonthlyBudget(dm, MySharedPreferences.getLoggedInUserEmail(getContext()));
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    updateViews();
                    monthlyBudgetValueEt.setText("");
                }
            }
        });
    }

    private void updateViews() {
        updateMonthlyBudget();
        setChartData();
        selectedMonthTv.setText(selectedMonth);
        selectedYearTv.setText(selectedYear);
        monthlyBudgetValueTv.setText(String.valueOf(monthlyBudget));
    }

    private void setChartData() {

        List<BarEntry> entriesGroup1 = new ArrayList<>();
        List<BarEntry> entriesGroup2 = new ArrayList<>();


            entriesGroup1.add(new BarEntry(0, monthlyBudget));
            entriesGroup2.add(new BarEntry(0, totalSpending));


        BarDataSet set1 = new BarDataSet(entriesGroup1, "Budget");
        BarDataSet set2 = new BarDataSet(entriesGroup2, "Expense");

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 5f; // x2 dataset
// (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData data = new BarData(set1, set2);
        data.setBarWidth(barWidth); // set the width of each bar
        barChart.setData(data);
        barChart.groupBars(1000f, groupSpace, barSpace); // perform the "explicit" grouping
        barChart.invalidate();

    }

    private void updateMonthlyBudget() {
        MonthlyBudgetDm dm = dbHelper.getBudgetByMonth(MySharedPreferences.getLoggedInUserEmail(getContext()),
                selectedMonth,selectedYear);
        String  budget = "0";
        budget = dm.getBudget_amount();
        if(budget == null){
            monthlyBudget = 0f;
        }else{
            monthlyBudget = Float.parseFloat(budget);
        }

    }

    private void initView(View view) {

        cardViewGraph = (CardView) view.findViewById(R.id.cardViewGraph);
        barChart = (BarChart) view.findViewById(R.id.bar_chart);
        otherDetailsCv = (CardView) view.findViewById(R.id.other_details_cv);
        totalSpendingHeadTv = (TextView) view.findViewById(R.id.total_spending_head_tv);
        totalSpendingValueTv = (TextView) view.findViewById(R.id.total_spending_value_tv);
        totalIncomeHeadTv = (TextView) view.findViewById(R.id.total_income_head_tv);
        totalIncomeValueTv = (TextView) view.findViewById(R.id.total_income_value_tv);
        monthlyBudgetCv = (CardView) view.findViewById(R.id.monthly_budget_cv);
        monthSpinnerHeadTv = (TextView) view.findViewById(R.id.month_spinner_head_tv);
        spinnerCv = (CardView) view.findViewById(R.id.spinner_cv);
        monthSpinner = (AppCompatSpinner) view.findViewById(R.id.month_spinner);
        monthlyBudgetHeadTv = (TextView) view.findViewById(R.id.monthly_budget_head_tv);
        monthlyBudgetValueEt = (EditText) view.findViewById(R.id.monthly_budget_value_et);
        yearSpinnerHeadTv = (TextView) view.findViewById(R.id.year_spinner_head_tv);
        yearSpinnerCv = (CardView) view.findViewById(R.id.year_spinner_cv);
        yearSpinner = (AppCompatSpinner) view.findViewById(R.id.year_spinner);
        submitMonthlyBudgetTv = (Button) view.findViewById(R.id.submit_monthly_budget_tv);
        selectedMonthTv = (TextView) view.findViewById(R.id.selected_month_tv);
        selectedYearTv = (TextView) view.findViewById(R.id.selected_year_tv);
        monthlyBudgetValueTv = (TextView) view.findViewById(R.id.monthly_budget_value_tv);


    }

    private void bindData() {

        dbHelper = new DBHelper(getContext());

        monthsArray = new String[]{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        Integer currentYear = Calendar.getInstance().get(Calendar.YEAR) + 1;
         yearsArray = new String[7];
        for (int i = 7; i > 0; i--) {
            yearsArray[7-i] = String.valueOf(currentYear - (i-1));
        }

        yearAdapter = new ArrayAdapter(
                getContext(),
                android.R.layout.select_dialog_item,
                yearsArray );
        yearSpinner.setAdapter(yearAdapter);
        selectedYear = String.valueOf(currentYear);
        yearSpinner.setSelection(CommonFunction.getIndex(yearSpinner, String.valueOf(currentYear-1)));

        monthAdapter = new ArrayAdapter(
                getContext(),
                android.R.layout.select_dialog_item,
                monthsArray );
        monthSpinner.setAdapter(monthAdapter);
        Integer currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        selectedMonth = monthsArray[currentMonth];
        monthSpinner.setSelection(CommonFunction.getIndex(monthSpinner, selectedMonth));

        getSpendingDataByMonth(selectedMonth, selectedYear);
        getIncomeDataByMonth(selectedMonth,selectedYear);

         totalSpending = dbHelper.getTotalSpending(MySharedPreferences.getLoggedInUserEmail(getContext()));
        totalSpendingValueTv.setText(String.valueOf(totalSpending));

         totalIncome = dbHelper.getTotalIncome(MySharedPreferences.getLoggedInUserEmail(getContext()));
        totalIncomeValueTv.setText(String.valueOf(totalIncome));

        updateViews();




    }


    private void getSpendingDataByMonth(String month, String year) {
        spendingList =  dbHelper.getSpendingHistory(MySharedPreferences.getLoggedInUserEmail(getContext()));
        monthList = new ArrayList<>();
        for(int i = 0; i< spendingList.size(); i++){
            if(spendingList.get(i).getDate().contains(month)
                    && spendingList.get(i).getDate().contains(year)){
                monthList.add(spendingList.get(i));
            }
        }
    }

    private void getSpendingDataByYear(String year) {
        spendingList =  dbHelper.getSpendingHistory(MySharedPreferences.getLoggedInUserEmail(getContext()));
        monthList = new ArrayList<>();
        for(int i = 0; i< spendingList.size(); i++){
            if(spendingList.get(i).getDate().contains(year)){
                monthList.add(spendingList.get(i));
            }
        }
    }


    private void getIncomeDataByMonth(String month, String year) {
        incomeList =  dbHelper.getIncomeData(MySharedPreferences.getLoggedInUserEmail(getContext()));
        monthIncomeList = new ArrayList<>();
        for(int i=0; i<incomeList.size(); i++){
            if(incomeList.get(i).getDate().contains(month)
                    && incomeList.get(i).getDate().contains(year)){
                monthIncomeList.add(incomeList.get(i));
            }
        }
    }

    private void getIncomeDataByYear(String year) {
        incomeList =  dbHelper.getIncomeData(MySharedPreferences.getLoggedInUserEmail(getContext()));
        monthIncomeList = new ArrayList<>();
        for(int i=0; i<incomeList.size(); i++){
            if(incomeList.get(i).getDate().contains(year)){
                monthIncomeList.add(incomeList.get(i));
            }
        }
    }



}