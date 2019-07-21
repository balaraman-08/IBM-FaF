package com.example.friendaffinityfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SharedPreferences sharedPreferences;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TwitterLoginButton twitterLoginButton;
    ImageView twitterLogin;
    //Progress bars
    private ProgressBar pr_open, pr_diligence, pr_extraversion, pr_emotional, pr_agreeableness;

    //Text views for progresses
    private TextView open_txt, diligence_txt, extra_txt, agree_txt, emotion_txt;
    private Handler handler = new Handler();
    private CardView bigTraitsCard, valuesCard, needCard;
    private JSONArray personality, needs, values;
    private NeedsListAdapter needAdapter, valueAdapter;
    private LinearLayout analyse_ll;
    private TextView error_tv;

    private OnFragmentInteractionListener mListener;

    //    Values for all Progresses
    private double open_value, diligence_value, extra_value, agree_value, emotion_value;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    private static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //Initialising twitter login
        TwitterConfig config = new TwitterConfig.Builder(Objects.requireNonNull(getActivity()))
                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.consumer_key), getResources().getString(R.string.consumer_secret_key)))//pass the created app Consumer KEY and Secret also called API Key and Secret
                .debug(true)//enable debug mode
                .build();

        //finally initialize twitter with created configs
        Twitter.initialize(config);

        sharedPreferences = getActivity().getSharedPreferences("myPref", MODE_PRIVATE);

        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        analyse_ll = v.findViewById(R.id.analysing_progress);
        error_tv = v.findViewById(R.id.error_text);
        TextView username_tv = v.findViewById(R.id.username);
        username_tv.setText(sharedPreferences.getString("username", ""));
        CardView profileCard = v.findViewById(R.id.profilecard);
        profileCard.setBackgroundResource(R.drawable.heading_tags);
        bigTraitsCard = v.findViewById(R.id.progressCircles);
        valuesCard = v.findViewById(R.id.values_card);
        twitterLogin = v.findViewById(R.id.addTwitter);
        twitterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomActivity(view);
            }
        });
        needCard = v.findViewById(R.id.need_card);

        needs = new JSONArray();
        values = new JSONArray();
        personality = new JSONArray();

        RecyclerView needsView = v.findViewById(R.id.need_list);
        needAdapter = new NeedsListAdapter(this.getActivity(), needs);
        needsView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        needsView.setHasFixedSize(true);
        needsView.setAdapter(needAdapter);

        RecyclerView valuesView = v.findViewById(R.id.value_list);
        valueAdapter = new NeedsListAdapter(this.getActivity(), values);
        valuesView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        valuesView.setHasFixedSize(true);
        valuesView.setAdapter(valueAdapter);

        getMyAnalyse();

        ImageView navitate_2_value = v.findViewById(R.id.navigate_value);
        ImageView navigatetoneed = v.findViewById(R.id.navigate_need);
        ImageView naigatetopersonality = v.findViewById(R.id.navigate_personality);
        navitate_2_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bigTraitsCard.setVisibility(View.GONE);
                valuesCard.setVisibility(View.VISIBLE);
            }

        });
        navigatetoneed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valuesCard.setVisibility(View.GONE);
                needCard.setVisibility(View.VISIBLE);
            }
        });
        naigatetopersonality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                needCard.setVisibility(View.GONE);
                bigTraitsCard.setVisibility(View.VISIBLE);
                personalityThread();
            }
        });


        //Progress Circles
        pr_open = v.findViewById(R.id.progress_open);
        pr_diligence = v.findViewById(R.id.progress_diligence);
        pr_extraversion = v.findViewById(R.id.progress_extraversion);
        pr_agreeableness = v.findViewById(R.id.progress_agree);
        pr_emotional = v.findViewById(R.id.progress_emotion);

        //Getting values for personality
        open_value = 0.7928032207775499 * 100;
        diligence_value = 0.34284755466944594 * 100;
        extra_value = 0.15961545962928608 * 100;
        agree_value = 0.15032514588920376 * 100;
        emotion_value = 0.5390207258387878 * 100;

        ///Progress Texts1
        open_txt = v.findViewById(R.id.pr_open_no);
        diligence_txt = v.findViewById(R.id.pr_diligence_no);
        extra_txt = v.findViewById(R.id.pr_extra_no);
        agree_txt = v.findViewById(R.id.pr_agree_no);
        emotion_txt = v.findViewById(R.id.pr_emotion_no);

        String userId = sharedPreferences.getString("userid", "");

        ProfilePictureView profilePictureView = v.findViewById(R.id.friendProfilePicture);
        profilePictureView.setProfileId(userId);

        return v;

    }

    private void getMyAnalyse() {
        JsonObjectRequest my_analyse_request = new JsonObjectRequest(Request.Method.GET,
                getResources().getString(R.string.URL) + "/analyse/me",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Analyse", "onResponse: " + response.toString());
                        try {
                            needs = response.getJSONArray("needs");
                            values = response.getJSONArray("values");
                            personality = response.getJSONArray("personality");

                            analyse_ll.setVisibility(View.GONE);
                            needCard.setVisibility(View.GONE);
                            valuesCard.setVisibility(View.GONE);
                            bigTraitsCard.setVisibility(View.VISIBLE);

                            open_value = personality.getJSONObject(0).getDouble("percentile") * 100;
                            diligence_value = personality.getJSONObject(1).getDouble("percentile") * 100;
                            extra_value = personality.getJSONObject(2).getDouble("percentile") * 100;
                            agree_value = personality.getJSONObject(3).getDouble("percentile") * 100;
                            emotion_value = personality.getJSONObject(4).getDouble("percentile") * 100;

                            //PersonalityThreadCalling
                            personalityThread();

                            Log.e("Analyse", needs.toString() + '\n' + values.toString());

                            needAdapter.updateList(needs);
                            valueAdapter.updateList(values);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("my_analyse_request", "onErrorResponse: " + error);
                        analyse_ll.setVisibility(View.GONE);
                        error_tv.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Cookie", Objects.requireNonNull(sharedPreferences.getString("cookies", "")));
                return headers;
            }
        };
        my_analyse_request.setRetryPolicy(new DefaultRetryPolicy(120000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(Objects.requireNonNull(this.getActivity())).add(my_analyse_request);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("entering", "entering");
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void bottomActivity(final View view1) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        twitterLoginButton = view.findViewById(R.id.default_twitter_login_button);

        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                Log.d("twitterresponse", result.data.getUserName());
                bottomSheetDialog.dismiss();

            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Log.d("twitterfailure", exception.toString());

            }
        });


    }

    private void personalityThread() {
        //Running threads for progress cirlces

        //Openness thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus = 0;
                while (pStatus <= open_value) {
                    final int finalPStatus = pStatus;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_open.setProgress(finalPStatus);
                            open_txt.setText(finalPStatus + " %");
                        }
                    });
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pStatus++;
                }
            }
        }).start();

        //Diligence progress thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus1 = 0;
                while (pStatus1 <= diligence_value) {
                    final int finalPStatus = pStatus1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_diligence.setProgress(finalPStatus);
                            diligence_txt.setText(finalPStatus + " %");
                        }
                    });
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pStatus1++;
                }
            }
        }).start();

        //Extraversion thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus = 0;
                while (pStatus <= extra_value) {
                    final int finalPStatus = pStatus;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_extraversion.setProgress(finalPStatus);
                            extra_txt.setText(finalPStatus + " %");
                        }
                    });
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pStatus++;
                }
            }
        }).start();

        //Agreeableness progress thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus1 = 0;
                while (pStatus1 <= agree_value) {
                    final int finalPStatus = pStatus1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_agreeableness.setProgress(finalPStatus);
                            agree_txt.setText(finalPStatus + " %");
                        }
                    });
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pStatus1++;
                }
            }
        }).start();

        //Emotional progress thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus1 = 0;
                while (pStatus1 <= emotion_value) {
                    final int finalPStatus = pStatus1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_emotional.setProgress(finalPStatus);
                            emotion_txt.setText(finalPStatus + "%");
                        }
                    });
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pStatus1++;
                }
            }
        }).start();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
