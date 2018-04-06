package com.android.andrewgarver.recipegrabber;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 *
 *
 *
 *
 * @author  Andrew Garver, Landon Jamieson, and Reed Atwood
 * @version 1.0
 * @since   11/2/2015
 */
public class Cupboard extends Fragment {

    /**
     * Debugging Tag to display LogCat messages for debugging
     */
    private static final String TAG = Cupboard.class.getSimpleName();

    /**
     * Private member variables
     */
    private static DatabaseAdapter dbHelper;
    private static ArrayAdapter<String> adapter;
    private ListView list;

    /**
     *
     *
     *
     * @param inflater The LayoutInflater object that can be used to inflate any
     *                 views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's
     *                  UI should be attached to. The fragment should not add the
     *                  view itself, but this can be used to generate the
     *                  LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /**
         * Sets up our Database helper
         */
        dbHelper = new DatabaseAdapter(getActivity());

        /**
         * Contains all the ingredients on hand
         */
        ArrayList<String> items = dbHelper.getAllIngredients();

        /**
         * Adapter used for the view
         */
        adapter = new ArrayAdapter<>(getContext(), R.layout.row_layout, items);

        /**
         * Sets the view adapter
         */
        View view = inflater.inflate(R.layout.frag_cupboard, container, false);
        list = (ListView) view.findViewById(R.id.listView);
        list.setAdapter(adapter);

        /**
         * OnLongClick listener
         */
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            /**
             *
             *
             *
             * @param parent
             * @param view
             * @param position
             * @param id
             * @return true
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                /**
                 * Gets the position of the clicked item
                 */
                final String preSplit = adapter.getItem(position);
                final String toDel = preSplit.split(" - ")[0];

                /**
                 * Creates a dialog box
                 */
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setTitle("Delete ingredient: " + toDel + '?');
                adb.setMessage("Are you sure you want to remove this ingredient from your cupboard?");

                /**
                 * Creates a toast to display to the user that the item was deleted
                 */
                adb.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    /**
                     *
                     *
                     *
                     * @param dialog
                     * @param which
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.remove(preSplit);
                        dbHelper.deleteIngredient(toDel);
                        Toast.makeText(getContext(), "Deleting ingredient", Toast.LENGTH_LONG).show();
                    }
                });

                /**
                 * Cancel button
                 */
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    /**
                     *
                     *
                     * @param dialog
                     * @param which
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                /**
                 * Shows an alert dialog
                 */
                AlertDialog ad = adb.create();
                ad.show();
                return true;
            }
        });

        ImageButton addBtn = (ImageButton) view.findViewById(R.id.addTo);
        addBtn.setOnClickListener(new View.OnClickListener() {
            /**
             *
             *
             * @param v is a view
             */
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddToCupboard.class));
            }
        });

        return view;
    }

    /**
     * Deletes contents from cupboard and then adds them back in order to refresh
     */
    public static void refreshCupboard () {
        adapter.clear();
        adapter.addAll(dbHelper.getAllIngredients());
    }
}
