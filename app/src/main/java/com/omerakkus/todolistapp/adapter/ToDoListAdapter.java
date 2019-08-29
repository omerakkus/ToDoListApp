package com.omerakkus.todolistapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omerakkus.todolistapp.R;
import com.omerakkus.todolistapp.model.TodoTitle;
import com.omerakkus.todolistapp.ui.MainActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public  class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.CustomViewHolder> {
    Context context;
    List<TodoTitle> todoTitleList;
    MainActivity mainActivity;

    public ToDoListAdapter(Context context, List<TodoTitle> todoItems) {
        this.context = context;
        this.todoTitleList = todoItems;

    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_title_item, parent, false);
        return new CustomViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        mainActivity = (MainActivity) context;
        TodoTitle todoTitle  = todoTitleList.get(position);
        holder.item_title.setText(todoTitle.getTodo_title());

        holder.llToDoItem.setOnClickListener(view -> mainActivity.setTodoTitle(todoTitle.getTodo_title()));

        holder.delete_title.setOnClickListener(view -> {
            mainActivity.deleteSelectedTitle(todoTitle.getTodo_title());
            todoTitleList.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return (null != todoTitleList ? todoTitleList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_title)
        TextView item_title;
        @BindView(R.id.llToDoItem)
        LinearLayout llToDoItem;
        @BindView(R.id.delete_title)
        FrameLayout delete_title;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,  itemView);
        }
    }
}