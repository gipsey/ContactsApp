package com.training.contactsapp.view.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.training.contactsapp.R;
import com.training.contactsapp.database.UserDBImplementation;
import com.training.contactsapp.model.User;
import com.training.contactsapp.view.fragments.DatePickerFragment;

public class ContactAddActivity extends ActionBarActivity implements DatePickerFragment.ProcessDate {
    private UserDBImplementation mUserDBImplementation;

    private LinearLayout mMainLinearLayout;
    private EditText mNameValueEditText;
    private Drawable mNameValueEditTextDrawable;
    private EditText mPhoneNumberValueEditText;
    private Drawable mPhoneNumberValueEditTextDrawable;
    private EditText mEmailValueEditText;
    private DatePickerFragment mDatePickerFragment;
    private EditText mDobValueEditText;
    private EditText mAddressValueEditText;
    private EditText mWebsiteValueEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);

        createUI();

        mUserDBImplementation = UserDBImplementation.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_new_contact:
                String name = mNameValueEditText.getText().toString();
                String phoneNumber = mPhoneNumberValueEditText.getText().toString();

                if (name.isEmpty() && phoneNumber.isEmpty()) {
                    Toast.makeText(this, R.string.name_and_phone_number_missing, Toast.LENGTH_LONG).show();
                    mNameValueEditText.setBackgroundColor(getResources().getColor(R.color.error_color));
                    mPhoneNumberValueEditText.setBackgroundColor(getResources().getColor(R.color.error_color));
                } else if (name.isEmpty()) {
                    Toast.makeText(this, R.string.name_missing, Toast.LENGTH_LONG).show();
                    mNameValueEditText.setBackgroundColor(getResources().getColor(R.color.error_color));
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        mPhoneNumberValueEditText.setBackgroundDrawable(mPhoneNumberValueEditTextDrawable);
                    } else {
                        mPhoneNumberValueEditText.setBackground(mPhoneNumberValueEditTextDrawable);
                    }
                } else if (phoneNumber.isEmpty()) {
                    Toast.makeText(this, R.string.phone_number_missing, Toast.LENGTH_LONG).show();
                    mPhoneNumberValueEditText.setBackgroundColor(getResources().getColor(R.color.error_color));
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        mNameValueEditText.setBackgroundDrawable(mNameValueEditTextDrawable);
                    } else {
                        mNameValueEditText.setBackground(mNameValueEditTextDrawable);
                    }
                } else {
                    User newUser = new User(-1, name, phoneNumber, mEmailValueEditText.getText().toString(),
                            mDobValueEditText.getText().toString(), mAddressValueEditText.getText().toString(), mWebsiteValueEditText.getText().toString());
                    mUserDBImplementation.insertUser(newUser);

                    Intent saveIntent = new Intent(this, ContactListActivity.class);
                    saveIntent.putExtra(ContactListActivity.ADD_STATUS, String.format(getResources().getString(R.string.contact_added), newUser.getName(), newUser.getPhoneNumber()));
                    startActivity(saveIntent);
                }
                break;
            case R.id.cancel_adding_new_contact:
                startActivity(new Intent(this, ContactListActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createUI() {
        mMainLinearLayout = new LinearLayout(this);
        mMainLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mMainLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mMainLinearLayout.setPadding(8, 8, 8, 8);

        // NAME
        TextView nameTextView = new TextView(this);
        nameTextView.setText(R.string.name_text_view);
        nameTextView.setTextSize(20);
        nameTextView.setPadding(0, 15, 0, 0);

        mNameValueEditText = new EditText(this);
        mNameValueEditText.setTextSize(30);
        mNameValueEditTextDrawable = mNameValueEditText.getBackground();

        // PHONE NUMBER
        TextView phoneNumberTextView = new TextView(this);
        phoneNumberTextView.setText(R.string.phone_number_text_view);
        phoneNumberTextView.setTextSize(20);
        phoneNumberTextView.setPadding(0, 15, 0, 0);

        mPhoneNumberValueEditText = new EditText(this);
        mPhoneNumberValueEditText.setTextSize(30);
        mPhoneNumberValueEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        mPhoneNumberValueEditTextDrawable = mPhoneNumberValueEditText.getBackground();

        // EMAIL
        TextView emailTextView = new TextView(this);
        emailTextView.setText(R.string.email_text_view);
        emailTextView.setTextSize(20);
        emailTextView.setPadding(0, 15, 0, 0);

        mEmailValueEditText = new EditText(this);
        mEmailValueEditText.setTextSize(30);

        // DOB
        TextView dobTextView = new TextView(this);
        dobTextView.setText(R.string.dob_text_view);
        dobTextView.setTextSize(20);
        dobTextView.setPadding(0, 15, 0, 0);

        mDatePickerFragment = new DatePickerFragment();
        mDatePickerFragment.setCancelable(true);

        mDobValueEditText = new EditText(this);
        mDobValueEditText.setTextSize(30);
        mDobValueEditText.setFocusable(false);
        mDobValueEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDatePickerFragment.isAdded()) {
                    mDatePickerFragment.show(getSupportFragmentManager(), "DATE_PICKER");
                }
            }
        });

        // ADDRESS
        TextView addressTextView = new TextView(this);
        addressTextView.setText(R.string.address_text_view);
        addressTextView.setTextSize(20);
        addressTextView.setPadding(0, 15, 0, 0);

        mAddressValueEditText = new EditText(this);
        mAddressValueEditText.setTextSize(30);

        // WEBSITE
        TextView websiteTextView = new TextView(this);
        websiteTextView.setText(R.string.website_text_view);
        websiteTextView.setTextSize(20);
        websiteTextView.setPadding(0, 15, 0, 0);

        mWebsiteValueEditText = new EditText(this);
        mWebsiteValueEditText.setTextSize(30);

        // ADD VIEWS TO MAIN LINEAR LAYOUT
        mMainLinearLayout.addView(nameTextView);
        mMainLinearLayout.addView(mNameValueEditText);
        mMainLinearLayout.addView(phoneNumberTextView);
        mMainLinearLayout.addView(mPhoneNumberValueEditText);
        mMainLinearLayout.addView(emailTextView);
        mMainLinearLayout.addView(mEmailValueEditText);
        mMainLinearLayout.addView(dobTextView);
        mMainLinearLayout.addView(mDobValueEditText);
        mMainLinearLayout.addView(addressTextView);
        mMainLinearLayout.addView(mAddressValueEditText);
        mMainLinearLayout.addView(websiteTextView);
        mMainLinearLayout.addView(mWebsiteValueEditText);

        RelativeLayout mainRelativeLayout = (RelativeLayout) findViewById(R.id.contact_edit_main_relative_layout);
        mainRelativeLayout.addView(mMainLinearLayout);
    }

    @Override
    public void onDateSetInTheDialog(String date) {
        mDobValueEditText.setText(date);
    }

}