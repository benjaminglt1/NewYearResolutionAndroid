package com.example.newyearresolution.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.newyearresolution.R;
import com.example.newyearresolution.classes.Resolution;
import com.example.newyearresolution.classes.TakeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TakeResolution extends AppCompatActivity{
    private long idUser;
    private List<Resolution> resolutionList = new ArrayList<Resolution>();
    private TakeAdapter adapter;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_resolution);

        rv = (RecyclerView) findViewById(R.id.rv);

        Intent i = getIntent();
        if(i!=null){
            if(i.hasExtra("idUser")){
                idUser = i.getLongExtra("idUser",0);
                String url = "http://192.168.0.16:8080/getNotResolutions?idUser="+idUser;


                RequestQueue queue = Volley.newRequestQueue(this);

                JsonArrayRequest objectRequest = new JsonArrayRequest(
                        Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray reponse) {
                                Log.e("Reponse=",reponse.toString());
                                for(int i=0;i<reponse.length();i++){
                                    try {
                                        JSONObject object = (JSONObject) reponse.get(i);
                                        Resolution r = new Resolution(object.getLong("idResolution"),object.getString("action"));
                                        resolutionList.add(r);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                adapter = new TakeAdapter(resolutionList,idUser,TakeResolution.this);
                                //adapter = new ResolutionAdapter(resolutionList);
                                rv.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));

                                rv.setAdapter(adapter);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError err) {
                                Log.e("Erreur=",err.toString());
                            }
                        }
                );

                queue.add(objectRequest);
            }
        }
    }

}