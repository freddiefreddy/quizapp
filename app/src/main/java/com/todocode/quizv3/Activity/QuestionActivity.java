package com.todocode.quizv3.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyReward;
import com.adcolony.sdk.AdColonyRewardListener;
import com.adcolony.sdk.AdColonyZone;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.VideoListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.todocode.quizv3.Model.Question;
import com.todocode.quizv3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {
    private TextView timer, numberOfQuestion, theQuestion, questionPoints;
    private String bannerBottomType, url, categoryId, categoryName, categoryLevel, rightAnswer, falseAnswerOne, falseAnswerTwo, submittedAnswer, userId;
    private int allQuestions, i, questionEarningPoints;
    private RequestQueue queue;
    private ArrayList<Question> questions;
    private RadioGroup mRadioGroup;
    private RadioButton answer1, answer2, answer3, answer4, selectedRadioButton;
    private Button submitBtn, finishBtn, fiftyFifty, rewardVideoBtn;
    private CountDownTimer countDown;
    private AdView bannerAdView;
    private LinearLayout adsLinear;
    private int collectedPoints, actualQuestionNum;
    private SharedPreferences userSituationId, points;
    private int earnedPoints, totalQuizPoints, rightAnswersNumber, seconds;
    private RewardedVideoAd rewardedVideoAd;
    private boolean gameOver;
    private boolean gamePaused;
    private int rewarded;
    private SharedPreferences fiftyFiftyOption, rewardVideoOption, questionTime, userSituation, admobBanner, bottomBannerType, facebookBanner, facebookNative, admobVideo, admobInterstitial, facebookInterstitial;
    private NativeAdLayout nativeAdLayout;
    private NativeBannerAd nativeBannerAd;
    private LinearLayout adView;
    private AdView bannerAdmobAdView;
    private com.facebook.ads.InterstitialAd facebookInterstitialAd;
    private InterstitialAd mInterstitialAd;
    static boolean active = false;
    private SharedPreferences adcolonyBanner, adcolonyInterstitial, adcolonyAppId, adcolonyReward, admobAppId;
    // AdColony Banner
    public AdColonyAdView adsView;
    public AdColonyAdOptions adColonyAdOptionsBanner;
    public static String[] AD_UNIT_ZONE_Ids;
    Banner startAppBanner;
    private LinearLayout adssLinear;
    private SharedPreferences videoTypeShared, FbVideo, interstitialTypeShared;
    Button clickReward;
    public AdColonyInterstitial rewardAdColony;
    public AdColonyInterstitialListener rewardListener;
    public AdColonyAdOptions adColonyAdOptionsReward;
    private static boolean isRewardLoaded;
    private com.facebook.ads.RewardedVideoAd fbrewardedVideoAd;
    private String TAG = "video";
    public AdColonyInterstitial adColonyInterstitiall;
    public AdColonyAdOptions adColonyAdOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.app_lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        StartAppAd.disableSplash();
        videoTypeShared = getSharedPreferences("videoTypeShared", MODE_PRIVATE);
        FbVideo = getSharedPreferences("FbVideo", MODE_PRIVATE);
        userSituation = getSharedPreferences("userEmail",MODE_PRIVATE);
        userSituationId = getSharedPreferences("userId", MODE_PRIVATE);
        admobBanner = getSharedPreferences("admobBanner",MODE_PRIVATE);
        interstitialTypeShared = getSharedPreferences("interstitialTypeShared",MODE_PRIVATE);
        bottomBannerType = getSharedPreferences("bottomBannerType",MODE_PRIVATE);
        admobInterstitial = getSharedPreferences("admobInterstitial",MODE_PRIVATE);
        facebookBanner = getSharedPreferences("facebookBanner",MODE_PRIVATE);
        facebookNative = getSharedPreferences("facebookNative",MODE_PRIVATE);
        facebookInterstitial = getSharedPreferences("facebookInterstitial",MODE_PRIVATE);
        admobVideo = getSharedPreferences("admobVideo",MODE_PRIVATE);
        questionTime = getSharedPreferences("seconds", MODE_PRIVATE);
        fiftyFiftyOption = getSharedPreferences("fiftyFiftyOption", Context.MODE_PRIVATE);
        rewardVideoOption = getSharedPreferences("rewardVideoOption", Context.MODE_PRIVATE);
        collectedPoints = 0;
        earnedPoints = 0;
        totalQuizPoints = 0;
        rightAnswersNumber = 0;
        nativeAdLayout = findViewById(R.id.question_native_banner_ad_container);
        seconds = questionTime.getInt("seconds", 0);
        userId = userSituationId.getString("userId", null);
        points = getSharedPreferences("points", MODE_PRIVATE);
        points.edit().putInt("points", 0).apply();
        queue = Volley.newRequestQueue(this);
        // Rewarded Video Ads
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        loadRewardedVideoAd();
        // Load Facebook Video Ads
        // Admob Banner Bottom
        bottomBannerType = getSharedPreferences("bottomBannerType",MODE_PRIVATE);
        bannerBottomType = bottomBannerType.getString("bottomBannerType", "");
        bottomBannerType = getSharedPreferences("bottomBannerType", MODE_PRIVATE);
        adcolonyBanner = getSharedPreferences("adcolonyBanner", MODE_PRIVATE);
        adcolonyInterstitial = getSharedPreferences("adcolonyInterstitial", MODE_PRIVATE);
        adcolonyAppId = getSharedPreferences("adcolonyAppId", MODE_PRIVATE);
        adcolonyReward = getSharedPreferences("adcolonyReward", MODE_PRIVATE);
        admobAppId = getSharedPreferences("admobAppId", MODE_PRIVATE);
        facebookBanner = getSharedPreferences("facebookBanner",MODE_PRIVATE);
        admobBanner = getSharedPreferences("admobBanner",MODE_PRIVATE);
        MobileAds.initialize(QuestionActivity.this, admobAppId.getString("admobAppId", ""));
        if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("facebook")) {
            prepareInterstitialAd();
        } else if(interstitialTypeShared.getString("interstitialTypeShared", "").equals("admob")) {
            prepareInterstitialAdmobAd();
        }
        // Banner AD
        AD_UNIT_ZONE_Ids = new String[] {adcolonyBanner.getString("adcolonyBanner", ""), adcolonyInterstitial.getString("adcolonyInterstitial", ""), adcolonyReward.getString("adcolonyReward", "")};
        // Configure AdColony Ads
        AdColony.configure(QuestionActivity.this, adcolonyAppId.getString("adcolonyAppId", ""), AD_UNIT_ZONE_Ids);
        final String bannerBottomType = bottomBannerType.getString("bottomBannerType", "");
        if (bannerBottomType.equals("admob")) {
            admobBanner = getSharedPreferences("admobBanner",MODE_PRIVATE);
            adssLinear = (LinearLayout) findViewById(R.id.banner_container_profile_activity);
            adssLinear.setVisibility(View.VISIBLE);
            bannerAdmobAdView = new AdView(this);
            bannerAdmobAdView.setAdUnitId(admobBanner.getString("admobBanner", ""));
            bannerAdmobAdView.setAdSize(com.google.android.gms.ads.AdSize.FULL_BANNER);
            adssLinear.addView(bannerAdmobAdView);
            adssLinear.setGravity(Gravity.CENTER_HORIZONTAL);
            AdRequest adRequest = new AdRequest.Builder().build();
            bannerAdmobAdView.loadAd(adRequest);
            bannerAdmobAdView.setAdListener(new AdListener(){
                @Override
                public void onAdLoaded() {
                    adssLinear.setVisibility(View.VISIBLE);
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
                    adsView = adColonyAdView;
                }

            };
            AdColony.requestAdView(adcolonyBanner.getString("adcolonyBanner", ""), listener1, AdColonyAdSize.BANNER, adColonyAdOptionsBanner);
        }else if(bannerBottomType.equals("startapp")) {
            startAppBanner = (Banner) findViewById(R.id.startapp_banner);
            startAppBanner.setVisibility(View.VISIBLE);
        }
        // Questions
        questions = new ArrayList<>();
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        answer1 = (RadioButton) findViewById(R.id.answer1);
        answer2 = (RadioButton) findViewById(R.id.answer2);
        answer3 = (RadioButton) findViewById(R.id.answer3);
        answer4 = (RadioButton) findViewById(R.id.answer4);
        questionPoints = (TextView) findViewById(R.id.question_points);
        submitBtn = (Button) findViewById(R.id.submit);
        finishBtn = (Button) findViewById(R.id.finish);
        fiftyFifty = (Button) findViewById(R.id.fifty_fifty);
        rewardVideoBtn = (Button) findViewById(R.id.reward_video);
        timer = (TextView) findViewById(R.id.timer);
        numberOfQuestion = (TextView) findViewById(R.id.number_of_question);
        theQuestion = (TextView) findViewById(R.id.question);
        if (fiftyFiftyOption.getString("fiftyFiftyOption", "").equals("yes")) {
            fiftyFifty.setVisibility(View.VISIBLE);
        }
        if (rewardVideoOption.getString("rewardVideoOption", "").equals("yes")) {
            rewardVideoBtn.setVisibility(View.VISIBLE);
        }
        countDown = new CountDownTimer(seconds*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(millisUntilFinished / 1000));
            }
            public void onFinish() {
                // If Time is Finished --> submit button Automatically
                timer.setText(getString(R.string.java_question_time_finished));
                if (mRadioGroup.getCheckedRadioButtonId() == -1) {
                    if (actualQuestionNum == 0) {
                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.wrong);
                            mp.start();
                        AlertDialog.Builder alertDialogg = new AlertDialog.Builder(QuestionActivity.this)
                                    .setTitle(getString(R.string.java_question_time_finished_title))
                                    .setMessage(getString(R.string.java_question_time_finished_message))
                                    .setPositiveButton(getString(R.string.java_question_show_my_score), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            goToScore();
                                        }
                                    })
                                    .setIcon(getDrawable(R.drawable.timer))
                                    .setCancelable(false);
                        try {
                            alertDialogg.show();
                        }
                        catch (WindowManager.BadTokenException e) {
                            e.getMessage();
                        }
                    } else {
                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.wrong);
                            mp.start();
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(QuestionActivity.this)
                                    .setTitle(getString(R.string.java_question_time_finished_title))
                                    .setMessage(getString(R.string.java_question_time_finished_message))
                                    .setPositiveButton(getString(R.string.java_question_next_question_title), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Go to next question
                                            getNextQuestion();
                                        }
                                    })
                                    .setIcon(getDrawable(R.drawable.ic_warning))
                                    .setCancelable(false);
                            try {
                                alertDialog.show();
                            }
                            catch (WindowManager.BadTokenException e) {
                                e.getMessage();
                            }
                    }
                } else {
                    submitBtn.performClick();
                }
            }
        }.start();
        // Get the API url from Intent
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        categoryId = intent.getStringExtra("categoryId");
        categoryLevel = intent.getStringExtra("categoryLevel");
        categoryName = intent.getStringExtra("categoryName");
        // Launch Request & get Data & parse JSON
        parseJson();
        // Fifty Fifty Option
        answer1.setVisibility(View.VISIBLE);
        answer2.setVisibility(View.VISIBLE);
        answer3.setVisibility(View.VISIBLE);
        answer4.setVisibility(View.VISIBLE);
        fiftyFifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fiftyPoints = (questionEarningPoints/2);
                questionEarningPoints = (questionEarningPoints/2);
                questionPoints.setText(" Points : "+String.valueOf(fiftyPoints));
                questionPoints.setCompoundDrawablesWithIntrinsicBounds( R.drawable.icon_50, 0, 0, 0);
                if (answer1.getText().toString().equalsIgnoreCase(falseAnswerOne)) {
                    answer1.setVisibility(View.GONE);
                }
                if (answer1.getText().toString().equalsIgnoreCase(falseAnswerTwo)) {
                    answer1.setVisibility(View.GONE);
                }
                if (answer2.getText().toString().equalsIgnoreCase(falseAnswerOne)) {
                    answer2.setVisibility(View.GONE);
                }
                if (answer2.getText().toString().equalsIgnoreCase(falseAnswerTwo)) {
                    answer2.setVisibility(View.GONE);
                }
                if (answer3.getText().toString().equalsIgnoreCase(falseAnswerOne)) {
                    answer3.setVisibility(View.GONE);
                }
                if (answer3.getText().toString().equalsIgnoreCase(falseAnswerTwo)) {
                    answer3.setVisibility(View.GONE);
                }
                if (answer4.getText().toString().equalsIgnoreCase(falseAnswerOne)) {
                    answer4.setVisibility(View.GONE);
                }
                if (answer4.getText().toString().equalsIgnoreCase(falseAnswerTwo)) {
                    answer4.setVisibility(View.GONE);
                }
                Toast.makeText(QuestionActivity.this, "50/50 Mode Activated!", Toast.LENGTH_SHORT).show();
                fiftyFifty.setVisibility(View.GONE);
                rewardVideoBtn.setVisibility(View.GONE);
            }
        });
        // Facebook Video Ad Loading
        AudienceNetworkAds.initialize(QuestionActivity.this);
        AdSettings.addTestDevice("c377b922-aa02-4425-a50b-af7a1f1ee1b6");
        fbrewardedVideoAd = new com.facebook.ads.RewardedVideoAd(QuestionActivity.this, FbVideo.getString("FbVideo", ""));
        final com.facebook.ads.RewardedVideoAdListener rewardedVideoAdListener = new com.facebook.ads.RewardedVideoAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                // Rewarded video ad failed to load
                Log.e(TAG, "Rewarded video ad failed to load: " + error.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                fbrewardedVideoAd.show();
                // Rewarded video ad is loaded and ready to be displayed
                Log.d(TAG, "Rewarded video ad is loaded and ready to be displayed!");
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Rewarded video ad clicked
                Log.d(TAG, "Rewarded video ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Rewarded Video ad impression - the event will fire when the
                // video starts playing
                Log.d(TAG, "Rewarded video ad impression logged!");
            }

            @Override
            public void onRewardedVideoCompleted() {
                // Rewarded Video View Complete - the video has been played to the end.
                // You can use this event to initialize your reward
                Log.d(TAG, "Rewarded video completed!");
                // Call method to give reward
                // giveReward();
            }

            @Override
            public void onRewardedVideoClosed() {
                // The Rewarded Video ad was closed - this can occur during the video
                // by closing the app, or closing the end card.
                Log.d(TAG, "Rewarded video ad closed!");
                if (answer1.getText().toString().equalsIgnoreCase(falseAnswerOne)) {
                    answer1.setVisibility(View.GONE);
                }
                if (answer1.getText().toString().equalsIgnoreCase(falseAnswerTwo)) {
                    answer1.setVisibility(View.GONE);
                }
                if (answer2.getText().toString().equalsIgnoreCase(falseAnswerOne)) {
                    answer2.setVisibility(View.GONE);
                }
                if (answer2.getText().toString().equalsIgnoreCase(falseAnswerTwo)) {
                    answer2.setVisibility(View.GONE);
                }
                if (answer3.getText().toString().equalsIgnoreCase(falseAnswerOne)) {
                    answer3.setVisibility(View.GONE);
                }
                if (answer3.getText().toString().equalsIgnoreCase(falseAnswerTwo)) {
                    answer3.setVisibility(View.GONE);
                }
                if (answer4.getText().toString().equalsIgnoreCase(falseAnswerOne)) {
                    answer4.setVisibility(View.GONE);
                }
                if (answer4.getText().toString().equalsIgnoreCase(falseAnswerTwo)) {
                    answer4.setVisibility(View.GONE);
                }
                fiftyFifty.setVisibility(View.GONE);
                rewardVideoBtn.setVisibility(View.GONE);
                // Restart Timer
                rewarded = 1;
            }
        };
        // Reward Video Type
        rewardVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoTypeShared.getString("videoTypeShared", "").equals("admob")) {
                    if (rewardedVideoAd.isLoaded()) {
                        rewardedVideoAd.show();
                    } else {
                        loadRewardedVideoAd();
                        rewardedVideoAd.show();
                    }
                }
                else if(videoTypeShared.getString("videoTypeShared", "").equals("facebook")) {
                    fbrewardedVideoAd.loadAd(
                            fbrewardedVideoAd.buildLoadAdConfig()
                                    .withAdListener(rewardedVideoAdListener)
                                    .build());
                }
                else if(videoTypeShared.getString("videoTypeShared", "").equals("adcolony")) {
                    // AdColony Reward
                    AdColony.setRewardListener(new AdColonyRewardListener() {
                        @Override
                        public void onReward(AdColonyReward adColonyReward) {
                            if (answer1.getText().toString().equalsIgnoreCase(falseAnswerOne)) {
                                answer1.setVisibility(View.GONE);
                            }
                            if (answer1.getText().toString().equalsIgnoreCase(falseAnswerTwo)) {
                                answer1.setVisibility(View.GONE);
                            }
                            if (answer2.getText().toString().equalsIgnoreCase(falseAnswerOne)) {
                                answer2.setVisibility(View.GONE);
                            }
                            if (answer2.getText().toString().equalsIgnoreCase(falseAnswerTwo)) {
                                answer2.setVisibility(View.GONE);
                            }
                            if (answer3.getText().toString().equalsIgnoreCase(falseAnswerOne)) {
                                answer3.setVisibility(View.GONE);
                            }
                            if (answer3.getText().toString().equalsIgnoreCase(falseAnswerTwo)) {
                                answer3.setVisibility(View.GONE);
                            }
                            if (answer4.getText().toString().equalsIgnoreCase(falseAnswerOne)) {
                                answer4.setVisibility(View.GONE);
                            }
                            if (answer4.getText().toString().equalsIgnoreCase(falseAnswerTwo)) {
                                answer4.setVisibility(View.GONE);
                            }
                            fiftyFifty.setVisibility(View.GONE);
                            rewardVideoBtn.setVisibility(View.GONE);
                            // Restart Timer
                            rewarded = 1;
                        }

                    });
                    rewardListener = new AdColonyInterstitialListener() {
                        @Override
                        public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {
                            rewardAdColony = adColonyInterstitial;
                            isRewardLoaded = true;
                            if (rewardAdColony!=null && isRewardLoaded) {
                                rewardAdColony.show();
                                isRewardLoaded = false;
                            }
                        }
                        @Override
                        public void onClosed(AdColonyInterstitial ad) {
                            super.onClosed(ad);
                            AdColony.requestInterstitial(adcolonyAppId.getString("adcolonyAppId",""), rewardListener, adColonyAdOptionsReward);
                        }
                    };
                    adColonyAdOptionsReward = new AdColonyAdOptions().enableConfirmationDialog(false)
                            .enableResultsDialog(false);
                    AdColony.requestInterstitial(adcolonyReward.getString("adcolonyReward", ""), rewardListener, adColonyAdOptionsReward);
                }
                else if(videoTypeShared.getString("videoTypeShared", "").equals("startapp")) {
                    final StartAppAd offerwallAd = new StartAppAd(QuestionActivity.this);
                    offerwallAd.setVideoListener(new VideoListener() {
                        @Override
                        public void onVideoCompleted() {
                            if (answer1.getText().toString().equalsIgnoreCase(falseAnswerOne)) {
                                answer1.setVisibility(View.GONE);
                            }
                            if (answer1.getText().toString().equalsIgnoreCase(falseAnswerTwo)) {
                                answer1.setVisibility(View.GONE);
                            }
                            if (answer2.getText().toString().equalsIgnoreCase(falseAnswerOne)) {
                                answer2.setVisibility(View.GONE);
                            }
                            if (answer2.getText().toString().equalsIgnoreCase(falseAnswerTwo)) {
                                answer2.setVisibility(View.GONE);
                            }
                            if (answer3.getText().toString().equalsIgnoreCase(falseAnswerOne)) {
                                answer3.setVisibility(View.GONE);
                            }
                            if (answer3.getText().toString().equalsIgnoreCase(falseAnswerTwo)) {
                                answer3.setVisibility(View.GONE);
                            }
                            if (answer4.getText().toString().equalsIgnoreCase(falseAnswerOne)) {
                                answer4.setVisibility(View.GONE);
                            }
                            if (answer4.getText().toString().equalsIgnoreCase(falseAnswerTwo)) {
                                answer4.setVisibility(View.GONE);
                            }
                            fiftyFifty.setVisibility(View.GONE);
                            rewardVideoBtn.setVisibility(View.GONE);
                            // Restart Timer
                            rewarded = 1;
                        }
                    });
                    offerwallAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
                        @Override
                        public void onReceiveAd(com.startapp.sdk.adsbase.Ad ad) {
                            offerwallAd.showAd();
                        }

                        @Override
                        public void onFailedToReceiveAd(com.startapp.sdk.adsbase.Ad ad) {
                            ad.getErrorMessage();
                        }
                    });
                }
            }
        });
        // Reward Video Clicked
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {
                countDown.cancel();
            }

            @Override
            public void onRewardedVideoStarted() {
                countDown.cancel();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                // Preload the next video ad.
                loadRewardedVideoAd();
                countDown.cancel();
                countDown.start();
                rewardVideoBtn.setVisibility(View.GONE);
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                if (answer1.getText().toString().equalsIgnoreCase(falseAnswerOne)) {
                    answer1.setVisibility(View.GONE);
                }
                if (answer1.getText().toString().equalsIgnoreCase(falseAnswerTwo)) {
                    answer1.setVisibility(View.GONE);
                }
                if (answer2.getText().toString().equalsIgnoreCase(falseAnswerOne)) {
                    answer2.setVisibility(View.GONE);
                }
                if (answer2.getText().toString().equalsIgnoreCase(falseAnswerTwo)) {
                    answer2.setVisibility(View.GONE);
                }
                if (answer3.getText().toString().equalsIgnoreCase(falseAnswerOne)) {
                    answer3.setVisibility(View.GONE);
                }
                if (answer3.getText().toString().equalsIgnoreCase(falseAnswerTwo)) {
                    answer3.setVisibility(View.GONE);
                }
                if (answer4.getText().toString().equalsIgnoreCase(falseAnswerOne)) {
                    answer4.setVisibility(View.GONE);
                }
                if (answer4.getText().toString().equalsIgnoreCase(falseAnswerTwo)) {
                    answer4.setVisibility(View.GONE);
                }
                fiftyFifty.setVisibility(View.GONE);
                rewardVideoBtn.setVisibility(View.GONE);
                // Restart Timer
                rewarded = 1;
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });
        // Next Question if Submit Clicked
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer1.setVisibility(View.VISIBLE);
                answer2.setVisibility(View.VISIBLE);
                answer3.setVisibility(View.VISIBLE);
                answer4.setVisibility(View.VISIBLE);
                if (fiftyFiftyOption.getString("fiftyFiftyOption", "").equals("yes")) {
                    fiftyFifty.setVisibility(View.VISIBLE);
                } else {
                    fiftyFifty.setVisibility(View.GONE);
                }
                if (rewardVideoOption.getString("rewardVideoOption", "").equals("yes")) {
                    rewardVideoBtn.setVisibility(View.VISIBLE);
                } else {
                    rewardVideoBtn.setVisibility(View.GONE);
                }
                questionPoints.setCompoundDrawablesWithIntrinsicBounds( 0, 0, 0, 0);
                if (mRadioGroup.getCheckedRadioButtonId() == -1) {
                    /******* Nothing is Checked *******/
                    Toast.makeText(QuestionActivity.this, getString(R.string.java_question_toast), Toast.LENGTH_LONG).show();
                } else {
                    // Get Submitted Answer
                    int selectedId = mRadioGroup.getCheckedRadioButtonId();
                    selectedRadioButton = (RadioButton) findViewById(selectedId);
                    submittedAnswer = selectedRadioButton.getText().toString();
                    /******* IF ANSWER IS RIGHT *******/
                    if (submittedAnswer.equals(rightAnswer)) {
                        countDown.cancel();
                        rightAnswersNumber = rightAnswersNumber+1;
                        earnedPoints = earnedPoints + questionEarningPoints;
                        // Add Points To User
                        int actualPoints = points.getInt("points", 0);
                        int newPoints = actualPoints+collectedPoints;
                        points.edit().putInt("points", newPoints).apply();
                        /* Display Alert Dialog That Answer is True && Clear Radio Group Check */
                        mRadioGroup.clearCheck();
                        if (actualQuestionNum == 0) {
                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.correct);
                            mp.start();
                           AlertDialog.Builder alert = new AlertDialog.Builder(QuestionActivity.this)
                                    .setTitle(getString(R.string.java_question_nice_job))
                                    .setMessage(getString(R.string.java_question_congrats))
                                    .setPositiveButton(getString(R.string.java_question_show_score), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            goToScore();
                                        }
                                    })
                                    .setIcon(getDrawable(R.drawable.ic_clap))
                                    .setCancelable(false);
                            try {
                                alert.show();
                            }
                            catch (WindowManager.BadTokenException e) {
                                e.getMessage();
                            }
                        } else {
                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.correct);
                            mp.start();
                            AlertDialog.Builder alertt = new AlertDialog.Builder(QuestionActivity.this)
                                    .setTitle(getString(R.string.java_question_nice_job))
                                    .setMessage(getString(R.string.java_question_answer_true))
                                    .setPositiveButton(getString(R.string.java_question_next_question), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Go to next question
                                            getNextQuestion();
                                        }
                                    })
                                    .setIcon(getDrawable(R.drawable.ic_clap))
                                    .setCancelable(false);
                            try {
                                alertt.show();
                            }
                            catch (WindowManager.BadTokenException e) {
                                e.getMessage();
                            }
                        }
                    } else {
                        /******* IF ANSWER IS FALSE *******/
                        countDown.cancel();
                        earnedPoints = earnedPoints + 0;
                        mRadioGroup.clearCheck();
                        if (actualQuestionNum == 0) {
                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.wrong);
                            mp.start();
                            AlertDialog.Builder allert = new AlertDialog.Builder(QuestionActivity.this)
                                    .setTitle(getString(R.string.java_question_sorry))
                                    .setMessage(getString(R.string.java_question_false))
                                    .setPositiveButton(getString(R.string.java_question_show_score), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            goToScore();
                                        }
                                    })
                                    .setIcon(getDrawable(R.drawable.ic_anxiety))
                                    .setCancelable(false);
                            try {
                                allert.show();
                            }
                            catch (WindowManager.BadTokenException e) {
                                e.getMessage();
                            }
                        } else {
                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.wrong);
                            mp.start();
                            AlertDialog.Builder aalert = new AlertDialog.Builder(QuestionActivity.this)
                                    .setTitle(getString(R.string.java_question_sorry))
                                    .setMessage(getString(R.string.java_question_false_good_luck))
                                    .setPositiveButton(getString(R.string.java_question_next_question), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Go to next question
                                            getNextQuestion();
                                        }
                                    })
                                    .setIcon(getDrawable(R.drawable.ic_anxiety))
                                    .setCancelable(false);
                            try {
                                aalert.show();
                            }
                            catch (WindowManager.BadTokenException e) {
                                e.getMessage();
                            }
                        }
                    }
                }
            }
        });
        // if Stop QUIZ button is clicked
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToScore();
            }
        });
        // Facebook Ads
        AudienceNetworkAds.initialize(this);
        loadNativeAd();
    }


    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    // Parse JSON Function
    private void parseJson() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject record = jsonArray.getJSONObject(i);
                                String question = record.getString("question");
                                String trueAnswer = record.getString("trueAnswer");
                                String falseAnswer1 = record.getString("falseAnswer1");
                                String falseAnswer2 = record.getString("falseAnswer2");
                                String falseAnswer3 = record.getString("falseAnswer3");
                                int points = record.getInt("points");
                                Question newQuestion = new Question(question,trueAnswer,falseAnswer1,falseAnswer2, falseAnswer3, points);
                                questions.add(newQuestion);
                            }
                            // Choose a random Question to Show
                            Random random = new Random();
                            int randomNum = random.nextInt(questions.size());
                            // All Questions Size
                            allQuestions = questions.size();
                            // Set Text for Number Of Questions
                            Question firstQuestion = questions.get(randomNum);
                            i = 1;
                            actualQuestionNum = allQuestions - i;
                            numberOfQuestion.setText(String.valueOf(allQuestions - (actualQuestionNum)) + " / " + allQuestions);
                            // Get Question Text
                            theQuestion.setText(firstQuestion.getQuestion());
                            // Get True Answer
                            rightAnswer = firstQuestion.getTrueAnswer();
                            falseAnswerOne=firstQuestion.getFalseAnswer1();
                            falseAnswerTwo=firstQuestion.getFalseAnswer2();
                            questionEarningPoints = firstQuestion.getPoints();
                            totalQuizPoints = firstQuestion.getPoints();
                            // Create a List of Answer to Shuffle Them
                            List<String> listOfAnswers = new ArrayList<>();
                            listOfAnswers.add(firstQuestion.getTrueAnswer());
                            listOfAnswers.add(firstQuestion.getFalseAnswer1());
                            listOfAnswers.add(firstQuestion.getFalseAnswer2());
                            listOfAnswers.add(firstQuestion.getFalseAnswer3());
                            Collections.shuffle(listOfAnswers);
                            // Set Answers To Radio Buttons
                            answer1.setText(listOfAnswers.get(0));
                            answer2.setText(listOfAnswers.get(1));
                            answer3.setText(listOfAnswers.get(2));
                            answer4.setText(listOfAnswers.get(3));
                            questionPoints.setText(getString(R.string.java_question_questions_points)+String.valueOf(firstQuestion.getPoints()));
                            collectedPoints = firstQuestion.getPoints();
                            // Remove This Question From Array To Show Another Next One
                            questions.remove(randomNum);
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
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void getNextQuestion() {
        answer1.setVisibility(View.VISIBLE);
        answer2.setVisibility(View.VISIBLE);
        answer3.setVisibility(View.VISIBLE);
        answer4.setVisibility(View.VISIBLE);
        // Restart Timer
        countDown.cancel();
        // Get Next question
        Random random = new Random();
        int randomNum = random.nextInt(questions.size());
        // Set Text for Number Of Questions
        Question secondQuestion = questions.get(randomNum);
        i = i+1;
        actualQuestionNum = allQuestions - i;
        numberOfQuestion.setText(String.valueOf(allQuestions - (actualQuestionNum)) + " / " + allQuestions);
        // Get Question Text
        theQuestion.setText(secondQuestion.getQuestion());
        // Get True Answer
        rightAnswer = secondQuestion.getTrueAnswer();
        falseAnswerOne=secondQuestion.getFalseAnswer1();
        falseAnswerTwo=secondQuestion.getFalseAnswer2();
        questionEarningPoints = secondQuestion.getPoints();
        totalQuizPoints = totalQuizPoints + secondQuestion.getPoints();
        // Create a List of Answer to Shuffle Them
        List<String> listOfAnswers = new ArrayList<>();
        listOfAnswers.add(secondQuestion.getTrueAnswer());
        listOfAnswers.add(secondQuestion.getFalseAnswer1());
        listOfAnswers.add(secondQuestion.getFalseAnswer2());
        listOfAnswers.add(secondQuestion.getFalseAnswer3());
        Collections.shuffle(listOfAnswers);
        // Set Answers To Radio Buttons
        answer1.setText(listOfAnswers.get(0));
        answer2.setText(listOfAnswers.get(1));
        answer3.setText(listOfAnswers.get(2));
        answer4.setText(listOfAnswers.get(3));
        questionPoints.setText(getString(R.string.java_question_questions_points)+String.valueOf(secondQuestion.getPoints()));
        collectedPoints = secondQuestion.getPoints();
        // Remove This Question From Array To Show Another Next One
        questions.remove(randomNum);
        // Restart Timer
        countDown.start();
    }

    // Change Player Points
    public void goToScore() {
        // Add Interstitial
        if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("facebook")) {
            if (facebookInterstitialAd.isAdLoaded()) {
                facebookInterstitialAd.show();
                InterstitialAdListener listener = new InterstitialAdListener() {
                    @Override
                    public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                    }

                    @Override
                    public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                        Intent homePage = new Intent(QuestionActivity.this, ScoreActivity.class);
                        homePage.putExtra("earnedPoints", String.valueOf(earnedPoints));
                        homePage.putExtra("totalPoints", String.valueOf(totalQuizPoints));
                        homePage.putExtra("allQuestions", String.valueOf(allQuestions));
                        homePage.putExtra("trueAnswers", String.valueOf(rightAnswersNumber));
                        homePage.putExtra("categoryId", categoryId);
                        homePage.putExtra("categoryName", categoryName);
                        homePage.putExtra("categoryLevel", categoryLevel);
                        homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homePage);
                        finish();
                    }

                    @Override
                    public void onError(com.facebook.ads.Ad ad, AdError adError) {
                        Intent homePage = new Intent(QuestionActivity.this, ScoreActivity.class);
                        homePage.putExtra("earnedPoints", String.valueOf(earnedPoints));
                        homePage.putExtra("totalPoints", String.valueOf(totalQuizPoints));
                        homePage.putExtra("allQuestions", String.valueOf(allQuestions));
                        homePage.putExtra("trueAnswers", String.valueOf(rightAnswersNumber));
                        homePage.putExtra("categoryId", categoryId);
                        homePage.putExtra("categoryName", categoryName);
                        homePage.putExtra("categoryLevel", categoryLevel);
                        homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homePage);
                        finish();
                    }

                    @Override
                    public void onAdLoaded(com.facebook.ads.Ad ad) {
                        facebookInterstitialAd.show();
                    }

                    @Override
                    public void onAdClicked(com.facebook.ads.Ad ad) {

                    }

                    @Override
                    public void onLoggingImpression(com.facebook.ads.Ad ad) {

                    }
                };
                facebookInterstitialAd.loadAd(facebookInterstitialAd.buildLoadAdConfig().withAdListener(listener).build());
                prepareInterstitialAd();
            } else {
                Intent homePage = new Intent(QuestionActivity.this, ScoreActivity.class);
                homePage.putExtra("earnedPoints", String.valueOf(earnedPoints));
                homePage.putExtra("totalPoints", String.valueOf(totalQuizPoints));
                homePage.putExtra("allQuestions", String.valueOf(allQuestions));
                homePage.putExtra("trueAnswers", String.valueOf(rightAnswersNumber));
                homePage.putExtra("categoryId", categoryId);
                homePage.putExtra("categoryName", categoryName);
                homePage.putExtra("categoryLevel", categoryLevel);
                homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homePage);
                finish();
            }
        }
        else if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("admob")) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        // Code to be executed when an ad finishes loading.
                        mInterstitialAd.show();
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        Intent homePage = new Intent(QuestionActivity.this, ScoreActivity.class);
                        homePage.putExtra("earnedPoints", String.valueOf(earnedPoints));
                        homePage.putExtra("totalPoints", String.valueOf(totalQuizPoints));
                        homePage.putExtra("allQuestions", String.valueOf(allQuestions));
                        homePage.putExtra("trueAnswers", String.valueOf(rightAnswersNumber));
                        homePage.putExtra("categoryId", categoryId);
                        homePage.putExtra("categoryName", categoryName);
                        homePage.putExtra("categoryLevel", categoryLevel);
                        homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homePage);
                        finish();
                    }

                    @Override
                    public void onAdOpened() {
                        // Code to be executed when the ad is displayed.
                    }

                    @Override
                    public void onAdClicked() {
                        // Code to be executed when the user clicks on an ad.
                    }

                    @Override
                    public void onAdLeftApplication() {
                        // Code to be executed when the user has left the app.
                    }

                    @Override
                    public void onAdClosed() {
                        Intent homePage = new Intent(QuestionActivity.this, ScoreActivity.class);
                        homePage.putExtra("earnedPoints", String.valueOf(earnedPoints));
                        homePage.putExtra("totalPoints", String.valueOf(totalQuizPoints));
                        homePage.putExtra("allQuestions", String.valueOf(allQuestions));
                        homePage.putExtra("trueAnswers", String.valueOf(rightAnswersNumber));
                        homePage.putExtra("categoryId", categoryId);
                        homePage.putExtra("categoryName", categoryName);
                        homePage.putExtra("categoryLevel", categoryLevel);
                        homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homePage);
                        finish();
                    }
                });
                prepareInterstitialAdmobAd();
            } else {
                Intent homePage = new Intent(QuestionActivity.this, ScoreActivity.class);
                homePage.putExtra("earnedPoints", String.valueOf(earnedPoints));
                homePage.putExtra("totalPoints", String.valueOf(totalQuizPoints));
                homePage.putExtra("allQuestions", String.valueOf(allQuestions));
                homePage.putExtra("trueAnswers", String.valueOf(rightAnswersNumber));
                homePage.putExtra("categoryId", categoryId);
                homePage.putExtra("categoryName", categoryName);
                homePage.putExtra("categoryLevel", categoryLevel);
                homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homePage);
                finish();
            }
        } else if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("adcolony")) {
            AdColonyInterstitialListener listener = new AdColonyInterstitialListener() {
                @Override
                public void onRequestFilled(AdColonyInterstitial adColonyInterstitial) {
                    adColonyInterstitiall = adColonyInterstitial;
                    adColonyInterstitiall.show();
                }

                @Override
                public void onRequestNotFilled(AdColonyZone zone) {
                    Intent homePage = new Intent(QuestionActivity.this, ScoreActivity.class);
                    homePage.putExtra("earnedPoints", String.valueOf(earnedPoints));
                    homePage.putExtra("totalPoints", String.valueOf(totalQuizPoints));
                    homePage.putExtra("allQuestions", String.valueOf(allQuestions));
                    homePage.putExtra("trueAnswers", String.valueOf(rightAnswersNumber));
                    homePage.putExtra("categoryId", categoryId);
                    homePage.putExtra("categoryName", categoryName);
                    homePage.putExtra("categoryLevel", categoryLevel);
                    homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homePage);
                    finish();
                }

                @Override
                public void onClosed(AdColonyInterstitial ad) {
                    Intent homePage = new Intent(QuestionActivity.this, ScoreActivity.class);
                    homePage.putExtra("earnedPoints", String.valueOf(earnedPoints));
                    homePage.putExtra("totalPoints", String.valueOf(totalQuizPoints));
                    homePage.putExtra("allQuestions", String.valueOf(allQuestions));
                    homePage.putExtra("trueAnswers", String.valueOf(rightAnswersNumber));
                    homePage.putExtra("categoryId", categoryId);
                    homePage.putExtra("categoryName", categoryName);
                    homePage.putExtra("categoryLevel", categoryLevel);
                    homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homePage);
                    finish();
                }

                @Override
                public void onExpiring(AdColonyInterstitial ad) {
                    Intent homePage = new Intent(QuestionActivity.this, ScoreActivity.class);
                    homePage.putExtra("earnedPoints", String.valueOf(earnedPoints));
                    homePage.putExtra("totalPoints", String.valueOf(totalQuizPoints));
                    homePage.putExtra("allQuestions", String.valueOf(allQuestions));
                    homePage.putExtra("trueAnswers", String.valueOf(rightAnswersNumber));
                    homePage.putExtra("categoryId", categoryId);
                    homePage.putExtra("categoryName", categoryName);
                    homePage.putExtra("categoryLevel", categoryLevel);
                    homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homePage);
                    finish();
                }
            };
            AdColony.requestInterstitial(adcolonyInterstitial.getString("adcolonyInterstitial", ""), listener, adColonyAdOptions);
        } else if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("startapp")) {
            Intent homePage = new Intent(QuestionActivity.this, ScoreActivity.class);
            homePage.putExtra("earnedPoints", String.valueOf(earnedPoints));
            homePage.putExtra("totalPoints", String.valueOf(totalQuizPoints));
            homePage.putExtra("allQuestions", String.valueOf(allQuestions));
            homePage.putExtra("trueAnswers", String.valueOf(rightAnswersNumber));
            homePage.putExtra("categoryId", categoryId);
            homePage.putExtra("categoryName", categoryName);
            homePage.putExtra("categoryLevel", categoryLevel);
            homePage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homePage);
            finish();
            StartAppAd.showAd(QuestionActivity.this);
        }
    }

    private void prepareInterstitialAd() {
        AudienceNetworkAds.initialize(this);
        facebookInterstitialAd = new com.facebook.ads.InterstitialAd(this, facebookInterstitial.getString("facebookInterstitial", ""));
        AdSettings.addTestDevice("c377b922-aa02-4425-a50b-af7a1f1ee1b6");
        facebookInterstitialAd.loadAd();
    }

    private void prepareInterstitialAdmobAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(admobInterstitial.getString("admobInterstitial", ""));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void loadNativeAd() {
        // Instantiate a NativeAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
        nativeBannerAd = new NativeBannerAd(this, facebookNative.getString("facebookNative", null));
        NativeAdListener listener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Race condition, load() called again before last ad was displayed
                if (nativeBannerAd == null || nativeBannerAd != ad) {
                    return;
                }
                // Inflate Native Banner Ad into Container
                inflateAd(nativeBannerAd);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };
        nativeBannerAd.loadAd(nativeBannerAd.buildLoadAdConfig().withAdListener(listener).build());
    }

    private void inflateAd(NativeBannerAd nativeBannerAd) {
        // Unregister last ad
        nativeBannerAd.unregisterView();

        // Add the Ad view into the ad container.
        // nativeAdLayout = getView().findViewById(R.id.statistics_native_banner_ad_container);
        LayoutInflater inflater = LayoutInflater.from(this);
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.facebook_native_banner_layout, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdChoices icon
        RelativeLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(this, nativeBannerAd, nativeAdLayout);
        adOptionsView.setIconSizeDp(23);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        MediaView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
    }

    private void loadRewardedVideoAd() {
        if (!rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.loadAd(admobVideo.getString("admobVideo", null), new AdRequest.Builder().build());
        }
    }

    @Override
    protected void onStart() {
        if (!rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.loadAd(admobVideo.getString("admobVideo", null), new AdRequest.Builder().build());
        }
        super.onStart();
        active = true;
    }

    @Override
    public void onResume() {
        rewardedVideoAd.resume(QuestionActivity.this);
        super.onResume();
        active = true;
    }

    @Override
    public void onPause() {
        rewardedVideoAd.pause(QuestionActivity.this);
        super.onPause();
        active = false;
    }

    @Override
    public void onBackPressed() {
        Intent main = new Intent(QuestionActivity.this, MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(main);
        finish();
        super.onBackPressed();
    }
}
