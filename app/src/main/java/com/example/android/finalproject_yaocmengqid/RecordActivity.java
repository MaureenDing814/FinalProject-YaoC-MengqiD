package com.example.android.finalproject_yaocmengqid;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;

public class RecordActivity extends AppCompatActivity
    implements DatePickerDialog.OnDateSetListener {
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        datePickerDialog = new DatePickerDialog(this, RecordActivity.this, 1970, 1, 1);
    }

    public void showDatePickerDialog(View view) {
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    public void saveExpense(View view){
        finish();
    }
}
