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
import com.example.newyearresolution.classes.TakeAdapter;
import com.example.newyearresolution.classes.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TakeResolution extends AppCompatActivity{
    private long idUser;
    private List<Resolution> resolutionList = new ArrayList<Resolution>();
    private TakeAdapter adapter;
    private RecyclerView rv;
    private Button retour;
    private Button newRes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_resolution);

        rv = (RecyclerView) findViewById(R.id.rv);



        Intent i = getIntent();
        if(i!=null){
            if(i.hasExtra("idUser")){
                idUser = i.getLongExtra("idUser",0);
                String url = null;
                try {
                    url = Util.getProperty("method",getApplicationContext())+"://"+Util.getProperty("apiUrl",getApplicationContext())+"/getNotResolutions?idUser="+idUser;
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

                retour = (Button) findViewById(R.id.retour2);
                retour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TakeResolution.this,MyResolutions.class);
                        intent.putExtra("idUser",idUser);
                        startActivity(intent);
                        finish();
                    }
                });


                newRes = (Button) findViewById(R.id.button2);
                newRes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TakeResolution.this,TakeNewResolution.class);
                        intent.putExtra("idUser",idUser);
                        startActivity(intent);
                        finish();
                    }
                });


            }
        }
    }

}