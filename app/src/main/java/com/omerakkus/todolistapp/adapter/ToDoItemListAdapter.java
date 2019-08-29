package com.omerakkus.todolistapp.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omerakkus.todolistapp.R;
import com.omerakkus.todolistapp.db.TaskContract;
import com.omerakkus.todolistapp.db.TaskDbHelper;
import com.omerakkus.todolistapp.model.ToDoItem;
import com.omerakkus.todolistapp.model.TodoTitle;
import com.omerakkus.todolistapp.ui.MainActivity;
import com.omerakkus.todolistapp.ui.ToDoDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToDoItemListAdapter extends RecyclerView.Adapter<ToDoItemListAdapter.CustomViewHolder>  {

    Context context;
    List<ToDoItem> toDoItems;
    private LayoutInflater mInflater;
    ToDoDetailActivity toDoDetailActivity;
    TaskDbHelper mHelper;

    public ToDoItemListAdapter(Context context, List<ToDoItem> toDoItems) {
        this.context = context;
        this.toDoItems = toDoItems;
        mInflater = LayoutInflater.from(context);
        toDoDetailActivity = (ToDoDetailActivity) context;
        mHelper = new TaskDbHelper(context);
    }

    @NonNull
    @Override
    public ToDoItemListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        return new ToDoItemListAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        ToDoItem toDoItem = toDoItems.get(position);

        holder.todo_item_name.setText(toDoItem.getName());
        holder.todo_item_desc.setText(toDoItem.getDescription());
        holder.todo_item_due.setText(toDoItem.getDue_time());
        holder.todo_item_status.setText(toDoItem.getStatus());

        holder.delete_layout.setOnClickListener(view -> {
            toDoDetailActivity.deleteSelectedItem(toDoItem.getName());
            toDoItems.remove(position);
            notifyItemRemoved(position);
        });

        holder.llToDoItem.setOnLongClickListener(view -> {
            SQLiteDatabase db = mHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            if (toDoItem.getStatus().equals("In Progress")) {
                values.put(TaskContract.TaskEntry.COL_TASK_STATUS, "Done");
                holder.todo_item_name.setPaintFlags((holder.todo_item_name.getPaintFlags() | (~ Paint.STRIKE_THRU_TEXT_FLAG)));
            } else {
                values.put(TaskContract.TaskEntry.COL_TASK_STATUS, "In Progress");
            }
            db.update(TaskContract.TaskEntry.TASK_TABLE, values, " name= '" + toDoItem.getName() + "'", null);
            db.close();
            ((ToDoDetailActivity) context).refreshItemList();
            return true;
        });
    }


    @Override
    public int getItemCount() {
        return (null != toDoItems ? toDoItems.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.todo_item_name)
        TextView todo_item_name;
        @BindView(R.id.todo_item_desc)
        TextView todo_item_desc;
        @BindView(R.id.todo_item_due)
        TextView todo_item_due;
        @BindView(R.id.todo_item_status)
        TextView todo_item_status;
        @BindView(R.id.delete_layout)
        FrameLayout delete_layout;
        @BindView(R.id.llToDoItem)
        LinearLayout llToDoItem;

        public CustomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
