package com.android.andrewgarver.recipegrabber;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 *  Main Activity for the start of the app this will handle the fragments
 *  and their switching.
 *
 * @author  Andrew Garver, Landon Jamieson, and Reed Atwood
 * @version 1.0
 * @since   12/10/2015
 */
public class ActivityMain extends AppCompatActivity {

    /**
     * Debugging Tag to display LogCat messages for debugging
     */
    private static final String TAG = ActivityMain.class.getSimpleName();

    /**
     * Adapter for displaying the fragment.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    /**
     * The SQLiteOpenHelper object that will facilitate database queries
     */
    private DatabaseAdapter dbHelper;
    public static SQLiteDatabase database;

    /**
     * Sets up this activity and our fragments.
     *
     * @param savedInstanceState save the activity for reopening
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * Create the adapter that will return a fragment for each of the three
         * primary sections of the activity.
         */
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        /**
         * Set up the ViewPager with the sections adapter.
         */
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        /**
         * Sets up the tabs to connect to the ViewPager
         */
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        /**
         * Set up the DatabaseAdapter
         */
        Log.i(TAG, "here");
        dbHelper = new DatabaseAdapter(this); // are these supposed to be used in the fragments?
        database = dbHelper.getHelper().getWritableDatabase();
        Log.i(TAG, "after getWriteableDatabase");

    }

    /**
     * Inflates the menu and adds items to to the action bar if present
     *
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         * Inflate the menu this adds items to the action bar if it is present.
         */
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    /**
     * Handles action bar item clicks
     *
     * Action bar clicks open About Page
     *   automatically handle clicks on the Home/Up button, so long
     *   as you specify a parent activity in AndroidManifest.xml.
     *
     * @param item About Page menu item
     * @return true if action bar about is click and opened otherwise
     *           super.onOptionsItemSelected(item)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*
         * Save the Menu items id so that we can open the correct page
         */
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            startActivity(new Intent(this, About_page.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     *
     * @author  Andrew Garver, Landon Jamieson, and Reed Atwood
     * @version 1.0
     * @since   12/10/2015
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        /**
         * Sets the FragmentManager to the super class
         *
         * @param fm FragmentManager
         */
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Gets the Items given the position
         *
         * @param position which tab you are on
         * @return Cookbook, Menu, Cupboard, or ShoppingList object depending on the
         *         position passed in
         */
        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new Cookbook();
                case 1:
                    return new com.android.andrewgarver.recipegrabber.Menu();
                case 2:
                    return new Cupboard();
                default:
                    return new ShoppingList();
            }
        }

        /**
         * Gets the number of tabs and pages
         *
         * @return 4 - the number of tabs and pages.
         */
        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        /**
         * Gets the title of each page
         *
         * @param position which page you are on
         * @return Cookbook, Menu, My Cupboard, Shopping List, or null depending
         *           on the position passed in
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Cookbook";
                case 1:
                    return "Menu";
                case 2:
                    return "My Cupboard";
                case 3:
                    return "Shopping List";
            }
            return null;
        }
    }
}
