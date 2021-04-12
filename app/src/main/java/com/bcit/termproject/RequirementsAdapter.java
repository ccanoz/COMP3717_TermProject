package com.bcit.termproject;

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

    /**
     * Inflates the layout and returns a RequirementsViewHolder view holder.
     * @param parent a ViewGroup
     * @param viewType an int
     * @return a RequirementsViewHolder
     */
    @NonNull
    @Override
    public RequirementsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View requirementsView = inflater.inflate(R.layout.requirements_row, parent, false);
        return new RequirementsViewHolder(requirementsView);
    }

    /**
     * Sets each requirement's title and description text views.
     * @param holder the RequirementsViewHolder object
     * @param position an int
     */
    @Override
    public void onBindViewHolder(@NonNull RequirementsViewHolder holder, int position) {
        TextView reqTitle = holder.reqTitle;
        TextView reqDesc = holder.reqDesc;

        String requirement = requirementsList.get(position);
        reqTitle.setText(requirement);
        reqDesc.setText(requirementsDesc.get(requirement));
    }

    /**
     * Get the size of the list of requirements stored in this adapter.
     * @return an int
     */
    @Override
    public int getItemCount() {
        return requirementsList.size();
    }
}
