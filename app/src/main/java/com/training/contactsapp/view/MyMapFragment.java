package com.training.contactsapp.view;

import android.app.Activity;
import android.app.Fragment;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.training.contactsapp.R;

import java.io.IOException;
import java.util.List;

public class MyMapFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //    private OnFragmentInteractionListener mListener;

    private final LatLng periceiChurch = new LatLng(47.231595, 22.863289);
    private final LatLng fortechMeteor = new LatLng(46.754277, 23.593797);

    private MapFragment googleMapFragment;
    private GoogleMap googleMap;
    private final int googleMapZoom = 15;

    public static final String ADDRESS_TO_BE_DISPLAYED_TAG = "ADDRESS_TO_BE_DISPLAYED";

    public MyMapFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
//            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_map, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        googleMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.google_map);
        googleMap = googleMapFragment.getMap();
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(12, 56)));


//        String addressToBeDisplayed = getArguments().getString(ADDRESS_TO_BE_DISPLAYED_TAG);
//        LatLng latLngForTheGivenPosition = getLocationFromAddress(addressToBeDisplayed);
//        if (latLngForTheGivenPosition == null) {
//            showToast(getResources().getString(R.string.cannot_find_address));
//        } else {
//            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngForTheGivenPosition, googleMapZoom);
//            googleMap.animateCamera(cameraUpdate);
//            googleMap.addMarker(new MarkerOptions().position(latLngForTheGivenPosition));
//        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private LatLng getLocationFromAddress(String addressToBeDisplayed) {
        Geocoder geocoder = new Geocoder(getActivity());

        List<Address> addresses = null;
        LatLng returnLatLng = null;

        try {
            addresses = geocoder.getFromLocationName(addressToBeDisplayed, 5);
            if (addresses == null)
                return null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        returnLatLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

        return  returnLatLng;
    }

    private void showToast(String text) {
        Toast.makeText(getActivity(), "MyWeatherFragment " + text, Toast.LENGTH_LONG).show();
    }



}
