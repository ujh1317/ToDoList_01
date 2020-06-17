package com.example.todolist_01;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    private EditText edit_task;
    private Spinner spinner;
    private Button btn_add;

    private Integer[] priority = {0, 1, 2};

    SQLiteDatabase mDb;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // actionbar 이름 변경
        final ActionBar actionBar = getSupportActionBar();
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

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("id")) {

            long id = intent.getLongExtra("id", 1);

            getTask(id);

            actionBar.setTitle("Update");
            btn_add.setText("Update");
        }

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_task.getText().length() == 0) {
                    return;
                }
                if (actionBar.getTitle().equals("Add")) {

                    String task = edit_task.getText().toString();
                    int priority = (int) spinner.getSelectedItem();

                    addNewTask(task, priority);
                } else {
                    updateTask(id);

                    Toast.makeText(SecondActivity.this, "task is updated", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

    }

    private void getTask(long id) {

        Cursor cursor = mDb.query(ToDoContract.ToDoListEntry.TABLE_NAME,
                null,
                ToDoContract.ToDoListEntry._ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null);
        cursor.moveToFirst();

        String task = cursor.getString(cursor.getColumnIndex(ToDoContract.ToDoListEntry.COLUMN_TASK));
        int priority = cursor.getInt(cursor.getColumnIndex(ToDoContract.ToDoListEntry.COLUMN_PRIORITY));

        edit_task.setText(task);
        spinner.setSelection(priority);

    }

    private void addNewTask(String task, int priority) {
        ContentValues cv = new ContentValues();
        cv.put(ToDoContract.ToDoListEntry.COLUMN_TASK, task);
        cv.put(ToDoContract.ToDoListEntry.COLUMN_PRIORITY, priority);

        mDb.insert(ToDoContract.ToDoListEntry.TABLE_NAME, null, cv);

        Toast.makeText(this, "Task is added successfully", Toast.LENGTH_SHORT).show();

    }

    private void updateTask(long id) {

        String task = edit_task.getText().toString();
        int priority = (int) spinner.getSelectedItem();

        ContentValues cv = new ContentValues();
        cv.put(ToDoContract.ToDoListEntry.COLUMN_TASK, task);
        cv.put(ToDoContract.ToDoListEntry.COLUMN_PRIORITY, priority);

        mDb.update(ToDoContract.ToDoListEntry.TABLE_NAME,
                cv,
                ToDoContract.ToDoListEntry._ID + "=?",
                new String[]{String.valueOf(id)});

    }

}