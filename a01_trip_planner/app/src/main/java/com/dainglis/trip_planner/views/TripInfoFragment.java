package com.dainglis.trip_planner.views;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.controllers.TripInfoViewModel;
import com.dainglis.trip_planner.models.Event;
import com.dainglis.trip_planner.models.Trip;

import org.xml.sax.InputSource;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripInfoFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private TripInfoViewModel mViewModel;

    View view;
    Context context;
    String FILENAME;
    TextView tripNameView;
    TextView startLocationView;
    TextView endLocationView;
    TextView tripDateView;
    ImageView imageView;
    FloatingActionButton addButton;
    FloatingActionButton editButton;

    public long currentTripId;
    //Bundle extras;

    public TripInfoFragment() {
        // Required empty public constructor
    }

    public void setCurrentTripId(long id, Context c){
        currentTripId = id;
        context = c;
    }
    public static TripInfoFragment newInstance() {
        TripInfoFragment fragment = new TripInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trip_info, container, false);

        tripNameView = view.findViewById(R.id.tripName);
        startLocationView = view.findViewById(R.id.tripStart);
        endLocationView = view.findViewById(R.id.tripEnd);
        tripDateView = view.findViewById(R.id.tripDateInfo);
        imageView = view.findViewById(R.id.imageView);
        addButton = view.findViewById(R.id.buttonAdd);
        editButton = view.findViewById(R.id.buttonEdit);

        if (currentTripId == 0) {
            Toast.makeText(view.getContext(),"No tripId received", Toast.LENGTH_SHORT)
                    .show();
        }
        return view;
    }


    /*
     * Add Listeners and Observers to data objects when the Fragment is fully instantiated
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TripInfoViewModel.class);
        mViewModel.setTripId(currentTripId);

        mViewModel.getTrip().observe(this, new Observer<Trip>() {
            @Override
            public void onChanged(@Nullable Trip trip) {
                updateTripView(trip);
            }
        });

        mViewModel.getEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                updateEventView(events);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                raiseAddButtonPressed(mViewModel.getTripId());
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                raiseEditButtonPressed(mViewModel.getTripId());
            }
        });

        startLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                raiseCityPressed(view);
            }
        });

        endLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                raiseCityPressed(view);
            }
        });
    }

    private void updateTripView(@Nullable Trip trip) {
        if (trip != null) {
            tripNameView.setText(trip.getTitle());
            startLocationView.setText(trip.getStartLocation());
            endLocationView.setText(trip.getEndLocation());
            tripDateView.setText(trip.getDateStamp());

            new getCityImage().execute(new String[]{ trip.getEndLocation()});
        }
    }

    private void setTripImage(Bitmap img){
        try {
            imageView.setImageBitmap(img);
        } catch (Exception e){
            e.printStackTrace();

        }
    }
    private void updateEventView(@Nullable List<Event> events) {
        if (events != null && events.size() > 0) {
            // A simplified two-line list item using the
            // two_line_list_item.xml layout file

            SimpleAdapter eventsAdapter = new SimpleAdapter(view.getContext(),
                    mViewModel.generateEventInfoList(events),
                    R.layout.two_line_list_item,
                    new String[] {TripInfoViewModel.KEY_MAIN_TEXT, TripInfoViewModel.KEY_SECONDARY_TEXT },
                    new int[] {R.id.main_text, R.id.secondary_text });

            ListView eventList = view.findViewById(R.id.eventListView);
            eventList.setAdapter(eventsAdapter);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TripInfoFragment.OnFragmentInteractionListener) {
            mListener = (TripInfoFragment.OnFragmentInteractionListener) context;
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

    private void raiseAddButtonPressed(long tripId) {
        mListener.onAddButtonPressed(tripId);
    }

    private void raiseEditButtonPressed(long tripId) {
        mListener.onEditButtonPressed(tripId);
    }

    private void raiseCityPressed(View view) {
        mListener.onCityClick(view);
    }


    public interface OnFragmentInteractionListener {
        //void interfaceFunction();
        void onAddButtonPressed(long tripId);
        void onEditButtonPressed(long tripId);
        void onCityClick(View view);
    }

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

       Method:         displayTripDetailsAsync()
       Description:    This method executes the displayTripDetails method as an asynchronous task.
       Parameters:     final long  tripId      The trip for which details are required.
       Returns:        Void.

   --------------------------------------------------------------------------------------------- */
    /*
    protected void displayTripDetailsAsync(final long tripId) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                displayTripDetails(tripId);
            }
        });
    }

     */



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         displayTripDetails()
        Description:    Given a unique id for a Trip, the details of the Trip are retrieved from
                        the "trip_planner" database.
        Parameters:     long        tripId      The id of the Trip to query the database for and
                                                populate the layout elements with the Trip data.
        Returns:        Void.

    --------------------------------------------------------------------------------------------- */
    /*
    protected void displayTripDetails(long tripId) {

        // Query the database for the Trip associated with tripId
        Trip currentTrip = getCurrentTrip(tripId);

        if (currentTrip != null) {
            // Text views to be populated


            tripNameView.setText(currentTrip.getTitle());
            startLocationView.setText(currentTrip.getStartLocation());
            endLocationView.setText(currentTrip.getEndLocation());
            tripDateView.setText(currentTrip.getDateStamp());


            // A simplified two-line list item using the
            // two_line_list_item.xml layout file

            SimpleAdapter eventsAdapter = new SimpleAdapter(getActivity().getApplicationContext(),
                    generateEventInfoList(tripId),
                    R.layout.two_line_list_item,
                    new String[] {KEY_MAIN_TEXT, KEY_SECONDARY_TEXT },
                    new int[] {R.id.main_text, R.id.secondary_text });

            ListView eventList = view.findViewById(R.id.eventListView);
            eventList.setAdapter(eventsAdapter);

        }
    }
     */

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------
        Method:         getCurrentTrip()
        Description:    Queries the `trip_planner` database for the Trip object with the specified
                        id.
        Parameters:     long        id      The unique id of the requested Trip object.
        Returns:        Trip                The Trip object corresponding to the provided Id
    ---------------------------------------------------------------------------------------------
    private Trip getCurrentTrip(long id) {
        return TripDatabase.getInstance().tripDAO().getByIdStatic(id);
    }

     */


    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         getEvents()
        Description:    Queries the "trip_planner" database for the Trip object with the specified
                        id.
        Parameters:     long        id          The id of the Trip to query the database for.
        Returns:        List<Event>             The events in the specified Trip.

    --------------------------------------------------------------------------------------------- */

    /*
    private List<Event> getEvents(long tripId) {
        return TripDatabase.getInstance().eventDAO().getAllByTripId(tripId);
    }

     */
    class getCityImage extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            try {

                FILENAME = params[0] + ".jpg";
                // get the URL
                URL url = new URL("https://mada2.000webhostapp.com/" + FILENAME);

                InputStream in = url.openStream();

                // get the output stream

                FileOutputStream out =
                        context.openFileOutput(FILENAME, Context.MODE_PRIVATE);

                // read input and write output
                byte[] buffer = new byte[1024];
                int bytesRead = in.read(buffer);
                StringBuffer sb = new StringBuffer();
                while (bytesRead != -1)
                {
                    sb.append(new String(buffer, "UTF-8"));
                    out.write(buffer, 0, bytesRead);
                    bytesRead = in.read(buffer);
                }
                Log.i("MyApp", sb.toString());
                out.close();
                in.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("Image downloaded", "Loading");
            new ReadImage().execute(FILENAME);
        }
    }

    class ReadImage extends AsyncTask<String, Void, Void> {
        Bitmap img;
        @Override
        protected Void doInBackground(String... FILENAME) {
            try {
                // read the file from internal storage
                FileInputStream in = context.openFileInput(FILENAME[0]);
                img = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("News reader", e.toString());
                return null;
            }
        return null;
        }

        // This is executed after the feed has been read
        @Override
        protected void onPostExecute(Void result) {
            Log.d("News reader", "Feed read: " + new Date());
            TripInfoFragment.this.setTripImage(img);

        }
    }
}
