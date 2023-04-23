package com.budget_tracker.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.budget_tracker.data_model.CategoryDm;
import com.budget_tracker.data_model.IncomeDm;
import com.budget_tracker.data_model.MonthlyBudgetDm;
import com.budget_tracker.data_model.SpendingHistoryDm;
import com.budget_tracker.data_model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "budget_tracker.db";
    private static final int DATABASE_VERSION = 1;
    private HashMap hp;

    private static final String TABLE_USER = "user";
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_SPENDING_HISTORY = "spending_history";
    private static final String TABLE_INCOME = "income";
    private static final String TABLE_MONTHLY_BUDGET = "monthly_budget";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";


    // Table Monthly BUdget

    private static final String COLUMN_MONTHLY_BUDGET_ID = "budget_id";
    private static final String COLUMN_MONTHLY_BUDGET_AMOUNT = "budget_amount";
    private static final String COLUMN_MONTHLY_BUDGET_MONTH = "budget_month";
    private static final String COLUMN_MONTHLY_BUDGET_YEAR = "budget_year";


    // category table columns

    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_CATEGORY_NAME = "category_name";


    //Spending History table
    private static final String COLUMN_SPENDING_HISTORY_ID = "spending_history_id";
    private static final String COLUMN_HISTORY_CATEGORY_NAME = "history_category_name";
    private static final String COLUMN_HISTORY_AMOUNT = "history_amount";
    private static final String COLUMN_HISTORY_DATE = "history_date";
    private static final String COLUMN_HISTORY_TYPE = "history_type";



    // Income Details Table

    private static final String COLUMN_INCOME_ID = "income_id";
    private static final String COLUMN_INCOME_SOURCE = "income_source";
    private static final String COLUMN_INCOME_AMOUNT = "income_amount";

    private static final String COLUMN_INCOME_DATE = "income_date";


