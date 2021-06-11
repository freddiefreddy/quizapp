package com.todocode.quizv3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codemybrainsout.ratingdialog.RatingDialog;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;
import com.startapp.sdk.ads.banner.Banner;
import com.todocode.quizv3.Adapter.AllPlayersAdapter;
import com.todocode.quizv3.Model.Player;
import com.todocode.quizv3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderboardsActivity extends AppCompatActivity {
    private CircleImageView firstPlayerImg, secondPlayerImg, tirthPlayerImg;
    private TextView firstPlayerName, secondPlayerName, tirthPlayerName;
    private String url, bannerBottomType;
    private RequestQueue queue;
    private AllPlayersAdapter playersAdapter;
    private RecyclerView playersRecyclerView;
    private ArrayList<Player> playersArrayList;
    private SharedPreferences adcolonyInterstitial, adcolonyAppId, adcolonyReward, startappAppId, admobAppId, adcolonyBanner, facebookBanner, admobBanner, admobInterstitial, bottomBannerType, facebookInterstitial;
    private AdView bannerAdmobAdView;
    public AdColonyAdView adView;
    public AdColonyAdOptions adColonyAdOptionsBanner;
    public static String[] AD_UNIT_ZONE_Ids;
    Banner startAppBanner;
    public LinearLayout adsLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.app_lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboards);
        Toolbar toolbar = findViewById(R.id.leaderboards_toolbar);
        toolbar.setTitle(getString(R.string.drawer_menu_leader));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        queue = Volley.newRequestQueue(this);
        bottomBannerType = getSharedPreferences("bottomBannerType", MODE_PRIVATE);
        adcolonyBanner = getSharedPreferences("adcolonyBanner", MODE_PRIVATE);
        startappAppId = getSharedPreferences("startappAppId", MODE_PRIVATE);
        admobAppId = getSharedPreferences("admobAppId", MODE_PRIVATE);
        adcolonyInterstitial = getSharedPreferences("adcolonyInterstitial", MODE_PRIVATE);
        adcolonyAppId = getSharedPreferences("adcolonyAppId", MODE_PRIVATE);
        adcolonyReward = getSharedPreferences("adcolonyReward", MODE_PRIVATE);
        firstPlayerImg = (CircleImageView) findViewById(R.id.first_player_image);
        firstPlayerName = (TextView) findViewById(R.id.first_player_name);
        secondPlayerImg = (CircleImageView) findViewById(R.id.second_player_image);
        secondPlayerName = (TextView) findViewById(R.id.second_player_name);
        tirthPlayerImg = (CircleImageView) findViewById(R.id.tirth_player_image);
        tirthPlayerName = (TextView) findViewById(R.id.tirth_player_name);
        url = getResources().getString(R.string.domain_name);
        playersRecyclerView = (RecyclerView) findViewById(R.id.all_players_recycler);
        playersArrayList = new ArrayList<>();
        playersAdapter = new AllPlayersAdapter(this, playersArrayList);
        playersRecyclerView.setAdapter(playersAdapter);
        playersRecyclerView.setHasFixedSize(true);
        playersRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        getFirstPlayer();
        getSecondsPlayer();
        getPlayersDesc();
        MobileAds.initialize(LeaderboardsActivity.this, admobAppId.getString("admobAppId", ""));
        AD_UNIT_ZONE_Ids = new String[] {adcolonyBanner.getString("adcolonyBanner", ""), adcolonyInterstitial.getString("adcolonyInterstitial", ""), adcolonyReward.getString("adcolonyReward", "")};
        // Configure AdColony Ads
        AdColony.configure(LeaderboardsActivity.this, adcolonyAppId.getString("adcolonyAppId", ""), AD_UNIT_ZONE_Ids);
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
            adsLinear = (LinearLayout) findViewById(R.id.banner_container_profile_activity);
            adsLinear.setVisibility(View.VISIBLE);
            adsLinear.addView(facebookAdView);
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

    private void getFirstPlayer() {
        String firstUrl = url+"/api/players/first";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, firstUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject player = jsonArray.getJSONObject(i);
                                String name = player.getString("name");
                                String imageUrl = player.getString("image");
                                int points = player.getInt("score");
                                String strPoint = String.valueOf(points);
                                firstPlayerName.setText("1/ "+name + " ("+ strPoint +")");
                                Picasso.get().load(imageUrl).fit().centerInside().into(firstPlayerImg);
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
        });
        queue.add(request);
    }

    private void getSecondsPlayer() {
        String secondsUrl = url+"/api/players/seconds";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, secondsUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            // Second Player Data
                            JSONObject secondPlayer = jsonArray.getJSONObject(0);
                            String name1 = secondPlayer.getString("name");
                            String imageUrl1 = secondPlayer.getString("image");
                            int points = secondPlayer.getInt("score");
                            String strPoint = String.valueOf(points);
                            secondPlayerName.setText("2/ "+name1 + " ("+ strPoint +")");
                            Picasso.get().load(imageUrl1).fit().centerInside().into(secondPlayerImg);
                            // Tirth Player Data
                            JSONObject tirthPlayer = jsonArray.getJSONObject(1);
                            String name2 = tirthPlayer.getString("name");
                            String imageUrl2 = tirthPlayer.getString("image");
                            int points2 = tirthPlayer.getInt("score");
                            String strPoint2 = String.valueOf(points2);
                            tirthPlayerName.setText("3/ "+name2 + " ("+ strPoint2 +")");
                            Picasso.get().load(imageUrl2).fit().centerInside().into(tirthPlayerImg);
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

    private void getPlayersDesc() {
        String allUrl = url+"/api/players/all/desc";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, allUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject player = jsonArray.getJSONObject(i);
                                String name = player.getString("name");
                                String email = player.getString("email");
                                String imageUrl = player.getString("image");
                                String memberSince = player.getString("member_since");
                                int points = player.getInt("score");
                                playersArrayList.add(new Player(name, email, memberSince, imageUrl, points));
                            }
                            playersAdapter.notifyDataSetChanged();
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
}
