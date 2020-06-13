package com.example.todolist_01;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SecondActivity extends AppCompatActivity {

    private EditText edit_task;
    private Spinner spinner;
    private Button btn_add;

    private Integer[] priority = {0, 1, 2};

    SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // actionbar 이름 변경
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add");

        // actionbar 에 back button 추가
        actionBar.setDisplayHomeAsUpEnabled(true);

        edit_task = findViewById(R.id.edit_task);
        spinner = findViewById(R.id.spinner);
        btn_add = findViewById(R.id.btn_add);

        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, priority);
        spinner.setAdapter(arrayAdapter);

        ToDoDbHelper dbHelper = new ToDoDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

    }
}