package com.example.android.finalproject_yaocmengqid;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by YaoChen on 4/24/17.
 */

public class Expense implements Serializable{
    String amount;
    Date date;
    String expenseType;
    ArrayList<People> payers;
    ArrayList<People> members;

    public Expense(String amount, Date date, String expenseType, ArrayList<People> payers, ArrayList<People> members) {
        this.amount = amount;
        this.date = date;
        this.expenseType = expenseType;
        this.payers = payers;
        this.members = members;
    }

    public Expense() {

    }
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public void setDate(Date date) {
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
