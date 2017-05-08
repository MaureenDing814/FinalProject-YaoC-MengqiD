package com.example.android.finalproject_yaocmengqid.MainContent;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;

import com.example.android.finalproject_yaocmengqid.SideMenu.ChoosePeopleActivity;
import com.example.android.finalproject_yaocmengqid.R;

public class RecordActivity extends AppCompatActivity
    implements DatePickerDialog.OnDateSetListener {
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        datePickerDialog = new DatePickerDialog(this, RecordActivity.this, 2017, 5, 1);
    }

    public void showDatePickerDialog(View view) {
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    public void choosePayer(View view){
        startActivityForResult(new Intent(this, ChoosePeopleActivity.class), 1);
    }

    public void chooseMember(View view){
        startActivityForResult(new Intent(this, ChoosePeopleActivity.class), 2);
    }

    public void saveExpense(View view){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            // Payer selected
        } else if (requestCode == 2) {
            // Member selected
        }
    }
}
