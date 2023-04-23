package com.budget_tracker.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.budget_tracker.BottomNavigationActivity;
import com.budget_tracker.BudgetTrackerBaseFragment;
import com.budget_tracker.MySharedPreferences;
import com.budget_tracker.OnBackPressedListener;
import com.budget_tracker.R;
import com.budget_tracker.data_model.CategoryDm;
import com.budget_tracker.data_model.SpendingHistoryDm;
import com.budget_tracker.database.DBHelper;
import com.budget_tracker.event.PrimaryButtonEvent;
import com.budget_tracker.fragment.bottom_nav.HomeIncomeExpenseFragment;
import com.budget_tracker.fragment.bottom_nav.SpendingHistory;
import com.budget_tracker.views.PrimaryButtonView;
import com.budget_tracker.views.SimpleTitleBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;


public class AddExpense extends BudgetTrackerBaseFragment   {

    protected View savedView;
    private AppCompatSpinner categorySpinner;
    private EditText amountEt;
    private EditText dateEt;
    private EditText typeEt;
    private PrimaryButtonView vAddExpense;
    private CardView spinnerCv;
    int[] categoryId;
    String[] categoryName;

    View toolbar;
    ViewGroup toolbarFrame;


    public static AddExpense newInstance(String param1, String param2) {
        AddExpense fragment = new AddExpense();
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
            view = inflater.inflate(R.layout.fragment_add_expense, container, false);
            SimpleTitleBar simpleTitleBar = new SimpleTitleBar(getContext());
            toolbar = simpleTitleBar;
            toolbarFrame = (ViewGroup) view.findViewById(R.id.fl_toolbar_frame);

            toolbarFrame.addView(toolbar);
            ((SimpleTitleBar) toolbar).setLeftButtonVisible();
            ((SimpleTitleBar) toolbar).setTitleVisible("Sign Up");
            ((SimpleTitleBar) toolbar).setRightButton2Invisible();
            initView(view);
            bindData();
            bindEvent();

            savedView = view;
        }
        return view;
    }

    private void initView(View view) {

        categorySpinner = (AppCompatSpinner) view.findViewById(R.id.category_spinner);
        amountEt = (EditText) view.findViewById(R.id.amount_et);
        dateEt = (EditText) view.findViewById(R.id.date_et);
        typeEt = (EditText) view.findViewById(R.id.type_et);
        vAddExpense = (PrimaryButtonView) view.findViewById(R.id.v_add_expense);
        spinnerCv = (CardView) view.findViewById(R.id.spinner_cv);
    }



    private void bindData() {
        vAddExpense.setId("add_expense");
        vAddExpense.setTitle("Add Expense");

        DBHelper dbHelper = new DBHelper(getContext());
        List<CategoryDm> list = dbHelper.getAllCategories();
        Log.e("list", "list Size --"+ list.size());

        categoryId = new int[list.size()];
        categoryName = new String[list.size()];

        for(int i = 0; i<list.size(); i++){
            categoryId[i] = list.get(i).getCategory_id();
            categoryName[i] = list.get(i).getCategory_name();
        }

        ArrayAdapter adapter = new ArrayAdapter(
                getContext(),
                android.R.layout.select_dialog_item,
                categoryName );
        categorySpinner.setAdapter(adapter);


    }

    String selectedCategoryName = "";
    String selectedCategoryId = "";

    Date date = new Date();
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener datePickerDialog;
    public void bindEvent() {
        ((SimpleTitleBar) toolbar).setLeftButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
//                changeFragments(new SpendingHistory(), true);
            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                selectedCategoryName = categoryName[position];
                Log.e("selected", "value --" + position + "  ,  " + selectedCategoryName);
            }

            public void onNothingSelected(AdapterView<?> parent) {
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

    public void onEventMainThread(PrimaryButtonEvent event){
        if(allValueFilled()) {

            DBHelper dbHelper = new DBHelper(getContext());
            SpendingHistoryDm dm = new SpendingHistoryDm();
            dm.setType(typeEt.getText().toString());
            dm.setAmount(amountEt.getText().toString());
            dm.setDate(dateEt.getText().toString());
            dm.setCategory_name(selectedCategoryName);
            try {
                dbHelper.addExpenses(dm, MySharedPreferences.getLoggedInUserEmail(getContext()));
                getActivity().onBackPressed();
                //changeFragments(new SpendingHistory(),true);
            }
            catch (Exception e){
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


    private boolean allValueFilled() {
        if(selectedCategoryName.equals("")){
            Toast.makeText(getContext(),"Select Category", Toast.LENGTH_LONG).show();
            return false;
        }

        else if(amountEt.getText().toString() == null || amountEt.getText().toString().isEmpty()){
            amountEt.requestFocus();
            amountEt.setError("Enter Amount");
            return false;

        }
        else if(dateEt.getText().toString() == null || dateEt.getText().toString().isEmpty()){
            dateEt.requestFocus();
            dateEt.setError("Enter Date");
            return false;

        }
        else if(typeEt.getText().toString() == null || typeEt.getText().toString().isEmpty()){
            typeEt.requestFocus();
            typeEt.setError("Enter Transaction Type");
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