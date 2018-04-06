package com.android.andrewgarver.recipegrabber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * This activity will handle displaying the recipe by pulling information from the database
 * <p>
 * The recipe will be accessed by its name. Each field will be filled directly from the database.
 * </p>
 *
 *
 * @author  Andrew Garver, Landon Jamieson, and Reed Atwood
 * @version 1.0
 * @since   12/10/2015
 */
public class DisplayRecipeActivity extends AppCompatActivity {

    /**
     * Debugging Tag to display LogCat messages for debugging
     */
    private static final String TAG = DisplayRecipeActivity.class.getSimpleName();

    /**
     * This is the access point for the database
     */
    private DatabaseAdapter dbHelper;

    /**
     * This is what is called when this activity is created. It handles everything that needs to be
     * done.
     *
     * @param savedInstanceState save the activity for reopening
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**
         * set the content view and the database adapter.
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_recipe);
        dbHelper = new DatabaseAdapter(this);

        /**
         * get the name of the selected recipe
         */
        String recipeName = getIntent().getStringExtra("recipeName");
        Log.i(TAG, "Displaying recipe: " + recipeName);

        /**
         * retrieve the recipe information
         */
        String[] recipeInfo = dbHelper.getRecipe(recipeName);
        Long id = Long.valueOf(recipeInfo[0]);
        String recipeIngredients = dbHelper.getRecipeIngredients(id);
        Log.i(TAG, "Successfully saved recipeInfo into array");

        /**
         * update text in recipe
         */
        TextView textView = (TextView)findViewById(R.id.recipeName);
        textView.setText(recipeInfo[1]);
        textView = (TextView)findViewById(R.id.recipeInstructions);
        textView.setText(recipeInfo[2]);
        textView = (TextView)findViewById(R.id.ingQuant);
        textView.setText(recipeIngredients);
    }
}