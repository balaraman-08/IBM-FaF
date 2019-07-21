package com.example.friendaffinityfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ConsumptionAdapter extends RecyclerView.Adapter<ConsumptionAdapter.ViewHolder> {

    Context context;
    int textColor=0;
    String[] consumption = {"Adventure movies","historical movies","science-fiction"};

    ConsumptionAdapter(int i) {
        textColor = i;
    }

    @NonNull
    @Override
    public ConsumptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.consumption_recycler, parent, false);
        context = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsumptionAdapter.ViewHolder holder, int position) {
        holder.consumptions.setText("Like to watch movies");

        if (textColor==1){
            holder.consumptions.setText(consumption[position]);
            holder.consumptions.setTextColor(context.getResources().getColor(R.color.colorViolet));
        }

        if (textColor==2)
            holder.consumptions.setTextColor(context.getResources().getColor(R.color.colorBlueFlash));
        if (textColor==3)
            holder.consumptions.setTextColor(context.getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView consumptions;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            consumptions = itemView.findViewById(R.id.consumptions);

        }
    }
}
