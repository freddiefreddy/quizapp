package com.todocode.quizv3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
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

public class InviteFriendsActivity extends AppCompatActivity {
    private TextView inviteFriendsGain, YourRefferalCode, TapToCopy;
    private String spUserEmail, bannerBottomType;
    private RequestQueue queue;
    private Button referNow;
    private SharedPreferences facebookInterstitial, admobInterstitial, bottomBannerType,facebookBanner, admobBanner, userSituation;
    private LinearLayout adsLinear;
    private AdView bannerAdmobAdView;
    private SharedPreferences interstitialTypeShared,adcolonyBanner,adcolonyInterstitial,adcolonyAppId,adcolonyReward,startappAppId, admobAppId;
    // AdColony Banner
    public AdColonyAdView adView;
    public AdColonyAdOptions adColonyAdOptionsBanner;
    public static String[] AD_UNIT_ZONE_Ids;
    Banner startAppBanner;
    private SharedPreferences playerNameShared, playerEmailShared, playerScoreShared, playerEarningsShared, playerEarningsInNumShared, playerImageShared, playerReferralCodeShared, playerMemberSinceShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.app_lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        StartAppAd.disableSplash();
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
        MobileAds.initialize(InviteFriendsActivity.this, admobAppId.getString("admobAppId", ""));
        AD_UNIT_ZONE_Ids = new String[] {adcolonyBanner.getString("adcolonyBanner", ""), adcolonyInterstitial.getString("adcolonyInterstitial", ""), adcolonyReward.getString("adcolonyReward", "")};
        // Configure AdColony Ads
        AdColony.configure(InviteFriendsActivity.this, adcolonyAppId.getString("adcolonyAppId", ""), AD_UNIT_ZONE_Ids);
        playerReferralCodeShared = getSharedPreferences("playerReferralCodeShared", MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar_invite);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        queue = Volley.newRequestQueue(this);
        inviteFriendsGain = (TextView) findViewById(R.id.invite_friends_gain);
        YourRefferalCode = (TextView) findViewById(R.id.your_referal_code);
        TapToCopy = (TextView) findViewById(R.id.tap_to_copy);
        referNow = (Button) findViewById(R.id.refer_now_btn);
        userSituation = getSharedPreferences("userEmail",MODE_PRIVATE);
        spUserEmail = userSituation.getString("userEmail", "");
        getSettings();
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


    private void getSettings() {
        String url = getResources().getString(R.string.domain_name)+"/api/settings/all/"+getResources().getString(R.string.api_secret_key);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject settings = jsonArray.getJSONObject(i);
                                int referral_register_points = settings.getInt("referral_register_points");
                                inviteFriendsGain.setText(String.valueOf(referral_register_points) + " points");
                            }
                            YourRefferalCode.setText(playerReferralCodeShared.getString("playerReferralCodeShared", ""));
                            TapToCopy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("ReferralCode", playerReferralCodeShared.getString("playerReferralCodeShared", ""));
                                    clipboard.setPrimaryClip(clip);
                                    Toast.makeText(InviteFriendsActivity.this, "Code Copied.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            referNow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("text/plain");
                                    intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                                    intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.java_invite_friends_description) + getApplicationContext().getPackageName());
                                    startActivity(Intent.createChooser(intent, getString(R.string.java_invite_friends_title)));
                                }
                            });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

