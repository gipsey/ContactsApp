package com.training.davidd.contactsapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.training.davidd.contactsapp.R;
import com.training.davidd.contactsapp.model.User;

import java.util.ArrayList;

/**
 * Created by davidd on 1/14/15.
 */
public class UserAdapterToContactList extends ArrayAdapter<User> {

    public UserAdapterToContactList(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }

        TextView nameTextView = (TextView) convertView.findViewById(R.id.name_text_view);
        TextView phoneNumberTextView = (TextView) convertView.findViewById(R.id.phone_number_text_view);

        nameTextView.setText(user.getName());
        phoneNumberTextView.setText(user.getPhoneNumber());

        return convertView;
    }
}
