package com.training.davidd.contactsapp;

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

import com.training.davidd.contactsapp.db.UserDBImplementation;
import com.training.davidd.contactsapp.model.User;
import com.training.davidd.contactsapp.utils.StyleProperties;
import com.training.davidd.contactsapp.view.DatePickerFragment;


public class ContactEditActivity extends ActionBarActivity implements DatePickerFragment.ProcessDate {

    // GENERAL
    protected final static String REMOVE_STATUS = "remove_status";
    protected final static String SAVE_STATUS = "save_status";
    // DB
    UserDBImplementation userDBImplementation;
    // UI
    private RelativeLayout mainRelativeLayout;
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
    private User actualUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_edit);

        Intent intent = getIntent();
        actualUser = (User) intent.getSerializableExtra("user");

        mainLinearLayout = new LinearLayout(this);
        mainLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mainLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mainLinearLayout.setPadding(8, 8, 8, 8);

        createUI();

        mainRelativeLayout = (RelativeLayout) findViewById(R.id.contact_edit_main_relative_layout);
        mainRelativeLayout.addView(mainLinearLayout);

        userDBImplementation = UserDBImplementation.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.save_contact:
                String name = nameValueEditText.getText().toString();
                String phoneNumber = phoneNumberValueEditText.getText().toString();

                if (name.isEmpty() && phoneNumber.isEmpty()) {
                    nameValueEditText.setBackgroundColor(StyleProperties.errorColor);
                    phoneNumberValueEditText.setBackgroundColor(StyleProperties.errorColor);
                    Toast.makeText(this, R.string.name_and_phone_number_missing, Toast.LENGTH_LONG).show();
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
                    User newUser = new User(actualUser.getUid(), name, phoneNumber, emailValueEditText.getText().toString(),
                            dobValueEditText.getText().toString(), addressValueEditText.getText().toString(), websiteValueEditText.getText().toString());
                    userDBImplementation.updateUser(newUser);
                    Intent saveIntent = new Intent(this, ContactDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", newUser);
                    saveIntent.putExtras(bundle);
                    saveIntent.putExtra(SAVE_STATUS, "Contact " + newUser.getName() + "(" + newUser.getPhoneNumber() + ") was updated");
                    startActivity(saveIntent);
                }
                return true;
            case R.id.cancel_editing_contact:
                Intent cancelIntent = new Intent(this, ContactDetailsActivity.class);
                Bundle cancelBundle = new Bundle();
                cancelBundle.putSerializable("user", actualUser);
                cancelIntent.putExtras(cancelBundle);
                startActivity(cancelIntent);
                return true;
            case R.id.delete_contact:
                userDBImplementation.deleteUserByUID(actualUser.getUid());

                AlertDialog.Builder deleteAlertDialog = new AlertDialog.Builder(this);
                deleteAlertDialog.setTitle("Delete contact");
                deleteAlertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                deleteAlertDialog.setMessage("Sure you want to delete " + actualUser.getName() + "(" + actualUser.getPhoneNumber() + ")?");
                deleteAlertDialog.setCancelable(true);
                deleteAlertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent deleteIntent = new Intent(ContactEditActivity.this, ContactListActivity.class);
                        deleteIntent.putExtra(REMOVE_STATUS, "Contact " + actualUser.getName() + "(" + actualUser.getPhoneNumber() + ") was removed");
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

        return super.onOptionsItemSelected(item);
    }


    private void createUI() {
        // NAME
        TextView nameTextView = new TextView(this); // TODO: Name and phone number to have a red color if input is wrong or empty
        nameTextView.setText(R.string.name_text_view);
        nameTextView.setTextSize(20);
        nameTextView.setPadding(0, 15, 0, 0);

        nameValueEditText = new EditText(this);
        nameValueEditText.setText(actualUser.getName());
        nameValueEditText.setTextSize(30);
        nameValueEditTextDrawable = nameValueEditText.getBackground();

        // PHONE NUMBER
        TextView phoneNumberTextView = new TextView(this);
        phoneNumberTextView.setText(R.string.phone_number_text_view);
        phoneNumberTextView.setTextSize(20);
        phoneNumberTextView.setPadding(0, 15, 0, 0);

        phoneNumberValueEditText = new EditText(this);
        phoneNumberValueEditText.setText(actualUser.getPhoneNumber());
        phoneNumberValueEditText.setTextSize(30);
        phoneNumberValueEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        phoneNumberValueEditTextDrawable = phoneNumberValueEditText.getBackground();

        // EMAIL
        TextView emailTextView = new TextView(this);
        emailTextView.setText(R.string.email_text_view);
        emailTextView.setTextSize(20);
        emailTextView.setPadding(0, 15, 0, 0);

        emailValueEditText = new EditText(this);
        emailValueEditText.setText(actualUser.getEmail());
        emailValueEditText.setTextSize(30);

        // DOB
        TextView dobTextView = new TextView(this);
        dobTextView.setText(R.string.dob_text_view);
        dobTextView.setTextSize(20);
        dobTextView.setPadding(0, 15, 0, 0);

        datePickerFragment = new DatePickerFragment();

        dobValueEditText = new EditText(this);
        dobValueEditText.setText(actualUser.getDob());
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
        addressValueEditText.setText(actualUser.getAddress());
        addressValueEditText.setTextSize(30);

        // WEBSITE
        TextView websiteTextView = new TextView(this);
        websiteTextView.setText(R.string.website_text_view);
        websiteTextView.setTextSize(20);
        websiteTextView.setPadding(0, 15, 0, 0);

        websiteValueEditText = new EditText(this);
        websiteValueEditText.setText(actualUser.getWebsite());
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

    @Override
    public void onDateSetInTheDialog(String date) {
        dobValueEditText.setText(date);
    }
}
