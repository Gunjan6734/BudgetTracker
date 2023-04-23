package com.budget_tracker.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budget_tracker.BottomNavigationActivity;
import com.budget_tracker.R;
import com.budget_tracker.data_model.User;
import com.budget_tracker.database.DBHelper;
import com.budget_tracker.event.PrimaryButtonEvent;
import com.budget_tracker.views.PrimaryButtonView;
import com.budget_tracker.views.SimpleTitleBar;

import de.greenrobot.event.EventBus;

public class SignUpFragment extends Fragment {
    private TextView tvTitle1;
    private ImageView ivLogo;
    private TextView tvTitle;
    private EditText vNameInputField;
    private EditText vInputField;
    private EditText vPasswordField;
    private TextView tvLogin;
    private PrimaryButtonView vSignupButton;

    View toolbar;
    ViewGroup toolbarFrame;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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

        View  view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        SimpleTitleBar simpleTitleBar = new SimpleTitleBar(getContext());

        toolbar = simpleTitleBar;
        toolbarFrame = (ViewGroup) view.findViewById(R.id.fl_toolbar_frame);

        toolbarFrame.addView(toolbar);
        ((SimpleTitleBar) toolbar).setLeftButtonVisible();
        ((SimpleTitleBar) toolbar).setTitleVisible("Sign Up");
        ((SimpleTitleBar) toolbar).setRightButton2Invisible();


        initObjects();
        intiView(view);
        bindData();
        bindEvent();
        return view;
    }




    private void bindEvent() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragments(new LoginFragment(), true);
            }
        });
        ((SimpleTitleBar) toolbar).setLeftButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 changeFragments(new LoginFragment(), true);
            }
        });
    }

    private void intiView(View view) {
        tvTitle1 = (TextView) view.findViewById(R.id.tv_title1);
        ivLogo = (ImageView) view.findViewById(R.id.iv_logo);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        vNameInputField = (EditText) view.findViewById(R.id.v_name_input_field);
        vInputField = (EditText) view.findViewById(R.id.v_input_field);
        vPasswordField = (EditText) view.findViewById(R.id.v_password_field);
        tvLogin = (TextView) view.findViewById(R.id.tv_login);
        vSignupButton = (PrimaryButtonView) view.findViewById(R.id.v_signup_button);

    }


    private void bindData() {
        vSignupButton.setId("signup");
        vSignupButton.setTitle(getString(R.string.signup_title));
    }


    public void onEventMainThread(PrimaryButtonEvent event){
        //Toast.makeText(getContext(), "Signup Button Clicked", Toast.LENGTH_LONG).show();
        checkValidation();
    }

    private void checkValidation() {
        if(vNameInputField.getText() == null || vNameInputField.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Enter User Name", Toast.LENGTH_LONG).show();
        }
        else if(vInputField.getText() == null || vInputField.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Enter Email", Toast.LENGTH_LONG).show();
        }
        else if(vPasswordField.getText() == null || vPasswordField.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Enter Password", Toast.LENGTH_LONG).show();
        }
        else{
            runQuery(vInputField.getText().toString(),
                    vPasswordField.getText().toString(),
                    vNameInputField.getText().toString());
        }
    }
    DBHelper databaseHelper;
    private void initObjects() {
        databaseHelper = new DBHelper(getContext());
    }


    private void runQuery(String email, String password, String name) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        try {
            String res = databaseHelper.addUser(user);
            if(res.equalsIgnoreCase("User Added")){
                Toast.makeText(getContext(), "ser Added Successfully",Toast.LENGTH_LONG).show();
                changeFragments(new LoginFragment(),true);

            }
            else{
                Toast.makeText(getContext(), "Email Already Exist",Toast.LENGTH_LONG).show();
            }


        }
        catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_LONG).show();
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