package com.example.florapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    private ArrayList<Flora> floraList;

    public ResultAdapter(ArrayList<Flora> floraList) {
        this.floraList = floraList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Flora flora = floraList.get(position);

        holder.tv_commonName.setText(flora.getCommonName());
        holder.tv_scientificName.setText(flora.getScientificName());

        holder.cl_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FloraActivity.class);
                intent.putExtra("Data", flora);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return floraList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_commonName, tv_scientificName;
        ConstraintLayout cl_result;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tv_commonName = itemView.findViewById(R.id.row_tv_common);
            this.tv_scientificName = itemView.findViewById(R.id.row_tv_scientific);
            this.cl_result = itemView.findViewById(R.id.row_cl_result);
        }
    }
}
