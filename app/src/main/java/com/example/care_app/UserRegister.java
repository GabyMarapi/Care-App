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

public class UserRegister extends AppCompatActivity {
    EditText txtEmail, txtPassword, txtConfirmPassword;
    User result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        txtEmail = (EditText) findViewById(R.id.idETextEmail);
        txtPassword = (EditText) findViewById(R.id.idETextPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.idETextConfirmPassword);
    }


    public void saveUser(View view) {
        Bundle extras = new Bundle();
        String email =  txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String confirmpassword = txtConfirmPassword.getText().toString();
        UserDAO dao = new UserDAO(getBaseContext());

        if (email.isEmpty() || password.isEmpty() || confirmpassword.isEmpty()) {
            Toast toast = Toast.makeText(this, "Ingresa todos los datos solicitados", Toast.LENGTH_LONG);
            TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
            text.setTextColor(Color.RED);
            text.setTextSize(15);
            toast.show();
        } else {
            try {
                result = dao.obtener(email);

                if (result.getEmail() != null) {
                    Toast toast = Toast.makeText(this, "El correo ingresado ya esta siendo usado", Toast.LENGTH_LONG);
                    TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
                    text.setTextColor(Color.RED);
                    text.setTextSize(15);
                    toast.show();
                } else {
                    if (password.equals(confirmpassword)){
                        extras.putString("email",email);
                        extras.putString("password",password);

                        Intent intent = new Intent(UserRegister.this, NewRegister.class);
                        intent.putExtras(extras);
                        startActivity(intent);
                    } else {
                        Toast toast = Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG);
                        TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
                        text.setTextColor(Color.RED);
                        text.setTextSize(15);
                        toast.show();
                    }
                }
            } catch (DAOException e){
                Log.i("NewRegisterActi", "====> " + e.getMessage());
            }


        }
    }



    public void goSignIn(View view) {
        Intent intent = new Intent(UserRegister.this, SignIn.class);
        startActivity(intent);
    }
}
