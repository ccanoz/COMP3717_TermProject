package com.bcit.termproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;


import java.util.HashMap;
import java.util.List;

/**
 * RecyclerView Adapter class for account details shown in the AccountFragment.
 */
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private HashMap<String, String> accDetailMap;
    private List<String> accDetailList;

    private OnAdapterItemListener onAdapterItemListener;

    public void setOnAdapterItemListener(OnAdapterItemListener onAdapterItemListener) {
        this.onAdapterItemListener = onAdapterItemListener;
    }

    /**
     * Instantiates an AccountAdapter with a HashMap that maps the user detail values to their
     * corresponding field name, and a List of the field names.
     * @param accDetailMap a HashMap<String, String>
     * @param accDetailList a List<String></String>
     */
    public AccountAdapter(HashMap<String, String> accDetailMap, List<String> accDetailList) {
        this.accDetailList = accDetailList;
        this.accDetailMap = accDetailMap;
    }

    /**
     * Inflates the layout file to be used in the RecyclerView.
     * @param parent a ViewGroup
     * @param viewType an int
     * @return a AccountViewHolder
     */
    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View accDetailsView = inflater.inflate(R.layout.account_detail, parent, false);
        return new AccountViewHolder(accDetailsView);
    }

    /**
     * Sets up the views for each item in the accDetailList.
     * @param holder the AccountViewHolder
     * @param position int, index of item in the list
     */
    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        TextView tvDetailLabel = holder.tvDetailLabel;
        TextView tvDetailValue = holder.tvDetailValue;
        AppCompatImageButton editButton = holder.editButton;

        String label = accDetailList.get(position);
        tvDetailLabel.setText(label);
        tvDetailValue.setText(accDetailMap.get(label));
        editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onAdapterItemListener.OnClick(label);
            }
        });

    }

    /**
     * Returns the size of the accDetailList.
     * @return an int
     */
    @Override
    public int getItemCount() {
        return accDetailList.size();
    }

    /**
     * ViewHolder for a user's account details. Holds TextViews for the field's label and
     * current value, as well as an ImageButton for an edit button.
     */
    public class AccountViewHolder extends RecyclerView.ViewHolder {

        public TextView tvDetailLabel;
        public TextView tvDetailValue;
        public AppCompatImageButton editButton;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDetailLabel = itemView.findViewById(R.id.textView_accDetail_label);
            tvDetailValue = itemView.findViewById(R.id.textView_accDetail_value);
            editButton = itemView.findViewById(R.id.imgButton_editAccount);
        }
    }

    /**
     * Interface that is implemented by AccountAdapter items OnClick.
     */
    public interface OnAdapterItemListener {
        void OnClick(String label);
    }
}

