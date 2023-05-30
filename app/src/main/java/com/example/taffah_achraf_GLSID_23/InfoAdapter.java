package com.example.taffah_achraf_GLSID_23;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ajincodew.R;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.InfoViewHolder> {

    private List<IpActivity.InfoItem> infoItems;

    public InfoAdapter(List<IpActivity.InfoItem> infoItems) {
        this.infoItems = infoItems;
    }

    @NonNull
    @Override
    public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info, parent, false);
        return new InfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoViewHolder holder, int position) {
        IpActivity.InfoItem infoItem = infoItems.get(position);
        holder.tvTitle.setText(infoItem.getTitle());
        holder.tvValue.setText(infoItem.getValue());
    }

    @Override
    public int getItemCount() {
        return infoItems.size();
    }

    static class InfoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvValue;

        public InfoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvValue = itemView.findViewById(R.id.tvValue);
        }
    }
}
