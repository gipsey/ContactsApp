package com.training.contactsapp.view.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.training.contactsapp.R;
import com.training.contactsapp.model.User;

import butterknife.OnClick;

public class ContactDetailsAndEditActivity extends BaseActivityForDetailsEditAddActivities {
    public static final String USER_TAG = "USER";
    protected final static String REMOVE_STATUS = "REMOVE_STATUS";

    private User mUser;
    private Menu mMenu;

    private Button mCallButton;
    private Button mSendButton;
    private Button mLookButton;
    private Button mVisitButton;
    private Button mPlanningButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUser = (User) getIntent().getExtras().getSerializable(USER_TAG);

        createUi();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void createUi() {
        super.createUi();

        mNameEditText.setText(mUser.getName());
        mAvatarImageView.setImageBitmap(mUser.getAvatarAsBitmap());

        mPhoneNumberEditText.setText(mUser.getPhoneNumber());
        mCallButton = (Button) findViewById(R.id.button_call);

        mEmailEditText.setText(mUser.getEmail());
        mSendButton = (Button) findViewById(R.id.button_send);

        mDobEditText.setText(mUser.getDob());

        mAddressEditText.setText(mUser.getAddress());
        mLookButton = (Button) findViewById(R.id.button_look);

        mWebsiteEditText.setText(mUser.getWebsite());
        mVisitButton = (Button) findViewById(R.id.button_visit);

        mPlanningButton = (Button) findViewById(R.id.button_plan);

        setPrimaryBackground();
        setEditTextState(false);
    }

    private void changeUiToEdit() {
        setBackgroundForEdit();

        setEditTextState(true);
        setButtonState(false);
        changeMenuToEdit();
    }

    private void changeUiToDetails() {
        setPrimaryBackground();

        setEditTextState(false);
        setButtonState(true);
        changeMenuToDetails();
    }

    private void setEditTextState(boolean state) {
        mNameEditText.setEnabled(state);
        mAvatarImageView.setEnabled(state);
        mPhoneNumberEditText.setEnabled(state);
        mEmailEditText.setEnabled(state);
        mDobEditText.setEnabled(state);
        mAddressEditText.setEnabled(state);
        mWebsiteEditText.setEnabled(state);
    }

    private void setEditTextValuesToTheOriginal() {
        mNameEditText.setText(mUser.getName());
        mAvatarImageView.setImageBitmap(mUser.getAvatarAsBitmap());
        mPhoneNumberEditText.setText(mUser.getPhoneNumber());
        mEmailEditText.setText(mUser.getEmail());
        mDobEditText.setText(mUser.getDob());
        mAddressEditText.setText(mUser.getAddress());
        mWebsiteEditText.setText(mUser.getWebsite());
    }

