package com.training.contactsapp.view.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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
import com.training.contactsapp.model.User;
import com.training.contactsapp.repository.DataAccessFactory;
import com.training.contactsapp.repository.UserDataAccess;
import com.training.contactsapp.view.fragments.DatePickerFragment;

import java.io.File;
import java.io.IOException;

public class ContactDetailsAndEditActivity extends ActionBarActivity implements View.OnClickListener, DatePickerFragment.ProcessDate {
    public static final String USER_TAG = "USER";
    protected final static String REMOVE_STATUS = "REMOVE_STATUS";
    private static final int REQUEST_CODE_SELECT_PICTURE = 1;
    private static final int REQUEST_CODE_CROP_PICTURE = 2;

    private User mUser;
    private Menu mMenu;
    private UserDataAccess mUserDataAccess;

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
    private Button mPlanningButton;

    private Drawable mEditTextDrawable;

    private Uri tempUriForCroppedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details_and_edit);

        mCommonLinearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.contacts_details_edit_add_common_layout, null);

        ScrollView contactDetailsMainLinearLayout = (ScrollView) findViewById(R.id.contact_add_main_linear_layout);
        contactDetailsMainLinearLayout.addView(mCommonLinearLayout);

        Intent intent = getIntent();
        mUser = (User) intent.getSerializableExtra(USER_TAG);

        mUserDataAccess = DataAccessFactory.getInstance().getUserDataAccess();

        createDefaultUi();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CODE_SELECT_PICTURE) {
            Bitmap bitmap = pictureSelected(data);
            if (bitmap == null) {
                Toast.makeText(this, R.string.image_cannot_be_loaded, Toast.LENGTH_LONG).show();
            } else {
                try {
                    sendCropIntent(data.getData());
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, R.string.cannot_crop_image, Toast.LENGTH_LONG).show();
                    mAvatarImageView.setImageBitmap(bitmap);
                }
            }
        } else if (requestCode == REQUEST_CODE_CROP_PICTURE) {
            if (data != null) {
                Bundle extras = data.getExtras();
                Bitmap bitmap = extras.getParcelable("data");
                mAvatarImageView.setImageBitmap(bitmap);

                File file = new File(tempUriForCroppedImage.getPath());
                if (file.exists()) file.delete();

            } else {
                Log.e(getClass().getName(), "Data returned after crop is null.");
                Toast.makeText(this, R.string.cannot_crop_image, Toast.LENGTH_LONG).show();
            }
        }
    }

    private Bitmap pictureSelected(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        return bitmap;
    }

    private void sendCropIntent(Uri imageUri) throws ActivityNotFoundException {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);

        try {
            tempUriForCroppedImage = Uri.fromFile(File.createTempFile("tmp_contact_" + String.valueOf(System.currentTimeMillis()), "png", Environment.getExternalStorageDirectory()));
        } catch (IOException e) {
            Log.e(getClass().getName(), "Cannot create new file");
            e.printStackTrace();
        }
        intent.putExtra("output", tempUriForCroppedImage);
        intent.putExtra("outputFormat", "PNG");

        startActivityForResult(intent, REQUEST_CODE_CROP_PICTURE);
    }

    private void sendPictureSelectIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_picture_string)), REQUEST_CODE_SELECT_PICTURE);
    }

    private void createDefaultUi() {
        // Name
        mNameEditText = (EditText) findViewById(R.id.edit_text_name_id);
        mNameEditText.setText(mUser.getName());
        mEditTextDrawable = mNameEditText.getBackground();
        mAvatarImageView = (ImageView) findViewById(R.id.image_view_avatar);
        mAvatarImageView.setImageBitmap(mUser.getAvatarAsBitmap());
        mAvatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPictureSelectIntent();
            }
        });

        // Phone number
        mPhoneNumberEditText = (EditText) findViewById(R.id.edit_text_phone_number_id);
        mPhoneNumberEditText.setText(mUser.getPhoneNumber());
        mCallButton = (Button) findViewById(R.id.button_call);
        mCallButton.setOnClickListener(this);

        // Email
        mEmailEditText = (EditText) findViewById(R.id.edit_text_email_id);
        mEmailEditText.setText(mUser.getEmail());
        mSendButton = (Button) findViewById(R.id.button_send);
        mSendButton.setOnClickListener(this);

        // DOB
        mDobEditText = (EditText) findViewById(R.id.edit_text_dob_id);
        mDobEditText.setText(mUser.getDob());
        mDobEditText.setFocusable(false);

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
        mLookButton = (Button) findViewById(R.id.button_look);
        mLookButton.setOnClickListener(this);

        // Website
        mWebsiteEditText = (EditText) findViewById(R.id.edit_text_website_id);
        mWebsiteEditText.setText(mUser.getWebsite());
        mVisitButton = (Button) findViewById(R.id.button_visit);
        mVisitButton.setOnClickListener(this);

        // Meeting
        mPlanningButton = (Button) findViewById(R.id.button_plan);
        mPlanningButton.setOnClickListener(this);

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
        } else if (v.getId() == mPlanningButton.getId()) {
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
                        mPhoneNumberEditText.setBackgroundDrawable(mEditTextDrawable);
                    } else {
                        mPhoneNumberEditText.setBackground(mEditTextDrawable);
                    }
                } else if (phoneNumber.isEmpty()) {
                    Toast.makeText(this, R.string.phone_number_missing, Toast.LENGTH_LONG).show();
                    mPhoneNumberEditText.setBackgroundColor(getResources().getColor(R.color.error_color));
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        mNameEditText.setBackgroundDrawable(mEditTextDrawable);
                    } else {
                        mNameEditText.setBackground(mEditTextDrawable);
                    }
                } else {
                    mUser.setName(name);
                    mUser.setPhoneNumber(phoneNumber);
                    mUser.setEmail(mEmailEditText.getText().toString());
                    mUser.setDob(mDobEditText.getText().toString());
                    mUser.setAddress(mAddressEditText.getText().toString());
                    mUser.setWebsite(mWebsiteEditText.getText().toString());
                    mUser.setAvatarAsBitmap(((BitmapDrawable) mAvatarImageView.getDrawable()).getBitmap());

                    mUserDataAccess.updateUser(mUser);
                    Toast.makeText(this, String.format(getResources().getString(R.string.contact_updated), name, phoneNumber), Toast.LENGTH_LONG).show();
                    changeUiToDetails();
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSetInTheDialog(String date) {
        mDobEditText.setText(date);
    }
}
