package com.dainglis.trip_planner.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        return inflater.inflate(R.layout.dialog_fragment_about, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}
