package com.example.android.finalproject_yaocmengqid.MainContent;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.finalproject_yaocmengqid.Expense;
import com.example.android.finalproject_yaocmengqid.People;
import com.example.android.finalproject_yaocmengqid.SideMenu.ChoosePeopleActivity;
import com.example.android.finalproject_yaocmengqid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RecordActivity extends AppCompatActivity
    implements DatePickerDialog.OnDateSetListener {
    DatePickerDialog datePickerDialog;

    DatabaseReference mExpensesRef;

    private ArrayList<People> payers, members;
    private Date date;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, RecordActivity.this, year, month, day);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mExpensesRef = FirebaseDatabase.getInstance().getReference(user.getUid()).child("expenses");
                } else {
                    finish();
                }
            }
        });
    }

    public void showDatePickerDialog(View view) {
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        date = cal.getTime();
        String dateString = format.format(date);
        ((Button) findViewById(R.id.dateButton)).setText(dateString);
    }

    public void choosePayer(View view){
        Intent intent = new Intent(this, ChoosePeopleActivity.class);
        if (payers != null)
            intent.putExtra("selected", payers);
        startActivityForResult(intent, 1);
    }

    public void chooseMember(View view){
        Intent intent = new Intent(this, ChoosePeopleActivity.class);
        if (members != null)
            intent.putExtra("selected", members);
        startActivityForResult(intent, 2);
    }

    public void pickType(View view) {
        RadioButton radio = (RadioButton) view;
        if (radio.isChecked()) {
            type = radio.getText().toString();
        }
    }

    public void saveExpense(View view) {
        String amountString = ((EditText)findViewById(R.id.billAmount)).getText().toString();
        if (amountString.length() == 0) {
            Toast.makeText(this, "Please enter the total amount.", Toast.LENGTH_SHORT);
            return;
        }
        if (date == null) {
            Toast.makeText(this, "Please enter the date.", Toast.LENGTH_SHORT);
            return;
        }
        if (type == null || type.length() == 0) {
            Toast.makeText(this, "Please choose a type.", Toast.LENGTH_SHORT);
            return;
        }
        if (payers == null || payers.size() == 0) {
            Toast.makeText(this, "Please select at least one payer.", Toast.LENGTH_SHORT);
            return;
        }
        if (members == null || members.size() == 0) {
            Toast.makeText(this, "Please select at least one member.", Toast.LENGTH_SHORT);
            return;
        }
        double amount = Double.parseDouble(amountString);
        Expense expense = new Expense(amount, date, type, payers, members);
        mExpensesRef.push().setValue(expense).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RecordActivity.this, "Record expense successfully.", Toast.LENGTH_SHORT);
                    return;
                } else {
                    Toast.makeText(RecordActivity.this, "Record expense failed.", Toast.LENGTH_SHORT);
                    return;
                }
            }
        });
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!data.hasExtra("selected"))
            return;
        ArrayList<People> selected = (ArrayList<People>) data.getSerializableExtra("selected");
        String names = "";
        for (People people : selected) {
            if (names.length() == 0)
                names = people.getName();
            else names = names + "," + people.getName();
        }
        if (requestCode == 1) {
            // Payer selected
            payers = selected;
            ((TextView)findViewById(R.id.payer)).setText(names);
        } else if (requestCode == 2) {
            // Member selected
            members = selected;
            ((TextView)findViewById(R.id.member)).setText(names);
        }
    }
}
