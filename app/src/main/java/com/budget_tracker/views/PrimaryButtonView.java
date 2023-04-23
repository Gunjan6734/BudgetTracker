package com.budget_tracker.views;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.budget_tracker.R;
import com.budget_tracker.event.PrimaryButtonEvent;

import de.greenrobot.event.EventBus;


public class PrimaryButtonView extends ConstraintLayout {
    Context context;
    String title = "";
    String id = "";
    boolean isEnable = true;
    Drawable background;
    int textColor = ContextCompat.getColor(getContext(), R.color.white);

    private Button btPrimary;

    // MARK: - constructor

    public PrimaryButtonView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        initView();
        bindData();
        bindEvent();
    }

    // MARK: - setter

    public void setTitle(String title) {
        this.title = title;

        if(title != null && !title.isEmpty()) {
            btPrimary.setText(title);
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
        checkButtonEnable();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;

        btPrimary.setTextColor(textColor);
    }

    @Override
    public void setBackground(Drawable background) {
        this.background = background;

        btPrimary.setBackground(background);
    }

    // MARK: - init

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.common_primary_button, this);
        btPrimary = (Button) view.findViewById(R.id.bt_primary);
    }

    private void bindData() {
        checkButtonEnable();
    }

    private void bindEvent() {
        btPrimary.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new PrimaryButtonEvent(id));
            }
        });

    }

    private void checkButtonEnable() {
        if(isEnable) {
            setButtonEnable();
        } else {
            setButtonDisable();
        }
    }

    private void setButtonEnable() {
        btPrimary.setEnabled(true);
        btPrimary.setAlpha(1.0f);
    }

    private void setButtonDisable() {
        btPrimary.setEnabled(false);
        btPrimary.setAlpha(0.5f);
    }
}

