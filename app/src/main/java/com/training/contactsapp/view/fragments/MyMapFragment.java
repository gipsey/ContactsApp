package com.training.contactsapp.view.fragments;

import android.app.Fragment;

public class MyMapFragment extends Fragment {


//    {
//        googleMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.google_map);
//        googleMap = googleMapFragment.getMap();
//
//        String addressToBeDisplayed = getArguments().getString(ADDRESS_TO_BE_DISPLAYED_TAG);
//        List<Address> addresses = getAddressesFromString(addressToBeDisplayed);
//
//        if (addresses == null || addresses.isEmpty()) {
//            showToast(getResources().getString(R.string.cannot_find_address));
//        } else if (addresses.size() == 1) { // one location found
//            Log.w(getClass().getName(), "One address found");
//            showLocation(addresses.get(0), addressToBeDisplayed);
//        } else { // multiple locations found; should be fine to add a dialog here then user can select from max. 5 address
//            showToast(getResources().getString(R.string.more_than_one_address_found));
//            Log.w(getClass().getName(), "There was found more than one address");
//            for (Address a : addresses)
//                Log.i(getClass().getName(), a.toString());
//        }
//
//    }
//
//    private List<Address> getAddressesFromString(String addressToBeDisplayed) {
//        Geocoder geocoder = new Geocoder(getActivity());
//        List<Address> addresses = null;
//
//        try {
//            addresses = geocoder.getFromLocationName(addressToBeDisplayed, ADDRESS_TO_BE_FOUND);
//            Log.w(getClass().getName(), "There wasn't found any address");
//            return addresses;
//        } catch (IOException e) {
//            Log.e(getClass().getName(), "Error message while getting Address from string", e);
//            return null;
//        }
//
//    }
//
//    private void showLocation(Address address, String addressToBeDisplayed) {
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        googleMap.getUiSettings().setCompassEnabled(true);
//        googleMap.getUiSettings().setZoomControlsEnabled(true);
//
//        LatLng latLngToShow = new LatLng(address.getLatitude(), address.getLongitude());
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngToShow, GOOGLE_MAP_ZOOM);
//        googleMap.animateCamera(cameraUpdate);
//        googleMap.addMarker(new MarkerOptions().position(latLngToShow).title(addressToBeDisplayed));
//    }

}
