package com.todocode.quizv3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import android.widget.ScrollView;

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
import com.startapp.sdk.ads.banner.Banner;
import com.todocode.quizv3.Adapter.AllCategoriesAdapter;
import com.todocode.quizv3.Model.Category;
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

public class CategoriesActivity extends AppCompatActivity {
    String url;
    private RequestQueue queue;
    private RecyclerView categoriesRecyclerView;
    private AllCategoriesAdapter categoriesAdapter;
    private ArrayList<Category> categoriesArrayList;
    private ScrollView categoriesScroll;
    private SharedPreferences facebookBanner, admobBanner, bottomBannerType, admobInterstitial, facebookInterstitial;
    private AdView bannerAdmobAdView;
    private LinearLayout adsLinear;
    private String id, name, bannerBottomType;
    private SharedPreferences adcolonyBanner,adcolonyInterstitial,adcolonyAppId,adcolonyReward,startappAppId, admobAppId;
    // AdColony Banner
    public AdColonyAdView adView;
    public AdColonyAdOptions adColonyAdOptionsBanner;
    public static String[] AD_UNIT_ZONE_Ids;
    Banner startAppBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.app_lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = findViewById(R.id.categories_toolbar);
        toolbar.setTitle(getString(R.string.drawer_menu_categories));
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
        AdColony.configure(CategoriesActivity.this, adcolonyAppId.getString("adcolonyAppId", ""), AD_UNIT_ZONE_Ids);
        url = getResources().getString(R.string.domain_name);
        queue = Volley.newRequestQueue(this);
        categoriesRecyclerView = (RecyclerView) findViewById(R.id.all_categories_recycler);
        categoriesArrayList = new ArrayList<>();
        categoriesAdapter = new AllCategoriesAdapter(this, categoriesArrayList);
        categoriesRecyclerView.setAdapter(categoriesAdapter);
        categoriesScroll = (ScrollView) findViewById(R.id.categories_scroll);
        categoriesScroll.fullScroll(View.FOCUS_DOWN);
        categoriesScroll.setSmoothScrollingEnabled(false);
        categoriesRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        // Single Item Click Listener
        categoriesAdapter.setOnItemClickListener(new AllCategoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                id = categoriesArrayList.get(position).getId();
                name = categoriesArrayList.get(position).getTitle();
                Intent detailsIntent = new Intent(CategoriesActivity.this, CategoryActivity.class);
                detailsIntent.putExtra("categoryId", id);
                detailsIntent.putExtra("categoryName", name);
                startActivity(detailsIntent);
            }
        });
        getAllCategories();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void getAllCategories() {
        String allUrl = url+"/api/categories/all";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, allUrl, null,
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
}

