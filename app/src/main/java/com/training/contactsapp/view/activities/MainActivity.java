package com.training.contactsapp.view.activities;

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

public class MainActivity extends Activity {
    private Handler mStartHandler;

    private LinearLayout mMainLinearLayout;

    private TextView mNameOfApplicationTextView1;
    private TextView mNameOfApplicationTextView2;
    private TextView mNameOfApplicationTextView3;
    private TextView mAppVersionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainLinearLayout = (LinearLayout) findViewById(R.id.main_linear_layout);
        mNameOfApplicationTextView1 = new TextView(MainActivity.this);
        setTextViewProperties(mNameOfApplicationTextView1, R.id.name_of_application1, getResources().getString(R.string.name_of_application_string1), R.color.special_white, 50);
        mNameOfApplicationTextView2 = new TextView(MainActivity.this);
        setTextViewProperties(mNameOfApplicationTextView2, R.id.name_of_application2, getResources().getString(R.string.name_of_application_string2), R.color.special_white, 50);
        mNameOfApplicationTextView3 = new TextView(MainActivity.this);
        setTextViewProperties(mNameOfApplicationTextView3, R.id.name_of_application3, getResources().getString(R.string.name_of_application_string3), R.color.special_white, 50);

        String appVersionName = null;
        try {
            appVersionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mAppVersionTextView = new TextView(this);
        setTextViewProperties(mAppVersionTextView, R.id.version_of_application, appVersionName, R.color.darker_special_white, 30);

        mMainLinearLayout.addView(mNameOfApplicationTextView1);
        mMainLinearLayout.addView(mNameOfApplicationTextView2);
        mMainLinearLayout.addView(mNameOfApplicationTextView3);
        mMainLinearLayout.addView(mAppVersionTextView);

        mStartHandler = new Handler();

        mStartHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mNameOfApplicationTextView1.setVisibility(View.VISIBLE);
                mNameOfApplicationTextView1.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left));
            }
        }, 200);

        mStartHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mNameOfApplicationTextView2.setVisibility(View.VISIBLE);
                mNameOfApplicationTextView2.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left));
            }
        }, 700);

        mStartHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mNameOfApplicationTextView3.setVisibility(View.VISIBLE);
                mNameOfApplicationTextView3.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left));
            }
        }, 1200);

        mStartHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAppVersionTextView.setVisibility(View.VISIBLE);
                mAppVersionTextView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left));
            }
        }, 1700);

        mStartHandler.postDelayed(new Runnable() {
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
        startActivity(new Intent(MainActivity.this, ContactListActivity.class));
        finish();
    }

}
