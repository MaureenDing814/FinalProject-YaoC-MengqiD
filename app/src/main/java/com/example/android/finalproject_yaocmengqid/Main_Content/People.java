package com.example.android.finalproject_yaocmengqid.Main_Content;

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
import java.math.BigInteger;

/**
 * Created by Maureen_Ding on 4/24/17.
 */

public class People implements Serializable{
    public String name;
    public String email;
    public double pay;
    public double loan;

    //private DatabaseReference mRef;
    //private StorageReference mStorageRef;

    public People(){
    }

    public People(String name, String email, double pay, double loan)

    {
        this.name = name;
        this.email=email;
        this.pay = 0;
        this.loan = 0;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public double getPay() {
        return pay;
    }

    public double getLoan() {
        return loan;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }

    public void setLoan(double loan) {
        this.loan = loan;
    }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
