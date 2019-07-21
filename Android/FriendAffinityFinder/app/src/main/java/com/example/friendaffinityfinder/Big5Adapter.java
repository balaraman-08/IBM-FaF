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

public class Big5Adapter extends RecyclerView.Adapter<Big5Adapter.ViewHolder>  {

    private SharedPreferences sharedPreferences;
    private Context context ;
    private int bottomLine = 0;
    String[] userid = {"2342575749154999","345087719740421","214824776154108"};
    String[] name = {"Ganesh Ramkumar","Rahul Raj","Gowtham"};
    String[] percentage1={"85%","80%","78%"};
    String[] percentage2={"60%","58%","52%"};
    String[] percentage3={"14%","10%","7%"};
    String[] percentage4={"84","65%","44%"};
    String[] percentage5={"57%","56%","48%"};

    Big5Adapter(int i) {
        bottomLine = i;
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

        sharedPreferences = context.getSharedPreferences("myPref",Context.MODE_PRIVATE);
        String friendid = sharedPreferences.getString("userid","");
        holder.profilePictureView.setProfileId(userid[position]);
        Log.d("friendid",friendid);
        holder.friendname.setText(name[position]);


        Log.d("Value",""+bottomLine);
        if (bottomLine==1){
            holder.percentage.setText(percentage1[position]);
            holder.percentage.setTextColor(context.getResources().getColor(R.color.colorViolet));
        }

        if (bottomLine==2){
            holder.percentage.setText(percentage2[position]);
            holder.percentage.setTextColor(context.getResources().getColor(R.color.colorBlueFlash));
        }

        if (bottomLine==3){
            holder.percentage.setText(percentage3[position]);
            holder.percentage.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }

        if (bottomLine==4){
            holder.percentage.setText(percentage4[position]);
            holder.percentage.setTextColor(context.getResources().getColor(R.color.colorGreenDark));
        }

        if (bottomLine==5){
            holder.percentage.setText(percentage5[position]);
            holder.percentage.setTextColor(context.getResources().getColor(R.color.colorOrange));
        }


    }

    @Override
    public int getItemCount() {
        return userid.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ProfilePictureView profilePictureView;
//        View bottomLine;
        TextView percentage,friendname;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePictureView = itemView.findViewById(R.id.friendProfilePicture);
//            bottomLine = itemView.findViewById(R.id.view_bottom);
            percentage = itemView.findViewById(R.id.percentage_big5);
            friendname = itemView.findViewById(R.id.friend_name);
        }

    }

}
