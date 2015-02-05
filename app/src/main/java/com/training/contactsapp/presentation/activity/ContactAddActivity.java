package com.training.contactsapp.presentation.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.training.contactsapp.R;
import com.training.contactsapp.model.User;
import com.training.contactsapp.utils.ContactsApplication;

public class ContactAddActivity extends BaseActivityForDetailsEditAddActivities {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createUi();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void createUi() {
        super.createUi();
        setButtonsVisibility(View.GONE);
        setAvatarImageViewBackground();
    }

    private void setButtonsVisibility(int visibility) {
        findViewById(R.id.button_call).setVisibility(visibility);
        findViewById(R.id.button_send).setVisibility(visibility);
        findViewById(R.id.button_look).setVisibility(visibility);
        findViewById(R.id.button_visit).setVisibility(visibility);
        findViewById(R.id.button_plan).setVisibility(visibility);
    }

    private void setAvatarImageViewBackground() {
        mAvatarImageView.setImageBitmap(BitmapFactory.decodeResource(ContactsApplication.getContext().getResources(), R.drawable.avatar_unknown));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            mAvatarImageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.image_view_edit_style));
        } else {
            mAvatarImageView.setBackground(getResources().getDrawable(R.drawable.image_view_edit_style));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_contact_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.save_new_contact:
                String errorMessage = validateFields();
                if (errorMessage.equals(getResources().getString(R.string.sorry))) {
                    User newUser = new User(-1, mNameEditText.getText().toString(), mPhoneNumberEditText.getText().toString(), mEmailEditText.getText().toString(),
                            mDobEditText.getText().toString(), mAddressEditText.getText().toString(), mWebsiteEditText.getText().toString(), null);
                    newUser.setAvatarAsBitmap(((BitmapDrawable) mAvatarImageView.getDrawable()).getBitmap());

                    mUserDAO.insertUser(newUser);

                    Intent saveIntent = new Intent(this, ContactListActivity.class);
                    saveIntent.putExtra(ContactListActivity.ADD_STATUS, String.format(getResources().getString(R.string.contact_added), mNameEditText.getText().toString(), mPhoneNumberEditText.getText().toString()));
                    startActivity(saveIntent);
                } else {
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cancel_adding_new_contact:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onDateSetInTheDialog(String date) {
        mDobEditText.setText(date);
    }

}