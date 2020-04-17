
/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       ContactShareFragment.java
    PROJECT:        PROG3150 - Assignment 01
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           January 28th, 2020
    DESCRIPTION:    This file contains the definition of the ContactShareFragment class.
    
================================================================================================= */

package com.dainglis.trip_planner.views;

import android.arch.lifecycle.ViewModelProviders;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dainglis.trip_planner.R;

public class ContactShareFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener {


    // Define a ListView object
    ListView contactsList;

    public static ContactShareFragment newInstance() {
        return new ContactShareFragment();
    }

    // Called just before the Fragment displays its UI
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Always call the super method first
        super.onCreate(savedInstanceState);

        LoaderManager.getInstance(this).initLoader(0, null, this);
    }

        @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts_list, container, false);

        // Gets the ListView from the View list of the parent activity
        contactsList = view.findViewById(R.id.contacts_list_view);

        // Gets a CursorAdapter
        cursorAdapter = new SimpleCursorAdapter(
                view.getContext(),
                android.R.layout.simple_list_item_1,
                null,
                FROM_COLUMNS,
                TO_IDS,
                0);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // Sets the adapter for the ListView
        contactsList.setAdapter(cursorAdapter);

        contactsList.setOnItemClickListener(this);

    }


    /*
     * Defines an array that contains column names to move from
     * the Cursor to the ListView.
     */
    public static final String[] FROM_COLUMNS = {
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
    };
    /*
     * Defines an array that contains resource ids for the layout views
     * that get the Cursor column contents. The id is pre-defined in
     * the Android framework, so it is prefaced with "android.R.id"
     */
    public static final int[] TO_IDS = {
            android.R.id.text1
    };
    // Define global mutable variables
    // Define variables for the contact the user selects
    // The contact's _ID value
    long contactId;
    // The contact's LOOKUP_KEY
    String contactKey;
    // A content URI for the selected contact
    Uri contactUri;


    private static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
            };


    // The column index for the _ID column
    private static final int CONTACT_ID_INDEX = 0;
    // The column index for the CONTACT_KEY column
    private static final int CONTACT_KEY_INDEX = 1;

    // An adapter that binds the result Cursor to the ListView
    public SimpleCursorAdapter cursorAdapter;

    // Defines the text expression
    private static final String SELECTION =
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?";

    // Defines the array to hold values that replace the ?
    private String[] selectionArgs = { "%" };


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        Log.d("loader", "Cursor loader created");
        /*
         * Makes search string into pattern and
         * stores it in the selection array
         */
        //selectionArgs[0] = "%"; // + searchString + "%";
        // Starts the query
        return new CursorLoader(
                this.getContext(),
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                SELECTION,
                selectionArgs,
                null
        );

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        // Put the result Cursor in the adapter for the ListView
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // Delete the reference to the existing Cursor
        cursorAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(
            AdapterView<?> parent, View item, int position, long rowID) {

        /*
        // Get the Cursor
        Cursor cursor = parent.getAdapter().getCursor();
        // Move to the selected contact
        cursor.moveToPosition(position);
        // Get the _ID value
        contactId = cursor.getLong(CONTACT_ID_INDEX);
        // Get the selected LOOKUP KEY
        contactKey = cursor.getString(CONTACT_KEY_INDEX);
        // Create the contact's content Uri
        contactUri = ContactsContract.Contacts.getLookupUri(contactId, contactKey);
        /*
         * You can use contactUri as the content URI for retrieving
         * the details for a contact.
         */

    }



}
