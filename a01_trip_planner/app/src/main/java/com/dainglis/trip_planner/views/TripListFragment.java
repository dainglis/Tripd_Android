package com.dainglis.trip_planner.views;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.controllers.CustomArrayAdapter;
import com.dainglis.trip_planner.controllers.TripDatabase;
import com.dainglis.trip_planner.controllers.TripListViewModel;
import com.dainglis.trip_planner.models.Trip;

import java.util.List;

public class TripListFragment extends Fragment {

    private TripListViewModel mViewModel;
    ListView tripList;
    FloatingActionButton addBtnMove;
    CustomArrayAdapter adapt;

    public static TripListFragment newInstance() {
        return new TripListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trip_list_fragment, container, false);

        // create action for add button
        addBtnMove = view.findViewById(R.id.addBtn);
        tripList = view.findViewById(R.id.tripCardListView);

        setTripListView(view.getContext());

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
                Fragment fragment = null;
                fragment = new TripFormFragment();

                FragmentManager manager = getSupportFragmentManager(); // added a onlick to trigger fragment for tripform
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        });
    }

    /* METHOD HEADER COMMENT ---------------------------------------------------------------------------

        Method:         setTripListView()
        Description:    This method is called to set the list of trips using a custom array adapter.
        Parameters:     None.
        Returns:        Void.

    ------------------------------------------------------------------------------------------------- */
    public void setTripListView(final Context context) {
        TripDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {


                TripDatabase db = TripDatabase.getInstance();


                final List<Trip> trips = db.tripDAO().getAll();



        // "Card" Array Adapter

                adapt = new CustomArrayAdapter(context, R.layout.card_view_list_item, trips);
                tripList.setAdapter(adapt);
            }
        });


        /* temporarily removed onclicklistener, this will need to raise an event in the implementing activity
        tripList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), TripInfoActivity.class);
                intent.putExtra(KEY_TRIP_ID, trips.get(position).getId());
                startActivity(intent);

            }
        });
         */
    }

}
