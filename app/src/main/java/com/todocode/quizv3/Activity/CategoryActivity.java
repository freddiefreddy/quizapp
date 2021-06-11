package com.todocode.quizv3.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.todocode.quizv3.Adapter.AllLevelsAdapter;
import com.todocode.quizv3.Model.Level;
import com.todocode.quizv3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CategoryActivity extends AppCompatActivity {

    SharedPreferences bottomBannerType, admobInterstitial, facebookInterstitial, facebookBanner, admobBanner, questionTime, userSituationId, completedOption;
    String categoryId, categoryName, categoryLevel, userId, bannerBottomType;
    TextView catName;
    private AdView bannerAdmobAdView;
//    private AllLevelsAdapter levelsAdapterA;
    private RequestQueue queue;
//    private Button easyBtn, mediumBtn, hardBtn, expertBtn, easyCompletedBtn, mediumCompletedBtn, hardCompletedBtn, expertCompletedBtn;
    private Button levelBtn, levelCompletedBtn;
    private SharedPreferences adcolonyBanner, adcolonyInterstitial, adcolonyAppId, adcolonyReward, admobAppId;
    // AdColony Banner
    private ArrayList<Level> levelsArrayList;

    private AllLevelsAdapter levelsAdapter;
    public AdColonyAdView adView;
    public AdColonyAdOptions adColonyAdOptionsBanner;
    public static String[] AD_UNIT_ZONE_Ids;
    Banner startAppBanner;
    private LinearLayout adsLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.app_lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        StartAppAd.disableSplash();
        Toolbar toolbar = findViewById(R.id.category_toolbar);
        Intent intent = getIntent();
        categoryId = intent.getStringExtra("categoryId");
        categoryName = intent.getStringExtra("categoryName");
        userSituationId = getSharedPreferences("userId", Context.MODE_PRIVATE);
        completedOption = getSharedPreferences("completedOption", Context.MODE_PRIVATE);
        userId = userSituationId.getString("userId", null);
        toolbar.setTitle(categoryName);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        queue = Volley.newRequestQueue(this);
        questionTime = getSharedPreferences("seconds", MODE_PRIVATE);
        bottomBannerType = getSharedPreferences("bottomBannerType", MODE_PRIVATE);
        adcolonyBanner = getSharedPreferences("adcolonyBanner", MODE_PRIVATE);
        adcolonyInterstitial = getSharedPreferences("adcolonyInterstitial", MODE_PRIVATE);
        adcolonyAppId = getSharedPreferences("adcolonyAppId", MODE_PRIVATE);
        adcolonyReward = getSharedPreferences("adcolonyReward", MODE_PRIVATE);
        admobAppId = getSharedPreferences("admobAppId", MODE_PRIVATE);
        facebookBanner = getSharedPreferences("facebookBanner",MODE_PRIVATE);
        admobBanner = getSharedPreferences("admobBanner",MODE_PRIVATE);
        MobileAds.initialize(CategoryActivity.this, admobAppId.getString("admobAppId", ""));
        // Banner AD
        AD_UNIT_ZONE_Ids = new String[] {adcolonyBanner.getString("adcolonyBanner", ""), adcolonyInterstitial.getString("adcolonyInterstitial", ""), adcolonyReward.getString("adcolonyReward", "")};
        // Configure AdColony Ads
        AdColony.configure(CategoryActivity.this, adcolonyAppId.getString("adcolonyAppId", ""), AD_UNIT_ZONE_Ids);
        // Set Data
        catName = (TextView) findViewById(R.id.category_name);
        levelsArrayList = new ArrayList<>();
        catName.setText(categoryName);
        levelBtn = (Button) findViewById(R.id.level_btn);
        levelCompletedBtn = (Button) findViewById(R.id.level_completed_btn);
//        easyBtn = (Button) findViewById(R.id.easy_btn);
//        mediumBtn = (Button) findViewById(R.id.medium_btn);
//        hardBtn = (Button) findViewById(R.id.hard_btn);
//        expertBtn = (Button) findViewById(R.id.expert_btn);
//        easyCompletedBtn = (Button) findViewById(R.id.easy_completed_btn);
//        mediumCompletedBtn = (Button) findViewById(R.id.medium_completed_btn);
//        hardCompletedBtn = (Button) findViewById(R.id.hard_completed_btn);
//        expertCompletedBtn = (Button) findViewById(R.id.expert_completed_btn);
        if(completedOption.getString("completedOption", "").equals("yes")) {
            // Completed Option Activated
            checkifLevelIsCompletedAndContainsQuestions();
//            checkifEasyLevelIsCompletedAndContainsQuestions();
//            checkifMediumLevelIsCompletedAndContainsQuestions();
//            checkifHardLevelIsCompletedAndContainsQuestions();
//            checkifExpertLevelIsCompletedAndContainsQuestions();
        } else {
            // Completed Option Deactivated
            checkifLevelContainsQuestions();
//            checkifEasyContainsQuestions();
//            checkifMediumContainsQuestions();
//            checkifHardContainsQuestions();
//            checkifExpertContainsQuestions();
        }
        getAllLevels();
        final String bannerBottomType = bottomBannerType.getString("bottomBannerType", "");
        if (bannerBottomType.equals("admob")) {
            admobBanner = getSharedPreferences("admobBanner",MODE_PRIVATE);
            adsLinear = (LinearLayout) findViewById(R.id.banner_container_profile_activity);
            adsLinear.setVisibility(View.VISIBLE);
            bannerAdmobAdView = new AdView(this);
            bannerAdmobAdView.setAdUnitId(admobBanner.getString("admobBanner", ""));
            bannerAdmobAdView.setAdSize(com.google.android.gms.ads.AdSize.FULL_BANNER);
            adsLinear.addView(bannerAdmobAdView);
            adsLinear.setGravity(Gravity.CENTER_HORIZONTAL);
            AdRequest adRequest = new AdRequest.Builder().build();
            bannerAdmobAdView.loadAd(adRequest);
            bannerAdmobAdView.setAdListener(new AdListener(){
                @Override
                public void onAdLoaded() {
                    adsLinear.setVisibility(View.VISIBLE);
                }
            });
        } else if(bannerBottomType.equals("facebook")) {
            AudienceNetworkAds.initialize(this);
            com.facebook.ads.AdView facebookAdView = new com.facebook.ads.AdView(this, facebookBanner.getString("facebookBanner", null), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container_profile_activity);
            adContainer.setVisibility(View.VISIBLE);
            adContainer.addView(facebookAdView);
            AdSettings.addTestDevice("c377b922-aa02-4425-a50b-af7a1f1ee1b6");
            facebookAdView.loadAd();
        }else if(bannerBottomType.equals("adcolony")) {
            final LinearLayout linearBannerAdContainer = (LinearLayout) findViewById(R.id.banner_container_profile_activity);
            linearBannerAdContainer.setVisibility(View.VISIBLE);
            AdColonyAdViewListener listener1 = new AdColonyAdViewListener() {
                @Override
                public void onRequestFilled(AdColonyAdView adColonyAdView) {
                    linearBannerAdContainer.addView(adColonyAdView);
                    adView = adColonyAdView;
                }

            };
            AdColony.requestAdView(adcolonyBanner.getString("adcolonyBanner", ""), listener1, AdColonyAdSize.BANNER, adColonyAdOptionsBanner);
        }else if(bannerBottomType.equals("startapp")) {
            startAppBanner = (Banner) findViewById(R.id.startapp_banner);
            startAppBanner.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getAllLevels() {
//        String allUrl = url+"/api/levels/all";
        String updateUrl = getResources().getString(R.string.domain_name);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, updateUrl+"/api/levels/all", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject level= jsonArray.getJSONObject(i);
                                String name = level.getString("name");
                                String details = level.getString("details");
                                levelsArrayList.add(new Level(name, details));
                            }
                            levelsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }




    private void checkifLevelContainsQuestions() {
        String updateUrl = getResources().getString(R.string.domain_name);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl+"/api/quiz/completed/check", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    // No Errors
                    if (success.equals("quizCompleted")){
                        levelCompletedBtn.setVisibility(View.VISIBLE);
                    } else {
                        levelBtn.setVisibility(View.VISIBLE);
                        levelBtn.setEnabled(true);
                        levelBtn.setClickable(true);
                        levelBtn.setFocusable(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("player_id", userId);
                params.put("key", getResources().getString(R.string.api_secret_key));
                params.put("category_id", categoryId);
                params.put("category_level", categoryLevel);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }


        private void checkifLevelIsCompletedAndContainsQuestions() {
        String updateUrl = getResources().getString(R.string.domain_name);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl+"/api/quiz/level/check/questions", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    // No Errors
                    if (success.equals("questionsExists")){
                        checkIfLevelIsCompleted();
                        // On Click Listeners (Levels)
                        levelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = getResources().getString(R.string.domain_name)+"/api/category/questions/level";
                                Intent questionIntent = new Intent(CategoryActivity.this, QuestionActivity.class);
                                questionIntent.putExtra("url", url);
                                questionIntent.putExtra("categoryId", String.valueOf(categoryId));
                                questionIntent.putExtra("categoryLevel", String.valueOf(categoryLevel));
                                questionIntent.putExtra("categoryName", catName.getText().toString());
                                questionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(questionIntent);
                                finish();
                            }
                        });
                    }
                    if (success.equals("noQuestions")) {
                        levelBtn.setVisibility(View.GONE);
                        levelCompletedBtn.setVisibility(View.VISIBLE);
                        levelCompletedBtn.setEnabled(false);
                        levelCompletedBtn.setText(getString(R.string.java_easy_no_question));
                        levelCompletedBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.empty, 0, 0, 0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("category_id", categoryId);
                params.put("key", getResources().getString(R.string.api_secret_key));
                params.put("level", categoryLevel);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void checkIfLevelIsCompleted() {
        String updateUrl = getResources().getString(R.string.domain_name);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl+"/api/quiz/completed/check", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    // No Errors
                    if (success.equals("quizCompleted")){
                        levelCompletedBtn.setVisibility(View.VISIBLE);
                    } else {
                        levelBtn.setVisibility(View.VISIBLE);
                        levelBtn.setEnabled(true);
                        levelBtn.setClickable(true);
                        levelBtn.setFocusable(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("player_id", userId);
                params.put("key", getResources().getString(R.string.api_secret_key));
                params.put("category_id", categoryId);
                params.put("category_level", categoryLevel);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

//    public void checkIfEasyLevelIsCompleted() {
//        String updateUrl = getResources().getString(R.string.domain_name);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl+"/api/quiz/completed/check", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
//                    // No Errors
//                    if (success.equals("quizCompleted")){
//                        easyCompletedBtn.setVisibility(View.VISIBLE);
//                    } else {
//                        easyBtn.setVisibility(View.VISIBLE);
//                        easyBtn.setEnabled(true);
//                        easyBtn.setClickable(true);
//                        easyBtn.setFocusable(true);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("player_id", userId);
//                params.put("key", getResources().getString(R.string.api_secret_key));
//                params.put("category_id", categoryId);
//                params.put("category_level", categoryLevel);
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);
//    }
//
//    public void checkIfMediumLevelIsCompleted() {
//        String updateUrl = getResources().getString(R.string.domain_name);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl+"/api/quiz/completed/check", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
//                    // No Errors
//                    if (success.equals("quizCompleted")){
//                        mediumCompletedBtn.setVisibility(View.VISIBLE);
//                    } else if (success.equals("no")){
//                        mediumBtn.setVisibility(View.VISIBLE);
//                        mediumBtn.setEnabled(true);
//                        mediumBtn.setClickable(true);
//                        mediumBtn.setFocusable(true);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("player_id", userId);
//                params.put("key", getResources().getString(R.string.api_secret_key));
//                params.put("category_id", categoryId);
//                params.put("category_level", "medium");
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);
//    }

//    public void checkIfHardLevelIsCompleted() {
//        String updateUrl = getResources().getString(R.string.domain_name);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl+"/api/quiz/completed/check", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
//                    // No Errors
//                    if (success.equals("quizCompleted")){
//                        hardCompletedBtn.setVisibility(View.VISIBLE);
//                    } else {
//                        hardBtn.setVisibility(View.VISIBLE);
//                        hardBtn.setEnabled(true);
//                        hardBtn.setClickable(true);
//                        hardBtn.setFocusable(true);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("player_id", userId);
//                params.put("key", getResources().getString(R.string.api_secret_key));
//                params.put("category_id", categoryId);
//                params.put("category_level", "hard");
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);
//    }

//    public void checkIfExpertLevelIsCompleted() {
//        String updateUrl = getResources().getString(R.string.domain_name);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl+"/api/quiz/completed/check", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
//                    // No Errors
//                    if (success.equals("quizCompleted")){
//                        expertCompletedBtn.setVisibility(View.VISIBLE);
//                    } else {
//                        expertBtn.setVisibility(View.VISIBLE);
//                        expertBtn.setEnabled(true);
//                        expertBtn.setClickable(true);
//                        expertBtn.setFocusable(true);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("player_id", userId);
//                params.put("key", getResources().getString(R.string.api_secret_key));
//                params.put("category_id", categoryId);
//                params.put("category_level", "expert");
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);
//    }

//    private void checkifEasyLevelIsCompletedAndContainsQuestions() {
//        String updateUrl = getResources().getString(R.string.domain_name);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl+"/api/quiz/level/check/questions", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
//                    // No Errors
//                    if (success.equals("questionsExists")){
//                        checkIfEasyLevelIsCompleted();
//                        // On Click Listeners (Levels)
//                        easyBtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                String url = getResources().getString(R.string.domain_name)+"/api/categories/" + categoryId + "/questions/easy";
//                                Intent questionIntent = new Intent(CategoryActivity.this, QuestionActivity.class);
//                                questionIntent.putExtra("url", url);
//                                questionIntent.putExtra("categoryId", String.valueOf(categoryId));
//                                questionIntent.putExtra("categoryLevel", "easy");
//                                questionIntent.putExtra("categoryName", catName.getText().toString());
//                                questionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(questionIntent);
//                                finish();
//                            }
//                        });
//                    }
//                    if (success.equals("noQuestions")) {
//                        easyBtn.setVisibility(View.GONE);
//                        easyCompletedBtn.setVisibility(View.VISIBLE);
//                        easyCompletedBtn.setEnabled(false);
//                        easyCompletedBtn.setText(getString(R.string.java_easy_no_question));
//                        easyCompletedBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.empty, 0, 0, 0);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("category_id", categoryId);
//                params.put("key", getResources().getString(R.string.api_secret_key));
//                params.put("level", "easy");
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);
//    }

