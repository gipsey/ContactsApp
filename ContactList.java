package com.training.davidd.contactsapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.training.davidd.contactsapp.db.UserDBImplementation;
import com.training.davidd.contactsapp.utils.UserAdapterToContactList;

import java.util.ArrayList;
import java.util.List;


public class ContactList extends ActionBarActivity {
    private static final String TAG = "com.training.davidd.contactsapp.ContactList";

    private UserDBImplementation userDBImplementation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        // DB
        userDBImplementation = UserDBImplementation.getInstance(this);
        Log.i(TAG + " getInstance", "OK");


        long re = userDBImplementation.deleteUsers();
        Log.i(TAG + " deleteUsers", Long.toString(re));


//        userDBImplementation.insertUsers(new User(0, "Name1", "001", "name1@yahoo.com", "1999-01-01", "address1", "http://www.gipsey.tk"));
//        userDBImplementation.insertUsers(new User(0, "Name2", "002", "name2@yahoo.com", "1999-01-02", "address2", "http://www.gipsey.tk"));
//
//
//        List<User> users = userDBImplementation.queryUsers();
//        String userString = "";
//        for (User user : users) {
//            userString += user;
//        }
//
//        Toast.makeText(this, userString, Toast.LENGTH_LONG).show();


        createUI();
    }

    private void createUI() {

        // Data to be displayed
        ArrayList<User> users = userDBImplementation.queryUsersNameAndPhoneNumber();

        // Adapter which will display the data
        UserAdapterToContactList userAdapterToContactList = new UserAdapterToContactList(this, users);

        ListView mainListView = (ListView) findViewById(R.id.list_view);
        mainListView.setAdapter(userAdapterToContactList);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
