package com.bcit.termproject;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RequirementsViewHolder extends RecyclerView.ViewHolder {

    public TextView reqTitle;
    public TextView reqDesc;

    public RequirementsViewHolder(@NonNull View itemView) {
        super(itemView);

        reqTitle = itemView.findViewById(R.id.textView_reqTitle);
        reqDesc = itemView.findViewById(R.id.textView_reqDescription);
    }
}
