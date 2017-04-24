package com.example.android.finalproject_yaocmengqid;

/**
 * Created by YaoChen on 4/24/17.
 */

public class Plan {
    String action;
    String money;
    String receiver;

    public Plan(String action, String money, String receiver) {
        this.action = action;
        this.money = money;
        this.receiver = receiver;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "action='" + action + '\'' +
                ", money='" + money + '\'' +
                ", receiver='" + receiver + '\'' +
                '}';
    }
}
