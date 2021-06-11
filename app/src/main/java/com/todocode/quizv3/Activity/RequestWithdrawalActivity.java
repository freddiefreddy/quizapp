package com.todocode.quizv3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.todocode.quizv3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RequestWithdrawalActivity extends AppCompatActivity {
    private EditText paymentInfos;
    private Spinner spinnerPaymentMethod;
    private Button sendRequestBtn;
    ArrayList<String> methods;
    private RequestQueue queue;
    SharedPreferences userSituationId, playerScoreShared, playerEarningsInNumShared;
    private String userId, selectedSpinnerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Locale locale = new Locale(getApplicationContext().getResources().getString(R.string.app_lang));
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_withdrawal);
        Toolbar toolbar = findViewById(R.id.toolbar_withdrawals_request);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        queue = Volley.newRequestQueue(this);
        userSituationId = getSharedPreferences("userId",MODE_PRIVATE);
        playerScoreShared = getSharedPreferences("playerScoreShared",MODE_PRIVATE);
        playerEarningsInNumShared = getSharedPreferences("playerEarningsInNumShared",MODE_PRIVATE);
        userId = userSituationId.getString("userId", "");
        paymentInfos = (EditText) findViewById(R.id.payment_infos_edittext);
        spinnerPaymentMethod = (Spinner) findViewById(R.id.spinner_payment_methods);
        sendRequestBtn = (Button) findViewById(R.id.send_request_button);
        getPaymentMethods();
        sendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedSpinnerItem = spinnerPaymentMethod.getSelectedItem().toString();
                sendNewRequest();
            }
        });
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

    private void getPaymentMethods() {
        String url = getResources().getString(R.string.domain_name)+"/api/payments/methods/all";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            methods = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject method = jsonArray.getJSONObject(i);
                                String payment_method = method.getString("method");
                                methods.add(payment_method);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                    android.R.layout.simple_spinner_item, methods);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerPaymentMethod.setAdapter(adapter);
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

    private void sendNewRequest() {
        final String enteredPaymentInfos = this.paymentInfos.getText().toString();
        String url = getResources().getString(R.string.domain_name)+"/api/withdrawals/request/new";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if (success.equals("1")){
                        sendRequestBtn.setVisibility(View.GONE);
                        Toast.makeText(RequestWithdrawalActivity.this, getString(R.string.java_question_request_withdrawal), Toast.LENGTH_LONG).show();
                        Intent main = new Intent(RequestWithdrawalActivity.this, MainActivity.class);
                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(main);
                        finishAffinity();
                    }
                } catch(JSONException e){
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
                params.put("account", enteredPaymentInfos);
                params.put("method", selectedSpinnerItem);
                params.put("points", playerScoreShared.getString("playerScoreShared", ""));
                params.put("key", getResources().getString(R.string.api_secret_key));
                params.put("amount", playerEarningsInNumShared.getString("playerEarningsInNumShared", ""));
                Log.e("amount", playerEarningsInNumShared.getString("playerEarningsInNumShared", ""));
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
}

