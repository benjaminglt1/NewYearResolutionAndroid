package com.example.newyearresolution.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.newyearresolution.R;
import com.example.newyearresolution.classes.Resolution;
import com.example.newyearresolution.classes.ResolutionAdapter;
import com.example.newyearresolution.classes.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyResolutions extends AppCompatActivity {
    private Button accueil;
    private Button prendreResolution;
    private RecyclerView rv;
    private ResolutionAdapter adapter;
    private List<Resolution> resolutionList = new ArrayList<Resolution>();
    private long idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_resolutions);

        accueil = (Button) findViewById(R.id.accueil);
        prendreResolution = (Button) findViewById(R.id.prendreResolution);
        rv = (RecyclerView) findViewById(R.id.recyclerViewMyResolutions);




        Intent intent = getIntent();
        if(intent != null){

            if (intent.hasExtra("idUser")){
                idUser = intent.getLongExtra("idUser",0);
                String url = null;
                try {
                    url = Util.getProperty("method",getApplicationContext())+"://"+Util.getProperty("apiUrl",getApplicationContext())+"/getResolutionsOfUser?idUser="+idUser;
                } catch (IOException e) {
                    e.printStackTrace();
                }


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
                                        Resolution r = new Resolution(object.getLong("idResolution"),object.getString("action"),object.getString("frequence"),object.getInt("nbOccurence"));
                                        resolutionList.add(r);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                adapter = new ResolutionAdapter(resolutionList,R.layout.resolution_layout);

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

                prendreResolution.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MyResolutions.this, TakeResolution.class);
                        intent.putExtra("idUser",idUser);
                        startActivity(intent);
                        finish();
                    }
                });


                accueil.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MyResolutions.this, MainActivity.class);
                        intent.putExtra("idUser", idUser);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }


    }
}