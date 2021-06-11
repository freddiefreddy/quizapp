package com.todocode.quizv3.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codemybrainsout.ratingdialog.RatingDialog;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.todocode.quizv3.Adapter.CategoriesAdapter;
import com.todocode.quizv3.Adapter.PlayersAdapter;
import com.todocode.quizv3.Manager.MyApplication;
import com.todocode.quizv3.Model.Category;
import com.todocode.quizv3.Model.Player;
import com.todocode.quizv3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private InterstitialAd mInterstitialAd;
    private SharedPreferences fiftyFiftyOption, rewardVideoOption, questionTime , completedOption, admobInterstitial, facebookBanner, bottomBannerType, admobBanner, admobNative;
    private AdView bannerAdmobAdView;
    private LinearLayout adsLinear;
    private String bannerBottomType;
    private DrawerLayout drawer;
    private TextView currentUserName, currentEmail;
    private CircleImageView currentProfileImage;
    public String spUserEmail, userId, userName, userEmail, userImageUrl;
    SharedPreferences userSituationId, userSituation;
    GoogleSignInClient mGoogleSignInClient;
    private String url;
    ActionBarDrawerToggle toggle;
    private RequestQueue queue;
    private PlayersAdapter playersAdapter;
    private ArrayList<Player> playersArrayList;
    private CategoriesAdapter categoriesAdapter;
    private ArrayList<Category> categoriesArrayList;
    private String id, name;
    private UnifiedNativeAd nativeAd;
    private UnifiedNativeAdView adViewNative;
    private FrameLayout frameLayout;
    MyApplication mMyApplication;
    private SharedPreferences facebookInterstitial;
    private com.facebook.ads.InterstitialAd facebookInterstitialAd;
    private CircleImageView profile_image_main;
    private TextView main_username, main_email, my_balance;
    public SharedPreferences admobVideo, facebookNative, interstitialTypeShared, videoTypeShared, FbVideo, adcolonyBanner, adcolonyInterstitial,
            adcolonyAppId, adcolonyReward, startappAppId, admobAppId;
    // AdColony Banner
    public AdColonyAdView adView;
    public AdColonyAdOptions adColonyAdOptionsBanner;
    public static String[] AD_UNIT_ZONE_Ids;
    Banner startAppBanner;
    public AdColonyInterstitial adColonyInterstitiall;
    public AdColonyAdOptions adColonyAdOptions;
    private SharedPreferences currencyShared, minToWithdrawShared, playerNameShared, playerEmailShared, playerScoreShared, playerEarningsShared, playerEarningsInNumShared, playerImageShared, playerReferralCodeShared, playerMemberSinceShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.app_lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartAppAd.disableSplash();
        mMyApplication = MyApplication.getmInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        url = getResources().getString(R.string.domain_name);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        playerEarningsInNumShared = getSharedPreferences("playerEarningsInNumShared", MODE_PRIVATE);
        currencyShared = getSharedPreferences("currencyShared", MODE_PRIVATE);
        minToWithdrawShared = getSharedPreferences("minToWithdrawShared", MODE_PRIVATE);
        playerNameShared = getSharedPreferences("playerNameShared", MODE_PRIVATE);
        playerEmailShared = getSharedPreferences("playerEmailShared", MODE_PRIVATE);
        playerScoreShared = getSharedPreferences("playerScoreShared", MODE_PRIVATE);
        playerEarningsShared = getSharedPreferences("playerEarningsShared", MODE_PRIVATE);
        playerImageShared = getSharedPreferences("playerImageShared", MODE_PRIVATE);
        playerReferralCodeShared = getSharedPreferences("playerReferralCodeShared", MODE_PRIVATE);
        playerMemberSinceShared = getSharedPreferences("playerMemberSinceShared", MODE_PRIVATE);
        interstitialTypeShared = getSharedPreferences("interstitialTypeShared", MODE_PRIVATE);
        videoTypeShared = getSharedPreferences("videoTypeShared", MODE_PRIVATE);
        FbVideo = getSharedPreferences("FbVideo", MODE_PRIVATE);
        adcolonyBanner = getSharedPreferences("adcolonyBanner", MODE_PRIVATE);
        adcolonyInterstitial = getSharedPreferences("adcolonyInterstitial", MODE_PRIVATE);
        adcolonyAppId = getSharedPreferences("adcolonyAppId", MODE_PRIVATE);
        adcolonyReward = getSharedPreferences("adcolonyReward", MODE_PRIVATE);
        startappAppId = getSharedPreferences("startappAppId", MODE_PRIVATE);
        admobAppId = getSharedPreferences("admobAppId", MODE_PRIVATE);
        MobileAds.initialize(MainActivity.this, admobAppId.getString("admobAppId", ""));
        userSituation = getSharedPreferences("userEmail",MODE_PRIVATE);
        admobBanner = getSharedPreferences("admobBanner",MODE_PRIVATE);
        bottomBannerType = getSharedPreferences("bottomBannerType",MODE_PRIVATE);
        admobNative = getSharedPreferences("admobNative",MODE_PRIVATE);
        admobVideo = getSharedPreferences("admobVideo",MODE_PRIVATE);
        admobInterstitial = getSharedPreferences("admobInterstitial",MODE_PRIVATE);
        facebookBanner = getSharedPreferences("facebookBanner",MODE_PRIVATE);
        facebookNative = getSharedPreferences("facebookNative",MODE_PRIVATE);
        facebookInterstitial = getSharedPreferences("facebookInterstitial",MODE_PRIVATE);
        if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("facebook")) {
            prepareInterstitialAd();
        } else if(interstitialTypeShared.getString("interstitialTypeShared", "").equals("admob")) {
            prepareInterstitialAdmobAd();
        }
        AD_UNIT_ZONE_Ids = new String[] {adcolonyBanner.getString("adcolonyBanner", ""), adcolonyInterstitial.getString("adcolonyInterstitial", ""), adcolonyReward.getString("adcolonyReward", "")};
        // Configure AdColony Ads
        AdColony.configure(MainActivity.this, adcolonyAppId.getString("adcolonyAppId", ""), AD_UNIT_ZONE_Ids);
        View header = navigationView.getHeaderView(0);
        Button logout = (Button) header.findViewById(R.id.logout);
        fiftyFiftyOption = getSharedPreferences("fiftyFiftyOption", Context.MODE_PRIVATE);
        rewardVideoOption = getSharedPreferences("rewardVideoOption", Context.MODE_PRIVATE);
        completedOption = getSharedPreferences("completedOption", Context.MODE_PRIVATE);
        currencyShared = getSharedPreferences("currencyShared", Context.MODE_PRIVATE);
        questionTime = getSharedPreferences("seconds", MODE_PRIVATE);
        userSituationId = getSharedPreferences("userId",MODE_PRIVATE);
        userSituation = getSharedPreferences("userEmail",MODE_PRIVATE);
        admobNative = getSharedPreferences("admobNative",MODE_PRIVATE);
        currentUserName = (TextView) header.findViewById(R.id.current_user_name);
        currentEmail = (TextView) header.findViewById(R.id.current_user_email);
        currentProfileImage = (CircleImageView) header.findViewById(R.id.profile_image_header);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change Shared Preferences
                userSituation.edit().putString("userEmail", "").apply();
                userSituationId.edit().putString("userId", "").apply();
                // Logout Google
                if (mGoogleSignInClient != null) {
                    mGoogleSignInClient.signOut();
                }
                // Logout Facebook
                if (LoginManager.getInstance() != null) {
                    LoginManager.getInstance().logOut();
                }
                // Go To Login Page
                Intent loginPage = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginPage);
                finish();
            }
        });
        // Connected Player DATA
        spUserEmail = userSituation.getString("userEmail", null);
        currentUserName = (TextView) header.findViewById(R.id.current_user_name);
        currentEmail = (TextView) header.findViewById(R.id.current_user_email);
        currentProfileImage = (CircleImageView) header.findViewById(R.id.profile_image_header);
        profile_image_main = (CircleImageView) findViewById(R.id.profile_image_main);
        main_username = (TextView) findViewById(R.id.main_username);
        main_email = (TextView) findViewById(R.id.main_email);
        my_balance = (TextView) findViewById(R.id.my_balance);
        getConnectedUserData();
        queue = Volley.newRequestQueue(this);
        TextView viewAllCategories = (TextView) findViewById(R.id.view_all);
        TextView viewAllPlayers = (TextView) findViewById(R.id.view_all_2);
        // Get Top Players
        RecyclerView playersRecyclerView = (RecyclerView) findViewById(R.id.top_10_player_recycler);
        playersArrayList = new ArrayList<>();
        playersAdapter = new PlayersAdapter(this, playersArrayList);
        playersRecyclerView.setAdapter(playersAdapter);
        playersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        getTopPlayers();
        viewAllPlayers.setOnClickListener(new View.OnClickListener() {
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
                                Intent lead = new Intent(MainActivity.this, LeaderboardsActivity.class);
                                startActivity(lead);
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                Intent lead = new Intent(MainActivity.this, LeaderboardsActivity.class);
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
                        Intent lead = new Intent(MainActivity.this, LeaderboardsActivity.class);
                        startActivity(lead);
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
                                Intent lead = new Intent(MainActivity.this, LeaderboardsActivity.class);
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
                                Intent lead = new Intent(MainActivity.this, LeaderboardsActivity.class);
                                startActivity(lead);
                            }
                        });
                        prepareInterstitialAdmobAd();
                    } else {
                        Intent lead = new Intent(MainActivity.this, LeaderboardsActivity.class);
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
                            Intent lead = new Intent(MainActivity.this, LeaderboardsActivity.class);
                            startActivity(lead);
                        }

                        @Override
                        public void onClosed(AdColonyInterstitial ad) {
                            Intent lead = new Intent(MainActivity.this, LeaderboardsActivity.class);
                            startActivity(lead);
                        }

                        @Override
                        public void onExpiring(AdColonyInterstitial ad) {
                            Intent lead = new Intent(MainActivity.this, LeaderboardsActivity.class);
                            startActivity(lead);
                        }
                    };
                    AdColony.requestInterstitial(adcolonyInterstitial.getString("adcolonyInterstitial", ""), listener, adColonyAdOptions);
                } else if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("startapp")) {
                    Intent lead = new Intent(MainActivity.this, LeaderboardsActivity.class);
                    startActivity(lead);
                    StartAppAd.showAd(MainActivity.this);
                }
            }
        });
        // Get Featured Categories
        RecyclerView categoriesRecyclerView = (RecyclerView) findViewById(R.id.featured_categories_recycler);
        categoriesArrayList = new ArrayList<>();
        categoriesAdapter = new CategoriesAdapter(this, categoriesArrayList);
        categoriesRecyclerView.setAdapter(categoriesAdapter);
        NestedScrollView categoriesScroll = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        categoriesScroll.fullScroll(View.FOCUS_DOWN);
        categoriesScroll.setSmoothScrollingEnabled(false);
        categoriesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        getFeaturedCategories();
        // Single Item Click Listener
        viewAllCategories.setOnClickListener(new View.OnClickListener() {
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
                                Intent cat = new Intent(MainActivity.this, CategoriesActivity.class);
                                startActivity(cat);
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                Intent cat = new Intent(MainActivity.this, CategoriesActivity.class);
                                startActivity(cat);
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
                        Intent cat = new Intent(MainActivity.this, CategoriesActivity.class);
                        startActivity(cat);
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
                                Intent cat = new Intent(MainActivity.this, CategoriesActivity.class);
                                startActivity(cat);
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
                                Intent cat = new Intent(MainActivity.this, CategoriesActivity.class);
                                startActivity(cat);
                            }
                        });
                        prepareInterstitialAdmobAd();
                    } else {
                        Intent cat = new Intent(MainActivity.this, CategoriesActivity.class);
                        startActivity(cat);
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
                            Intent cat = new Intent(MainActivity.this, CategoriesActivity.class);
                            startActivity(cat);
                        }

                        @Override
                        public void onClosed(AdColonyInterstitial ad) {
                            Intent cat = new Intent(MainActivity.this, CategoriesActivity.class);
                            startActivity(cat);
                        }

                        @Override
                        public void onExpiring(AdColonyInterstitial ad) {
                            Intent cat = new Intent(MainActivity.this, CategoriesActivity.class);
                            startActivity(cat);
                        }
                    };
                    AdColony.requestInterstitial(adcolonyInterstitial.getString("adcolonyInterstitial", ""), listener, adColonyAdOptions);
                } else if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("startapp")) {
                    Intent cat = new Intent(MainActivity.this, CategoriesActivity.class);
                    startActivity(cat);
                    StartAppAd.showAd(MainActivity.this);
                }
            }
        });
        categoriesAdapter.setOnItemClickListener(new CategoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("facebook")) {
                    if (facebookInterstitialAd.isAdLoaded()) {
                        facebookInterstitialAd.show();
                        InterstitialAdListener listener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                id = categoriesArrayList.get(position).getId();
                                name = categoriesArrayList.get(position).getTitle();
                                Intent detailsIntent = new Intent(MainActivity.this, CategoryActivity.class);
                                detailsIntent.putExtra("categoryId", id);
                                detailsIntent.putExtra("categoryName", name);
                                startActivity(detailsIntent);
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                id = categoriesArrayList.get(position).getId();
                                name = categoriesArrayList.get(position).getTitle();
                                Intent detailsIntent = new Intent(MainActivity.this, CategoryActivity.class);
                                detailsIntent.putExtra("categoryId", id);
                                detailsIntent.putExtra("categoryName", name);
                                startActivity(detailsIntent);
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
                        id = categoriesArrayList.get(position).getId();
                        name = categoriesArrayList.get(position).getTitle();
                        Intent detailsIntent = new Intent(MainActivity.this, CategoryActivity.class);
                        detailsIntent.putExtra("categoryId", id);
                        detailsIntent.putExtra("categoryName", name);
                        startActivity(detailsIntent);
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
                                id = categoriesArrayList.get(position).getId();
                                name = categoriesArrayList.get(position).getTitle();
                                Intent detailsIntent = new Intent(MainActivity.this, CategoryActivity.class);
                                detailsIntent.putExtra("categoryId", id);
                                detailsIntent.putExtra("categoryName", name);
                                startActivity(detailsIntent);
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
                                id = categoriesArrayList.get(position).getId();
                                name = categoriesArrayList.get(position).getTitle();
                                Intent detailsIntent = new Intent(MainActivity.this, CategoryActivity.class);
                                detailsIntent.putExtra("categoryId", id);
                                detailsIntent.putExtra("categoryName", name);
                                startActivity(detailsIntent);
                            }
                        });
                        prepareInterstitialAdmobAd();
                    } else {
                        id = categoriesArrayList.get(position).getId();
                        name = categoriesArrayList.get(position).getTitle();
                        Intent detailsIntent = new Intent(MainActivity.this, CategoryActivity.class);
                        detailsIntent.putExtra("categoryId", id);
                        detailsIntent.putExtra("categoryName", name);
                        startActivity(detailsIntent);
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
                            id = categoriesArrayList.get(position).getId();
                            name = categoriesArrayList.get(position).getTitle();
                            Intent detailsIntent = new Intent(MainActivity.this, CategoryActivity.class);
                            detailsIntent.putExtra("categoryId", id);
                            detailsIntent.putExtra("categoryName", name);
                            startActivity(detailsIntent);
                        }

                        @Override
                        public void onClosed(AdColonyInterstitial ad) {
                            id = categoriesArrayList.get(position).getId();
                            name = categoriesArrayList.get(position).getTitle();
                            Intent detailsIntent = new Intent(MainActivity.this, CategoryActivity.class);
                            detailsIntent.putExtra("categoryId", id);
                            detailsIntent.putExtra("categoryName", name);
                            startActivity(detailsIntent);
                        }

                        @Override
                        public void onExpiring(AdColonyInterstitial ad) {
                            id = categoriesArrayList.get(position).getId();
                            name = categoriesArrayList.get(position).getTitle();
                            Intent detailsIntent = new Intent(MainActivity.this, CategoryActivity.class);
                            detailsIntent.putExtra("categoryId", id);
                            detailsIntent.putExtra("categoryName", name);
                            startActivity(detailsIntent);
                        }
                    };
                    AdColony.requestInterstitial(adcolonyInterstitial.getString("adcolonyInterstitial", ""), listener, adColonyAdOptions);
                } else if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("startapp")) {
                    id = categoriesArrayList.get(position).getId();
                    name = categoriesArrayList.get(position).getTitle();
                    Intent detailsIntent = new Intent(MainActivity.this, CategoryActivity.class);
                    detailsIntent.putExtra("categoryId", id);
                    detailsIntent.putExtra("categoryName", name);
                    startActivity(detailsIntent);
                    StartAppAd.showAd(MainActivity.this);
                }
            }
        });
        // Native Admob
        adViewNative = (UnifiedNativeAdView) this.getLayoutInflater()
                .inflate(R.layout.admob_native_ad_unified, null);
        frameLayout = findViewById(R.id.native_ad_home);
        refreshAd();
        // Admob Banner Bottom
        bannerBottomType = bottomBannerType.getString("bottomBannerType", "");
        if (bannerBottomType.equals("admob")) {
            admobBanner = getSharedPreferences("admobBanner",MODE_PRIVATE);
            adsLinear = (LinearLayout) findViewById(R.id.banner_container_main_activity);
            adsLinear.setVisibility(View.VISIBLE);
            bannerAdmobAdView = new AdView(this);
            bannerAdmobAdView.setAdUnitId(admobBanner.getString("admobBanner", ""));
            bannerAdmobAdView.setAdSize(AdSize.FULL_BANNER);
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
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container_main_activity);
            adContainer.setVisibility(View.VISIBLE);
            adContainer.addView(facebookAdView);
            AdSettings.addTestDevice("c377b922-aa02-4425-a50b-af7a1f1ee1b6");
            facebookAdView.loadAd();
        }else if(bannerBottomType.equals("adcolony")) {
            final LinearLayout linearBannerAdContainer = (LinearLayout) findViewById(R.id.banner_container_main_activity);
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

    private void prepareInterstitialAd() {
        AudienceNetworkAds.initialize(this);
        facebookInterstitialAd = new com.facebook.ads.InterstitialAd(this, facebookInterstitial.getString("facebookInterstitial", ""));
        AdSettings.addTestDevice("c377b922-aa02-4425-a50b-af7a1f1ee1b6");
        facebookInterstitialAd.loadAd();
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd);
        VideoController vc = nativeAd.getVideoController();
        if (vc.hasVideoContent()) {
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        }
    }

    private void refreshAd() {
        AdLoader.Builder builder = new AdLoader.Builder(this,admobNative.getString("admobNative", null) );
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            // OnUnifiedNativeAdLoadedListener implementation.
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = unifiedNativeAd;
                populateUnifiedNativeAdView(unifiedNativeAd, adViewNative);
                frameLayout.removeAllViews();
                frameLayout.addView(adViewNative);
            }

        });
        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void getConnectedUserData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url+"/api/players/getplayerdata", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    final int question_time = jsonObject.getInt("question_time");
                    final String completedOptionStr = jsonObject.getString("completed_option");
                    final String fiftyFiftyOptionStr = jsonObject.getString("fifty_fifty");
                    final String rewardVideoOptionStr = jsonObject.getString("video_reward");
                    final double minToWithdraw = jsonObject.getDouble("min_to_withdraw");
                    final String currency = jsonObject.getString("currency");
                    final double earningsNum = jsonObject.getDouble("earnings_num");
                    playerEarningsInNumShared.edit().putString("playerEarningsInNumShared", String.valueOf(earningsNum)).apply();
                    Log.e("earnings", String.valueOf(earningsNum));
                    currencyShared.edit().putString("currencyShared", currency).apply();
                    minToWithdrawShared.edit().putString("minToWithdrawShared", String.valueOf(minToWithdraw)).apply();
                    questionTime.edit().putInt("seconds", question_time).apply();
                    completedOption.edit().putString("completedOption", completedOptionStr).apply();
                    fiftyFiftyOption.edit().putString("fiftyFiftyOption", fiftyFiftyOptionStr).apply();
                    rewardVideoOption.edit().putString("rewardVideoOption", rewardVideoOptionStr).apply();
                    userName = jsonObject.getString("name");
                    playerNameShared.edit().putString("playerNameShared", userName).apply();
                    userEmail = jsonObject.getString("email");
                    playerEmailShared.edit().putString("playerEmailShared", userEmail).apply();
                    userId = String.valueOf(jsonObject.getInt("id"));
                    userImageUrl = jsonObject.getString("image");
                    playerImageShared.edit().putString("playerImageShared", userImageUrl).apply();
                    String userEarnings = jsonObject.getString("earnings");
                    playerEarningsShared.edit().putString("playerEarningsShared", userEarnings).apply();
                    int userScore = jsonObject.getInt("score");
                    playerScoreShared.edit().putString("playerScoreShared", String.valueOf(userScore)).apply();
                    String userReferral = jsonObject.getString("referral_code");
                    playerReferralCodeShared.edit().putString("playerReferralCodeShared", userReferral).apply();
                    String memberSince = jsonObject.getString("member_since");
                    playerMemberSinceShared.edit().putString("playerMemberSinceShared", memberSince).apply();
                    // Register User ID In Shared Prefs
                    userSituationId.edit().putString("userId", userId).apply();
                    userSituation.edit().putString("userEmail", userEmail).apply();
                    // Set Header User Infos
                    currentUserName.setText(userName);
                    currentEmail.setText(userEmail);
                    Picasso.get().load(userImageUrl).fit().centerInside().into(currentProfileImage);
                    // Set Banner Home
                    Picasso.get().load(userImageUrl).fit().centerInside().into(profile_image_main);
                    main_username.setText(userName);
                    main_email.setText(userEmail);
                    my_balance.setText(userEarnings);
                } catch(JSONException e){
                    Log.e("Error ", e.getMessage());
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
                params.put("email", spUserEmail);
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

    private void getTopPlayers() {
        String url = getResources().getString(R.string.domain_name)+"/api/players/top10";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
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
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void getFeaturedCategories() {
        String url = getResources().getString(R.string.domain_name)+"/api/categories/featured";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject category = jsonArray.getJSONObject(i);
                                String name = category.getString("name");
                                String imageUrl = category.getString("imageUrl");
                                String id = String.valueOf(category.getInt("id"));
                                categoriesArrayList.add(new Category(name, imageUrl,id));
                            }
                            categoriesAdapter.notifyDataSetChanged();
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.statistics :
                Intent stats = new Intent(MainActivity.this, StatisticsActivity.class);
                startActivity(stats);
                break;
            case R.id.google :
                Intent browse = new Intent(MainActivity.this, WebViewActivity.class);
                startActivity(browse);
                break;
            case R.id.instructions :
                Intent instr = new Intent(MainActivity.this, InstructionsActivity.class);
                startActivity(instr);
                break;
            case R.id.categories :
                Intent cat = new Intent(MainActivity.this, CategoriesActivity.class);
                startActivity(cat);
                break;
            case R.id.profile :
                Intent prof = new Intent(MainActivity.this, ProfileActivity.class);
                prof.putExtra("name", playerNameShared.getString("playerNameShared", ""));
                prof.putExtra("email", playerEmailShared.getString("playerEmailShared", ""));
                prof.putExtra("earnings", playerEarningsShared.getString("playerEarningsShared", ""));
                prof.putExtra("score", playerScoreShared.getString("playerScoreShared", ""));
                prof.putExtra("referral", playerReferralCodeShared.getString("playerReferralCodeShared", ""));
                prof.putExtra("image", playerImageShared.getString("playerImageShared", ""));
                prof.putExtra("member_since", playerMemberSinceShared.getString("playerMemberSinceShared", ""));
                startActivity(prof);
                break;
            case R.id.ranking :
                Intent leader = new Intent(MainActivity.this, LeaderboardsActivity.class);
                startActivity(leader);
                break;
            case R.id.privacy :
                Intent priv = new Intent(MainActivity.this, PrivacyActivity.class);
                startActivity(priv);
                break;
            case R.id.terms_of_use :
                Intent terms = new Intent(MainActivity.this, TermsOfUseActivity.class);
                startActivity(terms);
                break;
            case R.id.invite_friends :
                Intent inviteIntent = new Intent(MainActivity.this, InviteFriendsActivity.class);
                startActivity(inviteIntent);
                break;
            case R.id.report :
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Report a Bug");
                builder.setMessage("To report a bug or a problem in this application, please contact us via Email" +"\n\n Thank You!");
                builder.setPositiveButton("Send Email",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent emailSelectorIntent = new Intent(Intent.ACTION_SENDTO);
                                emailSelectorIntent.setData(Uri.parse("mailto:"));
                                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email)});
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.drawer_menu_report_bug));
                                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                emailIntent.setSelector( emailSelectorIntent );
                                if( emailIntent.resolveActivity(getPackageManager()) != null )
                                    startActivity(emailIntent);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.share :
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_TEXT, "Download this APP From : http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                startActivity(Intent.createChooser(intent, "Share Now"));
                break;
            case R.id.contact_us :
                Intent emailSelectorIntent = new Intent(Intent.ACTION_SENDTO);
                emailSelectorIntent.setData(Uri.parse("mailto:"));

                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email)});
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                emailIntent.setSelector( emailSelectorIntent );
                if( emailIntent.resolveActivity(getPackageManager()) != null )
                    startActivity(emailIntent);
                break;
            case R.id.rate :
                smartRating();
                break;
            case R.id.exit :
                finishAndRemoveTask();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lang_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options_rate:
                smartRating();
                return true;
            case R.id.options_profile:
                Intent prof = new Intent(MainActivity.this, ProfileActivity.class);
                prof.putExtra("name", playerNameShared.getString("playerNameShared", ""));
                prof.putExtra("email", playerEmailShared.getString("playerEmailShared", ""));
                prof.putExtra("earnings", playerEarningsShared.getString("playerEarningsShared", ""));
                prof.putExtra("score", playerScoreShared.getString("playerScoreShared", ""));
                prof.putExtra("referral", playerReferralCodeShared.getString("playerReferralCodeShared", ""));
                prof.putExtra("image", playerImageShared.getString("playerImageShared", ""));
                prof.putExtra("member_since", playerMemberSinceShared.getString("playerMemberSinceShared", ""));
                startActivity(prof);
                return true;
        }
        if (toggle.onOptionsItemSelected(item)) {
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

    private void prepareInterstitialAdmobAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(admobInterstitial.getString("admobInterstitial", ""));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}