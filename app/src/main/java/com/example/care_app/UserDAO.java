package com.example.care_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;


public class UserDAO {
    private DatabaseHelper _dbHelper;

    public UserDAO(Context c) {
        _dbHelper = new DatabaseHelper(c);
    }

    public void insertar(String email, String password, String name, String lastname, String phone) throws DAOException {
        Log.i("UserDAO", "insertar()");
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        try {
            String[] args = new String[]{email, password, name, lastname, phone};
            db.execSQL("INSERT INTO user(email, password, name, lastname, phone) VALUES(?,?,?,?,?)", args);
            Log.i("UserDAO", "Se insertó");
        } catch (Exception e) {
            throw new DAOException("UserDAO: Error al insertar: " + e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public User obtener(String userEmail) throws DAOException {
        Log.i("UserDAO", "obtener()");
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        User modelo = new User();
        try {
            Cursor c = db.rawQuery("select id, email, password, name, lastname, phone from user  where email like '%"+userEmail+"%'", null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    int id = c.getInt(c.getColumnIndex("id"));
                    String email = c.getString(c.getColumnIndex("email"));
                    String password = c.getString(c.getColumnIndex("password"));
                    String name = c.getString(c.getColumnIndex("name"));
                    String lastname = c.getString(c.getColumnIndex("lastname"));
                    int phone = c.getInt(c.getColumnIndex("phone"));

                    modelo.setId(id);
                    modelo.setEmail(email);
                    modelo.setPassword(password);
                    modelo.setName(name);
                    modelo.setLastname(lastname);
                    modelo.setPhone(phone);

                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            throw new DAOException("UserDAO: Error al obtener: " + e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return modelo;
    }
}
