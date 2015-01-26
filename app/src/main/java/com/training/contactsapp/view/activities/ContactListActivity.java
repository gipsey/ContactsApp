package com.training.contactsapp.view.activities;

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

import com.training.contactsapp.R;
import com.training.contactsapp.database.UserDBImplementation;
import com.training.contactsapp.model.User;
import com.training.contactsapp.utils.UserAdapterForContactList;

import java.util.ArrayList;

public class ContactListActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {
    public final static String ADD_STATUS = "ADD_STATUS";

    private UserDBImplementation mUserDBImplementation;
    private SearchView mSearchView;
    private ListView mMainListView;
    private ArrayList<User> mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        mUserDBImplementation = UserDBImplementation.getInstance();

        Intent intent = getIntent();
        String removeStatus = intent.getStringExtra(ContactEditActivity.REMOVE_STATUS);
        if (removeStatus != null) {
            Toast.makeText(this, removeStatus, Toast.LENGTH_LONG).show();
        }

        String addStatus = intent.getStringExtra(ADD_STATUS);
        if (addStatus != null) {
            Toast.makeText(this, addStatus, Toast.LENGTH_LONG).show();
        }

        createListView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mMainListView.setAdapter(new UserAdapterForContactList(this, mUserDBImplementation.queryUsersUIDAndNameAndPhoneNumberAndAvatar()));
    }

    private void createListView() {
        mUsers = mUserDBImplementation.queryUsersUIDAndNameAndPhoneNumberAndAvatar();

        UserAdapterForContactList userAdapterForContactList = new UserAdapterForContactList(this, mUsers);
        mMainListView = (ListView) findViewById(R.id.list_view);
        mMainListView.setAdapter(userAdapterForContactList);
        mMainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ContactListActivity.this, ContactDetailsActivity.class);
                Bundle bundle = new Bundle();
                User user = (User) parent.getItemAtPosition(position);
                bundle.putSerializable(ContactDetailsActivity.USER_TAG, mUserDBImplementation.queryUserByUID(user.getUid()));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void searchAndChangeListViewContent(String pattern) {
        ArrayList<User> newUserSet = new ArrayList<User>();
        pattern = pattern.toLowerCase();

        for (User user : mUsers) {
            if (user.getName().toLowerCase().startsWith(pattern) || user.getPhoneNumber().toLowerCase().startsWith(pattern)) {
                newUserSet.add(user);
            }
        }
        if (newUserSet.isEmpty()) {
            newUserSet.add(new User(0, getResources().getString(R.string.no_result_for_search), null, null, null, null, null, null));
        }
        mMainListView.setAdapter(new UserAdapterForContactList(this, newUserSet));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_contact_list, menu);

        MenuItem searchItem = menu.findItem(R.id.search_view);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setQueryHint(getResources().getString(R.string.search_hint));
        mSearchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_icon:
                startActivity(new Intent(this, ContactAddActivity.class));
                break;
            case R.id.search_view:
                break;
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

}
