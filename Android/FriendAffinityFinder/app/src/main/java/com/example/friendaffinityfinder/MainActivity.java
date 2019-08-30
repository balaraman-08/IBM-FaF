package com.example.friendaffinityfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public String URL;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    LoginButton loginButton;
    CallbackManager callbackManager;
    boolean isLoggedIn = false;
    String accessTokenfb = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Shared Preferences
        URL = getResources().getString(R.string.URL);
        sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Callback object
        callbackManager = CallbackManager.Factory.create();

        //LoginButton object
        loginButton = findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList("user_friends", "user_posts", "email", "user_photos", "public_profile"));
        //Logged in or not boolean
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn = accessToken != null && !accessToken.isExpired();
        goToHomeIfLoggedIn();
//        accessPermission();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("success", "success");
                accessTokenfb = loginResult.getAccessToken().getToken();
                Log.d("acceesstokenfb", accessTokenfb);
                editor.putString("userid", loginResult.getAccessToken().getUserId());
                editor.putString("accessToken", loginResult.getAccessToken().getToken());

                editor.apply();
                editor.commit();
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                isLoggedIn = accessToken != null && !accessToken.isExpired();
                Log.d("isloggedin", "log->" + isLoggedIn);
                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                Log.d("response", object.toString());
                                JSONObject location = null;
                                String city = null;
                                try {
                                    location = object.getJSONObject("location");
                                    city = location.getString("name");
                                    Log.d("city", city);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                try {
                                    editor.putString("username", object.getString("name"));
                                    editor.putString("email", object.getString("email"));
                                    editor.putString("gender", object.getString("gender"));
                                    editor.putString("birthday", object.getString("birthday"));
                                    editor.putString("city", city);
                                    editor.apply();
                                    editor.commit();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,hometown,location");
                request.setParameters(parameters);
                request.executeAsync();
                try {
                    sendLoginRequest(sharedPreferences.getString("userid", ""), sharedPreferences.getString("accessToken", ""));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("LoginError", "Cannot send login request" + e);
                }
            }

            @Override
            public void onCancel() {
                Log.d("fbcancel", "fbcancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("fberror", "fberror");
            }
        });
    }

    private void sendLoginRequest(String userId, String token) throws JSONException {
        JSONObject body = new JSONObject();
        body.put("userID", userId);
        body.put("facebookAccessToken", token);

        JsonObjectRequest login_request = new JsonObjectRequest(Request.Method.POST,
                URL + "/auth/login",
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Send Login Request", "onResponse: " + response.toString());
                        if (response.has("twitter")) {
                            try {
                                editor.putString("twitter", response.getString("twitter"));
                                editor.apply();
                                editor.commit();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Send Login Request", "onErrorResponse: " + error);
                    }
                }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> responseHeaders = response.headers;
                String rawCookies = responseHeaders.get("Set-Cookie");
                Log.d("entered","entered");
                Log.e("Login Cookie", "parseNetworkResponse: " + rawCookies);
                editor.putString("cookies", rawCookies);
                editor.apply();
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                return super.parseNetworkResponse(response);
            }
        };
        Volley.newRequestQueue(this).add(login_request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void goToHomeIfLoggedIn() {
        if (sharedPreferences.contains("userid") && isLoggedIn && sharedPreferences.contains("accessToken")) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            Log.d("contains", sharedPreferences.getString("accessToken", ""));
        }
        else{
            Log.d("Not present","Not present");
        }
    }


}