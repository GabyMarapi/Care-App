package com.example.care_app.ui.childrenList;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.care_app.ChidInformation;
import com.example.care_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ChildrenList extends Fragment {
    String userId;
    ListView lstChildren;
    TextView textViewLoading;
    private ChildrenListViewModel mViewModel;

    public static ChildrenList newInstance() {
        return new ChildrenList();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        userId = this.readFromFile(getContext());
        return inflater.inflate(R.layout.children_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChildrenListViewModel.class);
        // TODO: Use the ViewModel
        lstChildren = (ListView) getActivity().findViewById(R.id.lista);
        textViewLoading = (TextView) getActivity().findViewById(R.id.idETextLoading);

        this.getChildren();
    }

    public void getChildren() {
        String criterio = "criterio";
        String url = "http://gabymarapi.atwebpages.com/index.php/children/"+ userId;
        StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    Log.i("======>", jsonArray.toString());

                    List<String> items = new ArrayList<>();
                    final List<String> itemsId = new ArrayList<>();

                    if(jsonArray.length() > 0){
                        textViewLoading.setText("");
                    } else {
                        textViewLoading.setText("Aún no ha registrado a ningún niño");
                    }

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        items.add("+      "+ object.getString("name"));
                        itemsId.add(object.getString("idChild"));
                    }

                    ArrayAdapter<String> adaptador = new ArrayAdapter<>(
                            getActivity(),
                            android.R.layout.simple_list_item_1,
                            items);
                    lstChildren.setAdapter(adaptador);


                    lstChildren.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Integer selectedId = Integer.parseInt(itemsId.get(position));

                            Bundle extras = new Bundle();
                            extras.putString("childId", Integer.toString(selectedId));

                                Intent intent = new Intent(getActivity(), ChidInformation.class);
                                intent.putExtras(extras);
                                startActivity(intent);
                        }
                    });

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

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
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


}
