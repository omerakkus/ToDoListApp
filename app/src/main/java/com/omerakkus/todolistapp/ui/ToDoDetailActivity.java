package com.omerakkus.todolistapp.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.omerakkus.todolistapp.R;
import com.omerakkus.todolistapp.model.SerializedToDoItem;
import com.omerakkus.todolistapp.model.ToDoItem;
import com.omerakkus.todolistapp.adapter.ToDoItemListAdapter;
import com.omerakkus.todolistapp.db.TaskDbHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToDoDetailActivity extends AppCompatActivity {

    String  categoryName;
    @BindView(R.id.rvTodoDetail)
    RecyclerView rvTodoDetail;
    @BindView(R.id.tvNoToDoItem)
    TextView tvNoToDoItem;
    ToDoItemListAdapter toDoItemListAdapter;
    TaskDbHelper mHelper;
    @BindView(R.id.etSearchItem)
    EditText etSearchItem;
    public boolean isSearchItem = false;
    private String searchText;
    String checkStatus ="";
    boolean isAscOrder = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_detail);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mHelper = new TaskDbHelper(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            SerializedToDoItem serializedToDoItem = (SerializedToDoItem)extras.get("itemsoftodo");
            categoryName =  extras.getString("categoryName");
            getSupportActionBar().setTitle(categoryName);
            assert serializedToDoItem != null;
            toDoItemListAdapter = new ToDoItemListAdapter(this, serializedToDoItem.getToDoItems());
            rvTodoDetail.setLayoutManager(new LinearLayoutManager(this));
            rvTodoDetail.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            rvTodoDetail.setAdapter(toDoItemListAdapter);

            searchText="";


            if(serializedToDoItem.getToDoItems().size()== 0){
                tvNoToDoItem.setVisibility(View.VISIBLE);
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshItemList();

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                isSearchItem = (etSearchItem.getText().length() > 0) ? true : false;
                searchText = etSearchItem.getText().toString();
                refreshItemList();

            }
        };

        etSearchItem.addTextChangedListener(tw);
        /*List<ToDoItem> searchResults = mHelper.searchTodoItem(searchText);
        toDoItemListAdapter = new ToDoItemListAdapter(this, searchResults);
        rvTodoDetail.setLayoutManager(new LinearLayoutManager(this));
        rvTodoDetail.setAdapter(toDoItemListAdapter);
        toDoItemListAdapter.notifyDataSetChanged();*/
    }

    public void refreshItemList(){
        List<ToDoItem> toDoList;
        if(isSearchItem){
            toDoList =  mHelper.listOfTodo(searchText, true);
        }else{
            toDoList =  mHelper.listOfTodo(categoryName, false);
        }
        toDoItemListAdapter = new ToDoItemListAdapter(this, toDoList);
        rvTodoDetail.setLayoutManager(new LinearLayoutManager(this));
        rvTodoDetail.setAdapter(toDoItemListAdapter);
        toDoItemListAdapter.notifyDataSetChanged();
        if(toDoList.size()== 0){
            tvNoToDoItem.setVisibility(View.VISIBLE);
        }else{
            tvNoToDoItem.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity.
        }

        if (id == R.id.action_add) {
            Intent i = new Intent(this, CreateToDoActivity.class);
            i.putExtra("categoryName", categoryName);
            startActivity(i);
        }
        if(id == R.id.action_filter){
            filtertems();
        }
        if(id == R.id.action_order){
            orderNameItems();
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteSelectedItem(String title) {
        Integer deleteRow = mHelper.deleteTodoItem(title);
        if(deleteRow > 0){
            refreshItemList();
            Toast.makeText(getApplicationContext(),"Item deleted successfully.", Toast.LENGTH_SHORT).show();
        }
    }

    public void filtertems(){
        AlertDialog alertDialog = new AlertDialog.Builder(
                ToDoDetailActivity.this).create(); // Read
        alertDialog.setTitle("Select filter fields");
        View dialogView = getLayoutInflater().inflate(R.layout.filter_dialog, null);
        RadioGroup rg = (RadioGroup) dialogView.findViewById(R.id.radioGroup);

        MaterialSpinner msItemStatus = (MaterialSpinner) dialogView.findViewById(R.id.msItemStatus);
        msItemStatus.setBackgroundResource(R.drawable.spinner_unselect_border);
        msItemStatus.setItems(getResources().getStringArray(R.array.todo_status));
        rg.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            switch (checkedId){
                case R.id.sort_name_asc:
                    checkStatus = "ASC";
                    break;
                case R.id.sort_name_desc:
                    checkStatus = "DESC";
                    break;
            }
        });
        alertDialog.setView(dialogView);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Apply",
                (dialog, which) -> {
                    List<ToDoItem> toDoList = mHelper.filterItems(checkStatus, msItemStatus.getText().toString());
                    toDoItemListAdapter = new ToDoItemListAdapter(this, toDoList);
                    rvTodoDetail.setLayoutManager(new LinearLayoutManager(this));
                    rvTodoDetail.setAdapter(toDoItemListAdapter);
                    toDoItemListAdapter.notifyDataSetChanged();
                    alertDialog.dismiss();

                });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel",
                (dialog, which) -> {
                    alertDialog.dismiss();
                });
        alertDialog.show();
    }

    public void orderNameItems(){
        List<ToDoItem> orderNameItems;
        if(isAscOrder){
            orderNameItems = mHelper.filterItems("ASC", null);
            isAscOrder = false;
        }else{
            orderNameItems = mHelper.filterItems("DESC", null);
            isAscOrder = true;
        }
        toDoItemListAdapter = new ToDoItemListAdapter(this, orderNameItems);
        rvTodoDetail.setLayoutManager(new LinearLayoutManager(this));
        rvTodoDetail.setAdapter(toDoItemListAdapter);
        toDoItemListAdapter.notifyDataSetChanged();
    }

}
