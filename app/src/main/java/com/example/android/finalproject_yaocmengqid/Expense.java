package com.example.android.finalproject_yaocmengqid;

import java.util.ArrayList;

/**
 * Created by YaoChen on 4/24/17.
 */

public class Expense {
    double amount;
    String date;
    String expenseType;
    ArrayList<People> payers;
    ArrayList<People> members;

    public Expense(double amount, String date, String expenseType, ArrayList<People> payers, ArrayList<People> members) {
        this.amount = amount;
        this.date = date;
        this.expenseType = expenseType;
        this.payers = payers;
        this.members = members;
    }

    public Expense() {

    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public ArrayList<People> getPayers() {
        return payers;
    }

    public void setPayers(ArrayList<People> payers) {
        this.payers = payers;
    }

    public ArrayList<People> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<People> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "amount=" + amount +
                ", date='" + date + '\'' +
                ", expenseType='" + expenseType + '\'' +
                ", payers=" + payers +
                ", members=" + members +
                '}';
    }
}
