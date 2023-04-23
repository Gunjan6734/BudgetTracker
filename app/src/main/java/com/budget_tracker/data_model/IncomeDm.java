package com.budget_tracker.data_model;

public class IncomeDm {
    private int income_id;
    private String income_source;
    private String income_amount;

    private String date;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public IncomeDm() {
    }

    public int getIncome_id() {
        return income_id;
    }

    public void setIncome_id(int income_id) {
        this.income_id = income_id;
    }

    public String getIncome_source() {
        return income_source;
    }

    public void setIncome_source(String income_source) {
        this.income_source = income_source;
    }

    public String getIncome_amount() {
        return income_amount;
    }

    public void setIncome_amount(String income_amount) {
        this.income_amount = income_amount;
    }
}
