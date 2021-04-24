package com.example.newyearresolution.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class LoginActivity extends AppCompatActivity {
    private EditText login;
    private EditText password;
    private Button signin;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.pass);
        signin = (Button) findViewById(R.id.connect);
        queue = Volley.newRequestQueue(this);



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = null;
                try {
                    url = Util.getProperty("method",getApplicationContext())+"://"+Util.getProperty("apiUrl",getApplicationContext())+"/connectUser?login="+login.getText().toString()+"&pass="+password.getText().toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }


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
                                        Intent intent = new Intent(LoginActivity.this, MyResolutions.class);
                                        intent.putExtra("idUser",reponse.getLong("idUser"));
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast toast = Toast.makeText(getApplicationContext(),"Login ou password incorrect",Toast.LENGTH_LONG);
                                        toast.show();
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
                            }
                        }
                );

                queue.add(objectRequest);
            }
        });
    }
}