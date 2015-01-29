package com.training.contactsapp.view.activities;

import android.content.ActivityNotFoundException;
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
import com.training.contactsapp.business.ContactsApplication;
import com.training.contactsapp.model.User;
import com.training.contactsapp.repository.DataAccessFactory;
import com.training.contactsapp.repository.UserDataAccess;
import com.training.contactsapp.view.fragments.DatePickerFragment;

import java.io.File;
import java.io.IOException;

public class ContactAddActivity extends ActionBarActivity implements DatePickerFragment.ProcessDate {
    private static final int REQUEST_CODE_SELECT_PICTURE = 1;
    private static final int REQUEST_CODE_CROP_PICTURE = 2;

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

    private Drawable mEditTextDrawable;

    private Uri tempUriForCroppedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);

        mCommonLinearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.contacts_details_edit_add_common_layout, null);

        ScrollView contactAddMainLinearLayout = (ScrollView) findViewById(R.id.contact_add_main_linear_layout);
        contactAddMainLinearLayout.addView(mCommonLinearLayout);
        mUserDataAccess = DataAccessFactory.getInstance().getUserDataAccess();

        createUi();
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

    private void sendPictureSelectIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_picture_string)), REQUEST_CODE_SELECT_PICTURE);
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

    private void createUi() {
        mNameEditText = (EditText) findViewById(R.id.edit_text_name_id);
        mEditTextDrawable = mNameEditText.getBackground();
        mAvatarImageView = (ImageView) findViewById(R.id.image_view_avatar);
        mAvatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPictureSelectIntent();
            }
        });

        mPhoneNumberEditText = (EditText) findViewById(R.id.edit_text_phone_number_id);

        mEmailEditText = (EditText) findViewById(R.id.edit_text_email_id);

        mDobEditText = (EditText) findViewById(R.id.edit_text_dob_id);
        mDobEditText.setFocusable(false); // TODO: Should I remove?

        mDatePickerFragment = new DatePickerFragment();
        mDobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDatePickerFragment.isAdded()) {
                    mDatePickerFragment.show(getSupportFragmentManager(), "DATE_PICKER");
                }
            }
        });

        mAddressEditText = (EditText) findViewById(R.id.edit_text_address_id);

        mWebsiteEditText = (EditText) findViewById(R.id.edit_text_website_id);

        setButtonsVisibility(View.GONE);
        setAvatarImageViewBackground();
    }

    private void setButtonsVisibility(int visibility) {
        ((Button) findViewById(R.id.button_call)).setVisibility(visibility);
        ((Button) findViewById(R.id.button_send)).setVisibility(visibility);
        ((Button) findViewById(R.id.button_look)).setVisibility(visibility);
        ((Button) findViewById(R.id.button_visit)).setVisibility(visibility);
        ((Button) findViewById(R.id.button_plan)).setVisibility(visibility);
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
        getMenuInflater().inflate(R.menu.menu_contact_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_new_contact:
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
                    User newUser = new User(-1, name, phoneNumber, mEmailEditText.getText().toString(),
                            mDobEditText.getText().toString(), mAddressEditText.getText().toString(), mWebsiteEditText.getText().toString(), null);
                    newUser.setAvatarAsBitmap(((BitmapDrawable) mAvatarImageView.getDrawable()).getBitmap());

                    mUserDataAccess.insertUser(newUser);

                    Intent saveIntent = new Intent(this, ContactListActivity.class);
                    saveIntent.putExtra(ContactListActivity.ADD_STATUS, String.format(getResources().getString(R.string.contact_added), name, phoneNumber));
                    startActivity(saveIntent);
                }
                break;
            case R.id.cancel_adding_new_contact:
                startActivity(new Intent(this, ContactListActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSetInTheDialog(String date) {
        mDobEditText.setText(date);
    }


}