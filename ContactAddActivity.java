package com.training.davidd.contactsapp;

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

import com.training.davidd.contactsapp.db.UserDBImplementation;
import com.training.davidd.contactsapp.model.User;
import com.training.davidd.contactsapp.utils.StyleProperties;
import com.training.davidd.contactsapp.view.DatePickerFragment;


public class ContactAddActivity extends ActionBarActivity implements DatePickerFragment.ProcessDate {
    // GENERAL
    protected final static String ADD_STATUS = "add_status";

    // DB
    UserDBImplementation userDBImplementation;

    // UI
    private LinearLayout mainLinearLayout;
    private EditText nameValueEditText;
    private Drawable nameValueEditTextDrawable;
    private EditText phoneNumberValueEditText;
    private Drawable phoneNumberValueEditTextDrawable;
    private EditText emailValueEditText;
    private DatePickerFragment datePickerFragment;
    private EditText dobValueEditText;
    private EditText addressValueEditText;
    private EditText websiteValueEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);

        mainLinearLayout = new LinearLayout(this);
        mainLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mainLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mainLinearLayout.setPadding(8, 8, 8, 8);

        createUI();

        RelativeLayout mainRelativeLayout = (RelativeLayout) findViewById(R.id.contact_edit_main_relative_layout);
        mainRelativeLayout.addView(mainLinearLayout);


        userDBImplementation = UserDBImplementation.getInstance(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.save_new_contact:
                String name = nameValueEditText.getText().toString();
                String phoneNumber = phoneNumberValueEditText.getText().toString();

                if (name.isEmpty() && phoneNumber.isEmpty()) {
                    Toast.makeText(this, R.string.name_and_phone_number_missing, Toast.LENGTH_LONG).show();
                    nameValueEditText.setBackgroundColor(StyleProperties.errorColor);
                    phoneNumberValueEditText.setBackgroundColor(StyleProperties.errorColor);
                } else if (name.isEmpty()) {
                    Toast.makeText(this, R.string.name_missing, Toast.LENGTH_LONG).show();
                    nameValueEditText.setBackgroundColor(StyleProperties.errorColor);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        phoneNumberValueEditText.setBackgroundDrawable(phoneNumberValueEditTextDrawable);
                    } else {
                        phoneNumberValueEditText.setBackground(phoneNumberValueEditTextDrawable);
                    }
                } else if (phoneNumber.isEmpty()) {
                    Toast.makeText(this, R.string.phone_number_missing, Toast.LENGTH_LONG).show();
                    phoneNumberValueEditText.setBackgroundColor(StyleProperties.errorColor);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        nameValueEditText.setBackgroundDrawable(nameValueEditTextDrawable);
                    } else {
                        nameValueEditText.setBackground(nameValueEditTextDrawable);
                    }
                } else {
                    User newUser = new User(-1, name, phoneNumber, emailValueEditText.getText().toString(),
                            dobValueEditText.getText().toString(), addressValueEditText.getText().toString(), websiteValueEditText.getText().toString());
                    userDBImplementation.insertUser(newUser);
                    Intent saveIntent = new Intent(this, ContactListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", newUser);
                    saveIntent.putExtras(bundle);
                    saveIntent.putExtra(ADD_STATUS, "Contact " + newUser.getName() + "(" + newUser.getPhoneNumber() + ") was added");
                    startActivity(saveIntent);
                }
                return true;
            case R.id.cancel_adding_new_contact:
                startActivity(new Intent(this, ContactListActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createUI() {
        // NAME
        TextView nameTextView = new TextView(this);
        nameTextView.setText(R.string.name_text_view);
        nameTextView.setTextSize(20);
        nameTextView.setPadding(0, 15, 0, 0);

        nameValueEditText = new EditText(this);
        nameValueEditText.setTextSize(30);
        nameValueEditTextDrawable = nameValueEditText.getBackground();

        // PHONE NUMBER
        TextView phoneNumberTextView = new TextView(this);
        phoneNumberTextView.setText(R.string.phone_number_text_view);
        phoneNumberTextView.setTextSize(20);
        phoneNumberTextView.setPadding(0, 15, 0, 0);

        phoneNumberValueEditText = new EditText(this);
        phoneNumberValueEditText.setTextSize(30);
        phoneNumberValueEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        phoneNumberValueEditTextDrawable = phoneNumberValueEditText.getBackground();

        // EMAIL
        TextView emailTextView = new TextView(this);
        emailTextView.setText(R.string.email_text_view);
        emailTextView.setTextSize(20);
        emailTextView.setPadding(0, 15, 0, 0);

        emailValueEditText = new EditText(this);
        emailValueEditText.setTextSize(30);

        // DOB
        TextView dobTextView = new TextView(this);
        dobTextView.setText(R.string.dob_text_view);
        dobTextView.setTextSize(20);
        dobTextView.setPadding(0, 15, 0, 0);

        datePickerFragment = new DatePickerFragment();
        datePickerFragment.setCancelable(true);

        dobValueEditText = new EditText(this);
        dobValueEditText.setTextSize(30);
        dobValueEditText.setFocusable(false);
        dobValueEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!datePickerFragment.isAdded()) {
                    datePickerFragment.show(getSupportFragmentManager(), "datePicker");
                }
            }
        });

        // ADDRESS
        TextView addressTextView = new TextView(this);
        addressTextView.setText(R.string.address_text_view);
        addressTextView.setTextSize(20);
        addressTextView.setPadding(0, 15, 0, 0);

        addressValueEditText = new EditText(this);
        addressValueEditText.setTextSize(30);

        // WEBSITE
        TextView websiteTextView = new TextView(this);
        websiteTextView.setText(R.string.website_text_view);
        websiteTextView.setTextSize(20);
        websiteTextView.setPadding(0, 15, 0, 0);

        websiteValueEditText = new EditText(this);
        websiteValueEditText.setTextSize(30);

        // ADD VIEWS TO MAIN LINEAR LAYOUT
        mainLinearLayout.addView(nameTextView);
        mainLinearLayout.addView(nameValueEditText);
        mainLinearLayout.addView(phoneNumberTextView);
        mainLinearLayout.addView(phoneNumberValueEditText);
        mainLinearLayout.addView(emailTextView);
        mainLinearLayout.addView(emailValueEditText);
        mainLinearLayout.addView(dobTextView);
        mainLinearLayout.addView(dobValueEditText);
        mainLinearLayout.addView(addressTextView);
        mainLinearLayout.addView(addressValueEditText);
        mainLinearLayout.addView(websiteTextView);
        mainLinearLayout.addView(websiteValueEditText);
    }

    public void setDatePickerValue(String text) {
        dobValueEditText.setText(text);
    }

    @Override
    public void onDateSetInTheDialog(String date) {
        dobValueEditText.setText(date);
    }
}