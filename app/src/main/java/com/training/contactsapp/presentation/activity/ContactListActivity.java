package com.training.contactsapp.presentation.activity;

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
import com.training.contactsapp.access.AbstractDAOFactory;
import com.training.contactsapp.access.UserDAO;
import com.training.contactsapp.model.User;
import com.training.contactsapp.presentation.adapter.UserContactListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends ActionBarActivity {
    public final static String ADD_STATUS = "ADD_STATUS";
    private UserDAO mUserDAO;
    private ListView mMainListView;
    private List<User> mOriginalUsers;
    private List<User> mUsers;
    private UserContactListAdapter mUserContactListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        mUserDAO = AbstractDAOFactory.getInstance().getUserDAO();
        mOriginalUsers = mUserDAO.getUsersUidNamePhoneNumberAvatar();

        Intent intent = getIntent();
        String removeStatus = intent.getStringExtra(ContactDetailsAndEditActivity.REMOVE_STATUS);
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
        mMainListView.setAdapter(new UserContactListAdapter(this, mUserDAO.getUsersUidNamePhoneNumberAvatar()));
    }

    private void createListView() {
        mUsers = new ArrayList<User>();
        for (User u : mOriginalUsers) {
            mUsers.add(u);
        }

        mUserContactListAdapter = new UserContactListAdapter(this, mUsers);
        mMainListView = (ListView) findViewById(R.id.list_view);
        mMainListView.setAdapter(mUserContactListAdapter);
        mMainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ContactListActivity.this, ContactDetailsAndEditActivity.class);
                Bundle bundle = new Bundle();
                User user = (User) parent.getItemAtPosition(position);
                bundle.putSerializable(ContactDetailsAndEditActivity.USER_TAG, mUserDAO.getUserByUid(user.getUid()));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void searchAndChangeListViewContent(String pattern) {
        pattern = pattern.toLowerCase();

        mUsers.clear();
        for (User user : mOriginalUsers) {
            if (user.getName().toLowerCase().startsWith(pattern) || user.getPhoneNumber().toLowerCase().startsWith(pattern)) {
                mUsers.add(user);
            }
        }
        if (mUsers.isEmpty()) {
            mUsers.add(new User(0, getResources().getString(R.string.no_result_for_search), null, null, null, null, null, null));
        }
        mUserContactListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_contact_list, menu);

        MenuItem searchItem = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        });

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

}
