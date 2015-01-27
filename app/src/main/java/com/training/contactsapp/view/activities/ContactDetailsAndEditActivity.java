package com.training.contactsapp.view.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.training.contactsapp.R;
import com.training.contactsapp.database.UserDBImplementation;
import com.training.contactsapp.model.User;
import com.training.contactsapp.view.fragments.DatePickerFragment;

public class ContactDetailsAndEditActivity extends ActionBarActivity implements View.OnClickListener, DatePickerFragment.ProcessDate {
    public static final String USER_TAG = "USER";
    protected final static String REMOVE_STATUS = "REMOVE_STATUS";
    protected final static String SAVE_STATUS = "SAVE_STATUS";

    private User mUser;
    private Menu mMenu;
    private UserDBImplementation mUserDBImplementation;

    private LinearLayout mCommonLinearLayout;
    private EditText mNameEditText;
    private ImageView mAvatarImageView;
    private EditText mPhoneNumberEditText;
    private EditText mEmailEditText;
    private EditText mDobEditText;
    private EditText mAddressEditText;
    private EditText mWebsiteEditText;
    private DatePickerFragment mDatePickerFragment;

    private Button mCallButton;
    private Button mSendButton;
    private Button mLookButton;
    private Button mVisitButton;

    private Drawable mEditTextNameDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        mCommonLinearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.contacts_details_edit_add_common_layout, null);

        ScrollView contactDetailsMainLinearLayout = (ScrollView) findViewById(R.id.contact_details_main_linear_layout);
        contactDetailsMainLinearLayout.addView(mCommonLinearLayout);

        Intent intent = getIntent();
        mUser = (User) intent.getSerializableExtra(USER_TAG);

        mUserDBImplementation = UserDBImplementation.getInstance();

        createDefaultUi();
    }

    private void createDefaultUi() {
        // Name
        mNameEditText = (EditText) findViewById(R.id.edit_text_name_id);
        mNameEditText.setText(mUser.getName());
        mEditTextNameDrawable = mNameEditText.getBackground();
        mAvatarImageView = (ImageView) findViewById(R.id.image_view_avatar);
        mAvatarImageView.setImageBitmap(mUser.getAvatarAsBitmap());

        // Phone number
        mPhoneNumberEditText = (EditText) findViewById(R.id.edit_text_phone_number_id);
        mPhoneNumberEditText.setText(mUser.getPhoneNumber());
        mPhoneNumberEditText.setBackgroundColor(getResources().getColor(R.color.transparent));
        mCallButton = (Button) findViewById(R.id.button_call);
        mCallButton.setOnClickListener(this);

        // Email
        mEmailEditText = (EditText) findViewById(R.id.edit_text_email_id);
        mEmailEditText.setText(mUser.getEmail());
        mEmailEditText.setBackgroundColor(getResources().getColor(R.color.transparent));
        mSendButton = (Button) findViewById(R.id.button_send);
        mSendButton.setOnClickListener(this);

        // DOB
        mDobEditText = (EditText) findViewById(R.id.edit_text_dob_id);
        mDobEditText.setText(mUser.getDob());
        mDobEditText.setFocusable(false);
        mDobEditText.setBackgroundColor(getResources().getColor(R.color.transparent));

        mDatePickerFragment = new DatePickerFragment();
        mDobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDatePickerFragment.isAdded()) {
                    mDatePickerFragment.show(getSupportFragmentManager(), "DATE_PICKER");
                }
            }
        });

        // Address
        mAddressEditText = (EditText) findViewById(R.id.edit_text_address_id);
        mAddressEditText.setText(mUser.getAddress());
        mAddressEditText.setBackgroundColor(getResources().getColor(R.color.transparent));
        mLookButton = (Button) findViewById(R.id.button_look);
        mLookButton.setOnClickListener(this);

        // Website
        mWebsiteEditText = (EditText) findViewById(R.id.edit_text_website_id);
        mWebsiteEditText.setText(mUser.getWebsite());
        mWebsiteEditText.setBackgroundColor(getResources().getColor(R.color.transparent));
        mVisitButton = (Button) findViewById(R.id.button_visit);
        mVisitButton.setOnClickListener(this);

        setEditTextState(false);
    }

    private void changeUiToEdit() {
        setEditTextState(true);
//        setEdiTextPrimaryBackground();
        setButtonState(false);
        changeMenuToEdit();
    }

    private void changeUiToDetails() {
        setEditTextState(false);
        setEditTextValues();
//        setEdiTextPrimaryBackground();
        setButtonState(true);
        changeMenuToDetails();
    }

    private void setEditTextState(boolean editTextState) {
        mNameEditText.setEnabled(editTextState);
        mPhoneNumberEditText.setEnabled(editTextState);
        mEmailEditText.setEnabled(editTextState);
        mDobEditText.setEnabled(editTextState);
        mAddressEditText.setEnabled(editTextState);
        mWebsiteEditText.setEnabled(editTextState);
    }

    private void setEditTextValues() {
        mNameEditText.setText(mUser.getName());
        mPhoneNumberEditText.setText(mUser.getPhoneNumber());
        mEmailEditText.setText(mUser.getEmail());
        mDobEditText.setText(mUser.getDob());
        mAddressEditText.setText(mUser.getAddress());
        mWebsiteEditText.setText(mUser.getWebsite());
    }

    private void setEdiTextPrimaryBackground() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            mNameEditText.setBackgroundDrawable(mEditTextNameDrawable);
            mPhoneNumberEditText.setBackgroundDrawable(mEditTextNameDrawable);
            mEmailEditText.setBackgroundDrawable(mEditTextNameDrawable);
            mDobEditText.setBackgroundDrawable(mEditTextNameDrawable);
            mAddressEditText.setBackgroundDrawable(mEditTextNameDrawable);
            mWebsiteEditText.setBackgroundDrawable(mEditTextNameDrawable);
        } else {
            mNameEditText.setBackground(mEditTextNameDrawable);
            mPhoneNumberEditText.setBackground(mEditTextNameDrawable);
            mEmailEditText.setBackground(mEditTextNameDrawable);
            mDobEditText.setBackground(mEditTextNameDrawable);
            mAddressEditText.setBackground(mEditTextNameDrawable);
            mWebsiteEditText.setBackground(mEditTextNameDrawable);
        }
    }

    public void setButtonState(boolean buttonState) {
        mCallButton.setEnabled(buttonState);
        mSendButton.setEnabled(buttonState);
        mLookButton.setEnabled(buttonState);
        mVisitButton.setEnabled(buttonState);
    }

    private void changeMenuToEdit() {
        MenuItem menuItem = mMenu.findItem(R.id.menu_item_edit_contact);
        if (menuItem != null) mMenu.removeItem(R.id.menu_item_edit_contact);

        setTitle(getResources().getString(R.string.title_activity_contact_edit));
        getMenuInflater().inflate(R.menu.menu_contact_edit, mMenu);
    }

    private void changeMenuToDetails() {
        MenuItem menuItem = mMenu.findItem(R.id.menu_item_save_contact);
        if (menuItem != null) mMenu.removeItem(R.id.menu_item_save_contact);

        menuItem = mMenu.findItem(R.id.menu_item_cancel_editing_contact);
        if (menuItem != null) mMenu.removeItem(R.id.menu_item_cancel_editing_contact);

        menuItem = mMenu.findItem(R.id.menu_item_delete_contact);
        if (menuItem != null) mMenu.removeItem(R.id.menu_item_delete_contact);

        setTitle(getResources().getString(R.string.title_activity_contact_details));
        getMenuInflater().inflate(R.menu.menu_contact_details, mMenu);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mCallButton.getId()) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri phoneNumberUri = Uri.parse("tel:" + mUser.getPhoneNumber());
            intent.setData(phoneNumberUri);
            startActivity(intent);
        } else if (v.getId() == mSendButton.getId()) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mUser.getEmail()});
            startActivity(intent);
        } else if (v.getId() == mLookButton.getId()) {
            if (mUser.getAddress() == null || mUser.getAddress().isEmpty()) {
                Toast.makeText(this, getResources().getString(R.string.address_is_empty), Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(this, MapAndWeatherActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, mUser.getAddress());
                startActivity(intent);
            }
        } else if (v.getId() == mVisitButton.getId()) {
            String websiteValue = mUser.getWebsite();
            if (!websiteValue.startsWith(getResources().getString(R.string.httpPrefixForWebsiteAddress)) && !websiteValue.startsWith(getResources().getString(R.string.httpsPrefixForWebsiteAddress))) {
                websiteValue = getResources().getString(R.string.httpPrefixForWebsiteAddress) + websiteValue;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteValue));
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_contact_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_edit_contact: {
                changeUiToEdit();
                return true;
            }
            case R.id.menu_item_save_contact: {
                String name = mNameEditText.getText().toString();
                String phoneNumber = mPhoneNumberEditText.getText().toString();

                if (name.isEmpty() && phoneNumber.isEmpty()) {
                    Toast.makeText(this, R.string.name_and_phone_number_missing, Toast.LENGTH_LONG).show();
                    mNameEditText.setBackgroundColor(getResources().getColor(R.color.error_color));
                    mPhoneNumberEditText.setBackgroundColor(getResources().getColor(R.color.error_color));
                } else if (name.isEmpty()) {
                    Toast.makeText(this, R.string.name_missing, Toast.LENGTH_LONG).show();
                    mNameEditText.setBackgroundColor(getResources().getColor(R.color.error_color));
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        mPhoneNumberEditText.setBackgroundDrawable(mEditTextNameDrawable);
                    } else {
                        mPhoneNumberEditText.setBackground(mEditTextNameDrawable);
                    }
                } else if (phoneNumber.isEmpty()) {
                    Toast.makeText(this, R.string.phone_number_missing, Toast.LENGTH_LONG).show();
                    mPhoneNumberEditText.setBackgroundColor(getResources().getColor(R.color.error_color));
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        mNameEditText.setBackgroundDrawable(mEditTextNameDrawable);
                    } else {
                        mNameEditText.setBackground(mEditTextNameDrawable);
                    }
                } else {
                    User newUser = new User(mUser.getUid(), name, phoneNumber, mEmailEditText.getText().toString(),
                            mDobEditText.getText().toString(), mAddressEditText.getText().toString(), mWebsiteEditText.getText().toString(), null);
                    newUser.setAvatarAsBitmap(((BitmapDrawable) mAvatarImageView.getDrawable()).getBitmap());
                    mUserDBImplementation.updateUser(newUser);
                    Toast.makeText(this, String.format(getResources().getString(R.string.contact_updated), newUser.getName(), newUser.getPhoneNumber()), Toast.LENGTH_LONG).show();
                    mUser = newUser;
                    changeUiToDetails();
                }
                return true;
            }
            case R.id.menu_item_cancel_editing_contact: {
                changeUiToDetails();
                return true;
            }
            case R.id.menu_item_delete_contact: {
                AlertDialog.Builder deleteAlertDialog = new AlertDialog.Builder(this);
                deleteAlertDialog.setTitle(getResources().getString(R.string.delete_contact_confirmation));
                deleteAlertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                deleteAlertDialog.setMessage(String.format(getResources().getString(R.string.delete_contact_confirmation_question), mUser.getName(), mUser.getPhoneNumber()));
                deleteAlertDialog.setCancelable(true);
                deleteAlertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mUserDBImplementation.deleteUserByUid(mUser.getUid());
                        Intent deleteIntent = new Intent(ContactDetailsAndEditActivity.this, ContactListActivity.class);
                        deleteIntent.putExtra(REMOVE_STATUS, String.format(getResources().getString(R.string.contact_was_removed), mUser.getName(), mUser.getPhoneNumber()));
                        startActivity(deleteIntent);
                    }
                });
                deleteAlertDialog.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                deleteAlertDialog.show();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSetInTheDialog(String date) {
        mDobEditText.setText(date);
    }
}