    private void setPrimaryBackground() {
        mNameEditText.setBackgroundColor(getResources().getColor(R.color.transparent));
        mPhoneNumberEditText.setBackgroundColor(getResources().getColor(R.color.transparent));
        mEmailEditText.setBackgroundColor(getResources().getColor(R.color.transparent));
        mDobEditText.setBackgroundColor(getResources().getColor(R.color.transparent));
        mAddressEditText.setBackgroundColor(getResources().getColor(R.color.transparent));
        mWebsiteEditText.setBackgroundColor(getResources().getColor(R.color.transparent));

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            mAvatarImageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_view_default_style));
        } else {
            mAvatarImageView.setBackground(getResources().getDrawable(R.drawable.image_view_default_style));
        }
    }

    private void setBackgroundForEdit() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            mNameEditText.setBackgroundDrawable(mEditTextDrawable);
            mPhoneNumberEditText.setBackgroundDrawable(mEditTextDrawable);
            mEmailEditText.setBackgroundDrawable(mEditTextDrawable);
            mDobEditText.setBackgroundDrawable(mEditTextDrawable);
            mAddressEditText.setBackgroundDrawable(mEditTextDrawable);
            mWebsiteEditText.setBackgroundDrawable(mEditTextDrawable);
            mAvatarImageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_view_edit_style));
        } else {
            mNameEditText.setBackground(mEditTextDrawable);
            mPhoneNumberEditText.setBackground(mEditTextDrawable);
            mEmailEditText.setBackground(mEditTextDrawable);
            mDobEditText.setBackground(mEditTextDrawable);
            mAddressEditText.setBackground(mEditTextDrawable);
            mWebsiteEditText.setBackground(mEditTextDrawable);
            mAvatarImageView.setBackground(getResources().getDrawable(R.drawable.image_view_edit_style));
        }
    }

    public void setButtonState(boolean buttonState) {
        mCallButton.setEnabled(buttonState);
        mSendButton.setEnabled(buttonState);
        mLookButton.setEnabled(buttonState);
        mVisitButton.setEnabled(buttonState);
        mPlanningButton.setEnabled(buttonState);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_contact_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_item_edit_contact: {
                changeUiToEdit();
                return true;
            }
            case R.id.menu_item_save_contact: {
                String errorMessage = validateFields();
                if (errorMessage.equals(getResources().getString(R.string.sorry))) {
                    mUser.setName(mNameEditText.getText().toString());
                    mUser.setPhoneNumber(mPhoneNumberEditText.getText().toString());
                    mUser.setEmail(mEmailEditText.getText().toString());
                    mUser.setDob(mDobEditText.getText().toString());
                    mUser.setAddress(mAddressEditText.getText().toString());
                    mUser.setWebsite(mWebsiteEditText.getText().toString());
                    mUser.setAvatarAsBitmap(((BitmapDrawable) mAvatarImageView.getDrawable()).getBitmap());

                    mUserDataAccess.updateUser(mUser);
                    Toast.makeText(this, String.format(getResources().getString(R.string.contact_updated), mNameEditText.getText().toString(), mPhoneNumberEditText.getText().toString()), Toast.LENGTH_LONG).show();
                    changeUiToDetails();
                } else {
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                }
                return true;
            }
            case R.id.menu_item_cancel_editing_contact: {
                changeUiToDetails();
                setEditTextValuesToTheOriginal();
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
                        mUserDataAccess.deleteUserByUid(mUser.getUid());
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
        return true;
    }

    @Override
    public void onDateSetInTheDialog(String date) {
        mDobEditText.setText(date);
    }

    @OnClick(R.id.button_call)
    protected void onCallButtonClick() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri phoneNumberUri = Uri.parse("tel:" + mUser.getPhoneNumber());
        intent.setData(phoneNumberUri);
        startActivity(intent);
    }

    @OnClick(R.id.button_send)
    protected void onSendButtonClick() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mUser.getEmail()});
        startActivity(intent);
    }

    @OnClick(R.id.button_look)
    protected void onLookButtonClick() {
        if (mUser.getAddress() == null || mUser.getAddress().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.address_is_empty), Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, MapAndWeatherActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, mUser.getAddress());
            startActivity(intent);
        }
    }

    @OnClick(R.id.button_visit)
    protected void onVisitButtonClick() {
        String websiteValue = mUser.getWebsite();
        if (!websiteValue.startsWith(getResources().getString(R.string.httpPrefixForWebsiteAddress)) && !websiteValue.startsWith(getResources().getString(R.string.httpsPrefixForWebsiteAddress))) {
            websiteValue = getResources().getString(R.string.httpPrefixForWebsiteAddress) + websiteValue;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteValue));
        startActivity(intent);
    }

    @OnClick(R.id.button_plan)
    protected void onPlanButtonClick() {
        String titleDescription = String.format(getResources().getString(R.string.meeting_planning_title), mUser.getName());
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, titleDescription);
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, titleDescription);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, getResources().getString(R.string.cannot_create_meeting), Toast.LENGTH_LONG).show();
        }
    }

}
