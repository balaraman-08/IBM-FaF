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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

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

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CardView profilecard;
    private TwitterLoginButton twitterLoginButton;
    private SharedPreferences sharedPreferences;

    //Progress bars
    private ProgressBar pr_open, pr_diligence, pr_extraversion, pr_agreeableness, pr_emotional,pr_cons,pr_opentochange,pr_hedo,pr_enhance,pr_trascedence,pr_challenge,pr_closeness,pr_curiosity,pr_excitement,pr_harmony,pr_liberty,pr_ideal,pr_love,pr_practicality,pr_selfimpression,pr_stability,pr_structure;

    //Text views for progresses
    private TextView open_txt,diligence_txt,extra_txt,agree_txt,emotion_txt,cons_txt,otoc_txt,hedo_txt,enhance_txt,trans_txt,challenge_txt,closeness_txt,curiosity_txt,excitement_txt,harmony_txt,liberty_txt,ideal_txt,love_txt,practicality_txt,selfimpresssion_txt,stability_txt,structure_txt;
    private TextView name_t, gender_t, birthday_t, email_t, location_t;
    private ProfilePictureView profilePictureView;
    private Handler handler = new Handler();
    private ImageView navitate_2_value,navigatetoneed,naigatetopersonality;
    private CardView bigTraitsCard,valuesCard,needCard;

    private OnFragmentInteractionListener mListener;

