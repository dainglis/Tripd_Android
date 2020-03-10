package com.dainglis.trip_planner.views;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dainglis.trip_planner.OnFragmentInteractionListener;
import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.TestFrag;
import com.dainglis.trip_planner.controllers.TripListAdapter;
import com.dainglis.trip_planner.controllers.TripListViewModel;
import com.dainglis.trip_planner.models.Trip;

import java.util.List;
import java.util.concurrent.RecursiveAction;

public class TripListFragment extends Fragment {

    private TripListViewModel mViewModel;
    FloatingActionButton addBtnMove;

    private OnFragmentInteractionListener mListener;

    //TripListAdapter adapt;
    //ListView tripList;

    public static TripListFragment newInstance() {
        return new TripListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trip_list_fragment, container, false);


        RecyclerView rView = view.findViewById(R.id.trip_card_rec_view);
        final TripListAdapter tAdapter = new TripListAdapter(view.getContext(), this);

        rView.setAdapter(tAdapter);
        rView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mViewModel = new TripListViewModel();
        mViewModel.getTrips().observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(@Nullable List<Trip> trips) {
                tAdapter.setTrips(trips);
            }
        });

        // create action for add button
        addBtnMove = view.findViewById(R.id.addBtn);


        //mViewModel.setTripListView(tripList);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TripListViewModel.class);
        // TODO: Use the ViewModel



        //create addBtn listener
        addBtnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launchTripFormForResult();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(long tripId) {
        if (mListener != null) {
            Toast.makeText(this.getContext(), "Trip ID: " + tripId, Toast.LENGTH_SHORT)
                    .show();
            mListener.onFragmentInteraction(tripId);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TripListFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(long tripId);
    }


}
