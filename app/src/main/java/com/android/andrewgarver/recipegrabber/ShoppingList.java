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
import com.android.andrewgarver.recipegrabber.extendCalView.CalendarProvider;
import java.util.ArrayList;

/**
 *
 * <p>
 *
 *
 * @author  Andrew Garver, Landon Jamieson, and Reed Atwood
 * @version 1.0
 * @since   12/10/2015
 */
public class ShoppingList extends Fragment {

    /**
     * Debugging Tag to display LogCat messages for debugging
     */
    private final static String TAG = ShoppingList.class.getSimpleName();

    /**
     *
      */
    private static DatabaseAdapter dbHelper;
    private ListView list;
    private static ArrayAdapter<String> adapter;
    private CalendarProvider cpHelper;

    /**
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
     * @return Return the View for the fragment's UI, or null
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /**
         *
         */
        dbHelper = new DatabaseAdapter(getActivity());
        cpHelper = new CalendarProvider();
        cpHelper.setContext(getActivity());

        // This is where we automatically add things to the shopping list

        /**
         * We need to refresh the shoppingList before anything so we don't get duplicates
         */
        dbHelper.refreshShoppingList();

        /**
         * Gets the recipe names
         */
        ArrayList<String> plannedRecipes = cpHelper.getPlannedRecipes();

        /**
         * Get the recipe id numbers and store them in an ArrayList
         */
        ArrayList<Integer> plannedRecipeIds = new ArrayList<>();

        /**
         *
         */
        for (String s : plannedRecipes) {
            plannedRecipeIds.add(dbHelper.getRecipeId(s));
        }

        /**
         * Get the ingredients from the recipes using the recipe_ids
         */
        final ArrayList<Ingredient> plannedIngredients = dbHelper.getPlannedIngredients(plannedRecipeIds);

        /**
         * Get the ingredients in the Cupboard
         */
        ArrayList<Ingredient> cupboardIngredients = dbHelper.getAllIngredientsVerbose();

        /**
         * Do calculations with ingredients
         */
        for (Ingredient planned : plannedIngredients) {

            boolean haveInCupboard = false;

            /**
             * Loop through all ingredients in the cupboard
             */
            for (Ingredient cupboard : cupboardIngredients) {
                if (planned.getName().equalsIgnoreCase(cupboard.getName()) // the same ingredient
                        && planned.getMetric().equals(cupboard.getMetric())) { // with the same metric unit
                    haveInCupboard = true;

                    /**
                     * If there is inadequate amounts of the ingredient in the cupboard, add the appropriate amount
                     */
                    if (planned.getQuantity() > cupboard.getQuantity()) { // don't have enough in the cupboard
                        dbHelper.addToShoppingList(planned.getName(), String.valueOf(planned.getQuantity() - cupboard.getQuantity()), planned.getMetric(), true);
                    }
                }
            }

            /**
             * If the ingredient is not found in the cupboard, add it.
             */
            if (!haveInCupboard) {
                dbHelper.addToShoppingList(planned.getName(), String.valueOf(planned.getQuantity()), planned.getMetric(), true);
            }
        }

        /**
         * This just gets all the items in the shopping list.  This includes manually added as well as automatically added things.
         */
        ArrayList<String> items = dbHelper.getAllShoppingListItems();

        adapter = new ArrayAdapter<>(getContext(), R.layout.row_layout, items);

        View view = inflater.inflate(R.layout.frag_shoppinglist, container, false);
        list = (ListView) view.findViewById(R.id.listView);
        list.setAdapter(adapter);

        /**
         * OnLongClick listener for the list items
         */
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            /**
             * This will handle creating a AlertDialog to confirm deletion with the user
             *
             * @param parent Parent of the AdapterView
             * @param view The current view
             * @param position Position of the item in the listView
             * @param id Id of the item to be deleted
             * @return ture
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                /**
                 * This split will allow us to get the name of the ingredient we want to try to delete
                 */
                final String preSplit = adapter.getItem(position);
                final String toDel = preSplit.split(" - ")[0];

                /**
                 * We must build the Alert Dialog, by setting up the Title and the message
                 */
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setTitle("Delete ingredient: " + toDel + '?');
                adb.setMessage("Are you sure you want to remove this ingredient from your shopping list?");

                /**
                 * This is the button to go ahead as planned and delete this ingredient.
                 */
                adb.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    /**
                     * This will handle actually deleting the item, if it is not part of a planned
                     * recipe
                     *
                     * @param dialog The dialog box this was called from
                     * @param which Which option was selected
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        /**
                         * This will make sure you are not attempting to delete a ingre
                         */
                        for (Ingredient ing : plannedIngredients)
                            if (ing.getName().equalsIgnoreCase(toDel)) {
                                Toast.makeText(getContext(), "Unable to delete ingredient for a planned recipe.\n" +
                                        "Remove recipe from the Menu or add ingredient to the cupboard", Toast.LENGTH_LONG).show();
                                return;
                            }

                        /**
                         * remove the item from the list and also the database. So the user sees the removal.
                         */
                        adapter.remove(preSplit);
                        dbHelper.deleteFromShoppingList(toDel);
                        Toast.makeText(getContext(), "Deleting from shopping list", Toast.LENGTH_LONG).show();
                    }
                });


                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    /**
                     * The user cancelled so nothing will happen.
                     *
                     * @param dialog The dialog box this was called from
                     * @param which Which option was selected
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                /**
                 * Create the AlertDialog and show it to the user
                 */
                AlertDialog ad = adb.create();
                ad.show();

                //this action does consume the click, thus return true.
                return true;
            }
        });

        /**
         * The onclick listener for this button will allow the user to add to the shopping list
         */
        ImageButton addBtn = (ImageButton) view.findViewById(R.id.addToShoppingList);
        addBtn.setOnClickListener(new View.OnClickListener() {

            /**
             *
             *
             * @param v is a view
             */
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddToShoppingList.class));
            }
        });

        return view;
    }

    /**
     * Allows the shoppinglist to be refreshed if the database has changed.
     */
    public static void refreshShoppingList()
    {
        adapter.clear();
        adapter.addAll(dbHelper.getAllShoppingListItems());
    }
}