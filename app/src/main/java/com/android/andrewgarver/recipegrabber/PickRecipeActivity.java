package com.android.andrewgarver.recipegrabber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

/**
 * This will display all the recipes (similar to cookbook) but on selecting it will add it to the menu
 * <p>
 * Recipes will be displayed from the database. A single click will add it to the menu. A long click
 * will have the recipe displayed.
 *
 * @author  Andrew Garver, Landon Jamieson, and Reed Atwood
 * @version 1.0
 * @since   12/10/2015
 */
public class PickRecipeActivity extends AppCompatActivity {

    /**
     * Debugging Tag to display LogCat messages for debugging
     */
    private static final String TAG = PickRecipeActivity.class.getSimpleName();

    /**
     * Set up variables for later use
     */
    private ListView recipesList;
    private DatabaseAdapter dbHelper;

    /**
     * This handles setting up access to the database and the onclick listeners for the items in the
     * list.
     *
     * @param savedInstanceState save the activity for reopening
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_recipe);

        /**
         * Get the recipes from the database
         */
        dbHelper = new DatabaseAdapter(getApplicationContext());
        ArrayList<String> items = dbHelper.getAllRecipes();

        /**
         * Connect the adapter to the items, and the recipesList to the adapter so it will display
         */
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.row_layout, items);
        recipesList = (ListView) findViewById(R.id.allRecipes);
        recipesList.setAdapter(adapter);

        /**
         * This will set the items to handle sending the needed information back to Menu_Frag
         */
        recipesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * What actually is called when the item is clicked
             *
             * @param parent From override, not used here, the adapter of the view
             * @param view The view which the item was from
             * @param position The position in the list.
             * @param id Unused from override.
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /**
                 * Send the recipe name back to Menu_Frag with a RESULT_OK so it knows to add it to the
                 * calendar
                 */
                Intent data = new Intent();
                data.putExtra("recipeName", recipesList.getItemAtPosition(position).toString());
                setResult(RESULT_OK, data); //allows us to access this data in the previous activity
                finish();
            }
        });

        /**
         * This will set up the items to display the recipe if clicked.
         */
        recipesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            /**
             * This will call the DisplayRecipeActivity Activity for the item long clicked
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
                 * Get the name of the activity
                 */
                String recipeName = recipesList.getItemAtPosition(position).toString();

                /**
                 * Use Extras on intent to share information
                 */
                Intent intent = new Intent(getApplicationContext(), DisplayRecipeActivity.class);
                intent.putExtra("recipeName", recipeName);
                startActivity(intent);

                return true;
            }
        });
    }
}