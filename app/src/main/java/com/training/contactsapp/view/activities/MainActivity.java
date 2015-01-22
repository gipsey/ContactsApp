package com.training.contactsapp.view.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.training.contactsapp.R;


public class MainActivity extends Activity {
    private Handler startHandler;
    private Runnable runnable;

    private LinearLayout mainLinearLayout;

    private TextView nameOfApplicationTextView1;
    private TextView nameOfApplicationTextView2;
    private TextView nameOfApplicationTextView3;
    private TextView appVersionTextView;
//    private Button enterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLinearLayout = (LinearLayout) findViewById(R.id.main_linear_layout);

        // TextViews
        nameOfApplicationTextView1 = new TextView(MainActivity.this);
        setTextViewProperties(nameOfApplicationTextView1, R.id.name_of_application1, getResources().getString(R.string.name_of_application_string1), R.color.special_white, 50);

        nameOfApplicationTextView2 = new TextView(MainActivity.this);
        setTextViewProperties(nameOfApplicationTextView2, R.id.name_of_application2, getResources().getString(R.string.name_of_application_string2), R.color.special_white, 50);

        nameOfApplicationTextView3 = new TextView(MainActivity.this);
        setTextViewProperties(nameOfApplicationTextView3, R.id.name_of_application3, getResources().getString(R.string.name_of_application_string3), R.color.special_white, 50);

        String appVersionName = null;
        try {
            appVersionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        appVersionTextView = new TextView(this);
        setTextViewProperties(appVersionTextView, R.id.version_of_application, appVersionName, R.color.darker_special_white, 30);

        // Button
//        RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        buttonLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//
//        enterButton = new Button(this);
//        enterButton.setText(R.string.enter_button);
//        enterButton.setLayoutParams(buttonLayoutParams);
//        enterButton.setEnabled(false);
//        enterButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startHandler.removeCallbacks(runnable);
//                startNextActivity();
//            }
//        });

        mainLinearLayout.addView(nameOfApplicationTextView1);
        mainLinearLayout.addView(nameOfApplicationTextView2);
        mainLinearLayout.addView(nameOfApplicationTextView3);
        mainLinearLayout.addView(appVersionTextView);
//        mainRelativeLayout.addView(enterButton);

        // Handler and thread
        startHandler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                startNextActivity();
            }
        };

        startHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nameOfApplicationTextView1.setVisibility(View.VISIBLE);
            }
        }, 200);
        startHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nameOfApplicationTextView2.setVisibility(View.VISIBLE);
            }
        }, 700);
        startHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nameOfApplicationTextView3.setVisibility(View.VISIBLE);
            }
        }, 1200);
        startHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                appVersionTextView.setVisibility(View.VISIBLE);
//                enterButton.setEnabled(true);
            }
        }, 1700);
        startHandler.postDelayed(runnable, 2000);
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

//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.addRule(LinearLayout.CENTER_HORIZONTAL);
//        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
//
//        if (whatIsAbove != -1) layoutParams.addRule(RelativeLayout.BELOW, whatIsAbove);
//        textView.setLayoutParams(layoutParams);
    }

    private void startNextActivity() {
        startActivity(new Intent(MainActivity.this, ContactListActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
