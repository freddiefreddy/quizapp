package com.todocode.quizv3.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codemybrainsout.ratingdialog.RatingDialog;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.squareup.picasso.Picasso;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.todocode.quizv3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView name, email, memberSince, points;
    private CircleImageView profileImage;
    private Button earnings, withdrawals, leaderboard, statistics, logout;
    private RequestQueue queue;
    private LinearLayout adsLinear;
    private String avatarLink;
    GoogleSignInClient mGoogleSignInClient;
    SharedPreferences userSituation, admobBanner, facebookBanner, facebookInterstitial, admobInterstitial;
    public String spUserEmail, url;
    private AdView bannerAdmobAdView;
    private Button editProfile;
    private InterstitialAd mInterstitialAd;
    private com.facebook.ads.InterstitialAd facebookInterstitialAd;
    private SharedPreferences playerNameShared, playerEmailShared, playerScoreShared, playerEarningsShared, playerImageShared, playerReferralCodeShared, playerMemberSinceShared;
    private SharedPreferences interstitialTypeShared, bottomBannerType,adcolonyBanner,adcolonyInterstitial,adcolonyAppId,adcolonyReward,startappAppId, admobAppId;
    // AdColony Banner
    public AdColonyAdView adView;
    public AdColonyAdOptions adColonyAdOptionsBanner;
    public static String[] AD_UNIT_ZONE_Ids;
    Banner startAppBanner;
    public AdColonyInterstitial adColonyInterstitiall;
    public AdColonyAdOptions adColonyAdOptions;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.app_lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        StartAppAd.disableSplash();
        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        toolbar.setTitle(getString(R.string.drawer_menu_profile));
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
        admobInterstitial = getSharedPreferences("admobInterstitial",MODE_PRIVATE);
        facebookInterstitial = getSharedPreferences("facebookInterstitial",MODE_PRIVATE);
        interstitialTypeShared = getSharedPreferences("interstitialTypeShared",MODE_PRIVATE);
        MobileAds.initialize(ProfileActivity.this, admobAppId.getString("admobAppId", ""));
        if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("facebook")) {
            prepareInterstitialAd();
        } else if(interstitialTypeShared.getString("interstitialTypeShared", "").equals("admob")) {
            prepareInterstitialAdmobAd();
        }
        AD_UNIT_ZONE_Ids = new String[] {adcolonyBanner.getString("adcolonyBanner", ""), adcolonyInterstitial.getString("adcolonyInterstitial", ""), adcolonyReward.getString("adcolonyReward", "")};
        // Configure AdColony Ads
        AdColony.configure(ProfileActivity.this, adcolonyAppId.getString("adcolonyAppId", ""), AD_UNIT_ZONE_Ids);
        playerNameShared = getSharedPreferences("playerNameShared", MODE_PRIVATE);
        playerEmailShared = getSharedPreferences("playerEmailShared", MODE_PRIVATE);
        playerScoreShared = getSharedPreferences("playerScoreShared", MODE_PRIVATE);
        playerEarningsShared = getSharedPreferences("playerEarningsShared", MODE_PRIVATE);
        playerImageShared = getSharedPreferences("playerImageShared", MODE_PRIVATE);
        playerReferralCodeShared = getSharedPreferences("playerReferralCodeShared", MODE_PRIVATE);
        playerMemberSinceShared = getSharedPreferences("playerMemberSinceShared", MODE_PRIVATE);
        admobBanner = getSharedPreferences("admobBanner", MODE_PRIVATE);
        userSituation = getSharedPreferences("userEmail",MODE_PRIVATE);
        spUserEmail = userSituation.getString("userEmail", "");
        name = (TextView) findViewById(R.id.profile_name);
        name.setText(playerNameShared.getString("playerNameShared", ""));
        email = (TextView) findViewById(R.id.profile_email);
        email.setText("Email : "+ playerEmailShared.getString("playerEmailShared", ""));
        memberSince = (TextView) findViewById(R.id.member_since);
        memberSince.setText("Member Since : "+playerMemberSinceShared.getString("playerMemberSinceShared", ""));
        points = (TextView) findViewById(R.id.profile_points);
        points.setText("Collected Points : "+playerScoreShared.getString("playerScoreShared", ""));
        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        Picasso.get().load(playerImageShared.getString("playerImageShared", "")).fit().centerInside().into(profileImage);
        earnings = (Button) findViewById(R.id.earnings_btn);
        withdrawals = (Button) findViewById(R.id.withdrawals_btn);
        leaderboard = (Button) findViewById(R.id.leaderboard_btn);
        statistics = (Button) findViewById(R.id.statistics_btn);
        logout = (Button) findViewById(R.id.profile_logout);
        earnings = (Button) findViewById(R.id.earnings_btn);
        withdrawals = (Button) findViewById(R.id.withdrawals_btn);
        editProfile = (Button) findViewById(R.id.edit_profile);
        url = getString(R.string.domain_name);
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
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change Shared Preferences
                userSituation.edit().putString("userEmail", "").apply();
                // Logout Google
                if (mGoogleSignInClient != null) {
                    mGoogleSignInClient.signOut();
                }
                // Logout Facebook
                if (LoginManager.getInstance() != null) {
                    LoginManager.getInstance().logOut();
                }
                // Go To Login Page
                Intent loginPage = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(loginPage);
                finishAffinity();
            }
        });
        // Go To Earnings Activity
        earnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("facebook")) {
                    if (facebookInterstitialAd.isAdLoaded()) {
                        facebookInterstitialAd.show();
                        InterstitialAdListener listener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                Intent gotoEarnings = new Intent(ProfileActivity.this, EarningsActivity.class);
                                startActivity(gotoEarnings);
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                Intent gotoEarnings = new Intent(ProfileActivity.this, EarningsActivity.class);
                                startActivity(gotoEarnings);
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
                        Intent gotoEarnings = new Intent(ProfileActivity.this, EarningsActivity.class);
                        startActivity(gotoEarnings);
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
                                Intent gotoEarnings = new Intent(ProfileActivity.this, EarningsActivity.class);
                                startActivity(gotoEarnings);
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
                                Intent gotoEarnings = new Intent(ProfileActivity.this, EarningsActivity.class);
                                startActivity(gotoEarnings);
                            }
                        });
                        prepareInterstitialAdmobAd();
                    } else {
                        Intent gotoEarnings = new Intent(ProfileActivity.this, EarningsActivity.class);
                        startActivity(gotoEarnings);
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
                            Intent gotoEarnings = new Intent(ProfileActivity.this, EarningsActivity.class);
                            startActivity(gotoEarnings);
                        }

                        @Override
                        public void onClosed(AdColonyInterstitial ad) {
                            Intent gotoEarnings = new Intent(ProfileActivity.this, EarningsActivity.class);
                            startActivity(gotoEarnings);
                        }

                        @Override
                        public void onExpiring(AdColonyInterstitial ad) {
                            Intent gotoEarnings = new Intent(ProfileActivity.this, EarningsActivity.class);
                            startActivity(gotoEarnings);
                        }
                    };
                    AdColony.requestInterstitial(adcolonyInterstitial.getString("adcolonyInterstitial", ""), listener, adColonyAdOptions);
                } else if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("startapp")) {
                    Log.e("startapp","here");
                    Intent gotoEarnings = new Intent(ProfileActivity.this, EarningsActivity.class);
                    startActivity(gotoEarnings);
                    StartAppAd.showAd(ProfileActivity.this);
                }
            }
        });
        // Go To Statistics
        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("facebook")) {
                    if (facebookInterstitialAd.isAdLoaded()) {
                        facebookInterstitialAd.show();
                        InterstitialAdListener listener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                Intent gotostat = new Intent(ProfileActivity.this, StatisticsActivity.class);
                                startActivity(gotostat);
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                Intent gotostat = new Intent(ProfileActivity.this, StatisticsActivity.class);
                                startActivity(gotostat);
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
                        Intent gotostat = new Intent(ProfileActivity.this, StatisticsActivity.class);
                        startActivity(gotostat);
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
                                Intent gotostat = new Intent(ProfileActivity.this, StatisticsActivity.class);
                                startActivity(gotostat);
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
                                Intent gotostat = new Intent(ProfileActivity.this, StatisticsActivity.class);
                                startActivity(gotostat);
                            }
                        });
                        prepareInterstitialAdmobAd();
                    } else {
                        Intent gotostat = new Intent(ProfileActivity.this, StatisticsActivity.class);
                        startActivity(gotostat);
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
                            Intent gotostat = new Intent(ProfileActivity.this, StatisticsActivity.class);
                            startActivity(gotostat);
                        }

                        @Override
                        public void onClosed(AdColonyInterstitial ad) {
                            Intent gotostat = new Intent(ProfileActivity.this, StatisticsActivity.class);
                            startActivity(gotostat);
                        }

                        @Override
                        public void onExpiring(AdColonyInterstitial ad) {
                            Intent gotostat = new Intent(ProfileActivity.this, StatisticsActivity.class);
                            startActivity(gotostat);
                        }
                    };
                    AdColony.requestInterstitial(adcolonyInterstitial.getString("adcolonyInterstitial", ""), listener, adColonyAdOptions);
                } else if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("startapp")) {
                    Intent gotostat = new Intent(ProfileActivity.this, StatisticsActivity.class);
                    startActivity(gotostat);
                    StartAppAd.showAd(ProfileActivity.this);
                }
            }
        });
        withdrawals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("facebook")) {
                    if (facebookInterstitialAd.isAdLoaded()) {
                        facebookInterstitialAd.show();
                        InterstitialAdListener listener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                Intent history = new Intent(ProfileActivity.this, WithdrawalsHistoryActivity.class);
                                history.putExtra("userEmail", spUserEmail);
                                startActivity(history);
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                Intent history = new Intent(ProfileActivity.this, WithdrawalsHistoryActivity.class);
                                history.putExtra("userEmail", spUserEmail);
                                startActivity(history);
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
                        Intent history = new Intent(ProfileActivity.this, WithdrawalsHistoryActivity.class);
                        history.putExtra("userEmail", spUserEmail);
                        startActivity(history);
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
                                Intent history = new Intent(ProfileActivity.this, WithdrawalsHistoryActivity.class);
                                history.putExtra("userEmail", spUserEmail);
                                startActivity(history);
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
                                Intent history = new Intent(ProfileActivity.this, WithdrawalsHistoryActivity.class);
                                history.putExtra("userEmail", spUserEmail);
                                startActivity(history);
                            }
                        });
                        prepareInterstitialAdmobAd();
                    } else {
                        Intent history = new Intent(ProfileActivity.this, WithdrawalsHistoryActivity.class);
                        history.putExtra("userEmail", spUserEmail);
                        startActivity(history);
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
                            Intent history = new Intent(ProfileActivity.this, WithdrawalsHistoryActivity.class);
                            history.putExtra("userEmail", spUserEmail);
                            startActivity(history);
                        }

                        @Override
                        public void onClosed(AdColonyInterstitial ad) {
                            Intent history = new Intent(ProfileActivity.this, WithdrawalsHistoryActivity.class);
                            history.putExtra("userEmail", spUserEmail);
                            startActivity(history);
                        }

                        @Override
                        public void onExpiring(AdColonyInterstitial ad) {
                            Intent history = new Intent(ProfileActivity.this, WithdrawalsHistoryActivity.class);
                            history.putExtra("userEmail", spUserEmail);
                            startActivity(history);
                        }
                    };
                    AdColony.requestInterstitial(adcolonyInterstitial.getString("adcolonyInterstitial", ""), listener, adColonyAdOptions);
                } else if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("startapp")) {
                    Intent history = new Intent(ProfileActivity.this, WithdrawalsHistoryActivity.class);
                    history.putExtra("userEmail", spUserEmail);
                    startActivity(history);
                    StartAppAd.showAd(ProfileActivity.this);
                }
            }
        });
        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("facebook")) {
                    if (facebookInterstitialAd.isAdLoaded()) {
                        facebookInterstitialAd.show();
                        InterstitialAdListener listener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                Intent lead = new Intent(ProfileActivity.this, LeaderboardsActivity.class);
                                startActivity(lead);
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                Intent lead = new Intent(ProfileActivity.this, LeaderboardsActivity.class);
                                startActivity(lead);
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
                        Intent lead = new Intent(ProfileActivity.this, LeaderboardsActivity.class);
                        startActivity(lead);
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
                                Intent lead = new Intent(ProfileActivity.this, LeaderboardsActivity.class);
                                startActivity(lead);
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
                                Intent lead = new Intent(ProfileActivity.this, LeaderboardsActivity.class);
                                startActivity(lead);
                            }
                        });
                        prepareInterstitialAdmobAd();
                    } else {
                        Intent lead = new Intent(ProfileActivity.this, LeaderboardsActivity.class);
                        startActivity(lead);
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
                            Intent lead = new Intent(ProfileActivity.this, LeaderboardsActivity.class);
                            startActivity(lead);
                        }

                        @Override
                        public void onClosed(AdColonyInterstitial ad) {
                            Intent lead = new Intent(ProfileActivity.this, LeaderboardsActivity.class);
                            startActivity(lead);
                        }

                        @Override
                        public void onExpiring(AdColonyInterstitial ad) {
                            Intent lead = new Intent(ProfileActivity.this, LeaderboardsActivity.class);
                            startActivity(lead);
                        }
                    };
                    AdColony.requestInterstitial(adcolonyInterstitial.getString("adcolonyInterstitial", ""), listener, adColonyAdOptions);
                } else if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("startapp")) {
                    Intent lead = new Intent(ProfileActivity.this, LeaderboardsActivity.class);
                    startActivity(lead);
                    StartAppAd.showAd(ProfileActivity.this);
                }
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("facebook")) {
                    if (facebookInterstitialAd.isAdLoaded()) {
                        facebookInterstitialAd.show();
                        InterstitialAdListener listener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                Intent edit = new Intent(ProfileActivity.this, EditProfileActivity.class);
                                edit.putExtra("username", playerNameShared.getString("playerNameShared", ""));
                                edit.putExtra("avatar", playerImageShared.getString("playerImageShared", ""));
                                startActivity(edit);
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                Intent edit = new Intent(ProfileActivity.this, EditProfileActivity.class);
                                edit.putExtra("username", playerNameShared.getString("playerNameShared", ""));
                                edit.putExtra("avatar", playerImageShared.getString("playerImageShared", ""));
                                startActivity(edit);
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
                        Intent edit = new Intent(ProfileActivity.this, EditProfileActivity.class);
                        edit.putExtra("username", playerNameShared.getString("playerNameShared", ""));
                        edit.putExtra("avatar", playerImageShared.getString("playerImageShared", ""));
                        startActivity(edit);
                    }
                } else if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("admob")) {
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
                                Intent edit = new Intent(ProfileActivity.this, EditProfileActivity.class);
                                edit.putExtra("username", playerNameShared.getString("playerNameShared", ""));
                                edit.putExtra("avatar", playerImageShared.getString("playerImageShared", ""));
                                startActivity(edit);
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
                                Intent edit = new Intent(ProfileActivity.this, EditProfileActivity.class);
                                edit.putExtra("username", playerNameShared.getString("playerNameShared", ""));
                                edit.putExtra("avatar", playerImageShared.getString("playerImageShared", ""));
                                startActivity(edit);
                            }
                        });
                        prepareInterstitialAdmobAd();
                    } else {
                        Intent edit = new Intent(ProfileActivity.this, EditProfileActivity.class);
                        edit.putExtra("username", playerNameShared.getString("playerNameShared", ""));
                        edit.putExtra("avatar", playerImageShared.getString("playerImageShared", ""));
                        startActivity(edit);
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
                            Intent edit = new Intent(ProfileActivity.this, EditProfileActivity.class);
                            edit.putExtra("username", playerNameShared.getString("playerNameShared", ""));
                            edit.putExtra("avatar", playerImageShared.getString("playerImageShared", ""));
                            startActivity(edit);
                        }

                        @Override
                        public void onClosed(AdColonyInterstitial ad) {
                            Intent edit = new Intent(ProfileActivity.this, EditProfileActivity.class);
                            edit.putExtra("username", playerNameShared.getString("playerNameShared", ""));
                            edit.putExtra("avatar", playerImageShared.getString("playerImageShared", ""));
                            startActivity(edit);
                        }

                        @Override
                        public void onExpiring(AdColonyInterstitial ad) {
                            Intent edit = new Intent(ProfileActivity.this, EditProfileActivity.class);
                            edit.putExtra("username", playerNameShared.getString("playerNameShared", ""));
                            edit.putExtra("avatar", playerImageShared.getString("playerImageShared", ""));
                            startActivity(edit);
                        }
                    };
                    AdColony.requestInterstitial(adcolonyInterstitial.getString("adcolonyInterstitial", ""), listener, adColonyAdOptions);
                } else if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("startapp")) {
                    Intent edit = new Intent(ProfileActivity.this, EditProfileActivity.class);
                    edit.putExtra("username", playerNameShared.getString("playerNameShared", ""));
                    edit.putExtra("avatar", playerImageShared.getString("playerImageShared", ""));
                    startActivity(edit);
                    StartAppAd.showAd(ProfileActivity.this);
                }
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void smartRating() {
        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .icon(getDrawable(R.drawable.smart_rating))
                .threshold(3)
                .title(getString(R.string.rate_dialog_title))
                .titleTextColor(R.color.black)
                .positiveButtonText(getString(R.string.rate_dialog_cancel))
                .negativeButtonText(getString(R.string.rate_dialog_no))
                .positiveButtonTextColor(R.color.colorPrimaryDark)
                .negativeButtonTextColor(R.color.grey_500)
                .formTitle(getString(R.string.rate_dialog_suggest))
                .formHint(getString(R.string.rate_dialog_suggestion))
                .formSubmitText(getString(R.string.rate_dialog_submit))
                .formCancelText(getString(R.string.rate_form_cancel))
                .playstoreUrl("http://play.google.com/store/apps/details?id=" + this.getPackageName())
                .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                    @Override
                    public void onFormSubmitted(String feedback) {
                        // Save Suggestion
                    }
                }).build();

        ratingDialog.show();
    }

}

