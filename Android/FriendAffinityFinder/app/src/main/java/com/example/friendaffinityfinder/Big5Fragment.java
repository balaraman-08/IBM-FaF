package com.example.friendaffinityfinder;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Big5Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Big5Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Big5Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CardView openCard,conscienceCard,extraCard,agreeCard,emotionCard,headingCard;
    private RecyclerView recyclerOpen,recyclerConscience,recyclerExtra,recyclerAgree,recyclerEmotion;
    private TextView bigOpen,bigcConscience,bigExtra,bigAgree,bigEmotion;

    //Functioning
    private int open = 1,conscience=1,extra=1,agree=1,emotion=1;

    private OnFragmentInteractionListener mListener;

    public Big5Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Big5Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Big5Fragment newInstance(String param1, String param2) {
        Big5Fragment fragment = new Big5Fragment();
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
        View v = inflater.inflate(R.layout.fragment_big5, container, false);


        //CardViews
        openCard = v.findViewById(R.id.open_card);
        conscienceCard = v.findViewById(R.id.conscience_card);
        extraCard = v.findViewById(R.id.extra_card);
        agreeCard = v.findViewById(R.id.agree_card);
        emotionCard = v.findViewById(R.id.emotion_card);
        headingCard = v.findViewById(R.id.heading_big5);

        //CardViews
        openCard.setBackgroundResource(R.drawable.heading_tags);
        conscienceCard.setBackgroundResource(R.drawable.heading_tags);
        extraCard.setBackgroundResource(R.drawable.heading_tags);
        agreeCard.setBackgroundResource(R.drawable.heading_tags);
        emotionCard.setBackgroundResource(R.drawable.heading_tags);
        headingCard.setBackgroundResource(R.drawable.heading_tags);


        //RecyclerViews
        recyclerOpen = v.findViewById(R.id.recycler_openness);
        recyclerConscience = v.findViewById(R.id.recycler_conscience);
        recyclerExtra = v.findViewById(R.id.recycler_extra);
        recyclerAgree = v.findViewById(R.id.recycler_agree);
        recyclerEmotion = v.findViewById(R.id.recycler_emotion);


        //TextViews
        bigOpen = v.findViewById(R.id.big_open);
        bigcConscience = v.findViewById(R.id.big_conscience);
        bigExtra = v.findViewById(R.id.big_extra);
        bigAgree = v.findViewById(R.id.big_agree);
        bigEmotion = v.findViewById(R.id.big_emotion);

        //Openness View
        openCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (open==1){
                    conscience=1;extra=1;agree=1;emotion=1;
                    recyclerOpen.setVisibility(View.VISIBLE);
                    recyclerConscience.setVisibility(View.GONE);
                    recyclerExtra.setVisibility(View.GONE);
                    recyclerAgree.setVisibility(View.GONE);
                    recyclerEmotion.setVisibility(View.GONE);
                    recyclerOpen.setHasFixedSize(true);
                    recyclerOpen.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerOpen.setAdapter(new Big5Adapter(1));
                    open=2;
                }
                else{
                    recyclerOpen.setVisibility(View.GONE);
                    open=1;
                }

            }
        });

        //Conscience View
        conscienceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (conscience==1){
                    open=1;conscience=2;extra=1;agree=1;emotion=1;
                    recyclerOpen.setVisibility(View.GONE);
                    recyclerConscience.setVisibility(View.VISIBLE);
                    recyclerExtra.setVisibility(View.GONE);
                    recyclerAgree.setVisibility(View.GONE);
                    recyclerEmotion.setVisibility(View.GONE);
                    recyclerConscience.setHasFixedSize(true);
                    recyclerConscience.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerConscience.setAdapter(new Big5Adapter(2));

                }
                else{
                    recyclerConscience.setVisibility(View.GONE);
                    conscience=1;
                }

            }
        });

        //Extra View
        extraCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (extra==1){
                    open=1;conscience=1;extra=2;agree=1;emotion=1;
                    recyclerOpen.setVisibility(View.GONE);
                    recyclerConscience.setVisibility(View.GONE);
                    recyclerExtra.setVisibility(View.VISIBLE);
                    recyclerAgree.setVisibility(View.GONE);
                    recyclerEmotion.setVisibility(View.GONE);
                    recyclerExtra.setHasFixedSize(true);
                    recyclerExtra.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerExtra.setAdapter(new Big5Adapter(3));

                }
                else{
                    recyclerExtra.setVisibility(View.GONE);
                    extra=1;
                }

            }
        });

        //Agree View
        agreeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (agree==1){
                    open=1;conscience=1;extra=1;agree=2;emotion=1;
                    recyclerOpen.setVisibility(View.GONE);
                    recyclerConscience.setVisibility(View.GONE);
                    recyclerExtra.setVisibility(View.GONE);
                    recyclerAgree.setVisibility(View.VISIBLE);
                    recyclerEmotion.setVisibility(View.GONE);
                    recyclerAgree.setHasFixedSize(true);
                    recyclerAgree.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerAgree.setAdapter(new Big5Adapter(4));

                }
                else{
                    recyclerAgree.setVisibility(View.GONE);
                    agree=1;
                }

            }
        });

        //Emotional View
        emotionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emotion==1){
                    open=1;conscience=1;extra=1;agree=1;emotion=2;
                    recyclerOpen.setVisibility(View.GONE);
                    recyclerConscience.setVisibility(View.GONE);
                    recyclerExtra.setVisibility(View.GONE);
                    recyclerAgree.setVisibility(View.GONE);
                    recyclerEmotion.setVisibility(View.VISIBLE);
                    recyclerEmotion.setHasFixedSize(true);
                    recyclerEmotion.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerEmotion.setAdapter(new Big5Adapter(5));

                }
                else{
                    recyclerEmotion.setVisibility(View.GONE);
                    emotion=1;
                }

            }
        });



        return v;
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
