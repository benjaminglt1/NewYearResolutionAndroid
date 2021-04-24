package com.example.newyearresolution.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.newyearresolution.R;

import java.util.List;

public class ResolutionAdapter extends RecyclerView.Adapter<ResolutionAdapter.ViewHolder> {

    List<Resolution> resolutions;
    private int layout;

    public ResolutionAdapter(List<Resolution> resolutions,int layout){
        this.resolutions=resolutions;
        this.layout = layout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(layout,parent,false);
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
        private TextView frequence;
        private TextView nbOccurence;

        ViewHolder(View itemView){
            super(itemView);
            action = (TextView) itemView.findViewById(R.id.action);
            frequence = (TextView) itemView.findViewById(R.id.freq);

        }

        public void display(Resolution resolution) {
            action.setText(resolution.getAction());
            System.err.println("[FREQUENCE] - "+resolution.getFrequence());
            if(resolution.getFrequence() != null){
                frequence.setVisibility(View.VISIBLE);
                frequence.setText(resolution.getNbOccurence()+" fois par "+resolution.getFrequence().toLowerCase());
            }
        }
    }
}

