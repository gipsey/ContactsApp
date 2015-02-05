package com.training.contactsapp.presentation.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.training.contactsapp.R;

public class SplashActivity extends Activity {
    private TextView mNameOfApplicationTextView1stPart;
    private TextView mNameOfApplicationTextView2ndPart;
    private TextView mNameOfApplicationTextView3rdPart;
    private TextView mAppVersionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LinearLayout mainLinearLayout = (LinearLayout) findViewById(R.id.main_linear_layout);
        mNameOfApplicationTextView1stPart = new TextView(SplashActivity.this);
        setTextViewProperties(mNameOfApplicationTextView1stPart, R.id.name_of_application1, getResources().getString(R.string.name_of_application_string1), R.color.special_white, 50);
        mNameOfApplicationTextView2ndPart = new TextView(SplashActivity.this);
        setTextViewProperties(mNameOfApplicationTextView2ndPart, R.id.name_of_application2, getResources().getString(R.string.name_of_application_string2), R.color.special_white, 50);
        mNameOfApplicationTextView3rdPart = new TextView(SplashActivity.this);
        setTextViewProperties(mNameOfApplicationTextView3rdPart, R.id.name_of_application3, getResources().getString(R.string.name_of_application_string3), R.color.special_white, 50);

        String appVersionName = null;
        try {
            appVersionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mAppVersionTextView = new TextView(this);
        setTextViewProperties(mAppVersionTextView, R.id.version_of_application, appVersionName, R.color.darker_special_white, 30);

        mainLinearLayout.addView(mNameOfApplicationTextView1stPart);
        mainLinearLayout.addView(mNameOfApplicationTextView2ndPart);
        mainLinearLayout.addView(mNameOfApplicationTextView3rdPart);
        mainLinearLayout.addView(mAppVersionTextView);

        Handler startHandler = new Handler();

        startHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mNameOfApplicationTextView1stPart.setVisibility(View.VISIBLE);
                mNameOfApplicationTextView1stPart.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, android.R.anim.slide_in_left));
            }
        }, 200);

        startHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mNameOfApplicationTextView2ndPart.setVisibility(View.VISIBLE);
                mNameOfApplicationTextView2ndPart.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, android.R.anim.slide_in_left));
            }
        }, 700);

        startHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mNameOfApplicationTextView3rdPart.setVisibility(View.VISIBLE);
                mNameOfApplicationTextView3rdPart.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, android.R.anim.slide_in_left));
            }
        }, 1200);

        startHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAppVersionTextView.setVisibility(View.VISIBLE);
                mAppVersionTextView.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, android.R.anim.slide_in_left));
            }
        }, 1700);

        startHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startNextActivity();
            }
        }, 2000);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setTextViewProperties(TextView textView, int id, String text, int colorId, int textSize) {
        textView.setId(id);
        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setTextColor(getResources().getColor(colorId));
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setVisibility(View.INVISIBLE);
        textView.setGravity(Gravity.CENTER);
    }

    private void startNextActivity() {
        startActivity(new Intent(SplashActivity.this, ContactListActivity.class));
        finish();
    }

}
