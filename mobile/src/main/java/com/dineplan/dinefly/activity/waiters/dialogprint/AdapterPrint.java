package com.dineplan.dinefly.activity.waiters.dialogprint;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dineplan.dinefly.R;

import java.util.ArrayList;
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
        holder.setIsRecyclable(false);
        final String row = listdata.get(position);
        if (row.contains("-------")) {
            showCenter(holder);
            holder.center.setText("-----------------------------------------------------------------------------");

        } else {
            if (row.contains("  ")) {
                hideCenter(holder);
                String left = row.substring(0, row.indexOf("  "));
                holder.left.setText(left.trim());
                String right = row.substring(row.indexOf("  "));
                setRightPrice(holder, right.trim());
            }else {
                showCenter(holder);
                holder.center.setText(row);
            }
        }
    }
    private void setRightPrice(ViewHolder holder, String right){
        String[] content = right.split("  ");
        if (content.length < 3) {
            holder.right.setText(right);
        } else {
            holder.right.setVisibility(View.GONE);
            try {
                List<String> newContent = new ArrayList<>();
                for (int i = 0; i < content.length; i++) {
                    if (!content[i].equals("")) {
                        newContent.add(content[i]);
                    }
                }
                holder.qty.setText(newContent.get(0));
                holder.price.setText(newContent.get(1));
                holder.price1.setText(newContent.get(2));

            }catch (Exception e){

            }
        }
    }
    private void hideCenter(ViewHolder holder){
        holder.textlr.setVisibility(View.VISIBLE);
        holder.right.setVisibility(View.VISIBLE);
        holder.center.setVisibility(View.GONE);
    }

    private void showCenter(ViewHolder holder){
        holder.textlr.setVisibility(View.GONE);
        holder.right.setVisibility(View.GONE);
        holder.center.setVisibility(View.VISIBLE);
    }
    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView left, center, right, qty, price, price1;
        private LinearLayout textlr;

        public ViewHolder(View itemView) {
            super(itemView);
            left = itemView.findViewById(R.id.left);
            center = itemView.findViewById(R.id.center);
            right = itemView.findViewById(R.id.right);
            textlr = itemView.findViewById(R.id.textlr);
            qty = itemView.findViewById(R.id.qty);
            price = itemView.findViewById(R.id.price);
            price1 = itemView.findViewById(R.id.price1);
        }
    }
}

