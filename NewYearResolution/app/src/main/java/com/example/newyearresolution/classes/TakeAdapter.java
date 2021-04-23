package com.example.newyearresolution.classes;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newyearresolution.R;
import com.example.newyearresolution.activity.MyResolutions;
import com.example.newyearresolution.activity.TakeResolution;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.widget.AdapterView.*;


public class TakeAdapter extends RecyclerView.Adapter<TakeAdapter.ViewHolder>{

    List<Resolution> resolutions;
    private long idUser;
    private TakeResolution tr;
    private String freq;
    private List<Resolution> resolutionList = new ArrayList<Resolution>();

    public TakeAdapter(List<Resolution> resolutions, long idUser, TakeResolution tr){
        this.resolutions=resolutions;
        this.idUser = idUser;
        this.tr = tr;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.take_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //System.err.println("[LOG] "+resolutions.get(0).toString());
        holder.display(resolutions.get(position));
    }

    @Override
    public int getItemCount() {
        return resolutions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView action;
        private Spinner selectFreq;
        private EditText nbOccurences;
        private Button take;
        private Resolution r;
        ViewHolder(View itemView){
            super(itemView);
            action = (TextView) itemView.findViewById(R.id.action);
            selectFreq = (Spinner) itemView.findViewById(R.id.selectFreq);
            nbOccurences = (EditText) itemView.findViewById(R.id.nbOccur);
            take = (Button) itemView.findViewById(R.id.take);
        }

        public void display(Resolution resolution) {
            this.r = resolution;
            action.setText(resolution.getAction());
            nbOccurences.setText("0");
            String[] options = new String[]{"JOUR", "SEMAINE", "MOIS", "ANNEE"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(tr, android.R.layout.simple_spinner_dropdown_item, options);
            selectFreq.setAdapter(adapter);
            selectFreq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    freq = (String) adapterView.getItemAtPosition(i);
                    return;
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });

            take.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    long idRes = r.getIdResolution();
                    String url = "http://192.168.0.16:8080/takeResolution?idUser="+idUser+"&idResolution="+idRes+"&frequence="+freq+"&nbOccurences="+nbOccurences.getText();
                    RequestQueue queue = Volley.newRequestQueue(tr);

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
                                            Intent intent = new Intent(tr, MyResolutions.class);
                                            intent.putExtra("idUser",idUser);
                                            tr.startActivity(intent);
                                            tr.finish();
                                        }else{
                                            Toast t = Toast.makeText(tr.getApplicationContext(),"Erreur vous n'avez pas réussi à prendre cette résolution",Toast.LENGTH_LONG);
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
                                    Toast t = Toast.makeText(tr.getApplicationContext(),"erreur",Toast.LENGTH_LONG);
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