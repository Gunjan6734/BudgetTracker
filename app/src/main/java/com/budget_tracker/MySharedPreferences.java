package com.budget_tracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MySharedPreferences {
        static final String PREF_USER_LOGGEDIN_STATUS = "logged_in_status";

        public static SharedPreferences getSharedPreferences(Context ctx)
        {
            return PreferenceManager.getDefaultSharedPreferences(ctx);
        }



        public static void setUserLoggedInStatus(Context ctx, boolean status, String email)
        {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putBoolean(PREF_USER_LOGGEDIN_STATUS, status);
            editor.putString("user_email", email);
            editor.apply();
        }

        public static boolean getUserLoggedInStatus(Context ctx)
        {
            return getSharedPreferences(ctx).getBoolean(PREF_USER_LOGGEDIN_STATUS, false);
        }


    public static String getLoggedInUserEmail(Context ctx)
    {
        return getSharedPreferences(ctx).getString("user_email", "");
    }


    public static void logout(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_USER_LOGGEDIN_STATUS, false);
        editor.putString("user_email","");
        editor.apply();
        Intent i = new Intent(ctx, LoginActivity.class);
        ctx.startActivity(i);

    }

    }
