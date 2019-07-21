package com.example.friendaffinityfinder;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by BALARAMAN on 21-07-2019 with love.
 */
public class NeedsListAdapter extends RecyclerView.Adapter<NeedsListAdapter.NeedsVH> {
    private JSONArray needs;
    private Activity context;
    private ArrayList<Integer> colors;

    NeedsListAdapter(Activity context, JSONArray needs) {
        this.context = context;
        this.needs = needs;
        this.colors = new ArrayList<>();
        colors.add(R.color.colorViolet);
        colors.add(R.color.colorGreenDark);
        colors.add(R.color.colorPrimary);
        colors.add(R.color.colorAccent);
        colors.add(R.color.colorOrange);

    }

    void updateList(JSONArray needs) {
        this.needs = needs;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NeedsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.needs_progress_view, parent, false);
        return new NeedsVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NeedsVH holder, int position) {
        JSONObject need;
        try {
            need = needs.getJSONObject(position);
            holder.needName.setText(need.getString("name"));
            holder.needName.setTextColor(ContextCompat.getColor(context, colors.get(position % 5)));
            int percentile = (int) (need.getDouble("percentile") * 100);
            holder.needPercentile.setText(String.valueOf(percentile));
            holder.needPercentile.setTextColor(ContextCompat.getColor(context, colors.get(position % 5)));
//            holder.progressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(context, colors.get(position % 5)), PorterDuff.Mode.SRC_IN);
            holder.progressBar.setProgress(percentile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return needs.length();
    }

    class NeedsVH extends RecyclerView.ViewHolder {
        TextView needName;
        TextView needPercentile;
        ProgressBar progressBar;

        NeedsVH(@NonNull View itemView) {
            super(itemView);
            needName = itemView.findViewById(R.id.need_name);
            needPercentile = itemView.findViewById(R.id.need_percentile);
            progressBar = itemView.findViewById(R.id.need_progress);
        }
    }
}
