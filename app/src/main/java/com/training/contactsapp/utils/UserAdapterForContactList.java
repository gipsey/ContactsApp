package com.training.contactsapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.training.contactsapp.R;
import com.training.contactsapp.model.User;

import java.util.ArrayList;

/**
 * Created by davidd on 1/14/15.
 */
public class UserAdapterForContactList extends ArrayAdapter<User> {

    public UserAdapterForContactList(Context context, ArrayList<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent,
                    false);
        }

        TextView nameTextView = (TextView) convertView.findViewById(R.id.name_text_view);
        TextView phoneNumberTextView = (TextView) convertView.findViewById(R.id.phone_number_text_view);
        ImageView avatarImageView = (ImageView) convertView.findViewById(R.id.avatar_image_view);

        User user = getItem(position);
        nameTextView.setText(user.getName());
        phoneNumberTextView.setText(user.getPhoneNumber());
        avatarImageView.setImageBitmap(user.getAvatarAsBitmap());

        return convertView;
    }
}
