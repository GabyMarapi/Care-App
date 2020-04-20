package com.example.care_app.ui.childrenList;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.care_app.ChildList;
import com.example.care_app.R;
import com.example.care_app.RegisterChild;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChildrenList extends Fragment {

    private ChildrenListViewModel mViewModel;

    public static ChildrenList newInstance() {
        return new ChildrenList();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.getChildren();
        return inflater.inflate(R.layout.children_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChildrenListViewModel.class);
        // TODO: Use the ViewModel
    }

    public void getChildren() {
        String criterio = "criterio";
        String url = "http://gabymarapi.atwebpages.com/index.php/children/"+ "20";
        StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    Log.i("======>", jsonArray.toString());

                    List<String> items = new ArrayList<>();
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        items.add(object.getString("name") + "  "+object.getString("date"));
                    }

                    ListView lstProductos = getView().findViewById(R.id.lista);

                    ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                            getActivity(),
                            android.R.layout.simple_list_item_1,
                            items);
                    lstProductos.setAdapter(adaptador);

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

        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }




}
