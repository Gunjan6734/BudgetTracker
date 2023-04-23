package com.budget_tracker.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.budget_tracker.BudgetTrackerBaseFragment;
import com.budget_tracker.CommonFunction;
import com.budget_tracker.MySharedPreferences;
import com.budget_tracker.R;
import com.budget_tracker.data_model.IncomeDm;
import com.budget_tracker.data_model.SpendingHistoryDm;
import com.budget_tracker.database.DBHelper;
import com.budget_tracker.event.PrimaryButtonEvent;
import com.budget_tracker.fragment.bottom_nav.HomeIncomeExpenseFragment;
import com.budget_tracker.views.PrimaryButtonView;
import com.budget_tracker.views.SimpleTitleBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddIncomeFragment extends BudgetTrackerBaseFragment {

    protected View savedView;
    View toolbar;
    ViewGroup toolbarFrame;
    private FrameLayout flToolbarFrame;
    private EditText amountEt;
    private EditText dateEt;
    private EditText sourceTypeEt;
    private PrimaryButtonView vAddIncome;

    Date date = new Date();
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener datePickerDialog;
    
    public AddIncomeFragment() {
        // Required empty public constructor
    }

    public static AddIncomeFragment newInstance(String param1, String param2) {
        AddIncomeFragment fragment = new AddIncomeFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;
        if (savedView != null) {
            view = savedView;
        } else {
            view = inflater.inflate(R.layout.fragment_add_income, container, false);
            initView(view);
            SimpleTitleBar simpleTitleBar = new SimpleTitleBar(getContext());
            toolbar = simpleTitleBar;
            toolbarFrame = (ViewGroup) view.findViewById(R.id.fl_toolbar_frame);

            toolbarFrame.addView(toolbar);
            ((SimpleTitleBar) toolbar).setLeftButtonVisible();
            ((SimpleTitleBar) toolbar).setTitleVisible("Sign Up");
            ((SimpleTitleBar) toolbar).setRightButton2Invisible();
            
            bindData();
            
            bindEvent();
            savedView = view;
        }
        return view;
    }

    private void bindData() {
        vAddIncome.setId("add_income");
        vAddIncome.setTitle("Add Income");
    }

    private void bindEvent() {
        ((SimpleTitleBar) toolbar).setLeftButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
//                changeFragments(new HomeIncomeExpenseFragment(), true);
            }
        });
        datePickerDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        dateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(),datePickerDialog,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
    }

    private void updateLabel(){
        String myFormat="dd/MMM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, new Locale("en", "IN"));
        dateEt.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void initView(View view) {
        flToolbarFrame = (FrameLayout) view.findViewById(R.id.fl_toolbar_frame);
        amountEt = (EditText) view.findViewById(R.id.amount_et);
        dateEt = (EditText) view.findViewById(R.id.date_et);
        sourceTypeEt = (EditText) view.findViewById(R.id.source_type_et);
        vAddIncome = (PrimaryButtonView) view.findViewById(R.id.v_add_income);

    }

    public void onEventMainThread(PrimaryButtonEvent event){
        if(allValueFilled()) {
            DBHelper dbHelper = new DBHelper(getContext());
            IncomeDm dm = new IncomeDm();
            dm.setIncome_amount(amountEt.getText().toString());
            dm.setDate(dateEt.getText().toString());
            dm.setIncome_source(sourceTypeEt.getText().toString());
            try {
                dbHelper.addIncome(dm, MySharedPreferences.getLoggedInUserEmail(getContext()));
                getActivity().onBackPressed();
//                changeFragments(new HomeIncomeExpenseFragment(),true);
            }
            catch (Exception e){
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean allValueFilled() {

         if(amountEt.getText().toString() == null || amountEt.getText().toString().isEmpty()){
             amountEt.requestFocus();
             amountEt.setError("Enter Amount");
             return false;

         }
         else if(dateEt.getText().toString() == null || dateEt.getText().toString().isEmpty()){
             dateEt.requestFocus();
             dateEt.setError("Enter Date");
             return false;

         }
         else if(sourceTypeEt.getText().toString() == null || sourceTypeEt.getText().toString().isEmpty()){
             sourceTypeEt.requestFocus();
             sourceTypeEt.setError("Enter Source");
             return false;

         }
         else{
             return true;
         }

    }

    public void changeFragments(Fragment fragment, boolean shouldAnimate) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (shouldAnimate) {
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        }
        ft.replace(R.id.frame_layout, fragment);

        ft.commit();
    }
}