// Query to create user table
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";

    // Query to create category table
    private String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "("
            + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_CATEGORY_NAME + " TEXT" + ")";

    // Query to create history table
    private String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_SPENDING_HISTORY + "("
            + COLUMN_SPENDING_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_HISTORY_CATEGORY_NAME + " TEXT,"
            + COLUMN_HISTORY_AMOUNT + " TEXT,"
            + COLUMN_HISTORY_DATE + " TEXT,"
            + COLUMN_HISTORY_TYPE + " TEXT" + ")";


    // Query to create Income table
    private String CREATE_INCOME_TABLE = "CREATE TABLE " + TABLE_INCOME + "("
            + COLUMN_INCOME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_INCOME_SOURCE + " TEXT,"
            + COLUMN_INCOME_DATE + " TEXT,"
            + COLUMN_INCOME_AMOUNT + " TEXT" + ")";

    // Query to create Income table
    private String CREATE_MONTHLY_BUDGET_TABLE = "CREATE TABLE " + TABLE_MONTHLY_BUDGET + "("
            + COLUMN_MONTHLY_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_MONTHLY_BUDGET_YEAR + " TEXT,"
            + COLUMN_MONTHLY_BUDGET_MONTH + " TEXT,"
            + COLUMN_MONTHLY_BUDGET_AMOUNT + " TEXT" + ")";



    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_TABLE_CATEGORY = "DROP TABLE IF EXISTS " + TABLE_CATEGORY;
    private String DROP_HISTORY_TABLE = "DROP TABLE IF EXISTS " + TABLE_SPENDING_HISTORY;
    private String DROP_INCOME_TABLE = "DROP TABLE IF EXISTS " + TABLE_INCOME;
    private String DROP_MONTHLY_BUDGET_TABLE = "DROP TABLE IF EXISTS " + TABLE_MONTHLY_BUDGET;


    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_HISTORY_TABLE);
        db.execSQL(CREATE_INCOME_TABLE);
        db.execSQL(CREATE_MONTHLY_BUDGET_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //Drop User Table if exist
            db.execSQL(DROP_USER_TABLE);
            db.execSQL(DROP_TABLE_CATEGORY);
            db.execSQL(DROP_HISTORY_TABLE);
            db.execSQL(DROP_INCOME_TABLE);
            db.execSQL(DROP_MONTHLY_BUDGET_TABLE);
            // Create tables again
            onCreate(db);
    }


    public String addUser(User user) {
        if(checkUserAlreadyExist(user.getEmail())) {
            return "Email Already Exist";
        }else{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_NAME, user.getName());
            values.put(COLUMN_USER_EMAIL, user.getEmail());
            values.put(COLUMN_USER_PASSWORD, user.getPassword());
            // Inserting Row
            db.insert(TABLE_USER, null, values);
            db.close();
            return "User Added";
        }
    }





    // Add or update Monthly Budget
    public void addMonthlyBudget(MonthlyBudgetDm dm, String email) {

        if(checkIfBudgetAlreadyExist(dm, email)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_EMAIL, email);
            values.put(COLUMN_MONTHLY_BUDGET_MONTH, dm.getBudget_month());
            values.put(COLUMN_MONTHLY_BUDGET_YEAR, dm.getBudget_year());
            values.put(COLUMN_MONTHLY_BUDGET_AMOUNT, dm.getBudget_amount());
            //updateBudget(dm,email);
            db.update(TABLE_MONTHLY_BUDGET, values, COLUMN_MONTHLY_BUDGET_ID + " = ?",
                    new String[]{String.valueOf(dm.getBudget_id())});
            db.close();
        }
        else{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_EMAIL, email);
            values.put(COLUMN_MONTHLY_BUDGET_MONTH, dm.getBudget_month());
            values.put(COLUMN_MONTHLY_BUDGET_YEAR, dm.getBudget_year());
            values.put(COLUMN_MONTHLY_BUDGET_AMOUNT, dm.getBudget_amount());
            // Inserting Row
            db.insert(TABLE_MONTHLY_BUDGET, null, values);
            db.close();

        }

    }

    private void updateBudget(MonthlyBudgetDm dm, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, dm.getBudget_month());
        values.put(COLUMN_MONTHLY_BUDGET_MONTH, dm.getBudget_month());
        values.put(COLUMN_MONTHLY_BUDGET_YEAR, dm.getBudget_year());
        values.put(COLUMN_MONTHLY_BUDGET_AMOUNT, dm.getBudget_amount());
        // updating row
        db.update(TABLE_MONTHLY_BUDGET, values, COLUMN_MONTHLY_BUDGET_ID + " = ?",
                new String[]{String.valueOf(dm.getBudget_id())});
        db.close();
    }

    public void addExpenses(SpendingHistoryDm user, String loggedInUserEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, loggedInUserEmail);
        values.put(COLUMN_HISTORY_CATEGORY_NAME, user.getCategory_name());
        values.put(COLUMN_HISTORY_AMOUNT, user.getAmount());
        values.put(COLUMN_HISTORY_DATE, user.getDate());
        values.put(COLUMN_HISTORY_TYPE, user.getType());
        // Inserting Row
        db.insert(TABLE_SPENDING_HISTORY, null, values);
        db.close();
    }


    public String addCategory(CategoryDm categoryDm){
        if(checkIfCategoryAlreadyExist(categoryDm)){
            return "Category Already Exist";
        }
        else{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY_NAME, categoryDm.getCategory_name());
            // Inserting Row
            db.insert(TABLE_CATEGORY, null, values);
            db.close();
            return "Category Added";
        }
    }

    private boolean checkIfCategoryAlreadyExist(CategoryDm categoryDm) {
        String[] columns = {
                COLUMN_CATEGORY_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_CATEGORY_NAME + " = ?" ;
        // selection arguments
        String[] selectionArgs = {categoryDm.getCategory_name()};
        // query user table with conditions
        Cursor cursor = db.query(TABLE_CATEGORY, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }


    public void add_categories(ArrayList<CategoryDm> list) {
        SQLiteDatabase database = this.getWritableDatabase();
        //String sql = "INSERT INTO " + TABLE_CATEGORY + " VALUES(?, ?)";
//        SQLiteStatement statement = database.compileStatement(sql);
//        database.beginTransaction();
//        try {
//            for (CategoryDm c : list) {
//                statement.clearBindings();
//                statement.bindString(1, c.getCategory_name());
//                statement.execute();
//            }
//            database.setTransactionSuccessful();
//        } finally {
//            database.endTransaction();
//        }
    }

    @SuppressLint("Range")
    public List<IncomeDm>  getIncomeData(String email) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_INCOME_ID,
                COLUMN_INCOME_DATE,
                COLUMN_INCOME_SOURCE,
                COLUMN_INCOME_AMOUNT

        };
        // sorting orders
//        String sortOrder =
//                COLUMN_INCOME_DATE + " ASC";
        String selection = COLUMN_USER_EMAIL + " = ?" ;
        // selection arguments
        String[] selectionArgs = {email};

        List<IncomeDm> incomeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the user table

        Cursor cursor = db.query(TABLE_INCOME, columns, selection,
                selectionArgs, null, null, null);
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                IncomeDm dm = new IncomeDm();
                dm.setIncome_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_ID))));
                dm.setIncome_source(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_SOURCE)));
                dm.setIncome_amount(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_AMOUNT)));
                dm.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_DATE)));

                // Adding income record to list
                incomeList.add(dm);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return incomeList;
    }


    @SuppressLint("Range")
    public MonthlyBudgetDm  getBudgetByMonth (String email,String month, String year) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_MONTHLY_BUDGET_ID,
                COLUMN_MONTHLY_BUDGET_MONTH,
                COLUMN_MONTHLY_BUDGET_YEAR,
                COLUMN_MONTHLY_BUDGET_AMOUNT
        };
        // sorting orders

        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_MONTHLY_BUDGET_YEAR + " = ?"
                + " AND " + COLUMN_MONTHLY_BUDGET_MONTH + " = ?";
        // selection arguments
        String[] selectionArgs = {email,year,month};

        MonthlyBudgetDm dm = new MonthlyBudgetDm();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the user table

        Cursor cursor = db.query(TABLE_MONTHLY_BUDGET, columns, selection,
                selectionArgs, null, null, null);
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                dm.setBudget_id(cursor.getString(cursor.getColumnIndex(COLUMN_MONTHLY_BUDGET_ID)));
                dm.setBudget_month(cursor.getString(cursor.getColumnIndex(COLUMN_MONTHLY_BUDGET_MONTH)));
                dm.setBudget_year(cursor.getString(cursor.getColumnIndex(COLUMN_MONTHLY_BUDGET_YEAR)));
                dm.setBudget_amount(cursor.getString(cursor.getColumnIndex(COLUMN_MONTHLY_BUDGET_AMOUNT)));
                // Adding income record to list
