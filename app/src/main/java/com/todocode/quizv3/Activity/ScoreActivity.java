package com.todocode.quizv3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codemybrainsout.ratingdialog.RatingDialog;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.todocode.quizv3.R;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {
    private TextView rightAnswers, falseAnswers, wastedPoints, earnedPoints, mPercentage, scoreTitle;
    private AdView bannerAdView;
    private LinearLayout adsLinear;
    private RequestQueue queue;
    private SharedPreferences userSituationId, facebookBanner, admobBanner, completedOption;
    private String userId, categoryId, categoryLevel, categoryName,passedTotalPoints;
    private Button retryBtn, shareScore, goToHome;
    private SharedPreferences bottomBannerType,adcolonyBanner,adcolonyInterstitial,adcolonyAppId,adcolonyReward,startappAppId, admobAppId;
    // AdColony Banner
    public AdColonyAdView adView;
    public AdColonyAdOptions adColonyAdOptionsBanner;
    public static String[] AD_UNIT_ZONE_Ids;
    Banner startAppBanner;
    private AdView bannerAdmobAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.app_lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        StartAppAd.disableSplash();
        Toolbar toolbar = findViewById(R.id.score_toolbar);
        toolbar.setTitle("Quiz Score");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        bottomBannerType = getSharedPreferences("bottomBannerType", MODE_PRIVATE);
        adcolonyBanner = getSharedPreferences("adcolonyBanner", MODE_PRIVATE);
        adcolonyInterstitial = getSharedPreferences("adcolonyInterstitial", MODE_PRIVATE);
        adcolonyAppId = getSharedPreferences("adcolonyAppId", MODE_PRIVATE);
        adcolonyReward = getSharedPreferences("adcolonyReward", MODE_PRIVATE);
        startappAppId = getSharedPreferences("startappAppId", MODE_PRIVATE);
        admobAppId = getSharedPreferences("admobAppId", MODE_PRIVATE);
        facebookBanner = getSharedPreferences("facebookBanner",MODE_PRIVATE);
        AD_UNIT_ZONE_Ids = new String[] {adcolonyBanner.getString("adcolonyBanner", ""), adcolonyInterstitial.getString("adcolonyInterstitial", ""), adcolonyReward.getString("adcolonyReward", "")};
        // Configure AdColony Ads
        AdColony.configure(ScoreActivity.this, adcolonyAppId.getString("adcolonyAppId", ""), AD_UNIT_ZONE_Ids);
        completedOption = getSharedPreferences("completedOption", Context.MODE_PRIVATE);
        queue = Volley.newRequestQueue(this);
        // Get Player Id
        userSituationId = getSharedPreferences("userId",MODE_PRIVATE);
        userId = userSituationId.getString("userId", "");
        rightAnswers = (TextView) findViewById(R.id.right_answers_number);
        falseAnswers = (TextView) findViewById(R.id.false_answers_number);
        wastedPoints = (TextView) findViewById(R.id.wasted_points);
        earnedPoints = (TextView) findViewById(R.id.earned_points);
        mPercentage = (TextView) findViewById(R.id.percentage);
        scoreTitle = (TextView) findViewById(R.id.score_title);
        retryBtn = (Button) findViewById(R.id.retry_again);
        shareScore = (Button) findViewById(R.id.share_score);
        goToHome = (Button) findViewById(R.id.go_home);
        Intent intent = getIntent();
        String passedEarnedPoints = intent.getStringExtra("earnedPoints");
        passedTotalPoints = intent.getStringExtra("totalPoints");
        categoryId = intent.getStringExtra("categoryId");
        categoryLevel = intent.getStringExtra("categoryLevel");
        categoryName = intent.getStringExtra("categoryName");
        int earnedPointsByPlayer = Integer.parseInt(passedEarnedPoints);
        int totalquizPoints = Integer.parseInt(passedTotalPoints);
        int wastedPointsByPlayer = totalquizPoints - earnedPointsByPlayer;
        earnedPoints.setText(passedEarnedPoints);
        wastedPoints.setText(String.valueOf(wastedPointsByPlayer));
        String allQuestionsNumber = intent.getStringExtra("allQuestions");
        String trueAnswers = intent.getStringExtra("trueAnswers");
        int numberOfTrueAnswers = Integer.parseInt(trueAnswers);
        int numberTotalOfQuestions = Integer.parseInt(allQuestionsNumber);
        int falseAnswersNumber = numberTotalOfQuestions - numberOfTrueAnswers;
        rightAnswers.setText(trueAnswers);
        falseAnswers.setText(String.valueOf(falseAnswersNumber));
        final int per = ((numberOfTrueAnswers*100)/numberTotalOfQuestions);
        mPercentage.setText(String.valueOf(per) + "%");
        if (per>49) {
            updatePlayerPoints();
            mPercentage.setTextColor(Color.GREEN);
            scoreTitle.setText(getString(R.string.java_question_score_success));
            shareScore.setVisibility(View.VISIBLE);
            if(completedOption.getString("completedOption", "").equals("yes")) {
                retryBtn.setVisibility(View.GONE);
            } else {
                retryBtn.setVisibility(View.VISIBLE);
            }
        } else {
            mPercentage.setTextColor(Color.RED);
            scoreTitle.setText(getString(R.string.java_question_score_failed));
            retryBtn.setVisibility(View.VISIBLE);
        }
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailsIntent = new Intent(ScoreActivity.this, CategoryActivity.class);
                detailsIntent.putExtra("categoryId", categoryId);
                detailsIntent.putExtra("categoryName", categoryName);
                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(detailsIntent);
                finish();
            }
        });
        goToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailsIntent = new Intent(ScoreActivity.this, MainActivity.class);
                detailsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(detailsIntent);
                finish();
            }
        });
        shareScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_TEXT, "I made "+ mPercentage.getText().toString() +" of true Answers in " + getResources().getString(R.string.app_name) + " http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName()+"\nDownload it & come to challenge me!");
                startActivity(Intent.createChooser(intent, "Share Score"));
            }
        });
        facebookBanner = getSharedPreferences("facebookBanner", Context.MODE_PRIVATE);
        // Admob Banner Bottom
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

    // Change Player Points
    public void updatePlayerPoints() {
        String updateUrl = getResources().getString(R.string.domain_name);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl+"/api/players/"+ userId +"/update", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Make quiz Completed
                if(completedOption.getString("completedOption", "").equals("yes")) {
                    makeQuizCompleted();
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
                params.put("points", earnedPoints.getText().toString());
                params.put("key", getResources().getString(R.string.api_secret_key));
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

    public void makeQuizCompleted(){
        String updateUrl = getResources().getString(R.string.domain_name);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUrl+"/api/quiz/passed/update", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                params.put("category_id", categoryId);
                params.put("category_level", categoryLevel);
                params.put("total_quiz_points", passedTotalPoints);
                params.put("points", earnedPoints.getText().toString());
                params.put("key", getResources().getString(R.string.api_secret_key));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent main = new Intent(ScoreActivity.this, MainActivity.class);
                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(main);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
