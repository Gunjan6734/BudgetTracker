package com.budget_tracker;

import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.budget_tracker.data_model.CategoryDm;
import com.budget_tracker.event.PopUpMenuEvent;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import de.greenrobot.event.EventBus;

public class CommonFunction {


    public static String convertCategoryListIntoString(List<CategoryDm>  list){
       // String commaSeparatedString = String.join(",", list);
        String commaSeparatedString = "";
         commaSeparatedString = list
                .stream()
                .map(a -> String.valueOf(a.getCategory_id()+ "_"+ a.getCategory_name()))
                .collect(Collectors.joining(","));
        return commaSeparatedString;
    }


    public static void showPopUpMenu(Context context, View v, boolean showReportGeneration){
        PopupMenu popup = new PopupMenu(context, v);
        popup.inflate(R.menu.top_right_menu_options);
        popup.getMenu().getItem(1).setVisible(showReportGeneration);
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                EventBus.getDefault().post(new PopUpMenuEvent(item));
                return true;
            }
        });

    }

    public static int getIndex(Spinner spinner, String myString){
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }


}