//                budgetList.add(dm);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return dm;
    }

    @SuppressLint("Range")
    public List<CategoryDm> getAllCategories(){
        String[] columns = {
                COLUMN_CATEGORY_ID,
                COLUMN_CATEGORY_NAME
        };
        // sorting orders
        String sortOrder =
                COLUMN_CATEGORY_NAME + " ASC";
        List<CategoryDm> catList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the user table

        Cursor cursor = db.query(TABLE_CATEGORY, columns, null,
                null, null, null, sortOrder);
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CategoryDm categoryDm = new CategoryDm();
                categoryDm.setCategory_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_ID))));
                categoryDm.setCategory_name(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME)));
                // Adding user record to list
                catList.add(categoryDm);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return catList;

    }

    @SuppressLint("Range")
    public List<SpendingHistoryDm>  getSpendingHistory(String email) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_SPENDING_HISTORY_ID,
                COLUMN_HISTORY_CATEGORY_NAME,
                COLUMN_HISTORY_AMOUNT,
                COLUMN_HISTORY_DATE,
                COLUMN_HISTORY_TYPE
        };
        // sorting orders
        String sortOrder =
                COLUMN_HISTORY_CATEGORY_NAME + " ASC";
        String selection = COLUMN_USER_EMAIL + " = ?" ;
        // selection arguments
        String[] selectionArgs = {email};

        List<SpendingHistoryDm> historyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the user table

        Cursor cursor = db.query(TABLE_SPENDING_HISTORY, columns, selection,
                selectionArgs, null, null, sortOrder);
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SpendingHistoryDm dm = new SpendingHistoryDm();
                dm.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SPENDING_HISTORY_ID))));
                dm.setCategory_name(cursor.getString(cursor.getColumnIndex(COLUMN_HISTORY_CATEGORY_NAME)));
                dm.setAmount(cursor.getString(cursor.getColumnIndex(COLUMN_HISTORY_AMOUNT)));
                dm.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_HISTORY_DATE)));
                dm.setType(cursor.getString(cursor.getColumnIndex(COLUMN_HISTORY_TYPE)));
                // Adding user record to list
                historyList.add(dm);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return historyList;
    }



    public void addIncome(IncomeDm dm, String loggedInUserEmail){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, loggedInUserEmail);
        values.put(COLUMN_INCOME_DATE, dm.getDate());
        values.put(COLUMN_INCOME_SOURCE, dm.getIncome_source());
        values.put(COLUMN_INCOME_AMOUNT, dm.getIncome_amount());
        // Inserting Row
        db.insert(TABLE_INCOME, null, values);
        db.close();
    }
    public boolean checkUserLogin(String email, String password) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        // selection arguments
        String[] selectionArgs = {email, password};
        // query user table with conditions
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }


    public boolean checkUserAlreadyExist(String email) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" ;
        // selection arguments
        String[] selectionArgs = {email};
        // query user table with conditions
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }


    public boolean checkIfBudgetAlreadyExist(MonthlyBudgetDm dm, String email) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_MONTHLY_BUDGET_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?"  + " AND " + COLUMN_MONTHLY_BUDGET_MONTH + " = ?"
                + " AND " + COLUMN_MONTHLY_BUDGET_YEAR + " = ?";
        // selection arguments
        String[] selectionArgs = {email, dm.getBudget_month(), dm.getBudget_year()};
        // query user table with conditions
        Cursor cursor = db.query(TABLE_MONTHLY_BUDGET, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }


    public int getTotalSpending(String email) {
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        // selection arguments
        String query = "select sum("  +  COLUMN_HISTORY_AMOUNT +") from " + TABLE_SPENDING_HISTORY + " WHERE " + COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.rawQuery(query, selectionArgs);            //The values for the WHERE clause);
        if (cursor.moveToFirst()) result = cursor.getInt(0);
        cursor.close();
        db.close();
        return result;
    }



    public int getTotalIncome(String email) {
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        // selection arguments
        String query = "select sum("  +  COLUMN_INCOME_AMOUNT +") from "
                + TABLE_INCOME + " WHERE " + COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.rawQuery(query, selectionArgs);            //The values for the WHERE clause);
        if (cursor.moveToFirst()) result = cursor.getInt(0);
        cursor.close();
        db.close();
        return result;
    }





    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public void insertDefaultCategories(){}



}
