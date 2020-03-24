/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       AboutDialogFragment.java
    PROJECT:        PROG3150 - Assignment 01
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           February 8th, 2020
    DESCRIPTION:    This file contains the definition of the AboutDialogFragment class.
                    An object of this class presents a simple DialogFragment with information
                    about the "Tripd" application

================================================================================================= */

package com.dainglis.trip_planner.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dainglis.trip_planner.R;

/**
 * A simple {@link DialogFragment} subclass.
 * This simple DialogFragment implements no listener, as no response
 * is required within its interaction
 * Use the {@link AboutDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutDialogFragment extends DialogFragment {
    public AboutDialogFragment() {
        // Required empty public constructor
    }

    TextView supportNumber;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AboutDialogFragment.
     */
    public static AboutDialogFragment newInstance() {
        AboutDialogFragment fragment = new AboutDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_fragment_about, container, false);
        supportNumber = view.findViewById(R.id.phone_support);

        supportNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSupportDialer();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void launchSupportDialer() {
        String supportNumberTelRequest = "tel:" + getResources().getString(R.string.fake_phone_support_num);

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(supportNumberTelRequest));
        startActivity(intent);
    }
}
