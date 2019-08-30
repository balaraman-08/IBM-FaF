package com.example.friendaffinityfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Big5Adapter extends RecyclerView.Adapter<Big5Adapter.ViewHolder> {

    private Context context;
    private JSONArray rank;
    private JSONObject friendsName;

    Big5Adapter(JSONArray rank, JSONObject friendsName) {
        this.rank = rank;
        this.friendsName = friendsName;
    }

    @NonNull
    @Override
    public Big5Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.big5_list_layout, parent, false);
        context = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Big5Adapter.ViewHolder holder, int position) {

        try {
            JSONObject object = rank.getJSONObject(position);
            String userID = object.getString("userID");
            holder.profilePictureView.setProfileId(userID);
            holder.friendname.setText(friendsName.getString(userID));
            String percentile = String.valueOf(object.getDouble("percentile") * 100);
            holder.percentage.setText(percentile.substring(0, 2) + "%");
            holder.percentage.setTextColor(context.getResources().getColor(R.color.colorViolet));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return rank.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ProfilePictureView profilePictureView;
        TextView percentage, friendname;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePictureView = itemView.findViewById(R.id.friendProfilePicture);
            percentage = itemView.findViewById(R.id.percentage_big5);
            friendname = itemView.findViewById(R.id.friend_name);
        }

    }

}
