/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       Google_Maps_Fragment.java
    PROJECT:        PROG3150 - Assignment 03
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           April 16th, 2020
    DESCRIPTION:    The Google_Maps_fragment calles the GoogleMaps object and creates it in
                    the fragment.

================================================================================================= */

package com.dainglis.trip_planner.views;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dainglis.trip_planner.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GoogleMapsFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;

    String address;

    public GoogleMapsFragment() { }

    public GoogleMapsFragment setMapAddress(String addr) {
        address = addr;
        return this;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_google_maps, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle saveInstanceState){
        super.onViewCreated(view, saveInstanceState);
        mMapView = (MapView) mView.findViewById(R.id.map);
        // check if mapView exists
        if(mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);

        }

    }

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------
        Method:         onMapReady(GoogleMap googleMap)
        Description:    This method is called ti initialize the google map object in a fragment.
        Parameters:     GoogleMap googleMap obj
        Returns:        void.

     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Context context = getContext();

        if (context != null) {
            MapsInitializer.initialize(getContext());

            //TripInfoFragment trf = new TripInfoFragment();

            LatLng addr = getLocationFromAddress(address);


            // set map type
            mGoogleMap = googleMap;
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // enable liteMode for minimal functionality and gestures
            GoogleMapOptions options = new GoogleMapOptions().liteMode(true);

            googleMap.addMarker(new MarkerOptions().position(new LatLng(addr.latitude, addr.longitude)));
        } else {
            Log.e("GoogleMapsAPI", "Error launching Maps API: no context");
        }
    }

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------
            Method:          getLocationFromAddress()
            Description:    This method calls the Geocoder API to get the lat and long of an address
            --------------------------------------------------------------------------------------------- */
    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        LatLng geoPlace = null;

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            geoPlace = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return geoPlace;
    }
}