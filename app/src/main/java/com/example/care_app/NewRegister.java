package com.example.care_app;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class NewRegister extends AppCompatActivity {
    String email, password, userId;
    EditText eName, eLastname, ePhone;
    User result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register);
        Bundle parametros = this.getIntent().getExtras();
        if(parametros !=null){
            email = parametros.getString("email");
            password = parametros.getString("password");
        }

        eName = (EditText) findViewById(R.id.idETextName);
        eLastname = (EditText) findViewById(R.id.idETexLatname);
        ePhone = (EditText) findViewById(R.id.idETextPhone);
    }

    public void saveUser(View view) {
        String name = eName.getText().toString();
        String lastname = eLastname.getText().toString();
        String phone = ePhone.getText().toString();

        Bundle extras = new Bundle();

        UserDAO dao = new UserDAO(getBaseContext());

        if (name.isEmpty() || lastname.isEmpty() || lastname.isEmpty()) {
            Toast toast = Toast.makeText(this, "Ingresa todos los datos solicitados", Toast.LENGTH_LONG);
            TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
            text.setTextColor(Color.RED);
            text.setTextSize(15);
            toast.show();

        } else {
            try {
                dao.insertar(email, password, name, lastname, phone);

                Toast toast = Toast.makeText(getApplicationContext(), "Se insertó correctamente", Toast.LENGTH_LONG);
                TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
                text.setTextColor(Color.BLUE);
                text.setTextSize(15);

                result = dao.obtener(email);
                extras.putString("userId", Integer.toString(result.getId()));
                extras.putString("userEmail", email);

                eName.setText("");
                eLastname.setText("");
                ePhone.setText("");

                Intent intent = new Intent(this, Main2Activity.class);
                intent.putExtras(extras);
                startActivity(intent);

            } catch (DAOException e){
                Log.i("NewRegisterActi", "====> " + e.getMessage());
            }
        }

    }

}