//    private void checkifMediumLevelIsCompletedAndContainsQuestions() {
//        String updateUrl = getResources().getString(R.string.domain_name);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl+"/api/quiz/level/check/questions", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
//                    // No Errors
//                    if (success.equals("questionsExists")){
//                        checkIfMediumLevelIsCompleted();
//                        mediumBtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                String url = getResources().getString(R.string.domain_name)+"/api/categories/" + categoryId + "/questions/medium";
//                                Intent questionIntent = new Intent(CategoryActivity.this, QuestionActivity.class);
//                                questionIntent.putExtra("url", url);
//                                questionIntent.putExtra("categoryId", String.valueOf(categoryId));
//                                questionIntent.putExtra("categoryName", catName.getText().toString());
//                                questionIntent.putExtra("categoryLevel", "medium");
//                                questionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(questionIntent);
//                                finish();
//                            }
//                        });
//                    }
//                    if (success.equals("noQuestions")){
//                        mediumBtn.setVisibility(View.GONE);
//                        mediumCompletedBtn.setEnabled(false);
//                        mediumCompletedBtn.setText(getString(R.string.java_medium_no_question));
//                        mediumCompletedBtn.setTextSize(18);
//                        mediumCompletedBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.empty, 0, 0, 0);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("category_id", categoryId);
//                params.put("key", getResources().getString(R.string.api_secret_key));
//                params.put("level", "medium");
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);
//    }
//
//    private void checkifHardLevelIsCompletedAndContainsQuestions() {
//        String updateUrl = getResources().getString(R.string.domain_name);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl+"/api/quiz/level/check/questions", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
//                    // No Errors
//                    if (success.equals("questionsExists")){
//                        checkIfHardLevelIsCompleted();
//                        hardBtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                String url = getResources().getString(R.string.domain_name)+"/api/categories/" + categoryId + "/questions/hard";
//                                Intent questionIntent = new Intent(CategoryActivity.this, QuestionActivity.class);
//                                questionIntent.putExtra("url", url);
//                                questionIntent.putExtra("categoryId", String.valueOf(categoryId));
//                                questionIntent.putExtra("categoryLevel", "hard");
//                                questionIntent.putExtra("categoryName", catName.getText().toString());
//                                questionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(questionIntent);
//                                finish();
//                            }
//                        });
//                    }
//                    if (success.equals("noQuestions")) {
//                        hardBtn.setVisibility(View.GONE);
//                        hardCompletedBtn.setEnabled(false);
//                        hardCompletedBtn.setText(getString(R.string.java_hard_no_question));
//                        hardCompletedBtn.setTextSize(18);
//                        hardCompletedBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.empty, 0, 0, 0);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("category_id", categoryId);
//                params.put("key", getResources().getString(R.string.api_secret_key));
//                params.put("level", "hard");
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);
//    }

