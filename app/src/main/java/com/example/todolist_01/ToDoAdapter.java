package com.example.todolist_01;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private Context mContext;
    private Cursor cursor;

    public ToDoAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, viewGroup, false);
        return new ToDoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder toDoViewHolder, int i) {

        if (cursor.moveToPosition(i)){
            return;
        }
        String task = cursor.getString(cursor.getColumnIndex(ToDoContract.ToDoListEntry.COLUMN_TASK));
        int priority = cursor.getInt(cursor.getColumnIndex(ToDoContract.ToDoListEntry.COLUMN_PRIORITY));
        String timestamp = cursor.getString(cursor.getColumnIndex(ToDoContract.ToDoListEntry.COLUMN_TIMESTAMP));

        toDoViewHolder.task_text.setText(task);
        toDoViewHolder.priority_text.setText(String.valueOf(priority));
        toDoViewHolder.timestamp_text.setText(timestamp);

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder {

        TextView task_text, priority_text, timestamp_text;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);

            task_text = itemView.findViewById(R.id.task_text);
            priority_text = itemView.findViewById(R.id.priority_text);
            timestamp_text = itemView.findViewById(R.id.timestamp_text);
        }
    }
    public void swapCursor(Cursor newCursor){

        if (cursor != null){
            cursor.close();
        }
        cursor = newCursor;
        if (newCursor != null){
            this.notifyDataSetChanged();
        }
    }
}
