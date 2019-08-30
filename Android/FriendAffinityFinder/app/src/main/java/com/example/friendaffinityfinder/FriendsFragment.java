package com.example.friendaffinityfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

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
 * {@link FriendsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends Fragment implements AffinityFragment.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ViewPager f_viewpager;
    private TabLayout f_tablayout;
    private FriendsPagerAdapter friendsPagerAdapter;
    private Big5Fragment big5Fragment;
    private AffinityFragment affinityFragment;
    private OnFragmentInteractionListener mListener;
    private AffinityNotifier affinityNotifier;
    private Big5Notifier big5Notifier;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public FriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        sharedPreferences = getActivity().getSharedPreferences("myPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        f_viewpager = v.findViewById(R.id.friends_viewpager);
        f_tablayout = v.findViewById(R.id.friends_tab);
        f_tablayout.addTab(f_tablayout.newTab().setText("Affinity"), 0);
        f_tablayout.addTab(f_tablayout.newTab().setText("Big 5"), 1);
        f_tablayout.setupWithViewPager(f_viewpager);
        f_viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(f_tablayout));
        f_tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                f_viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        f_viewpager.setAdapter(new FriendsPagerAdapter(getFragmentManager()));

        getFriendsAnalyse();

        return v;
    }



    private void getFriendsAnalyse() {
        JsonObjectRequest my_analyse_request = new JsonObjectRequest(Request.Method.GET,
                getResources().getString(R.string.URL) + "/analyse/friends",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Analyse", "onResponse: " + response.toString());
                        editor.putString("friendsData",response.toString());
                        Log.e("analyseddata",sharedPreferences.getString("friendsData",""));
                        try {
                            JSONArray opennessRank = response.getJSONArray("opennessRank");
                            JSONArray conscientiousnessRank = response.getJSONArray("conscientiousnessRank");
                            JSONArray extraversionRank = response.getJSONArray("extraversionRank");
                            JSONArray agreeablenessRank = response.getJSONArray("agreeablenessRank");
                            JSONArray neuroticismRank = response.getJSONArray("neuroticismRank");
                            JSONArray affinityRank = response.getJSONArray("affinityRank");
                            JSONObject consumption_preferences = response.getJSONObject("consumption_preferences");
                            JSONObject friendsName = response.getJSONObject("friendsName");

                            //Storing in shared preferences
                            editor.putString("opennessRank",opennessRank.toString());
                            editor.putString("conscientiousnessRank",conscientiousnessRank.toString());
                            editor.putString("extraversionRank",extraversionRank.toString());
                            editor.putString("agreeablenessRank",agreeablenessRank.toString());
                            editor.putString("neuroticismRank",neuroticismRank.toString());
                            editor.putString("affinityRank",affinityRank.toString());
                            editor.putString("consumption_preferences",consumption_preferences.toString());
                            editor.putString("friendsName",friendsName.toString());
                            editor.apply();


//                            Log.d("opennessRank",opennessRank.toString());
//                            Log.d("opennessRank",opennessRank.toString());
//                            Log.d("conscientiousnessRank",conscientiousnessRank.toString());
//                            Log.d("extraversionRank",extraversionRank.toString());
//                            Log.d("agreeablenessRank",agreeablenessRank.toString());
//                            Log.d("neuroticismRank",neuroticismRank.toString());
//                            Log.d("affinityRank",affinityRank.toString());
//                            Log.d("consumption_preferences",consumption_preferences.toString());
//                            Log.d("friendsName",friendsName.toString());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("my_analyse_request", "onErrorResponse: " + error);
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
    public void onFragmentInteraction(Uri uri) {

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
