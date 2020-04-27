package com.example.care_app.ui.childregister;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.care_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class ChildRegister extends Fragment  {
    EditText txtName, txtAge, txtWeight, txtSize, txtHemogl;
    String userId;
    String name, age, weight, size, hemogl, date, childId, state;
    private ChildRegisterViewModel mViewModel;
    public static ChildRegister newInstance() {
        return new ChildRegister();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
            userId = this.readFromFile(getContext());

        return inflater.inflate(R.layout.child_register_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChildRegisterViewModel.class);
        txtName = (EditText) getActivity().findViewById(R.id.idETextName);
        txtAge = (EditText) getActivity().findViewById(R.id.idETextAge);
        txtWeight = (EditText) getActivity().findViewById(R.id.idETextWeight);
        txtSize = (EditText) getActivity().findViewById(R.id.idETextSize);
        txtHemogl = (EditText) getActivity().findViewById(R.id.idETextHemogl);
        Button button = (Button) getView().findViewById(R.id.idRegister);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = txtName.getText().toString();
                age = txtAge.getText().toString();
                weight = txtWeight.getText().toString();
                size = txtSize.getText().toString();
                hemogl = txtHemogl.getText().toString();
                date = (String) android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", new java.util.Date());

                if(name.isEmpty() || age.isEmpty() || weight.isEmpty() || size.isEmpty() || hemogl.isEmpty()) {
                    Toast toast = Toast.makeText(getContext(), "Ingresa todos los datos solicitados", Toast.LENGTH_LONG);
                    TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
                    text.setTextColor(Color.RED);
                    text.setTextSize(15);
                    toast.show();
                } else {
                    this.registerChildren();

                }

            }

            private void registerChildren() {
                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    String url = "http://gabymarapi.atwebpages.com/index.php/child/add";
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("name", name);
                    jsonBody.put("age", age);
                    jsonBody.put("weight", weight);
                    jsonBody.put("size", size);
                    jsonBody.put("hemogl", hemogl);
                    jsonBody.put("date", date);
                    jsonBody.put("userId", userId);

                    final String requestBody = jsonBody.toString();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i("VOLLEY", response);
                                    this.settingChildId();
                                }

                                public void settingChildId() {
                                    //Registrar niñ@ en la tabla chilhistory

                                    String url = "http://gabymarapi.atwebpages.com/index.php/lastchild/"+ userId;
                                    StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONArray jsonArray = new JSONArray(response);
                                                Log.i("======>", jsonArray.toString());
                                                JSONObject object = jsonArray.getJSONObject(0);
                                                childId = object.getString("idChild");

                                                this.registerChildHistory();


                                            } catch (JSONException e) {
                                                Log.i("======>", e.getMessage());
                                            }
                                        }
                                        private void registerChildHistory() {
                                            String result = calculateIMC();

                                            try {
                                                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                                String url = "http://gabymarapi.atwebpages.com/index.php/childhistory";
                                                JSONObject jsonBody = new JSONObject();
                                                jsonBody.put("name", name);
                                                jsonBody.put("age", age);
                                                jsonBody.put("weight", weight);
                                                jsonBody.put("size", size);
                                                jsonBody.put("hemogl", hemogl);
                                                jsonBody.put("date", date);
                                                jsonBody.put("userId", userId);
                                                jsonBody.put("childId", childId);
                                                jsonBody.put("result", result);
                                                jsonBody.put("state", state);

                                                final String requestBody = jsonBody.toString();
                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                Log.i("VOLLEY", response);
                                                                this.resetForm();
                                                                Toast toast = Toast.makeText(getActivity(), "Se ha registrado correctamente", Toast.LENGTH_LONG);
                                                                TextView text = (TextView) toast.getView().findViewById(android.R.id.message);
                                                                text.setTextColor(Color.BLUE);
                                                                text.setTextSize(15);
                                                                toast.show();

                                                                this.resetForm();
                                                            }

                                                            private void resetForm() {
                                                                txtName.setText("");
                                                                txtAge.setText("");
                                                                txtWeight.setText("");
                                                                txtSize.setText("");
                                                                txtHemogl.setText("");
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

                                    },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.i("======>", error.toString());
                                                }
                                            }
                                    );

                                    RequestQueue requestQueue= Volley.newRequestQueue(getContext());
                                    requestQueue.add(stringRequest);

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


        });
        // TODO: Use the ViewModel
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
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
