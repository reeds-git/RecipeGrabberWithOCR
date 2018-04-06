package com.android.andrewgarver.recipegrabber;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.android.andrewgarver.recipegrabber.extendCalView.CalendarProvider;
import java.util.ArrayList;

/**
 * Class that stores, adds, and displays recipes in a ListView on the screen.
 *<p>
 * Displays recipes added by the user in a ListView.
 * Long ListView clicks pull up option to delete or cancel delete.
 * Clicking on the recipe opens DisplayRecipe and displays the recipe.
 * Recipes are stored in a SQLLite database
 *
 * @author  Andrew Garver, Landon Jamieson, and Reed Atwood
 * @version 1.0
 * @since   11/2/2015
 */
public class Cookbook extends Fragment {

    /**
     * Debugging Tag to display LogCat messages for debugging
     */
    private static final String TAG = Cookbook.class.getSimpleName();

    /**
     * Set up needed variables
     */
    private ListView list;
    private static DatabaseAdapter dbHelper;
    private static ArrayAdapter<String> adapter;

    /**
     * What gets called on create. This handles setting the list view to the contents of the
     * database, and setting up button onclick listeners.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any
     *                 views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's
     *                  UI should be attached to. The fragment should not add the
     *                  view itself, but this can be used to generate the
     *                  LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_cookbook, container, false);
        dbHelper = new DatabaseAdapter(getActivity());
        final ArrayList<String> items = dbHelper.getAllRecipes();
        adapter = new ArrayAdapter<>(getContext(), R.layout.row_layout, items);
        list = (ListView) view.findViewById(R.id.listView);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             *
             *
             * @param parent Saves the where the item was clicked
             * @param view The view that was clicked.
             * @param position The position inside the ListView that was clicked.
             * @param id Not used in this function, needed because parent overridden
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * Get the name of the activity
                 */
                String recipeName = list.getItemAtPosition(position).toString();

                /**
                 * Use Extras on intent to share information
                 */
                Intent intent = new Intent(getActivity(), DisplayRecipe.class);
                intent.putExtra("recipeName", recipeName);
                startActivity(intent);
            }
        });

        /**
         *
         */
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            /**
             *
             *
             * @param parent Saves the where the item was clicked
             * @param view The view that was clicked.
             * @param position The position inside the ListView that was clicked.
             * @param id Not used in this function, needed because parent overridden
             * @return true if
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                /**
                 * This must be final since it is accessed from an inner class
                 */
                final String toDel = adapter.getItem(position);

                /**
                 * Message to be displayed when deleting a recipe
                 */
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setTitle("Delete recipe: " + toDel + '?');
                adb.setMessage("Are you sure you want to remove this recipe?");

                /**
                 * Button that deletes
                 */
                adb.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    /**
                     *
                     *
                     * @param dialog
                     * @param which
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.remove(toDel);
                        dbHelper.deleteRecipe(toDel);
                        getActivity().getContentResolver().delete(CalendarProvider.CONTENT_URI, CalendarProvider.EVENT + "='" + toDel + "'", null);
                        Menu.refreshMenu();
                        Toast.makeText(getContext(), "Deleting recipe", Toast.LENGTH_LONG).show();
                    }
                });

                /**
                 *
                 */
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    /**
                     *
                     * @param dialog
                     * @param which
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                /**
                 *
                 */
                AlertDialog ad = adb.create();
                ad.show();
                return true;
            }
        });

        /**
         *
         */
        ImageButton addBtn = (ImageButton) view.findViewById(R.id.addRecipe);

        /**
         *
         */
        addBtn.setOnClickListener(new View.OnClickListener() {

            /**
             * Open the activity
             *
             *
             *
             * @param v is a view
             */
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddRecipe.class));
            }
            });

        return view;
    }

    /**
     * Update the list of recipes if needed.
     */
    public static void refreshCookbook() {
        adapter.clear();
        adapter.addAll(dbHelper.getAllRecipes());
    }

}
