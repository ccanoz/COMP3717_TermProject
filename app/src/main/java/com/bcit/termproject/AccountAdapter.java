package com.bcit.termproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private HashMap<String, String> accDetailMap;
    private List<String> accDetailList;

    public AccountAdapter(HashMap<String, String> accDetailMap, List<String> accDetailList) {
        this.accDetailList = accDetailList;
        this.accDetailMap = accDetailMap;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View accDetailsView = inflater.inflate(R.layout.account_detail, parent, false);
        return new AccountViewHolder(accDetailsView);

    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        TextView tvDetailLabel = holder.tvDetailLabel;
        TextView tvDetailValue = holder.tvDetailValue;
        Button editButton = holder.editButton;

        String label = accDetailList.get(position);
        tvDetailLabel.setText(label);
        tvDetailValue.setText(accDetailMap.get(label));

    }

    @Override
    public int getItemCount() {
        return accDetailList.size();
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder {

        public TextView tvDetailLabel;
        public TextView tvDetailValue;
        public Button editButton;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDetailLabel = itemView.findViewById(R.id.textView_accDetail_label);
            tvDetailValue = itemView.findViewById(R.id.textView_accDetail_value);
            editButton = itemView.findViewById(R.id.button_editAccount);
        }
    }
}

