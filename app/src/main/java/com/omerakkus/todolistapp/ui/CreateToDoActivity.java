package com.omerakkus.todolistapp.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.omerakkus.todolistapp.R;
import com.omerakkus.todolistapp.model.ToDoItem;
import com.omerakkus.todolistapp.db.TaskDbHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateToDoActivity extends AppCompatActivity {

    private String date_time;
    String titleTodo;
    String[] status;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private TaskDbHelper mHelper;

    ToDoItem toDoItem;
    @BindView(R.id.createButton)
    Button createButton;
    @BindView(R.id.cancelCreateButton)
    Button cancelButton;
    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.etDesc)
    EditText etDesc;
    @BindView(R.id.msToDoItem)
    MaterialSpinner msToDoItem;
    @BindView(R.id.btnDate)
    Button btnDate;
    @OnClick(R.id.btnDate)
    public void selectDate(){
        datePicker();
    }
    @OnClick(R.id.createButton)
    public void createTodo(){
        createTodoItems();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create To Do");

        mHelper = new TaskDbHelper(this);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            titleTodo = extras.getString("categoryName");
        }

        Calendar c = Calendar.getInstance(Locale.getDefault());
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
        btnDate.setText(sdf.format(new Date()));

        msToDoItem.setBackgroundResource(R.drawable.spinner_unselect_border);
        msToDoItem.setText("Select a status");
        status = getResources().getStringArray(R.array.todo_status);
        msToDoItem.setItems(status);


        updateCreateButtonState();
        updateDateButton();
    }

    private void createTodoItems() {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy H:mm");
        try {
            Date date = (Date)formatter.parse(date_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        toDoItem  = new ToDoItem(titleTodo, etTitle.getText().toString(),etDesc.getText().toString(), date_time ,msToDoItem.getText().toString());
        mHelper.insertTodoList(toDoItem);
        finish();

    }


    private void datePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    if (view.isShown()) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;

                        updateDateButton();
                        timePicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.setTitle("Select a day");
        datePickerDialog.show();
    }

    void updateDateButton() {

        // Build date string from Custom dialog
        date_time =  mDay + "/" +(mMonth + 1)  + "/" + mYear + " "
                + mHour + ":" + mMinute;

        String toUpdate;
        toUpdate = CustomDateFactory("dd/MM/yyyy H:mm",
                "EEE, d MMM yyyy HH:mm",
                date_time);

        btnDate.setText(toUpdate);
    }

    private void timePicker(){

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {

                    if (view.isShown()) {
                        mHour = hourOfDay;
                        mMinute = minute;

                        updateDateButton();
                    }
                }, mHour, mMinute, false);
        timePickerDialog.setTitle("Select an hour");
        timePickerDialog.show();
    }

    String CustomDateFactory(String expectedPattern, String outputPattern, String input) {

        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(outputPattern);
            Date date = formatter.parse(input);
            String output = sdf.format(date);
            return (output);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return (null);
    }

    void updateCreateButtonState() {
        //createButton.setEnabled(!TextUtils.isEmpty(etTitle.getText()));
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity.
        }
        return super.onOptionsItemSelected(item);
    }



}
