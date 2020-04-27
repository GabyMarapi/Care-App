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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class UpdateChild extends AppCompatActivity {
    String childId, childName, childAge, childWeight, childSize, childHemogl;
    EditText txtAge, txtWeight, txtSize, txtHemogl;
    TextView txtName, textViewLoading;
    String age, weight, size, hemogl, date, state, result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_child);
        Bundle parametros = this.getIntent().getExtras();
        if (parametros !=null) {
            childId = parametros.getString("childId");
            childName = parametros.getString("childName");
            childAge = parametros.getString("childAge");
            childWeight = parametros.getString("childWeight");
            childSize = parametros.getString("childSize");
            childHemogl = parametros.getString("childHemogl");
        }
        txtName = (TextView) findViewById(R.id.idETextName);
        txtAge = (EditText) findViewById(R.id.idETextAge);
        txtWeight = (EditText) findViewById(R.id.idETextWeight);
        txtSize = (EditText) findViewById(R.id.idETextSize);
        txtHemogl = (EditText) findViewById(R.id.idETextHemogl);

        textViewLoading = (TextView) findViewById(R.id.idETextLoading);

        txtName.setText(childName);
        txtAge.setText(childAge);
        txtWeight.setText(childWeight);
        txtSize.setText(childSize);
        txtHemogl.setText(childHemogl);
    }

    public void updateChild(View view) {
        textViewLoading.setText("Loading..");
        age = txtAge.getText().toString();
        weight = txtWeight.getText().toString();
        size = txtSize.getText().toString();
        hemogl = txtHemogl.getText().toString();
        date = (String) android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", new java.util.Date());
        result = calculateIMC();

        if(age.isEmpty() || weight.isEmpty() || size.isEmpty() || hemogl.isEmpty()) {
            Toast toast = Toast.makeText(this, "Ingresa todos los datos solicitados", Toast.LENGTH_LONG);
            TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
            text.setTextColor(Color.RED);
            text.setTextSize(15);
            toast.show();
        } else {
            this.updateChildInf();
            this.registerChildHistory();
        }
    }

    public void updateChildInf() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url = "http://gabymarapi.atwebpages.com/index.php/child/update";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("age", age);
            jsonBody.put("weight", weight);
            jsonBody.put("size", size);
            jsonBody.put("hemogl", hemogl);
            jsonBody.put("date", date);
            jsonBody.put("childId", childId);

            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("======>", error.toString());
                        }
                    }) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void registerChildHistory() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url = "http://gabymarapi.atwebpages.com/index.php/childhistory";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", childName);
            jsonBody.put("age", age);
            jsonBody.put("weight", weight);
            jsonBody.put("size", size);
            jsonBody.put("hemogl", hemogl);
            jsonBody.put("date", date);
            jsonBody.put("childId", childId);
            jsonBody.put("result", result);
            jsonBody.put("state", state);

            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", response);
                            textViewLoading.setText("");
                            Toast toast = Toast.makeText(UpdateChild.this, "Se ha actualizado correctamente", Toast.LENGTH_LONG);
                            TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
                            text.setTextColor(Color.BLUE);
                            text.setTextSize(15);
                            toast.show();
                            Bundle extras = new Bundle();
                            extras.putString("childId", childId);

                            Intent intent = new Intent(UpdateChild.this, ChidInformation.class);
                            intent.putExtras(extras);
                            startActivity(intent);
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("======>", error.toString());
                        }
                    }) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public String calculateIMC() {
        String result = "";
        float imc = Float.parseFloat(weight)/((Float.parseFloat(size)/100) *(Float.parseFloat(size)/100));
        if (imc < 18.5) {
            result = "Bajo peso";
            state = "0";
        }
        if (imc >= 18.5 && imc <= 24.9) {
            result = "Peso ideal";
            state = "1";
        }
        if (imc >= 25 && imc <= 29.9) {
            result = "Sobre peso";
            state = "0";
        }

        if (imc > 29.9) {
            result = "Obesidad";
            state = "0";
        }
        return result;
    }

}
