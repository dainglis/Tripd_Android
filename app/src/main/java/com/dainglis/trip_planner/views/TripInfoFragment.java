
/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       TripInfoFragment.java
    PROJECT:        PROG3150 - Assignment 02
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           March 10th, 2020
    DESCRIPTION:    The TripInfo Fragment presents the information about a created trip.
                    This file contains the classes and methods used to load a existing trip and display its info
                    to the user. In order to use Asynchronous tasks, this file has 2 classes extending AsynchronousTask.

 */

package com.dainglis.trip_planner.views;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.controllers.TripInfoViewModel;
import com.dainglis.trip_planner.models.Event;
import com.dainglis.trip_planner.models.Trip;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripInfoFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private TripInfoViewModel mViewModel;

    //Create variables  to maintain the Fragment Context
    View view;
    Context context;
    String FILENAME;
    public long currentTripId;

    //Create variables for Text and Image Views
    TextView tripNameView;
    TextView startLocationView;
    TextView endLocationView;
    TextView tripDateView;
    ImageView imageView;

    //Create variables for FloatingButtons
    FloatingActionButton addButton;
    FloatingActionButton editButton;
    FloatingActionButton shareButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Receives the view
        view = inflater.inflate(R.layout.fragment_trip_info, container, false);

        //Set the variables
        tripNameView = view.findViewById(R.id.tripName);
        startLocationView = view.findViewById(R.id.tripStart);
        endLocationView = view.findViewById(R.id.tripEnd);
        tripDateView = view.findViewById(R.id.tripDateInfo);
        imageView = view.findViewById(R.id.imageView);

        // FloatActionButtons from the view
        addButton = view.findViewById(R.id.buttonAdd);
        editButton = view.findViewById(R.id.buttonEdit);
        shareButton = view.findViewById(R.id.buttonShare);

        if (currentTripId == 0) {
            Toast.makeText(view.getContext(),"No tripId received", Toast.LENGTH_SHORT)
                    .show();
        }
        //And return the view
        return view;
    }
    /* METHOD HEADER COMMENT -----------------------------------------------------------------------
        Method:         setCurrentTripId(long id, Context c)
        Description:    This method is load when the user select a trip on the TripList.
                        It receives the trip selected and the context of the Main Activity.
        Parameters:     Long        id        Id of the trip selected on the previous fragment.
                        Context      c        Context of the MainActivity.
        Returns:        void.

     */
    public void setCurrentTripId(long id, Context c){
        currentTripId = id;
        context = c;
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

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                raiseShareButtonPressed();
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
    /* METHOD HEADER COMMENT -----------------------------------------------------------------------
        Method:         updateTripView(Trip trip)
        Description:    The purpose of this method is to receive a trip object and use its data to
                        update the datafields, showing the trip info to the user.
        Parameters:     Trip        trip        The instance of the Model Trip that will have its info loaded
        Returns:        void.

     */
    private void updateTripView(@Nullable Trip trip) {
        if (trip != null) {
            //Set the text of the text views
            tripNameView.setText(trip.getTitle());
            startLocationView.setText(trip.getStartLocation());
            endLocationView.setText(trip.getEndLocation());
            tripDateView.setText(trip.getDateStamp());

            //Call, Asynchronously, the method responsible for loading the image of the city visited
            new getCityImage().execute(new String[]{ trip.getEndLocation()});
        }
    }

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------
        Method:         setTripImage(Bitmap img)
        Description:    This method receives a Bitmap object from an Asynchronous call and use it to update a ImageView
        Parameters:     Bitmap        img        Bitmap object that will be placed on the ImageView
        Returns:        void.
     */
    private void setTripImage(Bitmap img){

        try {
            imageView.setImageBitmap(img);
        } catch (Exception e){
            e.printStackTrace();

        }
    }

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------
        Method:         updateEventView(List<Event> events)
        Description:    This method receives a instance of the Model event and use it to populate the List Adapter
        Parameters:     List<Event>        events        List of events that will be shown at the fragment
        Returns:        void.
    */
    private void updateEventView(@Nullable List<Event> events) {
        if (events != null && events.size() > 0) {
            // A simplified two-line list item using the
            // two_line_list_item.xml layout file

            SimpleAdapter eventsAdapter = new SimpleAdapter(view.getContext(),
                    mViewModel.generateEventInfoList(events),
                    R.layout.two_line_list_item,
                    new String[] {TripInfoViewModel.KEY_MAIN_TEXT, TripInfoViewModel.KEY_SECONDARY_TEXT },
                    new int[] {R.id.main_text, R.id.secondary_text });

            LinearLayout linearLayout = view.findViewById(R.id.eventLayout);
            linearLayout.removeAllViews();

            for (int i = 0; i < eventsAdapter.getCount(); i++) {
                linearLayout.addView(eventsAdapter.getView(i, null, null));
            }
        }
    }

    public TripInfoFragment() {
        // Required empty public constructor
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

    private void raiseShareButtonPressed() {

        Intent calTripIntent = mViewModel.getTripAsCalendarIntent();

        if (calTripIntent != null) {
            startActivity(calTripIntent);
        }

    }

    private void raiseCityPressed(View view) {
        mListener.onCityClick(view);
    }

    public static TripInfoFragment newInstance() {
        TripInfoFragment fragment = new TripInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    //Interface used to make the interactions contained on this fragment work.
    //This interface is implemented on the mainActivity, and each method if developed there.
    public interface OnFragmentInteractionListener {

        void onAddButtonPressed(long tripId);
        void onEditButtonPressed(long tripId);
        void onShareButtonPressed(long tripId);
        void onCityClick(View view);
    }


    /* CLASS HEADER COMMENT ======================================================================

    CLASS:          getCityImage
    DESCRIPTION:    The purpose of this class is to perform Asynchronous tasks
                    in order to download and store an image of the end city.
 */
    class getCityImage extends AsyncTask<String, Void, Void> {

            /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         doInBackground()
        Description:    Downloads a file from the internet and stores it on the
                        phone's internal storage.
        Parameters:     String[]        params          The name of the City.
    --------------------------------------------------------------------------------------------- */

        @Override
        protected Void doInBackground(String... params) {

            try {
                //Gets the filename
                FILENAME = params[0] + ".jpg";
                // get the URL of image required on the Mobile App Dev A2 website
                URL url = new URL("https://mada2.000webhostapp.com/" + FILENAME);

                //Creates the InputStream
                InputStream in = url.openStream();

                // Get the output stream so we can write the content of the
                // download to a file
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
//                Log.i("MyApp", sb.toString());
                Log.i("Tripd/ASYNC", "Image downloaded");
                out.close();
                in.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /* METHOD HEADER COMMENT -----------------------------------------------------------------------
        Method:          onPostExecute(Void result) {()
        Description:    After executing the method for download, call an another AsyncTask,
                        to continue the process to load the image
        --------------------------------------------------------------------------------------------- */
        @Override
        protected void onPostExecute(Void result) {
            Log.i("Tripd/ASYNC", "Download complete, changing view");
            new ReadImage().execute(FILENAME);
        }
    }
        /* CLASS HEADER COMMENT ======================================================================

        CLASS:          ReadImage
        DESCRIPTION:    The purpose of this class is to perform Asynchronous tasks
                        in order to load an image of the end city on the screen.

     */
    class ReadImage extends AsyncTask<String, Void, Void> {
        //Set the variable that will receive the decoded image
        Bitmap img;

        /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         doInBackground()
        Description:    Gets a file from the internal storage and decode it as a bitmap.
        Parameters:     String[]        params          The name of the City.
        --------------------------------------------------------------------------------------------- */
        @Override
        protected Void doInBackground(String... FILENAME) {
            try {
                // read the file from internal storage
                FileInputStream in = context.openFileInput(FILENAME[0]);
                // Decodes the stream in order the get a Bitmap object
                img = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("News reader", e.toString());
                return null;
            }
        return null;
        }

            /* METHOD HEADER COMMENT -----------------------------------------------------------------------
            Method:          onPostExecute(Void result) {()
            Description:    After executing the method for decoding the image,
                            call the method responsible for seting/changing the trip's image
            --------------------------------------------------------------------------------------------- */
        @Override
        protected void onPostExecute(Void result) {
            Log.d("News reader", "Feed read: " + new Date());
            TripInfoFragment.this.setTripImage(img);

        }
    }
}
