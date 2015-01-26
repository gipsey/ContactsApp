package com.training.contactsapp.view.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class ContactEditActivity extends ActionBarActivity implements DatePickerFragment.ProcessDate {
    protected final static String REMOVE_STATUS = "REMOVE_STATUS";
    protected final static String SAVE_STATUS = "SAVE_STATUS";

    private UserDBImplementation mUserDBImplementation;

    private RelativeLayout mMainRelativeLayout;
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
    private User mActualUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_edit);

        Intent intent = getIntent();
        mActualUser = (User) intent.getSerializableExtra(ContactDetailsActivity.USER_TAG);

        mMainLinearLayout = new LinearLayout(this);
        mMainLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mMainLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mMainLinearLayout.setPadding(8, 8, 8, 8);

        createUI();

        mMainRelativeLayout = (RelativeLayout) findViewById(R.id.contact_edit_main_relative_layout);
        mMainRelativeLayout.addView(mMainLinearLayout);

        mUserDBImplementation = UserDBImplementation.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_contact:
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
                    User newUser = new User(mActualUser.getUid(), name, phoneNumber, mEmailValueEditText.getText().toString(),
                            mDobValueEditText.getText().toString(), mAddressValueEditText.getText().toString(), mWebsiteValueEditText.getText().toString(), null);
                    mUserDBImplementation.updateUser(newUser);
                    Intent saveIntent = new Intent(this, ContactDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ContactDetailsActivity.USER_TAG, newUser);
                    saveIntent.putExtras(bundle);
                    saveIntent.putExtra(SAVE_STATUS, String.format(getResources().getString(R.string.contact_updated), newUser.getName(), newUser.getPhoneNumber()));
                    startActivity(saveIntent);
                }
                break;
            case R.id.cancel_editing_contact:
                Intent cancelIntent = new Intent(this, ContactDetailsActivity.class);
                Bundle cancelBundle = new Bundle();
                cancelBundle.putSerializable(ContactDetailsActivity.USER_TAG, mActualUser);
                cancelIntent.putExtras(cancelBundle);
                startActivity(cancelIntent);
                break;
            case R.id.delete_contact:
                AlertDialog.Builder deleteAlertDialog = new AlertDialog.Builder(this);
                deleteAlertDialog.setTitle(getResources().getString(R.string.delete_contact_confirmation));
                deleteAlertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                deleteAlertDialog.setMessage(String.format(getResources().getString(R.string.delete_contact_confirmation_question), mActualUser.getName(), mActualUser.getPhoneNumber()));
                deleteAlertDialog.setCancelable(true);
                deleteAlertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mUserDBImplementation.deleteUserByUid(mActualUser.getUid());
                        Intent deleteIntent = new Intent(ContactEditActivity.this, ContactListActivity.class);
                        deleteIntent.putExtra(REMOVE_STATUS, String.format(getResources().getString(R.string.contact_was_removed), mActualUser.getName(), mActualUser.getPhoneNumber()));
                        startActivity(deleteIntent);
                    }
                });
                deleteAlertDialog.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                deleteAlertDialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void createUI() {
        // NAME
        TextView nameTextView = (TextView) getLayoutInflater().inflate(R.layout.label_text_view_style, null);
        nameTextView.setText(R.string.name_text_view);

        mNameValueEditText = new EditText(this);
        mNameValueEditText.setText(mActualUser.getName());
        mNameValueEditText.setTextSize(30);
        mNameValueEditTextDrawable = mNameValueEditText.getBackground();

        // PHONE NUMBER
        TextView phoneNumberTextView = (TextView) getLayoutInflater().inflate(R.layout.label_text_view_style, null);
        phoneNumberTextView.setText(R.string.phone_number_text_view);

        mPhoneNumberValueEditText = new EditText(this);
        mPhoneNumberValueEditText.setText(mActualUser.getPhoneNumber());
        mPhoneNumberValueEditText.setTextSize(30);
        mPhoneNumberValueEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        mPhoneNumberValueEditTextDrawable = mPhoneNumberValueEditText.getBackground();

        // EMAIL
        TextView emailTextView = (TextView) getLayoutInflater().inflate(R.layout.label_text_view_style, null);
        emailTextView.setText(R.string.email_text_view);

        mEmailValueEditText = new EditText(this);
        mEmailValueEditText.setText(mActualUser.getEmail());
        mEmailValueEditText.setTextSize(30);

        // DOB
        TextView dobTextView = (TextView) getLayoutInflater().inflate(R.layout.label_text_view_style, null);
        dobTextView.setText(R.string.dob_text_view);

        mDatePickerFragment = new DatePickerFragment();

        mDobValueEditText = new EditText(this);
        mDobValueEditText.setText(mActualUser.getDob());
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
        TextView addressTextView = (TextView) getLayoutInflater().inflate(R.layout.label_text_view_style, null);
        addressTextView.setText(R.string.address_text_view);

        mAddressValueEditText = new EditText(this);
        mAddressValueEditText.setText(mActualUser.getAddress());
        mAddressValueEditText.setTextSize(30);

        // WEBSITE
        TextView websiteTextView = (TextView) getLayoutInflater().inflate(R.layout.label_text_view_style, null);
        websiteTextView.setText(R.string.website_text_view);

        mWebsiteValueEditText = new EditText(this);
        mWebsiteValueEditText.setText(mActualUser.getWebsite());
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
    }

    @Override
    public void onDateSetInTheDialog(String date) {
        mDobValueEditText.setText(date);
    }
}
