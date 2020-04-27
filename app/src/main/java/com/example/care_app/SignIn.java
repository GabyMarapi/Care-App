package com.example.care_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignIn extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    User result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        editTextEmail = (EditText) findViewById(R.id.idETextEmail);
        editTextPassword = (EditText) findViewById(R.id.idETextPassword);
    }

    public void login(View view) {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        UserDAO dao = new UserDAO(getBaseContext());

        if (email.isEmpty() || password.isEmpty()) {
            Toast toast = Toast.makeText(this, "Ingresa todos los datos solicitados", Toast.LENGTH_LONG);
            TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
            text.setTextColor(Color.RED);
            text.setTextSize(15);
            toast.show();
        } else {
            try {
                result = dao.obtener(email);
                //validate if user exist
                if (result.getEmail() != null) {

                    //validate password correct
                    if(password.equals( result.getPassword())) {
                        Bundle extras = new Bundle();
                        extras.putString("userId", Integer.toString(result.getId()));
                        extras.putString("userEmail", email);

                        Intent intent = new Intent(SignIn.this, Main2Activity.class);
                        intent.putExtras(extras);
                        startActivity(intent);

                    } else {
                        Toast toast = Toast.makeText(this, "La contraseña ingresada es incorrecta", Toast.LENGTH_LONG);
                        TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
                        text.setTextColor(Color.RED);
                        text.setTextSize(15);
                        toast.show();
                    }

                } else {
                    Toast toast = Toast.makeText(this, "Usuario no existe", Toast.LENGTH_LONG);
                    TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
                    text.setTextColor(Color.RED);
                    text.setTextSize(15);
                    toast.show();
                }
            } catch (DAOException e){
                Log.i("SignInActi", "====> " + e.getMessage());

            }
        }


    }

    public void goSignIn(View view) {
        Intent intent = new Intent(this, UserRegister.class);
        startActivity(intent);
    }
}