//    private void checkifExpertLevelIsCompletedAndContainsQuestions() {
//        String updateUrl = getResources().getString(R.string.domain_name);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl+"/api/quiz/level/check/questions", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
//                    // No Errors
//                    if (success.equals("questionsExists")){
//                        checkIfExpertLevelIsCompleted();
//                        expertBtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                String url = getResources().getString(R.string.domain_name)+"/api/categories/" + categoryId + "/questions/expert";
//                                Intent questionIntent = new Intent(CategoryActivity.this, QuestionActivity.class);
//                                questionIntent.putExtra("url", url);
//                                questionIntent.putExtra("categoryId", String.valueOf(categoryId));
//                                questionIntent.putExtra("categoryLevel", "expert");
//                                questionIntent.putExtra("categoryName", catName.getText().toString());
//                                questionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(questionIntent);
//                                finish();
//                            }
//                        });
//                    } if (success.equals("noQuestions")) {
//                        expertBtn.setVisibility(View.GONE);
//                        expertCompletedBtn.setEnabled(false);
//                        expertCompletedBtn.setText(getString(R.string.java_expert_no_question));
//                        expertCompletedBtn.setTextSize(18);
//                        expertCompletedBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.empty, 0, 0, 0);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("category_id", categoryId);
//                params.put("key", getResources().getString(R.string.api_secret_key));
//                params.put("level", "expert");
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);
//    }

