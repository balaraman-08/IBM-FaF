package com.example.friendaffinityfinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AffinityAdapter extends RecyclerView.Adapter<AffinityAdapter.ViewHolder> {

    private SharedPreferences sharedPreferences;
    Context context;
    int movie = 1, music = 1, book = 1;
    private JSONArray affinityRank;
    private JSONObject friendsName;

    AffinityAdapter(JSONArray affinityRank, JSONObject friendsName) {
        this.affinityRank = affinityRank;
        this.friendsName = friendsName;
    }


    @NonNull
    @Override
    public AffinityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list, parent, false);
        context = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AffinityAdapter.ViewHolder holder, int position) {

        sharedPreferences = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);

        try {
            JSONObject obj = (JSONObject) affinityRank.get(position);
            String userID = obj.getString("userID");
            String affinity = String.valueOf(obj.getDouble("affinity") * 100);
            holder.profilePictureView.setProfileId(userID);
            holder.percentage.setText(affinity.substring(0, 2) + "%");
            holder.percentage.setTextColor(context.getResources().getColor(R.color.colorBlueFlash));
            holder.friendname.setText(friendsName.getString(userID));
            holder.friendsCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder ipBuilder = new AlertDialog.Builder(context);
                    View v = LayoutInflater.from(context).inflate(R.layout.consumption_layout, null);
                    ipBuilder.setView(v);
                    final AlertDialog consumption = ipBuilder.create();
                    consumption.setCancelable(true);

                    //Consumption Layout
                    CardView preferences_card, movieCard, musicCard, booksCard;

                    //Recycler view
                    final RecyclerView r_movie, r_music, r_book;
                    r_movie = v.findViewById(R.id.recycler_movie);
                    r_music = v.findViewById(R.id.recycler_music);
                    r_book = v.findViewById(R.id.recyler_books);

                    //CardViews
                    preferences_card = v.findViewById(R.id.preferences_heading);
                    movieCard = v.findViewById(R.id.movie_card);
                    musicCard = v.findViewById(R.id.music_card);
                    booksCard = v.findViewById(R.id.book_card);
                    movieCard.setBackgroundResource(R.drawable.heading_tags);
                    musicCard.setBackgroundResource(R.drawable.heading_tags);
                    booksCard.setBackgroundResource(R.drawable.heading_tags);
                    preferences_card.setBackgroundResource(R.drawable.heading_tags);

                    //click listeners

                    //Movie
                    movieCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (movie == 1) {
                                movie = 2;
                                music = 1;
                                book = 1;
                                r_movie.setVisibility(View.VISIBLE);
                                r_music.setVisibility(View.GONE);
                                r_book.setVisibility(View.GONE);
                                r_movie.setHasFixedSize(true);
                                r_movie.setLayoutManager(new LinearLayoutManager(context));
                                r_movie.setAdapter(new ConsumptionAdapter(1));
                            } else {
                                movie = 1;
                                r_movie.setVisibility(View.GONE);
                            }
                        }
                    });

                    //Music
                    musicCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (music == 1) {
                                movie = 1;
                                music = 2;
                                book = 1;
                                r_movie.setVisibility(View.GONE);
                                r_music.setVisibility(View.VISIBLE);
                                r_book.setVisibility(View.GONE);
                                r_music.setHasFixedSize(true);
                                r_music.setLayoutManager(new LinearLayoutManager(context));
                                r_music.setAdapter(new ConsumptionAdapter(2));
                            } else {
                                music = 1;
                                r_music.setVisibility(View.GONE);
                            }
                        }
                    });

                    //Book
                    booksCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (book == 1) {
                                movie = 1;
                                music = 1;
                                book = 2;
                                r_movie.setVisibility(View.GONE);
                                r_music.setVisibility(View.GONE);
                                r_book.setVisibility(View.VISIBLE);
                                r_book.setHasFixedSize(true);
                                r_book.setLayoutManager(new LinearLayoutManager(context));
                                r_book.setAdapter(new ConsumptionAdapter(3));
                            } else {
                                music = 1;
                                r_book.setVisibility(View.GONE);
                            }
                        }
                    });

                    ImageView close = v.findViewById(R.id.close_consumption);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            consumption.dismiss();
                        }
                    });


//                consumption.show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return affinityRank.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ProfilePictureView profilePictureView;
        TextView percentage, friendname;
        CardView friendsCard;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePictureView = itemView.findViewById(R.id.friendProfilePicture);
            percentage = itemView.findViewById(R.id.percentage_affinity);
            friendsCard = itemView.findViewById(R.id.friends_card);
            friendname = itemView.findViewById(R.id.friend_name);
        }
    }

}
