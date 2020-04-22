package com.example.care_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UserRegister extends AppCompatActivity {
    public EditText txtEmail, txtPassword, txtConfirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

       /* txtEmail = (EditText) findViewById(R.id.idETextEmail);
        txtPassword = (EditText) findViewById(R.id.idETextPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.idETextConfirmPassword);*/

    }

    public void registerAccount(View view) {

    }

    public void practiceToast(View view) {
        Toast.makeText(this, "Proving toast", Toast.LENGTH_LONG).show();
    }

    public void saveUser(View view) {
        Intent intent = new Intent(this, NewRegister.class);
        startActivity(intent);

    }

    public void alertRegister(String warning) {
        Toast.makeText(this, warning, Toast.LENGTH_LONG).show();
    }


    public void goSignIn(View view) {
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
    }
}