//    Values for all Progresses
    private double open_value, diligence_value, extra_value, agree_value, emotion_value,cons_value,otoc_value,hedo_value,enhance_value,trans_value,challenge_value,closeness_value,curiosity_value,excitement_value,harmony_value,liberty_value,ideal_value,love_value,practicality_value,selfimpresssion_value,stability_value,structure_value;

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
    public static ProfileFragment newInstance(String param1, String param2) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //Initialising twitter login
        TwitterConfig config = new TwitterConfig.Builder(getActivity())
                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.consumer_key), getResources().getString(R.string.consumer_secret_key)))//pass the created app Consumer KEY and Secret also called API Key and Secret
                .debug(true)//enable debug mode
                .build();

        //finally initialize twitter with created configs
        Twitter.initialize(config);
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        profilecard = v.findViewById(R.id.profilecard);
        profilecard.setBackgroundResource(R.drawable.heading_tags);
        bigTraitsCard = v.findViewById(R.id.progressCircles);
        valuesCard = v.findViewById(R.id.values_card);
        needCard = v.findViewById(R.id.need_card);
        navitate_2_value = v.findViewById(R.id.navigate_value);
        navigatetoneed = v.findViewById(R.id.navigate_need);
        naigatetopersonality = v.findViewById(R.id.navigate_personality);
        navitate_2_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bigTraitsCard.setVisibility(View.GONE);
                valuesCard.setVisibility(View.VISIBLE);
                valueThreads();
            }

        });
        navigatetoneed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valuesCard.setVisibility(View.GONE);
                needCard.setVisibility(View.VISIBLE);
                needThread();
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
        //Progress Bars
        pr_cons = v.findViewById(R.id.const_pr);
        pr_opentochange = v.findViewById(R.id.o_c_pr);
        pr_hedo = v.findViewById(R.id.hedo_pr);
        pr_enhance = v.findViewById(R.id.self_e_pro);
        pr_trascedence = v.findViewById(R.id.self_t_pro);
        //Progress Bars
        pr_challenge = v.findViewById(R.id.challengepro);
        pr_closeness = v.findViewById(R.id.closenesspro);
        pr_curiosity = v.findViewById(R.id.curiositypro);
        pr_excitement = v.findViewById(R.id.excitementpro);
        pr_harmony = v.findViewById(R.id.harmonypro);
        pr_liberty = v.findViewById(R.id.libertypro);
        pr_ideal = v.findViewById(R.id.idealpro);
        pr_love = v.findViewById(R.id.lovepro);
        pr_practicality = v.findViewById(R.id.practicalitypro);
        pr_selfimpression = v.findViewById(R.id.selfexpressionpro);
        pr_stability = v.findViewById(R.id.stabilitypro);
        pr_structure = v.findViewById(R.id.structurepro);


        //Getting values for personality
        open_value = 0.7928032207775499 * 100;
        diligence_value = 0.34284755466944594 * 100;
        extra_value = 0.15961545962928608 * 100;
        agree_value = 0.15032514588920376 * 100;
        emotion_value = 0.5390207258387878 * 100;

        //Getting values for values card
        cons_value = 0.7928032207775499 * 100;
        otoc_value = 0.34284755466944594 * 100;
        hedo_value = 0.15961545962928608 * 100;
        enhance_value = 0.15032514588920376 * 100;
        trans_value = 0.5390207258387878 * 100;

        //Getting values for need card
        challenge_value = 0.45105915639828364*100;
        closeness_value = 0.09697076528337462*100;
        curiosity_value = 0.7165557074344118*100;
        excitement_value = 0.35876811702738504*100;
        harmony_value = 0.20933314830867866*100;
        liberty_value = 0.35624727414305585*100;
        ideal_value = 0.3507031384630498*100;
        love_value = 0.23269698299356145*100;
        practicality_value = 0.507946319202352*100;
        selfimpresssion_value = 0.2893917144236936*100;
        stability_value = 0.09162589450030012*100;
        structure_value = 0.3603255034390095*100;

        ///Progress Texts1
        open_txt = v.findViewById(R.id.pr_open_no);
        diligence_txt = v.findViewById(R.id.pr_diligence_no);
        extra_txt = v.findViewById(R.id.pr_extra_no);
        agree_txt = v.findViewById(R.id.pr_agree_no);
        emotion_txt = v.findViewById(R.id.pr_emotion_no);

        //Progress Texts2
        cons_txt = v.findViewById(R.id.const_pr_no);
        otoc_txt = v.findViewById(R.id.o_c_pr_no);
        hedo_txt = v.findViewById(R.id.hedo_pr_no);
        enhance_txt = v.findViewById(R.id.self_e_no);
        trans_txt = v.findViewById(R.id.self_t_no);

        //Progress Texts3
        challenge_txt = v.findViewById(R.id.challengeno);
        closeness_txt = v.findViewById(R.id.closenessno);
        curiosity_txt = v.findViewById(R.id.curiosityno);
        excitement_txt = v.findViewById(R.id.excitementno);
        harmony_txt = v.findViewById(R.id.harmonyno);
        ideal_txt = v.findViewById(R.id.idealno);
        liberty_txt = v.findViewById(R.id.libertyno);
        love_txt = v.findViewById(R.id.loveno);
        practicality_txt = v.findViewById(R.id.practicalityno);
        selfimpresssion_txt = v.findViewById(R.id.selfexpressionno);
        stability_txt = v.findViewById(R.id.stabilityno);
        structure_txt = v.findViewById(R.id.structureno);


        //PersonalityThreadCalling
        personalityThread();

        sharedPreferences = getActivity().getSharedPreferences("myPref", MODE_PRIVATE);

        String userid = sharedPreferences.getString("userid", "");
        String username = sharedPreferences.getString("username", "");
        String gender = sharedPreferences.getString("gender", "");
        String birthday = sharedPreferences.getString("birthday", "");
        String location = sharedPreferences.getString("city", "");
        String email = sharedPreferences.getString("email", "");

        profilePictureView = v.findViewById(R.id.friendProfilePicture);
        profilePictureView.setProfileId(userid);

        return v;

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


    //Bottom Activity

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

    private void valueThreads(){
        //Conservation thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus = 0;
                while (pStatus <= cons_value) {
                    final int finalPStatus = pStatus;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_cons.setProgress(finalPStatus);
                            cons_txt.setText(finalPStatus + " %");
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

        //Open to change progress thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus1 = 0;
                while (pStatus1 <= otoc_value) {
                    final int finalPStatus = pStatus1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_opentochange.setProgress(finalPStatus);
                            otoc_txt.setText(finalPStatus + " %");
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

        //Hedonism thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus = 0;
                while (pStatus <= hedo_value) {
                    final int finalPStatus = pStatus;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_hedo.setProgress(finalPStatus);
                            hedo_txt.setText(finalPStatus + " %");
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


        //Self enhance progress thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus1 = 0;
                while (pStatus1 <= trans_value) {
                    final int finalPStatus = pStatus1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_trascedence.setProgress(finalPStatus);
                            trans_txt.setText(finalPStatus+"%");
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

        //Self transcedence progress thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus1 = 0;
                while (pStatus1 <= hedo_value) {
                    final int finalPStatus = pStatus1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_hedo.setProgress(finalPStatus);
                            hedo_txt.setText(finalPStatus + " %");
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

    private void needThread(){

        //Challenge thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus = 0;
                while (pStatus <= challenge_value) {
                    final int finalPStatus = pStatus;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_challenge.setProgress(finalPStatus);
                            challenge_txt.setText(finalPStatus + " %");
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

        //Closeness progress thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus1 = 0;
                while (pStatus1 <= closeness_value) {
                    final int finalPStatus = pStatus1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_closeness.setProgress(finalPStatus);
                            closeness_txt.setText(finalPStatus + " %");
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

        //Curiosity thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus = 0;
                while (pStatus <= curiosity_value) {
                    final int finalPStatus = pStatus;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_curiosity.setProgress(finalPStatus);
                            curiosity_txt.setText(finalPStatus + " %");
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


        //Excitement progress thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus1 = 0;
                while (pStatus1 <= excitement_value) {
                    final int finalPStatus = pStatus1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_excitement.setProgress(finalPStatus);
                            excitement_txt.setText(finalPStatus+"%");
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

        //Harmony progress thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus1 = 0;
                while (pStatus1 <= harmony_value) {
                    final int finalPStatus = pStatus1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_harmony.setProgress(finalPStatus);
                            harmony_txt.setText(finalPStatus + " %");
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

        //Liberty thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus = 0;
                while (pStatus <= liberty_value) {
                    final int finalPStatus = pStatus;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_liberty.setProgress(finalPStatus);
                            liberty_txt.setText(finalPStatus + " %");
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

        //Ideal progress thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus1 = 0;
                while (pStatus1 <= ideal_value) {
                    final int finalPStatus = pStatus1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_ideal.setProgress(finalPStatus);
                            ideal_txt.setText(finalPStatus + " %");
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

        //Love thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus = 0;
                while (pStatus <= love_value) {
                    final int finalPStatus = pStatus;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_love.setProgress(finalPStatus);
                            love_txt.setText(finalPStatus + " %");
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


        //Practicality progress thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus1 = 0;
                while (pStatus1 <= practicality_value) {
                    final int finalPStatus = pStatus1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_practicality.setProgress(finalPStatus);
                            practicality_txt.setText(finalPStatus+"%");
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

        //Self expression progress thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus1 = 0;
                while (pStatus1 <= selfimpresssion_value) {
                    final int finalPStatus = pStatus1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_selfimpression.setProgress(finalPStatus);
                            selfimpresssion_txt.setText(finalPStatus + " %");
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

        //Stability thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus1 = 0;
                while (pStatus1 <= stability_value) {
                    final int finalPStatus = pStatus1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_stability.setProgress(finalPStatus);
                            stability_txt.setText(finalPStatus + " %");
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

        //Structure thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                int pStatus = 0;
                while (pStatus <= structure_value) {
                    final int finalPStatus = pStatus;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pr_structure.setProgress(finalPStatus);
                            structure_txt.setText(finalPStatus + " %");
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

    }

    private void personalityThread(){
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
                            emotion_txt.setText(finalPStatus+"%");
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

}
