package com.example.todolist_01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements ListItemClickListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private ToDoAdapter adapter;
    SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });

        ToDoDbHelper dbHelper = new ToDoDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        Cursor cursor = getAllTasks();

        adapter = new ToDoAdapter(this, cursor, this);

        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                long id = (long) viewHolder.itemView.getTag();
                removeTask(id);

                adapter.swapCursor(getAllTasks());
            }
        }).attachToRecyclerView(recyclerView);

    }

    private void removeTask(long id) {
        mDb.delete(ToDoContract.ToDoListEntry.TABLE_NAME,
                ToDoContract.ToDoListEntry._ID + "=" + id,
                null);

        Toast.makeText(this, "task is deleted", Toast.LENGTH_SHORT).show();
    }

    private Cursor getAllTasks() {
        return mDb.query(ToDoContract.ToDoListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ToDoContract.ToDoListEntry.COLUMN_TIMESTAMP);
    }

    @Override
    protected void onStart() {
        super.onStart();

        adapter.swapCursor(getAllTasks());
    }

    @Override
    public void onClick(View view, int position) {

        long id = (long) view.getTag();

        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.delete_all) {

            mDb.delete(ToDoContract.ToDoListEntry.TABLE_NAME,
                    null,
                    null);
            adapter.swapCursor(getAllTasks());

            Toast.makeText(this, "All tasks are deleted", Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}