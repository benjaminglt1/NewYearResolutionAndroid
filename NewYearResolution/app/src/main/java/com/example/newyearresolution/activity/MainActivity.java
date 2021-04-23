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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rv;
    private List<Resolution> resolutionList = new ArrayList<Resolution>();
    private ResolutionAdapter adapter;
    private Button signin;
    private Button signout;
    private Button mesRes;
    private long idUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.resolutionsRV);

        signin = (Button) findViewById(R.id.signin);
        signout = (Button) findViewById(R.id.signout);
        mesRes = (Button) findViewById(R.id.mesRes);





        String url = "http://192.168.0.16:8080/getResolutions";

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
                        //System.err.println(resolutionList.toString());
                        adapter = new ResolutionAdapter(resolutionList);

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


        Intent i = getIntent();
        if(i!=null){
            if (i.hasExtra("idUser")) {
                idUser = i.getLongExtra("idUser", 0);
                //display signout + mes resolution
                signout.setVisibility(View.VISIBLE);
                signout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                mesRes.setVisibility(View.VISIBLE);
                mesRes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, MyResolutions.class);
                        intent.putExtra("idUser", idUser);
                        startActivity(intent);
                        finish();
                    }
                });
                //undisplay signin
                signin.setVisibility(View.INVISIBLE);
            }else{
                //display signin
                signin.setVisibility(View.VISIBLE);
                signin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                //undisplay signout + mes resolutions
                signout.setVisibility(View.INVISIBLE);
                mesRes.setVisibility(View.INVISIBLE);
            }
        }


    }


}