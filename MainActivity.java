package com.training.davidd.contactsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends Activity {
    private static Context thisContext;
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private Handler delayedStartHandler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // MAKE COMMIT

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thisContext = MainActivity.this;

        String appVersionName = null;

        try {
            appVersionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView appVersionTextView = (TextView) findViewById(R.id.version_of_application);
        appVersionTextView.setText(appVersionName);


        delayedStartHandler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                startNextActivity();
            }
        };
        delayedStartHandler.postDelayed(runnable, SPLASH_DISPLAY_LENGTH);


        Button enterButton = new Button(this);
        enterButton.setText(R.string.enter_button);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delayedStartHandler.removeCallbacks(runnable);
                startNextActivity();
            }
        });

        RelativeLayout mainRelativeLayout = (RelativeLayout) findViewById(R.id.mainRelativeLayout);
        RelativeLayout.LayoutParams enterButtonLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        enterButtonLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        enterButtonLayoutParams.addRule(RelativeLayout.BELOW, R.id.version_of_application);

        mainRelativeLayout.addView(enterButton, enterButtonLayoutParams);

    }

    private void startNextActivity() {
        Intent intent = new Intent(thisContext, ContactListActivity.class);
        startActivity(intent);
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
