package com.training.contactsapp.view.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.training.contactsapp.R;
import com.training.contactsapp.repository.DataAccessFactory;
import com.training.contactsapp.repository.UserDataAccess;
import com.training.contactsapp.view.fragments.DatePickerFragment;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by davidd on 2/2/15.
 */
public abstract class BaseActivityForDetailsEditAddActivities extends ActionBarActivity implements DatePickerFragment.ProcessDate, ImageChooserListener {
    protected static final Pattern NAME_PATTERN = Pattern.compile("^.{3,15}$");
    protected static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$");
    protected static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("^[A-Za-z]+[A-Za-z0-9._%+-]*@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$");
    protected static final Pattern WEBSITE_ADDRESS_PATTERN = Pattern.compile("^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?$");
    protected static final int REQUEST_CODE_CROP_PICTURE = 2;
    protected UserDataAccess mUserDataAccess;

    @InjectView(R.id.edit_text_name_id)
    protected EditText mNameEditText;
    @InjectView(R.id.image_view_avatar)
    protected ImageView mAvatarImageView;
    @InjectView(R.id.edit_text_phone_number_id)
    protected EditText mPhoneNumberEditText;
    @InjectView(R.id.edit_text_email_id)
    protected EditText mEmailEditText;
    @InjectView(R.id.edit_text_dob_id)
    protected EditText mDobEditText;
    @InjectView(R.id.edit_text_address_id)
    protected EditText mAddressEditText;
    @InjectView(R.id.edit_text_website_id)
    protected EditText mWebsiteEditText;

    protected DatePickerFragment mDatePickerFragment;
    protected Drawable mEditTextDrawable;
    protected ImageChooserManager mImageChooserManager;
    private String mImageChooserDirectory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details_and_edit_and_add);

        ButterKnife.inject(this);

        mUserDataAccess = DataAccessFactory.getInstance().getUserDataAccess();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        if (requestCode == ChooserType.REQUEST_PICK_PICTURE) {
            mImageChooserManager.submit(requestCode, data);
        } else if (requestCode == REQUEST_CODE_CROP_PICTURE) {
            if (data != null) {
                Bundle extras = data.getExtras();
                Bitmap bitmap = extras.getParcelable("data");
                mAvatarImageView.setImageBitmap(bitmap);

                File file = new File(mImageChooserDirectory);
                for (File f : file.listFiles()) {
                    if (f.exists()) f.delete();
                }
                file.delete();

            } else {
                Log.e(getClass().getName(), "Data returned after crop is null.");
                Toast.makeText(this, R.string.cannot_crop_image, Toast.LENGTH_LONG).show();
            }
        }
    }

    protected Bitmap pictureSelected(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return BitmapFactory.decodeFile(filePath);
    }

    protected void sendPictureSelectIntent() {
        mImageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_PICK_PICTURE);
        mImageChooserManager.setImageChooserListener(this);
        try {
            mImageChooserManager.choose();
        } catch (Exception e) {
            Log.i(getClass().getName(), "Images chooser manager throws an error");
            e.printStackTrace();
        }
    }

    protected void sendCropIntent(Uri imageUri) throws ActivityNotFoundException {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);

        intent.putExtra("output", imageUri);
        intent.putExtra("outputFormat", "JPG");

        startActivityForResult(intent, REQUEST_CODE_CROP_PICTURE);
    }

    protected void createUi() {
        mEditTextDrawable = mNameEditText.getBackground();

        mEmailEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        mDobEditText.setFocusable(false);

        mDatePickerFragment = new DatePickerFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    protected String validateFields() {
        String errorMessage = getResources().getString(R.string.sorry);

        if (!isFieldContentValid(mNameEditText, NAME_PATTERN)) {
            errorMessage = errorMessage + "\n" + getResources().getString(R.string.name_missing_or_invalid);
        }
        if (!isFieldContentValid(mPhoneNumberEditText, PHONE_NUMBER_PATTERN)) {
            errorMessage = errorMessage + "\n" + getResources().getString(R.string.phone_number_missing_or_invalid);
        }
        if (!isFieldContentValid(mEmailEditText, EMAIL_ADDRESS_PATTERN)) {
            errorMessage = errorMessage + "\n" + getResources().getString(R.string.email_address_missing_or_invalid);
        }
        if (!isFieldContentValid(mWebsiteEditText, WEBSITE_ADDRESS_PATTERN)) {
            errorMessage = errorMessage + "\n" + getResources().getString(R.string.website_address_or_invalid);
        }

        return errorMessage;
    }

    protected boolean isFieldContentValid(EditText editText, Pattern pattern) {
        if (!isStringMatchesThePatternCorrect(editText.getText().toString(), pattern)) {
            editText.setBackgroundColor(getResources().getColor(R.color.error_color));
            return false;
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                editText.setBackgroundDrawable(mEditTextDrawable);
            } else {
                editText.setBackground(mEditTextDrawable);
            }
            return true;
        }
    }

    protected boolean isStringMatchesThePatternCorrect(String string, Pattern pattern) {
        if (string.isEmpty()) return false;
        Matcher matcher = pattern.matcher(string);
        if (matcher.matches()) return true;
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.image_view_avatar)
    public void onImageViewClick(View v) {
        sendPictureSelectIntent();
    }

    @OnClick(R.id.edit_text_dob_id)
    public void onEditTextClick(View view) {
        if (!mDatePickerFragment.isAdded()) {
            Bundle bundle = new Bundle();
            bundle.putString(DatePickerFragment.DATE, mDobEditText.getText().toString());
            mDatePickerFragment.setArguments(bundle);
            mDatePickerFragment.show(getSupportFragmentManager(), "DATE_PICKER");
        }
    }

    @Override
    public void onImageChosen(final ChosenImage image) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (image != null) {
                    try {
                        File imageFile = new File(image.getFileThumbnailSmall());
                        mImageChooserDirectory = imageFile.getParent();
                        sendCropIntent(Uri.fromFile(imageFile));
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(BaseActivityForDetailsEditAddActivities.this, R.string.cannot_crop_image, Toast.LENGTH_LONG).show();
                        mAvatarImageView.setImageBitmap(BitmapFactory.decodeFile(image.getFileThumbnailSmall()));
                    }
                }
            }
        });
    }

    @Override
    public void onError(final String reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivityForDetailsEditAddActivities.this, R.string.image_cannot_be_loaded, Toast.LENGTH_LONG).show();
            }
        });
    }

}