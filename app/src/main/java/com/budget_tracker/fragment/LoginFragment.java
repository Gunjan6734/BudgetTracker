package com.budget_tracker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.budget_tracker.BottomNavigationActivity;
import com.budget_tracker.MySharedPreferences;
import com.budget_tracker.R;
import com.budget_tracker.database.DBHelper;
import com.budget_tracker.event.PrimaryButtonEvent;
import com.budget_tracker.views.PrimaryButtonView;

import de.greenrobot.event.EventBus;

public class LoginFragment extends Fragment {


    private TextView tvTitle1;
    private ImageView ivLogo;
    private TextView tvTitle;
    private EditText vInputField;
    private EditText vPasswordField;
    private TextView tvSignUp;
    private PrimaryButtonView vLoginButton;


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initObjects();
        initView(view);
        bindData();
        bindEvent();
        return view;
    }



    private void bindEvent() {
            tvSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeFragments(new SignUpFragment(), true);


                }
            });
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

    private void initView(View view) {
            tvTitle1 = (TextView) view.findViewById(R.id.tv_title1);
            ivLogo = (ImageView) view.findViewById(R.id.iv_logo);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            vInputField = (EditText) view.findViewById(R.id.v_input_field);
            vPasswordField = (EditText) view.findViewById(R.id.v_password_field);
            tvSignUp = (TextView) view.findViewById(R.id.tv_sign_up);
            vLoginButton = (PrimaryButtonView) view.findViewById(R.id.v_login_button);
        }

        private void bindData() {
            vLoginButton.setId("login");
            vLoginButton.setTitle(getString(R.string.login_title));
        }


        public void onEventMainThread(PrimaryButtonEvent event){
            //Toast.makeText(getContext(), "Login Button Clicked", Toast.LENGTH_LONG).show();
            checkValidation();

        }

        private void checkValidation() {
            if(vInputField.getText() == null || vInputField.getText().toString().isEmpty()){
                Toast.makeText(getContext(), "Enter User Name", Toast.LENGTH_LONG).show();
                vInputField.setError("Enter User Name");
            }
            else if(vPasswordField.getText() == null || vPasswordField.getText().toString().isEmpty()){
                Toast.makeText(getContext(), "Enter Password", Toast.LENGTH_LONG).show();
                vPasswordField.setError("Enter Password");
            }
            else{
                runQuery(vInputField.getText().toString(),vPasswordField.getText().toString());
            }
        }

        DBHelper databaseHelper;

        private void initObjects() {
            databaseHelper = new DBHelper(getContext());
        }

        private void runQuery(String email, String password) {
            boolean isUserExist = databaseHelper.checkUserLogin(email,password);
            if(isUserExist){
                MySharedPreferences.setUserLoggedInStatus(getContext(),true,email);
                Intent intent = new Intent(getContext(), BottomNavigationActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
            else{
                Toast.makeText(getContext(), "User Does not Exist", Toast.LENGTH_LONG).show();
            }
        }

    public void changeFragments(Fragment fragment, boolean shouldAnimate) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (shouldAnimate) {
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        }
        ft.replace(R.id.frame_layout, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }


}


