package com.budget_tracker.data_model;

public class MonthlyBudgetDm {

    private String budget_id;
    private String budget_month;
    private String budget_year;
    private String budget_amount;


    public MonthlyBudgetDm() {
    }

    public String getBudget_id() {
        return budget_id;
    }

    public void setBudget_id(String budget_id) {
        this.budget_id = budget_id;
    }

    public String getBudget_month() {
        return budget_month;
    }

    public void setBudget_month(String budget_month) {
        this.budget_month = budget_month;
    }

    public String getBudget_year() {
        return budget_year;
    }

    public void setBudget_year(String budget_year) {
        this.budget_year = budget_year;
    }

    public String getBudget_amount() {
        return budget_amount;
    }

    public void setBudget_amount(String budget_amount) {
        this.budget_amount = budget_amount;
    }
}
