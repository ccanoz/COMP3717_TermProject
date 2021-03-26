package com.bcit.termproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

public class RequirementsAdapter extends RecyclerView.Adapter<RequirementsViewHolder> {

    private HashMap<String, String> requirementsDesc;
    private List<String> requirementsList;

    public RequirementsAdapter(HashMap<String, String> requirementsDesc, List<String> requirements) {
        this.requirementsList = requirements;
        this.requirementsDesc = requirementsDesc;
    }

    @NonNull
    @Override
    public RequirementsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View requirementsView = inflater.inflate(R.layout.requirements_row, parent, false);
        return new RequirementsViewHolder(requirementsView);
    }

    @Override
    public void onBindViewHolder(@NonNull RequirementsViewHolder holder, int position) {
        TextView reqTitle = holder.reqTitle;
        TextView reqDesc = holder.reqDesc;

        String requirement = requirementsList.get(position);
        reqTitle.setText(requirement);
        reqDesc.setText(requirementsDesc.get(requirement));
    }

    @Override
    public int getItemCount() {
        return requirementsList.size();
    }
}
