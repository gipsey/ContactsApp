package com.training.davidd.contactsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.training.davidd.contactsapp.db.UserDBImplementation;
import com.training.davidd.contactsapp.model.User;
import com.training.davidd.contactsapp.utils.UserAdapterToContactList;

import java.util.ArrayList;

public class ContactListActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = "com.training.davidd.contactsapp.ContactListActivity";

    private UserDBImplementation userDBImplementation;
    private SearchView searchView;
    private ListView mainListView;

    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        // DB
        userDBImplementation = UserDBImplementation.getInstance(this);
        Intent intent = getIntent();

        String removeStatus = intent.getStringExtra(ContactEditActivity.REMOVE_STATUS);
        if (removeStatus != null) {
            showToast(removeStatus);
        }
        String addStatus = intent.getStringExtra(ContactAddActivity.ADD_STATUS);
        if (addStatus != null) {
            showToast(addStatus);
        }

//        makeSomeDbSpecificTemporaryTasks(); // TODO: is for test purpose, I have to remove it

        createListView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mainListView.setAdapter(new UserAdapterToContactList(this, userDBImplementation.queryUsersUIDAndNameAndPhoneNumber()));
    }

    private void createListView() {
        // Data to be displayed
        users = userDBImplementation.queryUsersUIDAndNameAndPhoneNumber();

        // Adapter which will display the data
        UserAdapterToContactList userAdapterToContactList = new UserAdapterToContactList(this, users);

        mainListView = (ListView) findViewById(R.id.list_view);
        mainListView.setAdapter(userAdapterToContactList);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) parent.getItemAtPosition(position);
                int uid = user.getUid();

                Intent intentToContactDetails = new Intent(ContactListActivity.this, ContactDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", userDBImplementation.queryUserByUID(user.getUid()));
                intentToContactDetails.putExtras(bundle);
                startActivity(intentToContactDetails);
            }
        });
    }

    private void searchAndChangeListViewContent(String pattern) {
        ArrayList<User> newUserSet = new ArrayList<User>();
        pattern = pattern.toLowerCase();

        for (User user : users) {
            if (user.getName().toLowerCase().startsWith(pattern) || user.getPhoneNumber().toLowerCase().startsWith(pattern)) {
                newUserSet.add(user);
            }
        }

        if (newUserSet.isEmpty()) {
            newUserSet.add(new User(0, "no results", null, null, null, null, null));
        }

        mainListView.setAdapter(new UserAdapterToContactList(this, newUserSet));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_contact_list, menu);

        MenuItem searchItem = menu.findItem(R.id.search_view);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.add_icon:
                startActivity(new Intent(this, ContactAddActivity.class));
                return true;
            case R.id.search_view:
                // nothing to do here
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        searchAndChangeListViewContent(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        searchAndChangeListViewContent(s);
        return false;
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void makeSomeDbSpecificTemporaryTasks() { // TODO: is for test purpos, I have to remove it
        long re = userDBImplementation.deleteUsers();
        userDBImplementation.insertUser(new User(-1, "Mihu Cosmin", "0754919860", "cosmin.mihu@gmail.com", "1992-10-27", "Nr. 45, Str. B.P.Hasdeu, 400371, Cluj-Napoca, Jud. Cluj, Romania", "http://www.cosminmihu.info/"));
        userDBImplementation.insertUser(new User(-1, "Debre Elizabeth", "0735507173", "eliz_debre@yahoo.com", "1999-01-02", "address2", "https://www.facebook.com/debre.elizabeth"));
        userDBImplementation.insertUser(new User(-1, "gipsey", "0735502246", "debredavid@yahoo.com", "1992-08-15", "Perecsenyi Magyar Baptista Imaház", "http://www.gipsey.tk"));
        userDBImplementation.insertUser(new User(-1, "Fortech", "+40 264 438217", "office@fortech.ro", "2003-12-01", "Str. Frunzisului nr.106 400664 Cluj-Napoca", "http://www.fortech.ro/"));
        userDBImplementation.insertUser(new User(-1, "fortech1", "+40 264 453303", "office@fortech.ro", "1999-01-02", "Fortech 15-17, Strada Meteor, Cluj-Napoca 400000", "http://www.fortech.ro/"));
        userDBImplementation.insertUser(new User(-1, "Jakab Hunor", "+40747253683", "jakabh@cs.ubbcluj.ro", "1985-04-19", "str. Rozelor nr. 62 547535 Sangeorgiu de Padure Mures, Romania", "http://www.cs.ubbcluj.ro/~jakabh/"));
        userDBImplementation.insertUser(new User(-1, "Name4", "004", "name4@yahoo.com", "1999-01-04", "address4", "http://www.gipsey.tk/4"));
        userDBImplementation.insertUser(new User(-1, "Name5", "915673282", "name5@yahoo.com", "1999-01-05", "address5", "http://www.gipsey.tk/5"));
        userDBImplementation.insertUser(new User(-1, "Name6", "006", "name6@yahoo.com", "1999-01-06", "address6", "http://www.gipsey.tk/6"));
        userDBImplementation.insertUser(new User(-1, "Name7", "007", "name7@yahoo.com", "1999-01-07", "address7", "http://www.gipsey.tk/7"));
        userDBImplementation.insertUser(new User(-1, "Name8", "008", "name8@yahoo.com", "1999-01-08", "address8", "http://www.gipsey.tk/8"));
        userDBImplementation.insertUser(new User(-1, "Name9", "009", "name9@yahoo.com", "1999-9-9", "address9", "http://www.gipsey.tk/9"));
        userDBImplementation.insertUser(new User(-1, "Name10", "0010", "name10@yahoo.com", "1999-10-10", "address10", "http://www.gipsey.tk/10"));
    }

}
