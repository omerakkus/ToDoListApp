package com.omerakkus.todolistapp.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.omerakkus.todolistapp.R;
import com.omerakkus.todolistapp.model.Users;
import com.omerakkus.todolistapp.db.TaskDbHelper;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignUpActivity extends AppCompatActivity {

    private TaskDbHelper mHelper;

    @BindView(R.id.etEmail)
    EditText email;
    @BindView(R.id.etPassword)
    EditText password;

    @OnClick(R.id.btnSignUp)
    public void signUp(){
        createUser();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mHelper = new TaskDbHelper(this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity.
        }
        return super.onOptionsItemSelected(item);
    }

    private void createUser() {
        if(!email.getText().toString().equals("") && !password.getText().toString().equals("")){
            Users users = new Users();
            users.setEmail(email.getText().toString().trim());
            users.setPassword(password.getText().toString().trim());
            mHelper.insertUser(users);
            finish();
        }else{
            Toast.makeText(getApplicationContext(),"Please fill all fields.",Toast.LENGTH_SHORT).show();
        }
    }

    /*public Cursor getAllData(){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TaskContract.UserEntry.USER_TABLE, null);
        StringBuilder buffer = new StringBuilder();
        while (res.moveToNext()){
            buffer.append("Id:" + res.getString(0)+ "\n");
            buffer.append("Name:" + res.getString(1)+ "\n");
            buffer.append("Password:" + res.getString(2)+ "\n");
        }
        res.close();
        db.close();
        Toast.makeText(getApplicationContext(),buffer.toString(),Toast.LENGTH_LONG).show();
        return res;
    }*/
}

