package com.training.contactsapp.view.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.training.contactsapp.R;
import com.training.contactsapp.model.User;

public class ContactDetailsActivity extends ActionBarActivity implements View.OnClickListener {
    public static final String USER_TAG = "USER";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(USER_TAG);
        String saveStatus = intent.getStringExtra(ContactEditActivity.SAVE_STATUS);
        if (saveStatus != null) {
            Toast.makeText(this, saveStatus, Toast.LENGTH_LONG).show();
        }

        createUI();
    }

    private void createUI() {
        LinearLayout mainLinearLayout = (LinearLayout) findViewById(R.id.contact_details_main_linear_layout);

        // NAME
        TextView nameTextView = (TextView) getLayoutInflater().inflate(R.layout.label_text_view_style, null);
        nameTextView.setText(R.string.name_text_view);

        TextView nameValueTextView = new TextView(this);
        nameValueTextView.setText(user.getName());
        nameValueTextView.setTextSize(30);

        // PHONE NUMBER
        TextView phoneNumberTextView = (TextView) getLayoutInflater().inflate(R.layout.label_text_view_style, null);
        phoneNumberTextView.setText(R.string.phone_number_text_view);


        TextView phoneNumberValueTextView = new TextView(this);
        phoneNumberValueTextView.setText(user.getPhoneNumber());
        phoneNumberValueTextView.setTextSize(30);
        LinearLayout.LayoutParams phoneNumberLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        phoneNumberValueTextView.setLayoutParams(phoneNumberLayoutParams);

        Button callButton = new Button(this);
        callButton.setId(R.id.call_button);
        callButton.setText(R.string.call_button);
        callButton.setOnClickListener(this);

        LinearLayout phoneNumberLinearLayout = new LinearLayout(this);

        phoneNumberLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        phoneNumberLinearLayout.addView(phoneNumberValueTextView);
        phoneNumberLinearLayout.addView(callButton);


        // EMAIL
        TextView emailTextView = (TextView) getLayoutInflater().inflate(R.layout.label_text_view_style, null);
        emailTextView.setText(R.string.email_text_view);

        TextView emailValueTextView = new TextView(this);
        emailValueTextView.setText(user.getEmail());
        emailValueTextView.setTextSize(30);
        LinearLayout.LayoutParams emailValueLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        emailValueTextView.setLayoutParams(emailValueLayoutParams);

        Button sendButton = new Button(this);
        sendButton.setId(R.id.send_button);
        sendButton.setText(R.string.send_button);
        sendButton.setOnClickListener(this);

        LinearLayout emailLinearLayout = new LinearLayout(this);
        emailLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        emailLinearLayout.addView(emailValueTextView);
        emailLinearLayout.addView(sendButton);


        // DOB
        TextView dobTextView = (TextView) getLayoutInflater().inflate(R.layout.label_text_view_style, null);
        dobTextView.setText(R.string.dob_text_view);

        TextView dobValueTextView = new TextView(this);
        dobValueTextView.setText(user.getDob());
        dobValueTextView.setTextSize(30);


        // ADDRESS
        TextView addressTextView = (TextView) getLayoutInflater().inflate(R.layout.label_text_view_style, null);
        addressTextView.setText(R.string.address_text_view);

        TextView addressValueTextView = new TextView(this);
        addressValueTextView.setText(user.getAddress());
        addressValueTextView.setTextSize(30);
        LinearLayout.LayoutParams addressValueLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        addressValueTextView.setLayoutParams(addressValueLayoutParams);

        Button lookButton = new Button(this);
        lookButton.setId(R.id.look_button);
        lookButton.setText(R.string.look_button);
        lookButton.setOnClickListener(this);

        LinearLayout addressLinearLayout = new LinearLayout(this);
        addressLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        addressLinearLayout.addView(addressValueTextView);
        addressLinearLayout.addView(lookButton);


        // WEBSITE
        TextView websiteTextView = (TextView) getLayoutInflater().inflate(R.layout.label_text_view_style, null);
        websiteTextView.setText(R.string.website_text_view);

        TextView websiteValueTextView = new TextView(this);
        websiteValueTextView.setText(user.getWebsite());
        websiteValueTextView.setTextSize(30);
        LinearLayout.LayoutParams websiteValueLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        websiteValueTextView.setLayoutParams(websiteValueLayoutParams);

        Button visitButton = new Button(this);
        visitButton.setId(R.id.visit_button);
        visitButton.setText(R.string.visit_button);
        visitButton.setOnClickListener(this);

        LinearLayout websiteLinearLayout = new LinearLayout(this);
        websiteLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        websiteLinearLayout.addView(websiteValueTextView);
        websiteLinearLayout.addView(visitButton);


        // ADD VIEWS TO MAIN LINEAR LAYOUT
        mainLinearLayout.addView(nameTextView);
        mainLinearLayout.addView(nameValueTextView);
        mainLinearLayout.addView(phoneNumberTextView);
        mainLinearLayout.addView(phoneNumberLinearLayout);
        mainLinearLayout.addView(emailTextView);
        mainLinearLayout.addView(emailLinearLayout);
        mainLinearLayout.addView(dobTextView);
        mainLinearLayout.addView(dobValueTextView);
        mainLinearLayout.addView(addressTextView);
        mainLinearLayout.addView(addressLinearLayout);
        mainLinearLayout.addView(websiteTextView);
        mainLinearLayout.addView(websiteLinearLayout);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.call_button) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri phoneNumberUri = Uri.parse("tel:" + user.getPhoneNumber());
            intent.setData(phoneNumberUri);
            startActivity(intent);
        } else if (v.getId() == R.id.send_button) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{user.getEmail()});
            startActivity(intent);
        } else if (v.getId() == R.id.look_button) {
            if (user.getAddress() == null || user.getAddress().isEmpty()) {
                Toast.makeText(this, getResources().getString(R.string.address_is_empty), Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(this, MapAndWeatherActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, user.getAddress());
                startActivity(intent);
            }
        } else if (v.getId() == R.id.visit_button) {
            String websiteValue = user.getWebsite();
            if (!websiteValue.startsWith(getResources().getString(R.string.httpPrefixForWebsiteAddress)) && !websiteValue.startsWith(getResources().getString(R.string.httpsPrefixForWebsiteAddress))) {
                websiteValue = getResources().getString(R.string.httpPrefixForWebsiteAddress) + websiteValue;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteValue));
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.edit_contact) {
            Intent intent = new Intent(this, ContactEditActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(USER_TAG, user);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
