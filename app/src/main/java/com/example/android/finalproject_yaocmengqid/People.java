package com.example.android.finalproject_yaocmengqid;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by Maureen_Ding on 4/24/17.
 */

public class People implements Serializable{
    private String name;
    private String email;
    private String pay;
    private String expense;


    public People(){
    }

    public People(String name, String email) {
        this.name = name;
        this.email = email;
        this.pay = "0.00";
        this.expense = "0.00";
    }

    public People(String name, String email, String pay, String expense) {
        this.name = name;
        this.email = email;
        this.pay = pay;
        this.expense = expense;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPay() {
        return pay;
    }

    public String getExpense() {
        return expense;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
