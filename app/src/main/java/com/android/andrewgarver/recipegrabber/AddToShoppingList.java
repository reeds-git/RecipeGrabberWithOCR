package com.android.andrewgarver.recipegrabber;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Class to add ingredients to shopping list
 * <p>
 * Creates fields to add quantity, units, and ingredients up to 20 at a time
 *   to your shopping list.
 *
 * @author  Andrew Garver, Landon Jamieson, and Reed Atwood
 * @version 1.0
 * @since   12/10/2015
 */
public class AddToShoppingList extends AppCompatActivity {

    /**
     * Debugging Tag to display LogCat messages for debugging
     */
    private static final String TAG = AddToShoppingList.class.getSimpleName();
    private DatabaseAdapter dbHelper;
    private ArrayList<Ingredient> ingredientsForShoppingList = new ArrayList<>();

    /**
     * Flag to ensure all entries are filled
     */
    private boolean correctInput = true;

    /**
     * Keep track of how many new lines there is
     */
    private int numNewLines;

    /**
     * Array of ids for each of he rows to keep track of what is on them
     */
    private int ids[] = {R.id.newRow1, R.id.newRow2, R.id.newRow3, R.id.newRow4, R.id.newRow5,
            R.id.newRow6, R.id.newRow7, R.id.newRow8, R.id.newRow9, R.id.newRow10,
            R.id.newRow11, R.id.newRow12, R.id.newRow13, R.id.newRow14, R.id.newRow15,
            R.id.newRow16, R.id.newRow17, R.id.newRow18, R.id.newRow19, R.id.newRow20};


    /**
     * Opens AddToShoppingLst. Activity so that you can add a ingredients to the shopping list
     * <p>
     * Adds fields to add quantity, units, and ingredients to the database.
     * Sets numNewLines to 0.
     *
     * @param savedInstanceState save the activity for reopening
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_shoppinglist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        numNewLines = 0;
        dbHelper = new DatabaseAdapter(this);

        /**
         * this must be final since it is accessed from an inner class
         */
        final ImageButton add = (ImageButton) findViewById(R.id.addMore);
        final Button addIng = (Button) findViewById(R.id.addIng);
        final ArrayList<String> results = new ArrayList<>();

        /**
         * need to add the listener to add an extra row of input fields
         */
        add.setOnClickListener(new View.OnClickListener() {

            /**
             * When they click the + button, they will get another row for input.
             * <p>
             * Insures that there in no more than 20 new lines, When the plus(+)
             *   button is clicked, we add a new row to the View and increments
             *   numNewLines by 1.
             *
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {

                /**
                 * Don't let the user enter any more than 20 new lines
                 */
                if (numNewLines > 19)
                    return;

                /**
                 * We use the context of the button, since it is on the activity we are using
                 */
                LayoutInflater vi = (LayoutInflater) add.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.input_field, null); //This is the layout of the new row

                /**
                 * if we need to change the id we would do it here
                 */
                v.setId(ids[numNewLines]);
                ++numNewLines;

                /**
                 * There is an (at first) empty container LinearLayout that we insert these into
                 */
                ((ViewGroup) findViewById(R.id.container)).addView(v);

                Log.i(TAG, "added line " + numNewLines + " with id of " + ids[numNewLines - 1]);
            }
        });

        addIng.setOnClickListener(new View.OnClickListener() {

            /**
             * Adds ingredient to the database and broadcasts the changes
             * <p>
             * Adds ingredient's quantity, units, and ingredient to the database
             * Checks to ensure that all of the fields are filled out.
             * Clicking the add button adds the first row to the database
             *
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                Log.i(TAG, "adding item to shopping list");

                String item = "";

                /**
                 * Input the first line of ingredients.
                 * Ingredient Quantity = ingQuant.
                 * Ingredient Unit = ingUnit.
                 * Ingredient Name = ingName.
                 */
                String ingQuant = ((EditText) findViewById(R.id.ingQuant)).getText().toString();
                String ingUnit = ((Spinner) findViewById(R.id.ingUnit)).getSelectedItem().toString();
                String ingName = ((EditText) findViewById(R.id.ingName)).getText().toString();
                ingName = ingName.replace("  ", " "); //remove double spaces if any

                /**
                 * Error handling for if quantity and ingredient name are blank
                 */
                if (!ingQuant.equals("") && !ingName.equals("")) {
                    ingredientsForShoppingList.add(new Ingredient(ingName, Integer.parseInt(ingQuant), ingUnit));
                    //reset the flag
                    correctInput = true;

                    RelativeLayout rel;

                    /**
                     * Add more ingredients if there is more than one.
                     */
                    for (int i = 0; i < numNewLines; ++i) {
                        rel = ((RelativeLayout)findViewById(ids[i]));
                        ingQuant = ((EditText)rel.findViewById(R.id.quanNewRow)).getText().toString();
                        ingUnit = ((Spinner)rel.findViewById(R.id.unitNewRow)).getSelectedItem().toString();
                        ingName = ((EditText)rel.findViewById(R.id.nameNewRow)).getText().toString();
                        ingName = ingName.replace("  ", " "); //remove double spaces if any

                        /**
                         * only add them if they have something there.
                         */
                        if (!ingQuant.equals("") && !ingName.equals(""))
                            ingredientsForShoppingList.add(new Ingredient(ingName, Integer.parseInt(ingQuant), ingUnit));
                    }
                }
                else
                    correctInput = false;

                /**
                 * Update the activity if all fields are filled out correctly and displays a
                 *   message if not all filled out
                 */
                if (correctInput) {
                    for (Ingredient ingred : ingredientsForShoppingList)
                        dbHelper.addToShoppingList(ingred.getName(), ingred.getQuantityString(),
                                ingred.getMetric(), false);
                    ShoppingList.refreshShoppingList();
                    finish();
                }
                 else {
                    Toast.makeText(getApplicationContext(), "Please fill out all fields",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}