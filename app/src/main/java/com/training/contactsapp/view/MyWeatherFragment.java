package com.training.contactsapp.view;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.training.contactsapp.R;

public class MyWeatherFragment extends Fragment {


    public MyWeatherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_weather, container, false);
    }

//    private void showToast(String text) {
//        Toast.makeText(getActivity(), "MyWeatherFragment " + text, Toast.LENGTH_LONG).show();
//    }

}
