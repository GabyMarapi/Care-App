package com.example.care_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SignIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    public void redirectHome(View view) {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
        //Intent intent = new Intent(this, UpdateChild.class);
        //startActivity(intent);
    }
}
