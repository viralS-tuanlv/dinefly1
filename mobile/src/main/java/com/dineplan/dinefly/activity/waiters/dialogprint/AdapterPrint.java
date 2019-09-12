package com.dineplan.dinefly.activity.waiters.dialogprint;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dineplan.dinefly.R;

import java.util.List;

public class AdapterPrint extends RecyclerView.Adapter<AdapterPrint.ViewHolder> {
    private List<String> listdata;

    public AdapterPrint(List<String> listdata) {
        this.listdata = listdata;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_bill, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String row = listdata.get(position);
        if (row.contains("-------")) {
            holder.textView.setText("-----------------------------------------------------------------------------");

        } else {
            holder.textView.setText(row);

        }
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.row);
        }
    }
}

