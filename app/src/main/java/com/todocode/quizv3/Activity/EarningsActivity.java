package com.todocode.quizv3.Activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAdSize;
import com.adcolony.sdk.AdColonyAdView;
import com.adcolony.sdk.AdColonyAdViewListener;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyZone;
import com.codemybrainsout.ratingdialog.RatingDialog;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.adsbase.StartAppAd;
import com.todocode.quizv3.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

//import com.google.android.gms.ads.InterstitialAd;

public class EarningsActivity extends AppCompatActivity {
    private TextView avalaibleBalance, avalaiblePoints, minWithdraw, todayDate, referralCode, clickToCopy;
    private String url,spUserEmail, currency;
    private Button requestWithdrawalsBtn, withdrawalsHistory;
    private int userPointsInt;
    private SharedPreferences admobBanner,userSituation,facebookBanner, bottomBannerType, facebookInterstitial, admobInterstitial;
    private SharedPreferences adcolonyBanner,adcolonyInterstitial,adcolonyAppId,adcolonyReward,startappAppId, admobAppId;
    // AdColony Banner
    public AdColonyAdView adView;
    private LinearLayout adsLinear;
    public AdColonyAdOptions adColonyAdOptionsBanner;
    public static String[] AD_UNIT_ZONE_Ids;
    Banner startAppBanner;
    private AdView bannerAdmobAdView;
    public AdColonyInterstitial adColonyInterstitiall;
    public AdColonyAdOptions adColonyAdOptions;
    private InterstitialAd mInterstitialAd;
    private com.facebook.ads.InterstitialAd facebookInterstitialAd;
    private SharedPreferences interstitialTypeShared, playerEarningsInNumShared, playerEarningsShared, playerReferralCodeShared, playerScoreShared, minToWithdrawShared, currencyShared;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.app_lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        StartAppAd.disableSplash();
        setContentView(R.layout.activity_earnings);
        Toolbar toolbar = findViewById(R.id.toolbar_earnings);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        bottomBannerType = getSharedPreferences("bottomBannerType", MODE_PRIVATE);
        adcolonyBanner = getSharedPreferences("adcolonyBanner", MODE_PRIVATE);
        adcolonyInterstitial = getSharedPreferences("adcolonyInterstitial", MODE_PRIVATE);
        admobInterstitial = getSharedPreferences("admobInterstitial",MODE_PRIVATE);
        facebookInterstitial = getSharedPreferences("facebookInterstitial",MODE_PRIVATE);
        interstitialTypeShared = getSharedPreferences("interstitialTypeShared",MODE_PRIVATE);
        adcolonyAppId = getSharedPreferences("adcolonyAppId", MODE_PRIVATE);
        adcolonyReward = getSharedPreferences("adcolonyReward", MODE_PRIVATE);
        startappAppId = getSharedPreferences("startappAppId", MODE_PRIVATE);
        admobAppId = getSharedPreferences("admobAppId", MODE_PRIVATE);
        facebookBanner = getSharedPreferences("facebookBanner",MODE_PRIVATE);
        admobBanner = getSharedPreferences("admobBanner", MODE_PRIVATE);
        MobileAds.initialize(EarningsActivity.this, admobAppId.getString("admobAppId", ""));
        if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("facebook")) {
            prepareInterstitialAd();
        } else if(interstitialTypeShared.getString("interstitialTypeShared", "").equals("admob")) {
            prepareInterstitialAdmobAd();
        }
        AD_UNIT_ZONE_Ids = new String[] {adcolonyBanner.getString("adcolonyBanner", ""), adcolonyInterstitial.getString("adcolonyInterstitial", ""), adcolonyReward.getString("adcolonyReward", "")};
        // Configure AdColony Ads
        AdColony.configure(EarningsActivity.this, adcolonyAppId.getString("adcolonyAppId", ""), AD_UNIT_ZONE_Ids);
        playerEarningsInNumShared = getSharedPreferences("playerEarningsInNumShared", MODE_PRIVATE);
        minToWithdrawShared = getSharedPreferences("minToWithdrawShared", MODE_PRIVATE);
        currencyShared = getSharedPreferences("currencyShared", MODE_PRIVATE);
        playerEarningsShared = getSharedPreferences("playerEarningsShared", MODE_PRIVATE);
        playerScoreShared = getSharedPreferences("playerScoreShared", MODE_PRIVATE);
        playerReferralCodeShared = getSharedPreferences("playerReferralCodeShared", MODE_PRIVATE);
        userSituation = getSharedPreferences("userEmail",MODE_PRIVATE);
        avalaibleBalance = (TextView) findViewById(R.id.avalaible_balance);
        avalaiblePoints = (TextView) findViewById(R.id.avalaible_points);
        minWithdraw = (TextView) findViewById(R.id.min_points_to_withdraw);
        todayDate = (TextView) findViewById(R.id.today_date);
        referralCode = (TextView) findViewById(R.id.referral_code);
        requestWithdrawalsBtn = (Button) findViewById(R.id.request_withdrawals_btn);
        withdrawalsHistory = (Button) findViewById(R.id.withdrawals_history_btn);
        clickToCopy = (TextView) findViewById(R.id.click_here_to_copy);
        url = getResources().getString(R.string.domain_name);
        spUserEmail = userSituation.getString("userEmail", "");
        referralCode.setText(playerReferralCodeShared.getString("playerReferralCodeShared", ""));
        clickToCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("ReferralCode", playerReferralCodeShared.getString("playerReferralCodeShared", ""));
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(EarningsActivity.this, "Code Copied.", Toast.LENGTH_SHORT).show();
            }
        });
        avalaiblePoints.setText(playerScoreShared.getString("playerScoreShared", "") + " points");
        avalaibleBalance.setText(playerEarningsShared.getString("playerEarningsShared", ""));
        minWithdraw.setText("Minimum to Withdraw : "+minToWithdrawShared.getString("minToWithdrawShared", "") + currencyShared.getString("currencyShared", ""));
        withdrawalsHistory.setOnClickListener(new View.OnClickListener() {
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
                                Intent history = new Intent(EarningsActivity.this, WithdrawalsHistoryActivity.class);
                                history.putExtra("userEmail", spUserEmail);
                                startActivity(history);
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                Intent history = new Intent(EarningsActivity.this, WithdrawalsHistoryActivity.class);
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
                        Intent history = new Intent(EarningsActivity.this, WithdrawalsHistoryActivity.class);
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
                                Intent history = new Intent(EarningsActivity.this, WithdrawalsHistoryActivity.class);
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
                                Intent history = new Intent(EarningsActivity.this, WithdrawalsHistoryActivity.class);
                                history.putExtra("userEmail", spUserEmail);
                                startActivity(history);
                            }
                        });
                        prepareInterstitialAdmobAd();
                    } else {
                        Intent history = new Intent(EarningsActivity.this, WithdrawalsHistoryActivity.class);
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
                            Intent history = new Intent(EarningsActivity.this, WithdrawalsHistoryActivity.class);
                            history.putExtra("userEmail", spUserEmail);
                            startActivity(history);
                        }

                        @Override
                        public void onClosed(AdColonyInterstitial ad) {
                            Intent history = new Intent(EarningsActivity.this, WithdrawalsHistoryActivity.class);
                            history.putExtra("userEmail", spUserEmail);
                            startActivity(history);
                        }

                        @Override
                        public void onExpiring(AdColonyInterstitial ad) {
                            Intent history = new Intent(EarningsActivity.this, WithdrawalsHistoryActivity.class);
                            history.putExtra("userEmail", spUserEmail);
                            startActivity(history);
                        }
                    };
                    AdColony.requestInterstitial(adcolonyInterstitial.getString("adcolonyInterstitial", ""), listener, adColonyAdOptions);
                } else if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("startapp")) {
                    Intent history = new Intent(EarningsActivity.this, WithdrawalsHistoryActivity.class);
                    history.putExtra("userEmail", spUserEmail);
                    startActivity(history);
                    StartAppAd.showAd(EarningsActivity.this);
                }
            }
        });
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        todayDate.setText(formatter.format(date));
        requestWithdrawalsBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                if (Double.parseDouble(playerEarningsInNumShared.getString("playerEarningsInNumShared", null))>=Double.parseDouble(minToWithdrawShared.getString("minToWithdrawShared", null))) {
                    if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("facebook")) {
                        if (facebookInterstitialAd.isAdLoaded()) {
                            facebookInterstitialAd.show();
                            InterstitialAdListener listener = new InterstitialAdListener() {
                                @Override
                                public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                                }

                                @Override
                                public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                    Intent requestWithdrawal = new Intent(EarningsActivity.this, RequestWithdrawalActivity.class);
                                    startActivity(requestWithdrawal);
                                }

                                @Override
                                public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                    Intent requestWithdrawal = new Intent(EarningsActivity.this, RequestWithdrawalActivity.class);
                                    startActivity(requestWithdrawal);
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
                            Intent requestWithdrawal = new Intent(EarningsActivity.this, RequestWithdrawalActivity.class);
                            startActivity(requestWithdrawal);
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
                                    Intent requestWithdrawal = new Intent(EarningsActivity.this, RequestWithdrawalActivity.class);
                                    startActivity(requestWithdrawal);
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
                                    Intent requestWithdrawal = new Intent(EarningsActivity.this, RequestWithdrawalActivity.class);
                                    startActivity(requestWithdrawal);
                                }
                            });
                            prepareInterstitialAdmobAd();
                        } else {
                            Intent requestWithdrawal = new Intent(EarningsActivity.this, RequestWithdrawalActivity.class);
                            startActivity(requestWithdrawal);
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
                                Intent requestWithdrawal = new Intent(EarningsActivity.this, RequestWithdrawalActivity.class);
                                startActivity(requestWithdrawal);
                            }

                            @Override
                            public void onClosed(AdColonyInterstitial ad) {
                                Intent requestWithdrawal = new Intent(EarningsActivity.this, RequestWithdrawalActivity.class);
                                startActivity(requestWithdrawal);
                            }

                            @Override
                            public void onExpiring(AdColonyInterstitial ad) {
                                Intent requestWithdrawal = new Intent(EarningsActivity.this, RequestWithdrawalActivity.class);
                                startActivity(requestWithdrawal);
                            }
                        };
                        AdColony.requestInterstitial(adcolonyInterstitial.getString("adcolonyInterstitial", ""), listener, adColonyAdOptions);
                    } else if (interstitialTypeShared.getString("interstitialTypeShared", "").equals("startapp")) {
                        Intent requestWithdrawal = new Intent(EarningsActivity.this, RequestWithdrawalActivity.class);
                        startActivity(requestWithdrawal);
                        StartAppAd.showAd(EarningsActivity.this);
                    }
                } else {
                    Toast.makeText(EarningsActivity.this, "Minimum to Withdraw : "+minToWithdrawShared.getInt("minToWithdrawShared", 0) + currencyShared.getString("currencyShared", ""), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
