package com.training.contactsapp.presentation.adapter;

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

public class UserContactListAdapter extends ArrayAdapter<User> {

    public UserContactListAdapter(Context context, List<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(
                    getContext()).inflate(R.layout.item_user,
                    parent,
                    false);
            viewHolder = new ViewHolder();
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.name_text_view);
            viewHolder.phoneNumberTextView = (TextView) convertView.findViewById(R.id.phone_number_text_view);
            viewHolder.avatarImageView = (ImageView) convertView.findViewById(R.id.image_view_avatar);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        User user = getItem(position);
        viewHolder.nameTextView.setText(user.getName());
        viewHolder.phoneNumberTextView.setText(user.getPhoneNumber());
        if (user.getAvatar() != null) {
            viewHolder.avatarImageView.setImageBitmap(user.getAvatarAsBitmap());
        } else {
            viewHolder.avatarImageView.setImageBitmap(BitmapFactory.decodeFile(user.getAvatarPath()));
        }

        return convertView;
    }

    private static class ViewHolder {
        public TextView nameTextView;
        public TextView phoneNumberTextView;
        public ImageView avatarImageView;
    }

}
