package com.budget_tracker.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.budget_tracker.R;

/**
 * Created by maxwe on 4/18/2018.
 */

public class SimpleTitleBar extends ConstraintLayout {
    private ImageView ivLeftBtn;

    private TextView tvCenterTitle;
    private ImageView ivRightBtn2;
    private ConstraintLayout clHeaderBar;

    public ImageView getIvLeftBtn() {
        return ivLeftBtn;
    }


    public TextView getTvCenterTitle() {
        return tvCenterTitle;
    }

    public ImageView getIvRightBtn2() {
        return ivRightBtn2;
    }


    public SimpleTitleBar(Context context) {
        super(context);
        initializeViews(context);
    }

    public SimpleTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public SimpleTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.simple_title_bar, this);
        initView(view);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void initView(View view) {
        ivLeftBtn = (ImageView) view.findViewById(R.id.iv_left_btn);
        tvCenterTitle = (TextView) view.findViewById(R.id.tv_center_title);
        ivRightBtn2 = (ImageView) findViewById(R.id.iv_right_btn2);
        clHeaderBar = findViewById(R.id.cl_header_bar);
    }




    public void setLeftButtonVisible() {
        ivLeftBtn.setVisibility(VISIBLE);
    }

    public void setLeftButtonVisible(Drawable drawable) {
        setLeftButtonVisible();
        ivLeftBtn.setImageDrawable(drawable);
    }

    public void setLeftButtonInvisible() {
        ivLeftBtn.setVisibility(INVISIBLE);
    }


    public void setRightButton2Visible() {
        ivRightBtn2.setVisibility(VISIBLE);
    }

    public void setRightButton2Visible(Drawable drawable) {
        setRightButton2Visible();
        ivRightBtn2.setImageDrawable(drawable);
    }

    public void setRightButton2Invisible() {
        ivRightBtn2.setVisibility(INVISIBLE);
    }

    public void setTitleVisible(String title) {
        tvCenterTitle.setText(title);
        //setTitleVisible(title);
    }

    public void setLeftButtonOnClickListener(OnClickListener listener) {
        ivLeftBtn.setOnClickListener(listener);
    }

    public void setRightButton2OnClickListener(OnClickListener listener) {
        ivRightBtn2.setOnClickListener(listener);
    }

    public void changeHeaderBackground(int res_id) {
        clHeaderBar.setBackgroundColor(getResources().getColor(res_id, null));
    }


}