//    private void checkifEasyContainsQuestions() {
//        String updateUrl = getResources().getString(R.string.domain_name);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl+"/api/quiz/level/check/questions", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                easyBtn.setVisibility(View.VISIBLE);
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
//                    // No Errors
//                    if (success.equals("questionsExists")){
//                        // On Click Listeners (Levels)
//                        easyBtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                String url = getResources().getString(R.string.domain_name)+"/api/categories/" + categoryId + "/questions/easy";
//                                Intent questionIntent = new Intent(CategoryActivity.this, QuestionActivity.class);
//                                questionIntent.putExtra("url", url);
//                                questionIntent.putExtra("categoryId", String.valueOf(categoryId));
//                                questionIntent.putExtra("categoryLevel", "easy");
//                                questionIntent.putExtra("categoryName", catName.getText().toString());
//                                questionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(questionIntent);
//                                finish();
//                            }
//                        });
//                    }
//                    if (success.equals("noQuestions")) {
//                        easyBtn.setVisibility(View.GONE);
//                        easyCompletedBtn.setVisibility(View.VISIBLE);
//                        easyCompletedBtn.setEnabled(false);
//                        easyCompletedBtn.setText(getString(R.string.java_easy_no_question));
//                        easyCompletedBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.empty, 0, 0, 0);
//                        easyCompletedBtn.setTextSize(18);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("category_id", categoryId);
//                params.put("key", getResources().getString(R.string.api_secret_key));
//                params.put("level", "easy");
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);
//    }
//
//    private void checkifMediumContainsQuestions() {
//        String updateUrl = getResources().getString(R.string.domain_name);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl+"/api/quiz/level/check/questions", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                mediumBtn.setVisibility(View.VISIBLE);
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
//                    // No Errors
//                    if (success.equals("questionsExists")){
//                        mediumBtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                String url = getResources().getString(R.string.domain_name)+"/api/categories/" + categoryId + "/questions/medium";
//                                Intent questionIntent = new Intent(CategoryActivity.this, QuestionActivity.class);
//                                questionIntent.putExtra("url", url);
//                                questionIntent.putExtra("categoryId", String.valueOf(categoryId));
//                                questionIntent.putExtra("categoryName", catName.getText().toString());
//                                questionIntent.putExtra("categoryLevel", "medium");
//                                questionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(questionIntent);
//                                finish();
//                            }
//                        });
//                    }
//                    if (success.equals("noQuestions")){
//                        mediumBtn.setVisibility(View.GONE);
//                        mediumCompletedBtn.setVisibility(View.VISIBLE);
//                        mediumCompletedBtn.setEnabled(false);
//                        mediumCompletedBtn.setText(getString(R.string.java_medium_no_question));
//                        mediumCompletedBtn.setTextSize(18);
//                        mediumCompletedBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.empty, 0, 0, 0);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("category_id", categoryId);
//                params.put("key", getResources().getString(R.string.api_secret_key));
//                params.put("level", "medium");
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);
//    }

