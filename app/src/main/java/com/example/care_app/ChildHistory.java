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

public class ChildHistory extends AppCompatActivity {
    String childId, childName;
    TextView textViewName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_history);
        Bundle parametros = this.getIntent().getExtras();
        if(parametros !=null){
            childId = parametros.getString("childId");
            childName = parametros.getString("childName");
        }
        textViewName =  (TextView) findViewById(R.id.idETextName);
        textViewName.setText(childName);
        this.getChildHistory();
    }

    public void getChildHistory() {
        String url = "http://gabymarapi.atwebpages.com/index.php/childhistory/"+ childId;
        StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.i("======>", jsonArray.toString());
                    List<String> items = new ArrayList<>();
                    List<String> itemsId = new ArrayList<>();

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        items.add(object.getString("result") + "    " + object.getString("weight") + " kg    " +object.getString("size") + " cm    " +object.getString("date"));
                        itemsId.add(object.getString("childId"));
                    }

                    ListView lstChildren = (ListView) findViewById(R.id.lista);

                    ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                            ChildHistory.this,
                            android.R.layout.simple_list_item_1,
                            items);
                    lstChildren.setAdapter(adaptador);

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

}
