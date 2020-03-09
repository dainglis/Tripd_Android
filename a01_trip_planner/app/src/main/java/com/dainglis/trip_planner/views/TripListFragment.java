package com.dainglis.trip_planner.views;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.controllers.TripListViewModel;

public class TripListFragment extends Fragment {

    private TripListViewModel mViewModel;

    public static TripListFragment newInstance() {
        return new TripListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.trip_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TripListViewModel.class);
        // TODO: Use the ViewModel
    }

}
