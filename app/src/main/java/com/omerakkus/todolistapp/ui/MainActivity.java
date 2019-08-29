package com.omerakkus.todolistapp.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.view.Menu;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.omerakkus.todolistapp.R;
import com.omerakkus.todolistapp.adapter.ToDoItemListAdapter;
import com.omerakkus.todolistapp.adapter.ToDoListAdapter;
import com.omerakkus.todolistapp.model.SerializedToDoItem;
import com.omerakkus.todolistapp.model.SerializedToDoTitle;
import com.omerakkus.todolistapp.model.ToDoItem;
import com.omerakkus.todolistapp.model.TodoTitle;
import com.omerakkus.todolistapp.db.TaskDbHelper;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context context;
    String categoryName;
    private File pdfFile;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    SerializedToDoItem serializedToDoItem;
    ToDoDetailActivity toDoDetailActivity;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    ToDoListAdapter itemListAdapter;
    TaskDbHelper mHelper = new TaskDbHelper(this);
   @BindView(R.id.tvNoToDoList)
    TextView tvNoToDoList;
   @BindView(R.id.rvTodoList)
   RecyclerView rvTodoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        Objects.requireNonNull(getSupportActionBar()).setTitle("To Do List");
        toDoDetailActivity = (ToDoDetailActivity) context;
        refreshToDoList();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Objects.requireNonNull(getSupportActionBar()).setTitle(item.toString());
        int id = item.getItemId();
        mHelper.listOfTodo(item.toString(), false);

        if (id == R.id.nav_send) {
            try {
                createPdfWrapper();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void insertTodoTitle() {
        AlertDialog alertDialog = new AlertDialog.Builder(
                MainActivity.this).create(); // Read
        alertDialog.setTitle("Add title for to do");
        View dialogView = getLayoutInflater().inflate(R.layout.popup_todo_title, null);
        alertDialog.setView(dialogView);
        EditText etTodoTitle = dialogView.findViewById(R.id.etTodoTitle);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Add",
                (dialog, which) -> {
                    if(etTodoTitle.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"Please fill field.",Toast.LENGTH_SHORT).show();
                    }else{
                        TodoTitle todoTitle = new TodoTitle(etTodoTitle.getText().toString().trim());
                        todoTitle.setTodo_title(etTodoTitle.getText().toString().trim());
                        mHelper.insertTodoTitle(todoTitle);
                        alertDialog.dismiss();
                        refreshToDoList();
                    }
                });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel",
                (dialog, which) -> {
                    alertDialog.dismiss();
                });
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            insertTodoTitle();
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshToDoList(){
        List<TodoTitle> toDoList =  mHelper.listTodoTitle();
        itemListAdapter = new ToDoListAdapter(this, toDoList);
        rvTodoList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvTodoList.setAdapter(itemListAdapter);
        rvTodoList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        itemListAdapter.notifyDataSetChanged();
        if(toDoList.size() == 0){
            tvNoToDoList.setVisibility(View.VISIBLE);
        }else{
            tvNoToDoList.setVisibility(View.GONE);
        }
    }

    public void setTodoTitle(String todoTitle) {
        this.categoryName = todoTitle;
        List<ToDoItem> toDoItems = mHelper.listOfTodo(todoTitle, false);
        serializedToDoItem = new SerializedToDoItem();
        serializedToDoItem.setToDoItems(toDoItems);
        Intent i = new Intent(this, ToDoDetailActivity.class);
        i.putExtra("itemsoftodo", serializedToDoItem);
        i.putExtra("categoryName", todoTitle);
        startActivity(i);
    }

    public void deleteSelectedTitle(String todo_title) {
      Integer effectedCount =  mHelper.deleteTodoList(todo_title);
      if(effectedCount > 0){
          Toast.makeText(getApplicationContext()," To Do list deleted successfully.",Toast.LENGTH_SHORT).show();
      }
    }

    private void createPdfWrapper() throws FileNotFoundException,DocumentException{

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }else {
            createPdf();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(this, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void createPdf() throws FileNotFoundException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
        }

        SerializedToDoTitle serializedToDoTitle = new SerializedToDoTitle();
        serializedToDoTitle.setTodoTitles(mHelper.listTodoTitle());

        pdfFile = new File(docsFolder.getAbsolutePath(),"ToDoList.pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);

        document.open();
        for(int i=0;i<serializedToDoTitle.getTodoTitles().size();i++){
            document.add(new Paragraph(serializedToDoTitle.getTodoTitles().get(i).getTodo_title()));
        }
        document.close();
        previewPdf();
    }

    private void previewPdf() {

        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");

            startActivity(intent);
        }else{
            Toast.makeText(this,"Download a PDF Viewer to see the generated PDF",Toast.LENGTH_SHORT).show();
        }
    }


}
