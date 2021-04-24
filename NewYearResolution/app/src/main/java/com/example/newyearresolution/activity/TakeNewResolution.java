package com.example.newyearresolution.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newyearresolution.R;
import com.example.newyearresolution.classes.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class TakeNewResolution extends AppCompatActivity {
    private EditText action;
    private EditText nbOccurences;
    private Spinner select;
    private String freq;
    private Button newRes;
    private Button retour;
    private long idUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_new_resolution);

        Intent i = getIntent();
        if(i!=null) {
            if (i.hasExtra("idUser")) {
                idUser = i.getLongExtra("idUser", 0);





                action = (EditText) findViewById(R.id.actionFaire);
                nbOccurences = (EditText) findViewById(R.id.nbOccurences);

                select = (Spinner) findViewById(R.id.newSelect);
                String[] options = new String[]{"JOUR", "SEMAINE", "MOIS", "ANNEE"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, options);
                select.setAdapter(adapter);
                select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        freq = (String) adapterView.getItemAtPosition(i);
                        return;
                    }

                    public void onNothingSelected(AdapterView<?> adapterView) {
                        return;
                    }
                });

                retour = (Button) findViewById(R.id.retour2);
                retour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TakeNewResolution.this,TakeResolution.class);
                        intent.putExtra("idUser",idUser);
                        startActivity(intent);
                        finish();
                    }
                });

                newRes = (Button) findViewById(R.id.valider);
                newRes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strAction = action.getText().toString().replace(" ","_");
                        String url = null;
                        try {
                            url = Util.getProperty("method",getApplicationContext())+"://"+Util.getProperty("apiUrl",getApplicationContext())+"/takeNewResolution?idUser="+idUser+"&action="+strAction+"&frequence="+freq+"&nbOccurences="+nbOccurences.getText();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        RequestQueue queue = Volley.newRequestQueue(TakeNewResolution.this);

                        JsonObjectRequest objectRequest = new JsonObjectRequest(
                                Request.Method.GET,
                                url,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject reponse) {
                                        Log.e("Reponse=",reponse.toString());
                                        try {
                                            if(reponse.getBoolean("resultat")){
                                                Intent intent = new Intent(TakeNewResolution.this, MyResolutions.class);
                                                intent.putExtra("idUser",idUser);
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                Toast t = Toast.makeText(TakeNewResolution.this,"Erreur vous n'avez pas réussi à prendre cette résolution",Toast.LENGTH_LONG);
                                                t.show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError err) {
                                        Log.e("Erreur=",err.toString());
                                        Toast t = Toast.makeText(TakeNewResolution.this,"erreur",Toast.LENGTH_LONG);
                                        t.show();
                                    }
                                }
                        );

                        queue.add(objectRequest);
                    }
                });
            }
        }


    }
}