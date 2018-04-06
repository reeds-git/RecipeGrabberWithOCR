package com.android.andrewgarver.recipegrabber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * About Page displays the licencing information
 * <p>
 * A class that sets the Activity to display the licencing information
 *   comes from the ExtendedCalendarView created by TYCZJ
 *   (<a href="https://github.com/tyczj/ExtendedCalendarView">ExtendedCalendarView</a>).
 *   This fixes the deprecated problems from the CalendarView.</p>
 *
 * @author  Andrew Garver, Landon Jamieson, and Reed Atwood
 * @version 1.0
 * @since   12/10/2015
 */
public class About_pageActivity extends AppCompatActivity {

    /**
     * Forward declaration of the TextView
     */
    private TextView textView;

    /**
     * Opens the About Page for display and sets the view to the About TextView
     *
     * @param  savedInstanceState save the activity for reopening
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);

        /**
         * Set the TextView to display the About Page displays the licencing information
         *   calling {@link activity_about_page.xml
         */
        textView = (TextView)findViewById(R.id.tvAbout);
    }
}
