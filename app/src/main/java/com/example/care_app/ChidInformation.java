package com.example.care_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChidInformation extends AppCompatActivity {
    String childId, childName, childAge, childWeight, childSize, childHemogl;
    TextView textViewName, textViewAge, textViewWeight, textViewSize, textViewHemogl, textViewDate, textViewResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chid_information);

        Bundle parametros = this.getIntent().getExtras();
        if(parametros !=null){
            childId = parametros.getString("childId");
        }
        textViewName = (TextView) findViewById(R.id.idETextName);
        textViewAge = (TextView) findViewById(R.id.idETextAge);
        textViewWeight = (TextView) findViewById(R.id.idETextWeight);
        textViewSize = (TextView) findViewById(R.id.idETextSize);
        textViewHemogl = (TextView) findViewById(R.id.idETextHemogl);
        textViewDate = (TextView) findViewById(R.id.idETextDate);
        textViewResult = (TextView) findViewById(R.id.idETextResult);
        this.getChildren();
    }

    public void getChildren() {
        String url = "http://gabymarapi.atwebpages.com/index.php/lastchildhistory/"+ childId;
        StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    Log.i("======>", jsonArray.toString());

                    JSONObject object = jsonArray.getJSONObject(0);
                    childName = object.getString("name");
                    childAge = object.getString("age");
                    childWeight = object.getString("weight");
                    childSize = object.getString("size");
                    childHemogl = object.getString("hemogl");

                    textViewName.setText(childName);
                    textViewAge.setText(childAge+ " años");
                    textViewWeight.setText(childWeight+ " kg");
                    textViewSize.setText(childSize);
                    textViewHemogl.setText(childHemogl);
                    textViewDate.setText(object.getString("date"));
                    String state = "1";
                    if(state.equals(object.getString("state"))){
                        textViewResult.setText(object.getString("result") + "  :)");
                    } else {
                        textViewResult.setText(object.getString("result")  + "  :(");
                    }


                    childName = object.getString("name");
                } catch (JSONException e) {
                    Log.i("======>", e.getMessage());
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("======>", error.toString());
                    }
                }
        );

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void goUpdateChild(View view){
        Bundle extras = new Bundle();
        extras.putString("childId", childId);
        extras.putString("childName", childName);
        extras.putString("childAge", childAge);
        extras.putString("childWeight", childWeight);
        extras.putString("childSize", childSize);
        extras.putString("childHemogl", childHemogl);
        Intent intent = new Intent(this, UpdateChild.class);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }

    public void goChildHistory(View view){
        Bundle extras = new Bundle();
        extras.putString("childId", childId);
        extras.putString("childName", childName);

        Intent intent = new Intent(this, ChildHistory.class);
        intent.putExtras(extras);
        startActivity(intent);
    }


}