//    private void checkifHardContainsQuestions() {
//        String updateUrl = getResources().getString(R.string.domain_name);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl+"/api/quiz/level/check/questions", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                hardBtn.setVisibility(View.VISIBLE);
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
//                    // No Errors
//                    if (success.equals("questionsExists")){
//                        hardBtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                String url = getResources().getString(R.string.domain_name)+"/api/categories/" + categoryId + "/questions/hard";
//                                Intent questionIntent = new Intent(CategoryActivity.this, QuestionActivity.class);
//                                questionIntent.putExtra("url", url);
//                                questionIntent.putExtra("categoryId", String.valueOf(categoryId));
//                                questionIntent.putExtra("categoryLevel", "hard");
//                                questionIntent.putExtra("categoryName", catName.getText().toString());
//                                questionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(questionIntent);
//                                finish();
//                            }
//                        });
//                    }
//                    if (success.equals("noQuestions")) {
//                        hardBtn.setVisibility(View.GONE);
//                        hardCompletedBtn.setVisibility(View.VISIBLE);
//                        hardCompletedBtn.setEnabled(false);
//                        hardCompletedBtn.setText(getString(R.string.java_hard_no_question));
//                        hardCompletedBtn.setTextSize(18);
//                        hardCompletedBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.empty, 0, 0, 0);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("category_id", categoryId);
//                params.put("key", getResources().getString(R.string.api_secret_key));
//                params.put("level", "hard");
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);
//    }

