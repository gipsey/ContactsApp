package com.training.contactsapp.business;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.training.contactsapp.R;
import com.training.contactsapp.model.User;

import java.util.List;

/**
 * Created by davidd on 1/14/15.
 */
// review: rename UserContactListAdapter + View holder
public class UserAdapterForContactList extends ArrayAdapter<User> {

    public UserAdapterForContactList(Context context, List<User> users) {
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
        ImageView avatarImageView = (ImageView) convertView.findViewById(R.id.image_view_avatar);

        User user = getItem(position);
        nameTextView.setText(user.getName());
        phoneNumberTextView.setText(user.getPhoneNumber());
        if (user.getAvatar() != null) {
            avatarImageView.setImageBitmap(user.getAvatarAsBitmap());
        } else {
            avatarImageView.setImageBitmap(BitmapFactory.decodeFile(user.getAvatarPath()));
        }

        return convertView;
    }
}
