package com.omerakkus.todolistapp.ui;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.omerakkus.todolistapp.R;
import com.omerakkus.todolistapp.db.TaskDbHelper;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private TaskDbHelper mHelper;
    StringBuilder buffer;
    SQLiteDatabase db;
    String email, password;

    @BindView(R.id.tvSignUp)
    TextView tvSignUp;
    @OnClick(R.id.tvSignUp)
    public void signUpClick(){
        Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(i);
    }

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;

    @OnClick(R.id.btnLogin)
    public void btnLoginOnClick(){
        String password = mHelper.searchPass(etEmail.getText().toString());
        if(etPassword.getText().toString().equals(password)){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("password",password);
            startActivity(i);
            finish();
        }else{
            Toast.makeText(getApplicationContext(),"Dont match username or password",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle("To Do App");
        mHelper = new TaskDbHelper(this);
        db = mHelper.getWritableDatabase();


        //getAllData();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /*public Cursor getAllData(){
        Cursor res = db.rawQuery("SELECT * FROM " + TaskContract.UserEntry.USER_TABLE, null);
        buffer = new StringBuilder();
        while (res.moveToNext()){
            buffer.append("Id:" + res.getString(0)+ "\n");
            buffer.append("E-mail:" + res.getString(1)+ "\n");
            buffer.append("Password:" + res.getString(2)+ "\n");
        }
        res.close();
        db.close();
        return res;
    }*/
}