//    private void checkifExpertContainsQuestions() {
//        String updateUrl = getResources().getString(R.string.domain_name);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl+"/api/quiz/level/check/questions", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                expertBtn.setVisibility(View.VISIBLE);
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(response);
//                    String success = jsonObject.getString("success");
//                    // No Errors
//                    if (success.equals("questionsExists")){
//                        expertBtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                String url = getResources().getString(R.string.domain_name)+"/api/categories/" + categoryId + "/questions/expert";
//                                Intent questionIntent = new Intent(CategoryActivity.this, QuestionActivity.class);
//                                questionIntent.putExtra("url", url);
//                                questionIntent.putExtra("categoryId", String.valueOf(categoryId));
//                                questionIntent.putExtra("categoryLevel", "expert");
//                                questionIntent.putExtra("categoryName", catName.getText().toString());
//                                questionIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(questionIntent);
//                                finish();
//                            }
//                        });
//                    } if (success.equals("noQuestions")) {
//                        expertBtn.setVisibility(View.GONE);
//                        expertCompletedBtn.setVisibility(View.VISIBLE);
//                        expertCompletedBtn.setEnabled(false);
//                        expertCompletedBtn.setText(getString(R.string.java_expert_no_question));
//                        expertCompletedBtn.setTextSize(18);
//                        expertCompletedBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.empty, 0, 0, 0);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("category_id", categoryId);
//                params.put("key", getResources().getString(R.string.api_secret_key));
//                params.put("level", "expert");
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                10000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);
//    }
}
